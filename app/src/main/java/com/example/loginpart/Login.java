package com.example.loginpart;

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

    EditText email,password;
    Button loginBtn;
    TextView txtNewMember,forgotPW;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPassword);
        progressBar = findViewById(R.id.progressBarLog);
        firebaseAuth = firebaseAuth.getInstance();
        loginBtn = findViewById(R.id.btnLogin);
        txtNewMember = findViewById(R.id.txtNewMember);
        forgotPW = findViewById(R.id.txtForgotPW);
        builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailString = email.getText().toString().trim();
                String pw = password.getText().toString().trim();

                if (TextUtils.isEmpty(emailString)){
                    email.setError("Email is required!");
                    return;
                }

                if (TextUtils.isEmpty(pw)){
                    password.setError("Password is required!");
                    return;
                }

                if (pw.length() < 6){
                    password.setError("Password must be more than 6 characters!");
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                //authenticate the user

                firebaseAuth.signInWithEmailAndPassword(emailString,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Successfully logged in",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),navigation_drawer.class));
                        }
                        else{
                            Toast.makeText(Login.this,"Login Error !" + task.getException().getMessage(), Toast.LENGTH_LONG);
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });

        txtNewMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });



        forgotPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passResetDialog = new AlertDialog.Builder(v.getContext());
                passResetDialog.setTitle("Reset your password");
                passResetDialog.setMessage("Enter your email in order to reset your password");
                passResetDialog.setView(resetMail);

                passResetDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset link sent your email address", Toast.LENGTH_SHORT);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error ! Can not sent the link " + e.getMessage(), Toast.LENGTH_SHORT);
                            }
                        });
                    }
                });

                passResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                             //Close dialog
                    }
                });

                passResetDialog.create().show();


            }
        });
    }

    public EditText getPassword(){
        return password;
    }
}
