package com.example.product_inventory_application;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private static final int STORAGE_REQUEST = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Animation leftorright = AnimationUtils.loadAnimation (this, R.anim.bounce);
        Animation zoomin = AnimationUtils.loadAnimation (this, R.anim.blink_anim);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions (new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        Button btn_opensignin = findViewById(R.id.btn_GoToSignInPage);
        Button btn_opensignup = findViewById(R.id.btn_GoToSignUpPage);
        ImageButton txt_exit = findViewById(R.id.btn_exit);


        btn_opensignin.setOnClickListener(view ->{
            btn_opensignin.setAnimation (zoomin);
            startActivity(new Intent(activity_login.this, activity_signin.class));
        });

        btn_opensignup.setOnClickListener(view ->{
            btn_opensignup.setAnimation (leftorright);
            startActivity(new Intent(activity_login.this, activity_signup.class));
        });

        txt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish ();
                System.exit(0);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(activity_login.this, activity_home.class));
        }
    }


}
