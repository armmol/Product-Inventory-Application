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

public class activity_signup extends AppCompatActivity {
    private EditText edtxt_email ,edtxt_password, edtxt_confirmpassword;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button btn_signup = findViewById(R.id.btn_SignUp);
        edtxt_email = findViewById(R.id.edtxt_SignUpEmail);
        edtxt_password = findViewById(R.id.edtxt_SignUpPassword);
        edtxt_confirmpassword = findViewById(R.id.edtxt_SignUpConfirmPassword);
        TextView txt_signin = findViewById(R.id.txt_signin);

        mAuth = FirebaseAuth.getInstance();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_signup.this, activity_signin.class));
            }
        });
    }
    private void createAccount() {
        String email = edtxt_email.getText().toString();
        String password = edtxt_password.getText().toString();

        if (email.isEmpty()) {
            edtxt_email.setError("Enter E-mail");
            edtxt_email.requestFocus();
        }else if(password.isEmpty()){
            edtxt_password.setError("Enter Password");
            edtxt_password.requestFocus();
        }else if(!password.equals(password)){
            edtxt_confirmpassword.setError("Passwords do not match");
            edtxt_confirmpassword.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity_signup.this,"User created successfully. Continue by Loggging in.",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(activity_signup.this, activity_signin.class));
                    }else{
                        Toast.makeText(activity_signup.this,"Account creation Error: "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
