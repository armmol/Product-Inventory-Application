package com.example.product_inventory_application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class activity_forgotpassword extends AppCompatActivity {

    EditText edtxt_email;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        Button btn_forgotpassword = findViewById(R.id.btn_ForgotPassword);
        TextView txt_backtologin  = findViewById(R.id.txt_backtoLogin);
        edtxt_email = findViewById(R.id.edtxt_ForgotPasswordEmail);

        mAuth = FirebaseAuth.getInstance();

        btn_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpassword();
            }
        });

        txt_backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_forgotpassword.this, activity_login.class
                ));
            }
        });


    }

    private void resetpassword() {
        String email = edtxt_email.getText().toString();

        if(email.isEmpty()){
            edtxt_email.setError("Enter your account E-mail");
            edtxt_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtxt_email.setError("Please provide valif E-mail");
            edtxt_email.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(activity_forgotpassword.this,"Check E-mail to reset Password",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity_forgotpassword.this, activity_login.class));
                }else{
                    Toast.makeText(activity_forgotpassword.this,"Reset Password Error: ",Toast.LENGTH_SHORT).show();
                    edtxt_email.requestFocus();
                }
            }
        });

    }
}
