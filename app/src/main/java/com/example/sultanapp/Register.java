package com.example.sultanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.FullName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.registerbtn);
        mLoginBtn = findViewById(R.id.createtext);

        fAuth = FirebaseAuth.getInstance();
        ProgressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
                String FullName = mFullName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    mPassword.setError("password is required");
                    return;}
                if(TextUtils.isEmpty(phone)) {
                    mPhone.setError("phone is required");
                    return;}
                if(TextUtils.isEmpty(FullName)) {
                    mFullName.setError("FullName is required");
                    return;}


                if (password.length() < 6 ) {
                    mPassword.setError("Minimum 6 characters required");
                    mPassword.requestFocus();
                }


                ProgressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "user created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else {
                            Toast.makeText(Register.this, "error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            ProgressBar.setVisibility(View.GONE);

                        }
                    }
                });

            }
        });

         mLoginBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(),Login.class));
             }
         });

    }
}