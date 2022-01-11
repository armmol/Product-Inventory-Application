package com.example.product_inventory_application;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class activity_productdescription extends AppCompatActivity {

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    ImageView img;
    EditText name,quantity, length, width, heigth, description;
    TextView id,barcodeid,warehouseid;
    Button update,delete,paint;
    private FirebaseAuth mAuth;


    String d1,d7,d9;
    int d2,d3,d8; byte[] img1;
    double d4,d5,d6;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_productdescription);

        Animation bounce = AnimationUtils.loadAnimation (this, R.anim.bounce);
        Animation blink = AnimationUtils.loadAnimation (this, R.anim.blink_anim);

        img = findViewById (R.id.imageview_productdescription_image);
        name = findViewById (R.id.txt_productdescription_name);
        id = findViewById (R.id.txt_productdescription_id);
        quantity = findViewById (R.id.txt_productdescription_quantitiy);
        length = findViewById (R.id.txt_productdescription_length);
        width = findViewById (R.id.txt_productdescription_width);
        heigth = findViewById (R.id.txt_productdescription_height);
        description = findViewById (R.id.txt_productdescription_description);
        barcodeid = findViewById (R.id.txt_productdescription_barcodeid);
        warehouseid = findViewById (R.id.txt_productdescription_warehouseid);
        delete = findViewById (R.id.btn_productdescription_delete);
        update = findViewById (R.id.btn_productdescription_update);
        paint = findViewById (R.id.button_gotopaint);

        delete.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                /**Delete from Database */

                Connection connection = SQLConnection.CONN ();

                delete.startAnimation (bounce);

                if (connection != null) {

                    try {
                        String ID = id.getText ().toString ();

                        PreparedStatement pst = connection.prepareStatement (("DELETE FROM WarehouseItem WHERE ID = " + ID));

                        pst.executeUpdate ();

                        mAuth = FirebaseAuth.getInstance ();
                        String user = mAuth.getCurrentUser ().getEmail ();
                        pst = connection.prepareStatement ("INSERT INTO ChangeLog ([ItemName],[Action],[Time],[User]) VALUES (?,?,?,?)");
                        pst.setString (1, name.getText ().toString ());
                        pst.setString (2, "Item Deleted");
                        pst.setString (3, java.text.DateFormat.getDateTimeInstance ().format (new Date ()));
                        pst.setString (4, user);
                        pst.executeUpdate ();
                        Toast.makeText (activity_productdescription.this, "Successfully DELETED an item", Toast.LENGTH_SHORT).show ();

                    } catch (SQLException e) {
                        e.printStackTrace ();
                    }

                }

                startActivity (new Intent (activity_productdescription.this, activity_warehouse.class));
            }
        });

        update.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                /**Update Database*/

                Connection connection = SQLConnection.CONN ();

                update.startAnimation (blink);

                PreparedStatement pst = null;

                if (connection != null) {
                    Statement statement = null;
                    try {
                        // Creates a statement that is is sent to the server.
                        Bitmap bitmap = ((BitmapDrawable)img.getDrawable ()).getBitmap ();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        String btmap="";
                        /*for (byte b: byteArray) {
                            btmap += b;
                        }*/
                        bitmap.recycle ();
                        String sqldsffds = "UPDATE WarehouseItem SET [WarehouseID] = " + warehouseid.getText ().toString ()
                                + ",[Name] = '" + name.getText ().toString ()
                                + "',[Description] = '" + description.getText ().toString () +
                                /*+ "',[Image] = '" +btmap + */
                                "' WHERE ID = " + id.getText ().toString ();
                        statement = connection.createStatement ();
                        statement.executeUpdate (sqldsffds);
                        /*pst = connection.prepareStatement ("INSERT INTO WarehouseItem ([WarehouseID],[Name],[Description],[QRCode],[Height],[Lenght],[Width],[Image],[Quantity]) VALUES (?,?,?,?,?,?,?,?,?)");
                        pst.setInt (1, ids.get (arrayWarehouse.lastIndexOf (SWarehouse.getSelectedItem ().toString ())));
                        pst.setString (2, name.getText ().toString ());
                        pst.setString (3, description.getText ().toString ());
                        //pst.setString (4, barcodeid);
                        pst.setDouble (5, Double.parseDouble (heigth.getText ().toString ()));
                        pst.setDouble (6, Double.parseDouble (length.getText ().toString ()));
                        pst.setDouble (7, Double.parseDouble (width.getText ().toString ()));
                        pst.setBytes (8, byteArray);
                        pst.setInt (9, Integer.parseInt (quantity.getText ().toString ()));*/



                        Toast.makeText (activity_productdescription.this, "Successfully Updated an item", Toast.LENGTH_SHORT).show ();
                        mAuth = FirebaseAuth.getInstance ();
                        String user = mAuth.getCurrentUser ().getEmail ();
                        pst = connection.prepareStatement ("INSERT INTO ChangeLog ([ItemName],[Action],[Time],[User]) VALUES (?,?,?,?)");
                        pst.setString (1, name.getText ().toString ());
                        pst.setString (2, "Item Updated");
                        pst.setString (3, java.text.DateFormat.getDateTimeInstance ().format (new Date ()));
                        pst.setString (4, user);
                        pst.executeUpdate ();
                        startActivity (new Intent (activity_productdescription.this, activity_warehouse.class));

                    } catch (Exception e) {
                        Toast.makeText (activity_productdescription.this, "Unsuccessful", Toast.LENGTH_SHORT).show ();
                        e.printStackTrace ();
                    }
                } else
                    Toast.makeText (activity_productdescription.this, "Unsuccessful", Toast.LENGTH_SHORT).show ();
            }
        });

        img.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                pickfromgallery ();
            }
        });


        getdata ();
        setdata ();


        paint.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Bitmap bitmap = ((BitmapDrawable)img.getDrawable ()).getBitmap ();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                bitmap.recycle();

                Intent intent = new Intent (activity_productdescription.this, com.example.product_inventory_application.paint.class);
                intent.putExtra ("img", byteArray);
                intent.putExtra ("id", getIntent ().getIntExtra ("id",0));
                startActivity (intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        switch(requestCode){
            case MY_CAMERA_PERMISSION_CODE:{
                if (grantResults.length>0){
                    boolean camera_accepted = grantResults[0]==(PackageManager.PERMISSION_GRANTED);
                    boolean storage_accepted = grantResults[1]==(PackageManager.PERMISSION_GRANTED);
                    if(camera_accepted && storage_accepted){
                        pickfromgallery ();
                    }else{
                        Toast.makeText (this,"Please enable camera and storage permissions in phone settings", Toast.LENGTH_SHORT).show ();
                    }
                }
            }
        }
    }

    private void pickfromgallery() {
        CropImage.activity ().start (this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult (requestCode,resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult activityResult= CropImage.getActivityResult (data);
            if(resultCode==RESULT_OK){
                Uri resultUri= activityResult.getUri ();
                Picasso.with (this).load (resultUri).into (img);
            }
        }
    }

    private void setdata(){
        img.setImageBitmap (BitmapFactory.decodeByteArray (img1,0,img1.length));
        name.setText (d1);
        id.setText (""+d2);
        quantity.setText (""+d3);
        length.setText (""+d4);
        width.setText (""+d5);
        heigth.setText (""+d6);
        barcodeid.setText (d7);
        warehouseid.setText (""+d8);
        description.setText (d9);
    }

    private void getdata(){
        ArrayList<Product> products = getlistofproductstodisplay ();
        if(getIntent ().hasExtra ("id") ){
            d2 = getIntent ().getIntExtra ("id",0);
            for (Product product: products) {
                if(product.getId ()==d2)
                {
                    d1 = product.getName ();
                    d3 = product.getQuantity ();
                    d4=product.getLength ();
                    d5=product.getWidth ();
                    d6=product.getHeigth ();
                    d7=product.getBarcodeid ();
                    d8=product.getWarehouseid ();
                    d9=product.getDescription ();
                    img1=product.getImage ();
                }

            }
        }
        if(getIntent ().hasExtra ("imageexists") ){
            d2 = getIntent ().getIntExtra ("imageexists",0);
            for (Product product: products) {
                if(product.getId ()==d2)
                {
                    d1 = product.getName ();
                    d3 = product.getQuantity ();
                    d4=product.getLength ();
                    d5=product.getWidth ();
                    d6=product.getHeigth ();
                    d7=product.getBarcodeid ();
                    d8=product.getWarehouseid ();
                    d9=product.getDescription ();
                    Bundle extras = getIntent ().getExtras ();
                    if (extras.getByteArray ("imagefrompaint") != null) {
                        byte[] byteArray = extras.getByteArray ("imagefrompaint");
                        img1 = byteArray;
                    }
                    else{
                        img1 = product.getImage ();
                    }
                }

            }
        }
        if(getIntent ().hasExtra ("exists")) {
            d7 = getIntent ().getStringExtra ("exists");
            for (Product product : products) {
                if (d7.equals (product.getBarcodeid() )) {
                    d1 = product.getName ();
                    d3 = product.getQuantity ();
                    d4 = product.getLength ();
                    d5 = product.getWidth ();
                    d6 = product.getHeigth ();
                    d2 = product.getId ();
                    d8 = product.getWarehouseid ();
                    d9 = product.getDescription ();
                    Bundle extras = getIntent ().getExtras ();
                    img1 = product.getImage ();

                }
            }
        }
        else{
            Toast.makeText (this,"No Data to display", Toast.LENGTH_SHORT).show ();
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
