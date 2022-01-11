package com.example.product_inventory_application;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.product_inventory_application._direction.DirectionsAPICallback;
import com.example.product_inventory_application._direction.GetURL;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.zxing.client.result.ProductParsedResult;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class activity_map extends AppCompatActivity implements DirectionsAPICallback {
    private static final int LOCATION_MIN_UPDATE_TIME = 10;
    private static final int LOCATION_MIN_UPDATE_DISTANCE = 1000;

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private Location location = null;
    private LatLng DDestination, DOrigin;

    private ImageButton back;
    private TextView txt_address;
    private Spinner SWarehouse;

    private LocationListener locationListener = new LocationListener () {
        @Override
        public void onLocationChanged (Location location) {
            drawMarker (location, "I am here..");
            locationManager.removeUpdates (locationListener);
        }

        @Override
        public void onStatusChanged (String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled (String s) {

        }

        @Override
        public void onProviderDisabled (String s) {

        }
    };

    private LocationManager locationManager;
    private MarkerOptions Loc1,Loc2;
    private Polyline polyline;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_maps);

        //txt_address = findViewById (R.id.txt_currentaddress);
        back = findViewById (R.id.btn_backtohomescreenfrommap);
        SWarehouse = findViewById (R.id.spinner_maps);
        back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                startActivity (new Intent (activity_map.this, activity_home.class));
            }
        });

        /** Spinner for warehouse*/
        ArrayList<String> arrayWarehouse = new ArrayList<>();
        Connection connection = SQLConnection.CONN();
        Statement statement = null;
        if (connection != null) {
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Warehouse");
                while (resultSet.next()) {
                    arrayWarehouse.add(resultSet.getString(2));

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        ArrayAdapter<String> WarehouseSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arrayWarehouse);
        SWarehouse.setPrompt("Warehouse");
        WarehouseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SWarehouse.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        WarehouseSpinnerAdapter,
                        R.layout.contact_spinner_row_nothing_selected_warehouse,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
        SWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    //For Directions
                if(SWarehouse.getSelectedItem ()!=null) {
                    DDestination = getlatlong (SWarehouse.getSelectedItem ().toString ());
                    DOrigin = new LatLng (54.91423325378146, 23.95332742843735);

                    Loc1 = new MarkerOptions ().position (DOrigin);
                    Loc2 = new MarkerOptions ().position (DDestination);

                    String url = geturl (DOrigin, DDestination, "driving");
                    new GetURL (activity_map.this).execute (geturl (Loc1.getPosition (), Loc2.getPosition (), "driving"), "driving");
                    locationManager = (LocationManager) getSystemService (Context.LOCATION_SERVICE);
                    initView (savedInstanceState);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        DDestination = getlatlong("Dainava");
        DOrigin = new LatLng (54.91423325378146, 23.95332742843735);

        Loc1 = new MarkerOptions ().position (DOrigin);
        Loc2 = new MarkerOptions ().position (DDestination);

        String url = geturl (DOrigin,DDestination,"driving");
        new GetURL(activity_map.this).execute( geturl (Loc1.getPosition (),Loc2.getPosition (),"driving"),"driving");
        locationManager = (LocationManager) getSystemService (Context.LOCATION_SERVICE);
        initView (savedInstanceState);
    }

    private LatLng getlatlong (String warehouse) {
        LatLng latLng = null;
        if(warehouse!=null) {
            List<Warehouse> list = getlistofwarehousestodisplay ();
            for (Warehouse warehoue : list) {
                if (warehoue.getName ().equals (warehouse)) {
                    latLng = warehoue.getLatLng ();
                }
            }
        }
        return latLng;
    }

    private void initView (Bundle savedInstanceState) {
        mapFragment = (SupportMapFragment) getSupportFragmentManager ()
                .findFragmentById (R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync (new OnMapReadyCallback () {
            @Override
            public void onMapReady (@NonNull GoogleMap googleMap) {
                mapView_onMapReady (googleMap);
            }
        });
    }

    @Override
    protected void onResume () {
        super.onResume ();
        mapFragment.onResume ();
        getCurrentLocation ();
    }

    @Override
    protected void onPause () {
        super.onPause ();
        mapFragment.onPause ();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        mapFragment.onDestroy ();
    }

    private void initMap () {
        if (ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (googleMap != null) {
                googleMap.setMyLocationEnabled (true);
                googleMap.getUiSettings ().setMyLocationButtonEnabled (true);
                googleMap.getUiSettings ().setAllGesturesEnabled (true);
                googleMap.getUiSettings ().setZoomControlsEnabled (true);

            }
        } else {
            if (ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }
    }

    private void getCurrentLocation () {
        if (ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            boolean isGPSEnabled = locationManager.isProviderEnabled (LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled (LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText (getApplicationContext (), "Provider failed", Toast.LENGTH_LONG).show ();
            } else {
                location = null;
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates (LocationManager.GPS_PROVIDER, LOCATION_MIN_UPDATE_TIME, LOCATION_MIN_UPDATE_DISTANCE, locationListener);
                    location = locationManager.getLastKnownLocation (LocationManager.GPS_PROVIDER);
                }
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates (LocationManager.NETWORK_PROVIDER, LOCATION_MIN_UPDATE_TIME, LOCATION_MIN_UPDATE_DISTANCE, locationListener);
                    location = locationManager.getLastKnownLocation (LocationManager.NETWORK_PROVIDER);
                }
                if (location != null) {
                    drawMarker (location, "I am here..");
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }
    }

    private void mapView_onMapReady (GoogleMap googleMap) {
        this.googleMap = googleMap;

        initMap ();
        getCurrentLocation ();
    }

    private void drawMarker (Location location, String title) {
        if (this.googleMap != null) {
            googleMap.clear ();

            googleMap.setOnMarkerClickListener (new GoogleMap.OnMarkerClickListener () {
                @Override
                public boolean onMarkerClick (@NonNull Marker marker) {
                    if (marker.getTitle ().equals ("I am here.."))
                        return false;
                    else {
                        Intent intent = new Intent (activity_map.this, activity_warehouse.class);
                        intent.putExtra ("tag", (int) marker.getTag ());
                        startActivity (intent);
                        return false;
                    }
                }
            });

            List<Warehouse> warehouses = getlistofwarehousestodisplay ();
            ArrayList<Product> products = getlistofproductstodisplay ();
            ArrayList<LatLng> area = new ArrayList<> ();
            ArrayList<LatLng> slots = new ArrayList<> ();
            for (int i = 0; i < warehouses.size (); i++) {
                Objects.requireNonNull (googleMap.addMarker (new MarkerOptions ()
                        .position (warehouses.get (i).getLatLng ())//.getLatLng())
                        .title ("Warehouse " + warehouses.get (i).getName ())
                        .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_BLUE)))).setTag (warehouses.get (i).getId ());
                PolygonOptions polygonoptions = new PolygonOptions ();
                int warehouseID = warehouses.get (i).getId ();
                area = warehouses.get (i).getArea ();
                for (LatLng pt : area) {
                    polygonoptions.add (pt);
                }
                slots = warehouses.get (i).getSlots ();
                // Get back the mutable Polyline
                Polygon polygon1 = googleMap.addPolygon (polygonoptions);
                polygon1.setTag (warehouses.get (i).getId ());
                stylePolygon (polygon1);
                ArrayList<Product> ps = new ArrayList<> ();
                for (Product p : products) {
                    if (p.getWarehouseid () == warehouseID)
                        ps.add (p);
                }
                if (ps.size () > 0) {
                    for (int j = 0; j < ps.size (); j++) {
                        googleMap.addGroundOverlay (new GroundOverlayOptions ()
                                .image (BitmapDescriptorFactory.fromBitmap (BitmapFactory.decodeByteArray (ps.get (j).getImage (), 0, ps.get (j).getImage ().length)))
                                .position (slots.get (j), 15f, 15f)).setTag (j);
                    }
                }
            }

            LatLng lithuania = new LatLng (54.904307, 23.925197);
            googleMap.moveCamera (CameraUpdateFactory.newLatLng (lithuania));
            googleMap.animateCamera (CameraUpdateFactory.zoomTo (11));


            googleMap.setOnPolygonClickListener (new GoogleMap.OnPolygonClickListener () {
                @Override
                public void onPolygonClick (@NonNull Polygon polygon) {
                    Intent intent = new Intent (activity_map.this, activity_warehouse.class);
                    intent.putExtra ("tag", (int) polygon.getTag ());
                    startActivity (intent);
                }
            });


            /*Geocoder geocoder = new Geocoder (activity_map.this);
            try {
                List<Address> addressList = geocoder.getFromLocation (location.getLatitude (), location.getLongitude (), 100);
                txt_address.setText ("Current Address :- " + addressList.get (0).getAddressLine (0));
            } catch (IOException e) {
                txt_address.setText ("Unable to get Current Address");
            }*/

        }
    }


    private ArrayList<Product> getlistofproductstodisplay () {
        ArrayList<Product> products = new ArrayList<> ();
        Connection connection = SQLConnection.CONN ();
        Statement statement = null;
        if (connection != null) {
            try {
                statement = connection.createStatement ();
                ResultSet resultSet = statement.executeQuery ("SELECT * FROM WarehouseItem");
                while (resultSet.next ()) {
                    products.add (new Product (resultSet.getString (3), resultSet.getInt (1),
                            resultSet.getInt (10), resultSet.getInt (2), resultSet.getString (5),
                            resultSet.getString (4), resultSet.getDouble (8), resultSet.getDouble (6), resultSet.getDouble (7), resultSet.getBytes (9)));
                }
            } catch (SQLException e) {
                e.printStackTrace ();
            }

        }
        return products;
    }

    private List<Warehouse> getlistofwarehousestodisplay () {
        List<Warehouse> warehouse_location_list = new ArrayList<> ();
        Connection connection = SQLConnection.CONN ();
        Statement statement = null;

        if (connection != null) {
            try {
                statement = ((Connection) connection).createStatement ();
                ResultSet resultSet = ((Statement) statement).executeQuery ("SELECT * FROM Warehouse");
                String[] temp;
                while (((ResultSet) resultSet).next ()) {
                    ArrayList<LatLng> Area = new ArrayList<> ();
                    temp = resultSet.getString (5).split (";");
                    for (String latlong : temp) {
                        String[] temp1 = latlong.split (",", 2);
                        Area.add (new LatLng (Double.parseDouble (temp1[0]), Double.parseDouble (temp1[1])));
                    }
                    ArrayList<LatLng> slots = new ArrayList<> ();
                    temp = resultSet.getString (7).split (";", resultSet.getInt (6));
                    for (String latlong : temp) {
                        String[] temp1 = latlong.split (",", 2);
                        slots.add (new LatLng (Double.parseDouble (temp1[0]), Double.parseDouble (temp1[1])));
                    }

                    warehouse_location_list.add (new Warehouse (resultSet.getInt (1), resultSet.getString (2), new LatLng (resultSet.getDouble (3), resultSet.getDouble (4)), Area, resultSet.getInt (6), slots));
                }
            } catch (SQLException e) {
                e.printStackTrace ();
            }

        }
        return warehouse_location_list;
    }

    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot ();
    private static final PatternItem GAP = new Gap (PATTERN_GAP_LENGTH_PX);
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_DARK_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_LIGHT_GREEN_ARGB = 0xff81C784;
    private static final int COLOR_DARK_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_LIGHT_ORANGE_ARGB = 0xffF9A825;
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final PatternItem DASH = new Dash (PATTERN_DASH_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList (GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA = Arrays.asList (DOT, GAP, DASH, GAP);

    //Styling the Polygon
    private void stylePolygon (Polygon polygon) {
        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;
        pattern = PATTERN_POLYGON_BETA;
        strokeColor = COLOR_DARK_ORANGE_ARGB;
        fillColor = COLOR_LIGHT_ORANGE_ARGB;
        polygon.setStrokePattern (pattern);
        polygon.setClickable (true);
        polygon.setStrokeWidth (POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor (strokeColor);
        //polygon.setFillColor (fillColor);
    }

    private String geturl(LatLng origin, LatLng destination, String directionmode){
        String ORI = "origin="+origin.latitude+","+origin.longitude;
        String DES = "destination="+destination.latitude+","+destination.longitude;
        String MODE = "mode="+directionmode;
        String PARAMS = ORI+"&"+DES+"&"+MODE;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/"+output+"?"+PARAMS+"&key="+ getString (R.string.google_maps_key);
    }

    @Override
    public void onDirectionsRetrieved (Object... values) {
        if(polyline!=null)
            polyline.remove ();
        polyline = googleMap.addPolyline ((PolylineOptions) values[0]);
    }
}

