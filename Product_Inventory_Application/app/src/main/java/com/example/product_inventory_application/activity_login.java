package com.example.product_inventory_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        Button btn_opensignin = findViewById(R.id.btn_GoToSignInPage);
        Button btn_opensignup = findViewById(R.id.btn_GoToSignUpPage);
        TextView txt_exit = findViewById(R.id.txt_exit);


        btn_opensignin.setOnClickListener(view ->{
            startActivity(new Intent(activity_login.this, activity_signin.class));
        });

        btn_opensignup.setOnClickListener(view ->{
            startActivity(new Intent(activity_login.this, activity_signup.class));
        });

        txt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
