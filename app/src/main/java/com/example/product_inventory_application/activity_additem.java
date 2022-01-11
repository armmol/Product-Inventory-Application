package com.example.product_inventory_application;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class activity_additem extends AppCompatActivity {

    private TextView Name, Quantity, Description, Weight, Height, Length;
    private Button btn;
    private FirebaseAuth mAuth;
    ImageButton img;
    public int WarehouseID;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_additem);

        mAuth = FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser ().getEmail ().toString ();

        btn = findViewById (R.id.btn_additemmanually);


        Name = findViewById (R.id.edtxt_name);
        Quantity = findViewById (R.id.edtxt_quantity);
        Description = findViewById(R.id.edtxt_description);
        Weight = findViewById (R.id.edtxt_weight);
        Height = findViewById(R.id.edtxt_height);
        Length = findViewById(R.id.edtxt_lenght);
        img = findViewById (R.id.imgbtn_additem);
        Spinner SWarehouse;


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

        SWarehouse = findViewById(R.id.warehouse_spinner);
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


        img.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                pickfromgallery ();
            }
        });

        btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {

                if (Name.getText () == null || Quantity.getText () == null) {
                    Name.setError ("Enter Name!");
                    Quantity.setError ("Set Quantity");
                } else {
                    if(Description.getText ()== ""||Description.getText () == null)
                        Description.setText ("");
                    if(Length.getText ()== ""||Length.getText () == null)
                        Length.setText ("0");
                    if(Weight.getText ()== ""||Weight.getText () == null)
                        Weight.setText ("0");
                    if(Height.getText ()== ""||Height.getText () == null)
                        Height.setText ("0");
                    Bundle extras = getIntent ().getExtras ();

                    String QrID = extras.getString ("key");

                    Bitmap bitmap = ((BitmapDrawable)img.getDrawable ()).getBitmap ();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    bitmap.recycle();

                    Connection connection = SQLConnection.CONN ();

                    PreparedStatement pst = null;

                    if (connection != null) {
                        Statement statement = null;
                        try {
                            // Creates a statement that is is sent to the server.
                            statement = connection.createStatement ();
                            //ResultSet resultSet = statement.executeQuery("INSERT INTO WarehouseItem ,([WarehouseID],[Name],[Description],[BarcodeID],[Weight],[Height],[Lenght],[Width],[IMAGE]) VALUES (?,?,?,?,?,?,?,?,?)");
                            pst = connection.prepareStatement ("INSERT INTO WarehouseItem ([WarehouseID],[Name],[Description],[QRCode],[Height],[Lenght],[Width],[Image],[Quantity]) VALUES (?,?,?,?,?,?,?,?,?)");
                            pst.setInt (1, ids.get (arrayWarehouse.lastIndexOf (SWarehouse.getSelectedItem ().toString ())));
                            pst.setString (2, Name.getText ().toString ());
                            pst.setString (3, Description.getText ().toString ());
                            pst.setString (4, QrID);
                            pst.setDouble (5, Double.parseDouble (Height.getText ().toString ()));
                            pst.setDouble (6, Double.parseDouble (Length.getText ().toString ()));
                            pst.setDouble (7, Double.parseDouble (Weight.getText ().toString ()));
                            pst.setBytes (8, byteArray);
                            pst.setInt (9, Integer.parseInt (Quantity.getText ().toString ()));

                            pst.executeUpdate ();
                            Toast.makeText (activity_additem.this, "Successfully added an item", Toast.LENGTH_SHORT).show ();

                            pst = connection.prepareStatement ("INSERT INTO ChangeLog ([ItemName],[Action],[Time],[User]) VALUES (?,?,?,?)");
                            pst.setString (1, Name.getText ().toString ());
                            pst.setString (2, "Item Added");
                            pst.setString (3, java.text.DateFormat.getDateTimeInstance ().format (new Date ()));
                            pst.setString (4, user);

                            pst.executeUpdate ();

                            startActivity (new Intent (activity_additem.this, activity_warehouse.class));


                        } catch (SQLException e) {
                            e.printStackTrace ();
                        }
                    } else
                        Toast.makeText (activity_additem.this, "Unsuccessful", Toast.LENGTH_SHORT).show ();
                }
            }
        });
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

}