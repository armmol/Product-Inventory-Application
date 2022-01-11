package com.example.product_inventory_application;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class activity_home extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Button btn_openmap, btn_openwarehouse,  btn_logout, btn_openhistory, browse;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Animation leftorright = AnimationUtils.loadAnimation (this, R.anim.bounce);
        Animation rotate = AnimationUtils.loadAnimation (this, R.anim.bounce);
        Animation mixed = AnimationUtils.loadAnimation (this, R.anim.blink_anim);
        Animation bounce = AnimationUtils.loadAnimation (this, R.anim.bounce);
        Animation zoomin = AnimationUtils.loadAnimation (this, R.anim.blink_anim);

        btn_openmap = findViewById(R.id.btn_map);
        btn_openwarehouse= findViewById(R.id.btn_warehouse);
        btn_logout = findViewById(R.id.btn_logout);
        btn_openhistory = findViewById (R.id.btn_activityhistory);
        browse = findViewById (R.id.btn_browse);
        mAuth = FirebaseAuth.getInstance();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_logout.setAnimation (mixed);
                mAuth.signOut();
                startActivity(new Intent(activity_home.this, activity_login.class));
            }
        });
        btn_openmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_openmap.setAnimation (rotate);
                startActivity(new Intent(activity_home.this, activity_map.class));
            }
        });

        btn_openwarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_openwarehouse.setAnimation (leftorright);
                startActivity(new Intent(activity_home.this, activity_warehouse.class));
            }
        });

        btn_openhistory.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                btn_openhistory.setAnimation (zoomin);
                startActivity (new Intent (activity_home.this, activity_history.class));
            }
        });

        browse.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                browse (v);
            }
        });


        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent newintent = new Intent(activity_home.this, activity_additem.class);
                        Intent newintent2 = new Intent(activity_home.this, activity_productdescription.class);
                        Toast.makeText(activity_home.this, result.getText(), Toast.LENGTH_SHORT).show();
                        Connection connection = SQLConnection.CONN();
                        Statement statement = null;
                        ArrayList<Product> products = new ArrayList<> ();
                        if (connection != null) {
                            try {
                                statement = connection.createStatement();
                                ResultSet resultSet = statement.executeQuery("SELECT * FROM WarehouseItem");
                                while (resultSet.next()) {
                                    products.add(new Product(resultSet.getString(3), resultSet.getInt(1),
                                            resultSet.getInt(10), resultSet.getInt(2), resultSet.getString(5),
                                            resultSet.getString(4), resultSet.getDouble(8), resultSet.getDouble(6), resultSet.getDouble(7), resultSet.getBytes (9)));
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                        int flag =0;
                        if(flag == 0) {
                            ArrayList<String> barcode = new ArrayList<> ();
                            for (Product product : products) {
                                if (result.getText ().toString ().equals (product.getBarcodeid ())) //Is in a data base CHANGE
                                {
                                    newintent2.putExtra ("key2", result.getText ());
                                    newintent2.putExtra ("Scanner", true);
                                    startActivity (newintent2);
                                }
                                else
                                    flag = 1;
                            }
                        }
                        else if(flag == 1){
                            newintent.putExtra("key",result.getText());
                            startActivity(newintent);
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_CAMERA_PERMISSION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Start your camera handling here
                } else {
                    Toast.makeText(this, "You declined to allow the app to access your camera", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    public void browse(View view){
        Intent intentpicture  = new Intent (Intent.ACTION_PICK);
        intentpicture.setType ("image/*");
        startActivityForResult (intentpicture,1000);
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            try {
                final Uri image = data.getData();
                final InputStream imagestr = getContentResolver().openInputStream(image);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imagestr);
                try {

                    Bitmap bMap = selectedImage;
                    String resultimage = null;
                    int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
                    LuminanceSource source = new RGBLuminanceSource (bMap.getWidth(), bMap.getHeight(), intArray);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer (source));
                    Reader reader = new MultiFormatReader ();
                    Result result = reader.decode(bitmap);
                    resultimage = result.getText();
                    Intent newintent = new Intent(activity_home.this, activity_additem.class);
                    Intent newintent2 = new Intent(activity_home.this, activity_productdescription.class);
                    Toast.makeText(activity_home.this, result.getText(), Toast.LENGTH_SHORT).show();
                    ArrayList<Product> products = getlistofproductstodisplay ();
                    int flag = 1; String a = result.getText ();
                    for (Product product:products) {
                        if(a.equals (product.getBarcodeid ()) ){
                            newintent2.putExtra ("exists", result.getText ().toString ());
                            startActivity (newintent2);
                        }
                        else flag=0;
                    }
                    if(flag==0){
                        newintent.putExtra("key",result.getText());
                        startActivity(newintent);
                    }
                    Toast.makeText(getApplicationContext(),resultimage,Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(activity_home.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }



        }else {

            Toast.makeText(activity_home.this, "Image not selected",Toast.LENGTH_LONG).show();

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