package com.example.loginpart.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.loginpart.R;
import com.example.loginpart.model.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

//import static com.example.loginpart.ui.leaderBoard.LeaderBoardFragment.TAG;
import static java.lang.Integer.parseInt;

public class HomeFragment extends Fragment {

    private Context context;

    Button artifact1, artifact2, artifact3, artifact4, artifact5, artifact6, artifact7;
    Button artifact8, artifact9, artifact11, artifact12;
    Button playerButton;
    Button btnMove,btnReset;
    Button btnDeneme;

    ProgressBar progressBar;

    private String artifactInput;

    final ArtifactModel artifactItem = null;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private DocumentReference documentReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.context = getContext();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private Point getPointOfView(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new Point(location[0], location[1]);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState){

        playerButton = view.findViewById(R.id.btnPlayer);

        artifact1 = view.findViewById(R.id.btnCollection1);
        artifact2 = view.findViewById(R.id.btnCollection2);
        artifact3 = view.findViewById(R.id.btnCollection3);
        artifact4 = view.findViewById(R.id.btnCollection4);
        artifact5 = view.findViewById(R.id.btnCollection5);
        artifact6 = view.findViewById(R.id.btnCollection6);
        artifact7 = view.findViewById(R.id.btnCollection7);
        artifact8 = view.findViewById(R.id.btnCollection8);
        artifact9 = view.findViewById(R.id.btnCollection9);
        artifact11 = view.findViewById(R.id.btnCollection11);
        artifact12 = view.findViewById(R.id.btnCollection12);

        btnMove = view.findViewById(R.id.btnArtifact);
        btnReset = view.findViewById(R.id.btnReset);


        playerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Point p = getPointOfView(playerButton);


                Log.d("Coordinates","Coordinates x:" + p.x + " and y: " + p.y);


            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseFirestore = FirebaseFirestore.getInstance();

        //ezginin eklediği kısımlar--------------
        progressBar = view.findViewById(R.id.progressBar_home);
        progressBar.setMax(50);
        //Bunu buraya ekleyeceğim ama emin değilim yeri değişebilir.
        //int userPoint = getPoint();
        //progressBar.setProgress(userPoint);
        //int a = 5;
        //-----------------------

        btnDeneme = view.findViewById(R.id.btnDeneme);

        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputArtifact = new EditText(context);
                final AlertDialog.Builder artifactDialog = new AlertDialog.Builder(context);

                artifactDialog.setTitle("Artifact ID");
                artifactDialog.setMessage("Enter an artifact ID to move");
                artifactDialog.setView(inputArtifact);

                artifactDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputString = inputArtifact.getText().toString();
                        Log.d("Artifact ID: ", inputString);
                        movementFunction(inputString);

                    }
                });

                artifactDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel
                    }
                });

                artifactDialog.create().show();

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerButton.setX(50);
                playerButton.setY(50);
            }
        });


    }
    //Toast.makeText(context, "No artifact",Toast.LENGTH_LONG);
    private void movementFunction(final String inputString){

        documentReference = firebaseFirestore.collection("artifacts").document(inputString);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){
                    String str = documentSnapshot.getString("name");
                    if(str.equals("deneme")){
                        btnDeneme.setBackgroundColor(btnDeneme.getContext().getResources().getColor(R.color.Green));
                    }
                    Log.d("Document name", str);


                    //ezginin eklediği kısımlar--------------------
                    artifactItem.setName(documentSnapshot.getString("name"));
                    artifactItem.setCategory(documentSnapshot.getString("category"));
                    artifactItem.setPoint(parseInt(documentSnapshot.getString("point")));

                    updatePoint(parseInt(documentSnapshot.getString("point")));
                    //------------------------------

                } else{
                    Log.d("No document","Document error");
                    //Toast.makeText(context,"No document",Toast.LENGTH_LONG);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

    private void updatePoint(int artifactPoint) {
        final int[] userPoint = {0};

        documentReference = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){
                    userPoint[0] = parseInt(documentSnapshot.getString("point"));

                } else{
                    Log.d("No document","Document error");
                    //Toast.makeText(context,"No document",Toast.LENGTH_LONG);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        documentReference.update("point", userPoint[0] + artifactPoint);
    }

    private int getPoint() {

        final int[] userPoint = new int[1];
        String uID;
        uID = firebaseAuth.getCurrentUser().getUid();

        documentReference = firebaseFirestore.collection("users").document(uID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            //@RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    userPoint[0] = parseInt(documentSnapshot.getString("point"));
                    int a = 0;

                } else{
                    Log.d("No document","Document error");
                    //Toast.makeText(context,"No document",Toast.LENGTH_LONG);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });



        int usPoint = Integer.parseInt(String.valueOf(userPoint[0]));

        return usPoint;

    }
}