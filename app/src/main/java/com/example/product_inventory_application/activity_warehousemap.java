package com.example.product_inventory_application;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class activity_warehousemap extends AppCompatActivity {

    ViewPager2 pager;
    SliderItemAdapter adapter;
    List<Product> list;
    TextView[] dots;
    LinearLayout layout;
    Spinner SWarehouse;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehousemap);

        Connection connection = SQLConnection.CONN();
        Statement statement = null;

        ArrayList<String> arrayWarehouse = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<Integer> ();

        if (connection != null) {
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Warehouse");
                while (resultSet.next()) {
                    arrayWarehouse.add(resultSet.getString(2));
                    ids.add(resultSet.getInt (1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        SWarehouse = findViewById(R.id.spinner_warehousemap);
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

        pager = findViewById (R.id.viewpager_warehousemap);
        layout = findViewById (R.id.linearlayout_warehousemap);
        list = getlistofproductstodisplay ();

        /**/ SWarehouse.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {

            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                List<Product> products = new ArrayList<> ();
                if (SWarehouse.getSelectedItem() != null) {
                    String selectedItemText = (String) parent.getItemAtPosition (position);
                    for (Product product : list) {
                        if (product.getWarehouseid () == ids.get (arrayWarehouse.lastIndexOf (selectedItemText))) {
                            products.add (product);
                        }
                    }

                    if(products.size ()!=0) {
                        dots = new TextView[products.size ()];

                        adapter = new SliderItemAdapter (products);
                        pager.setAdapter (adapter);
                        setIndicators ();
                        pager.registerOnPageChangeCallback (new ViewPager2.OnPageChangeCallback () {
                            @Override
                            public void onPageSelected (int position) {
                                for (int i = 0; i < products.size (); i++) {
                                    dots[i].setTextColor (getResources ().getColor (R.color.browser_actions_bg_grey));
                                }
                                super.onPageSelected (position);
                            }
                        });
                    }
                    else {
                        products.add(new Product ("Nothing to display",0,0,0,"","",0,0,0,null));
                        adapter = new SliderItemAdapter (products);
                        dots = new TextView[products.size ()];
                        pager.setAdapter (adapter);
                        setIndicators ();
                        pager.registerOnPageChangeCallback (new ViewPager2.OnPageChangeCallback () {
                            @Override
                            public void onPageSelected (int position) {
                                for (int i = 0; i < products.size (); i++) {
                                    dots[i].setTextColor (getResources ().getColor (R.color.browser_actions_bg_grey));
                                }
                                super.onPageSelected (position);
                            }
                        });
                    }
                }

            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {
                List<Product> products = new ArrayList<> ();
                adapter = new SliderItemAdapter (list);
                pager.setAdapter (adapter);
                setIndicators ();
                pager.registerOnPageChangeCallback (new ViewPager2.OnPageChangeCallback () {
                    @Override
                    public void onPageSelected (int position) {
                        for(int i=0;i<list.size ();i++) {
                            dots[i].setTextColor (getResources ().getColor (R.color.browser_actions_bg_grey));
                        }
                        super.onPageSelected (position);
                    }
                });;
            }
        });//*/

        SWarehouse.setOnScrollChangeListener (new View.OnScrollChangeListener () {
            @Override
            public void onScrollChange (View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                pager.registerOnPageChangeCallback (new ViewPager2.OnPageChangeCallback () {
                    @Override
                    public void onPageSelected (int position) {
                        position = 0;
                        super.onPageSelected (position);
                    }
                });
            }
        });


    }

    private void setIndicators() {
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(activity_warehousemap.this);
            dots[i].setText(Html.fromHtml("&#9679;"));
            dots[i].setTextSize(18);
            layout.addView(dots[i]);
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

}
