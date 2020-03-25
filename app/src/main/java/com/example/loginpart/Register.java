package com.example.loginpart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText fullName, eMail, password,password2;
    Button registerButton;
    TextView txtLogin;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    String job,age;
    Spinner jobSpinner, ageSpinner;
    FirebaseFirestore firebaseFirestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        jobSpinner = (Spinner) findViewById(R.id.spinnerJob);
        ageSpinner = (Spinner) findViewById(R.id.spinnerAge);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(Register.this
                , android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.jobs));
        //myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSpinner.setAdapter(myAdapter);

        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<>(Register.this
                , android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.ages));
        //myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(myAdapter2);

        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                job = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                age = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        fullName = findViewById(R.id.txtName);
        eMail = findViewById(R.id.txtEmailReg);
        password = findViewById(R.id.txtPasswordReg);
        password2 = findViewById(R.id.txtPassword2Reg);
        registerButton = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtAlreadyReg);
        progressBar = findViewById(R.id.progressBarReg);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        /*//User already logged in
        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }*/

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = fullName.getText().toString();
                final String email = eMail.getText().toString().trim();
                final String pw1 = password.getText().toString().trim();
                String pw2 = password2.getText().toString().trim();
                final String jobString = job;
                final String ageString = age;



                if(TextUtils.isEmpty(name)){
                    fullName.setError("Your full name is required!");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    eMail.setError("Email is required!");
                    return;
                }

                if(TextUtils.isEmpty(pw1)){
                    password.setError("Password is required!");
                    return;
                }

                if(TextUtils.isEmpty(pw2)){
                    password2.setError("Password is required!");
                    return;
                }

                if(!(TextUtils.equals(pw1,pw2))){
                    password.setError("Passwords must be matched!");
                    password2.setError("Passwords must be matched!");
                    return;
                }

                if(pw1.length() <6 || pw2.length() < 6){
                    password.setError("Passwords must be longer than six characters!");
                    password2.setError("Passwords must be longer than six characters!");
                    return;
                }

                if(job.equals("Select Your Job")){
                    Toast.makeText(Register.this,"Please select a job to proceed!", Toast.LENGTH_SHORT).show();
                }

                if(age.equals("Select Your Age")){
                    Toast.makeText(Register.this,"Please select an age to proceed!", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.VISIBLE);

                //Register user in fireebase
                firebaseAuth.createUserWithEmailAndPassword(email,pw1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // Send verification link

                            FirebaseUser fuser = firebaseAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this,"Verification Email has been sent.",Toast.LENGTH_SHORT);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure: Email not sent " + e.getMessage());
                                }
                            });


                            Toast.makeText(Register.this,"Successfully registered",Toast.LENGTH_SHORT).show();

                            userID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fullName",name);
                            user.put("email",email);
                            user.put("job",jobString);
                            user.put("age",ageString);
                            user.put("password",pw1);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user profile is created for " + userID);
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }else{
                            Toast.makeText(Register.this,"Registration Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }
}

/*
builder.setTitle("Job Warning!");
                    builder.setMessage("Please select a job to proceed.");
                    builder.setPositiveButton("I understand", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
 */
