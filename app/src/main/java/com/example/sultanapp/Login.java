package com.example.sultanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginbtn;
    TextView mCreateBtn, forogtTextLink;
    FirebaseAuth fAuth;
    ProgressBar ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPassword = findViewById(R.id.password);
        mEmail = findViewById(R.id.Email);
        ProgressBar = findViewById(R.id.progressBar2);
        mLoginbtn = findViewById(R.id.registerbtn);
        mCreateBtn = findViewById(R.id.createtext);
        fAuth =FirebaseAuth.getInstance();
        forogtTextLink = findViewById(R.id.textView4);


        mLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    mPassword.setError("password is required");
                    return;}

                if (password.length() < 6 ) {
                    mPassword.setError("password must be >= 6 char ");
                }

                ProgressBar.setVisibility(View.VISIBLE);

                // auth the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "logged is successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        } else {
                            Toast.makeText(Login.this, "error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            ProgressBar.setVisibility(View.GONE);

                        }
                    }
                });
            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        forogtTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder PasswordResetDialog = new AlertDialog.Builder(v.getContext());
                PasswordResetDialog.setTitle("Reset Password ? ");
                PasswordResetDialog.setMessage(" Enter your Email To Receive Reset Link ");
                PasswordResetDialog.setView(resetMail);

                PasswordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "reset link sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "reset link is not sent " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                PasswordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // closing dialog
                    }
                });
                PasswordResetDialog.create().show();
            }
        });


    }
}