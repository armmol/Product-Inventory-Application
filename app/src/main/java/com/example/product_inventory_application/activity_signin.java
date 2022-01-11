package com.example.product_inventory_application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    public static final String Shared_prefs = "sharedprefs";
    public static final String email = "email";
    public static final String password = "password";
    private String texte, textp;
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

        edtxt_password.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged (Editable s) {
                savestate ();
            }
        });

        loadstate ();
        updatestate ();

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

    public void savestate(){
        SharedPreferences sharedPreferences = getSharedPreferences (Shared_prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit ();
        editor.putString (email, edtxt_email.getText ().toString ());
        editor.putString (password, edtxt_password.getText ().toString ());
        editor.apply ();
    }

    public void loadstate(){
        SharedPreferences sharedPreferences = getSharedPreferences (Shared_prefs, MODE_PRIVATE);
        texte = sharedPreferences.getString (email, "");
        textp = sharedPreferences.getString (password,"");
    }

    public void updatestate(){
        edtxt_email.setText (texte);
        edtxt_password.setText (textp);
    }
}
