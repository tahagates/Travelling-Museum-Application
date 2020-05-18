package com.example.loginpart.ui.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.loginpart.*;
import com.example.loginpart.R;
import com.example.loginpart.model.BadgeModel;
import com.example.loginpart.model.UserModel;
import com.example.loginpart.ui.leaderBoard.CustomAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class ProfileFragment extends Fragment {

    private Context context;
    TextView fName,email,job,age,verifyMsg;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Button btnVerify, changePassword,changeMail,btnLogout;

    String userID;
    AlertDialog.Builder builder;


    public static final String TAG = "TAG";
    public ArrayList<BadgeModel> badgeList = new ArrayList<>();
    public GridView gridview;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.context = getContext();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        //badgeler için
        gridview = (GridView) view.findViewById(R.id.gridView);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


        Task<QuerySnapshot> documentReference = firebaseFirestore.collection("badges")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> badge = new HashMap<>();
                                badge = document.getData();

                                String name = (String) badge.get("name");
                                String description = (String) badge.get("description");
                                ArrayList<Integer> path = new ArrayList<>();
                                path = (ArrayList<Integer>) badge.get("path");
                                //buraya bir kontrol eklenecek
                                badgeList.add(new BadgeModel(name,description,path));
/*
                                if(user.containsKey("age"))
                                {
                                    Log.d(TAG, "yes");
                                }*/
                            }
                            BadgeAdapter adapter = new BadgeAdapter(getActivity(), badgeList);
                            gridview.setAdapter(adapter);
                            int a = 5;
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                }
                );

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        fName = view.findViewById(R.id.txtMainName);
        email = view.findViewById(R.id.txtMainMail);
        job = view.findViewById(R.id.txtMainJob);
        age = view.findViewById(R.id.txtMainAge);

        //btnVerify = view.findViewById(R.id.btnVerify);
        verifyMsg = view.findViewById(R.id.txtVerifyMail);
        changePassword = view.findViewById(R.id.ch_password);
        changeMail = view.findViewById(R.id.ch_email);
        btnLogout = view.findViewById(R.id.btnLogoutProfile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);



        userID = firebaseAuth.getCurrentUser().getUid();
        final FirebaseUser user = firebaseAuth.getCurrentUser();


        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fName.setText(documentSnapshot.getString("fullName"));
                email.setText(documentSnapshot.getString("email"));
                job.setText(documentSnapshot.getString("job"));
                age.setText(documentSnapshot.getString("age"));
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(context, Login.class);
                //getActivity().finish();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(context);
                final AlertDialog.Builder passResetDialog = new AlertDialog.Builder(context);
                passResetDialog.setTitle("Recover your password");
                passResetDialog.setMessage("Enter your mail for recover link");
                passResetDialog.setView(resetMail);

                passResetDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(context, "Reset link sent your email address", Toast.LENGTH_SHORT);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Error ! Can not sent the link " + e.getMessage(), Toast.LENGTH_SHORT);
                            }
                        });
                    }
                });

                passResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Close
                    }
                });

                passResetDialog.create().show();
            }
        });

        changeMail.setOnClickListener(new View.OnClickListener() {

            private static final String TAG = "ProfileFragment";

            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(context);
                final AlertDialog.Builder mailResetDialog = new AlertDialog.Builder(context);
                mailResetDialog.setTitle("Reset your email address");
                mailResetDialog.setMessage("Enter your new email address");
                mailResetDialog.setView(resetMail);


                mailResetDialog.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newMail = resetMail.getText().toString();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(newMail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Your email address is changed.", Toast.LENGTH_SHORT);
                                }
                            }
                        });
                    }
                });
                mailResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                mailResetDialog.create().show();

            }
        });
    }


}

