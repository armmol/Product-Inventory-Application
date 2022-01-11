package com.example.product_inventory_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activity_signin extends AppCompatActivity {

    private EditText edtxt_email ,edtxt_password;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button btn_signin = findViewById(R.id.btn_SignIn);
        edtxt_email = findViewById(R.id.edtxt_SignInEmail);
        edtxt_password = findViewById(R.id.edtxt_SignInPassword);
        TextView txt_signup = findViewById(R.id.txt_signup);
        TextView txt_forgotpassword = findViewById(R.id.txt_forgotpassword);

        mAuth = FirebaseAuth.getInstance();

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIntoAccount();
            }
        });

        txt_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_signin.this, activity_forgotpassword.class));
            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_signin.this, activity_signup.class));
            }
        });
    }
    private void loginIntoAccount() {
        String email = edtxt_email.getText().toString();
        String password = edtxt_password.getText().toString();

        if (email.isEmpty()) {
            edtxt_email.setError("Enter E-mail");
            edtxt_email.requestFocus();
        }else if(password.isEmpty()){
            edtxt_password.setError("Enter Password");
            edtxt_password.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity_signin.this,"User logged in successfully.",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(activity_signin.this,activity_home.class));
                    }else{
                        Toast.makeText(activity_signin.this,"Login Error: "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        edtxt_email.requestFocus();
                        edtxt_password.requestFocus();
                    }
                }
            });
        }
    }
}
