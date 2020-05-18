package com.example.loginpart.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.example.loginpart.model.ArtifactModel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.loginpart.R;
import com.example.loginpart.model.*;
import com.example.loginpart.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.Nullable;

import static com.example.loginpart.ui.leaderBoard.LeaderBoardFragment.TAG;

import com.example.loginpart.ui.profile.ProfileFragment;

import static java.lang.Integer.parseInt;

public class HomeFragment extends Fragment {

    private Context context;

    Button artifact1, artifact2, artifact3, artifact4, artifact5, artifact6, artifact7;
    Button artifact8, artifact9, artifact11, artifact12;
    Button playerButton;
    Button btnMove, btnReset;
    Button btnDeneme;

    TextView tv_level;

    ArtifactModel art1 = new ArtifactModel("Art1", null, 1);
    ArtifactModel art2 = new ArtifactModel("Art2", null, 1);
    ArtifactModel art3 = new ArtifactModel("Art3", null, 1);
    ArtifactModel art4 = new ArtifactModel("Art4", null, 1);
    ArtifactModel art5 = new ArtifactModel("Art5", null, 1);
    ArtifactModel art6 = new ArtifactModel("Art6", null, 1);
    ArtifactModel art7 = new ArtifactModel("Art7", null, 1);
    ArtifactModel art8 = new ArtifactModel("Art8", null, 1);
    ArtifactModel art9 = new ArtifactModel("Art9", null, 1);
    ArtifactModel art11 = new ArtifactModel("Art11", null, 1);
    ArtifactModel art12 = new ArtifactModel("Art12", null, 1);
    ArtifactModel[] artButtons = {art1, art2, art3, art4, art5, art6, art7, art8, art9, art11, art12};


    ProgressBar progressBar;

    private String artifactInput;

    String userID = "";

    Object userPoint;
    Object userLevel;


    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private DocumentReference documentReference;
    private DocumentReference documentReferenceUser;

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
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

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

        tv_level = view.findViewById(R.id.tv_level);


        playerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Point p = getPointOfView(playerButton);


                Log.d("Coordinates", "Coordinates x:" + p.x + " and y: " + p.y);


            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseFirestore = FirebaseFirestore.getInstance();

        //Progress barın doldurulması--------------

        progressBar = view.findViewById(R.id.progressBar_home);
        progressBar.setMax(50);

        userID = firebaseAuth.getCurrentUser().getUid();

        documentReferenceUser = firebaseFirestore.collection("users").document(userID);
        documentReferenceUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    userPoint = documentSnapshot.get("point");
                    progressBar.setProgress((Integer.parseInt(userPoint.toString()))%50);
                    //level alınması
                    userLevel = documentSnapshot.get("level");
                    tv_level.setText(userLevel.toString());

                } else {
                    Log.d("No document", "Document error");
                    //Toast.makeText(context,"No document",Toast.LENGTH_LONG);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });
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
    final String[] artName = {""}; //??
    List<String> artPath = new ArrayList<String>(); //The path that holds artifacts' ids

    private void movementFunction(final String inputString) {
        final Object[] artifactPoint = new Object[1];
        final String artString = findArt(inputString);
        artPath.add(inputString);
        documentReference = firebaseFirestore.collection("artifacts").document(artString);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    Point p;
                    switch (inputString) {
                        case "c1":
                            artifact1.setBackgroundColor(artifact1.getContext().getResources().getColor((R.color.Green)));
                            p = getPointOfView(artifact1);
                            playerButton.setX(p.x - 30);
                            playerButton.setY(p.y - 150);
                            break;
                        case "c2":
                            artifact2.setBackgroundColor(artifact2.getContext().getResources().getColor((R.color.Green)));
                            p = getPointOfView(artifact2);
                            break;
                        case "c3":
                            artifact3.setBackgroundColor(artifact3.getContext().getResources().getColor((R.color.Green)));
                            p = getPointOfView(artifact3);
                            break;
                        case "c4":
                            artifact4.setBackgroundColor(artifact4.getContext().getResources().getColor((R.color.Green)));
                            p = getPointOfView(artifact4);
                            break;
                        case "c5":
                            artifact5.setBackgroundColor(artifact5.getContext().getResources().getColor((R.color.Green)));
                            p = getPointOfView(artifact5);
                            break;
                        case "c6":
                            artifact6.setBackgroundColor(artifact6.getContext().getResources().getColor((R.color.Green)));
                            p = getPointOfView(artifact6);
                            break;
                        case "c7":
                            artifact7.setBackgroundColor(artifact7.getContext().getResources().getColor((R.color.Green)));
                            p = getPointOfView(artifact7);
                            break;
                        case "c8":
                            artifact8.setBackgroundColor(artifact8.getContext().getResources().getColor((R.color.Green)));
                            p = getPointOfView(artifact8);
                            break;
                        case "c9":
                            artifact9.setBackgroundColor(artifact9.getContext().getResources().getColor((R.color.Green)));
                            p = getPointOfView(artifact9);
                            break;
                        case "c11":
                            artifact11.setBackgroundColor(artifact11.getContext().getResources().getColor((R.color.Green)));
                            p = getPointOfView(artifact11);
                            break;
                        case "c12":
                            artifact12.setBackgroundColor(artifact12.getContext().getResources().getColor((R.color.Green)));
                            p = getPointOfView(artifact12);
                            break;


                    }



                    //--artifact puanını usera ekleme
                    artifactPoint[0] = documentSnapshot.get("point");
                    userPoint = (Integer.parseInt(userPoint.toString()) + Integer.parseInt(artifactPoint[0].toString()));
                    int oldProgress = progressBar.getProgress();
                    documentReferenceUser.update("point", userPoint);
                    progressBar.setProgress((Integer.parseInt(userPoint.toString()))%50);
                    int newProgress = progressBar.getProgress();
                    //---------level update
                    if(newProgress < oldProgress)
                    {
                        userLevel = Integer.parseInt(userLevel.toString()) + 1 ;
                        documentReferenceUser.update("level", userLevel);
                        tv_level.setText(userLevel.toString());
                    }

                    Log.d("Document name", artName[0]);
                } else {
                    Log.d("No document", "Document error");
                    //Toast.makeText(context,"No document",Toast.LENGTH_LONG);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });

    }

    private String findArt(String inputString) {
        if (inputString.toLowerCase().trim().equals("c1")) {
            inputString = "0VxYe73NJNXBUujv8fY5";
            artName[0] = "Matisse özgün baskısı";
        } else if (inputString.toLowerCase().trim().equals("c2")) {
            inputString = "1Duxzjk86xH8PLw426tK";
            artName[0] = "papirüs";
        } else if (inputString.toLowerCase().trim().equals("c3")) {
            inputString = "4Jtu36bh2fBJGmqZ3RNY";
            artName[0] = "One world- dünya haritası";
        } else if (inputString.toLowerCase().trim().equals("c4")) {
            inputString = "4axUCCwfsRsBLcUNUHyK";
            artName[0] = "orijinal Vankulu Lügatı";
        } else if (inputString.toLowerCase().trim().equals("c5")) {
            inputString = "6I0vYEq3RdBydmK0cWSx";
            artName[0] = "hat örneği";
        } else if (inputString.toLowerCase().trim().equals("c6")) {
            inputString = "6fpybeMKYx6i8coNaCeg";
            artName[0] = "makta";
        } else if (inputString.toLowerCase().trim().equals("c7")) {
            inputString = "7jQurtSZIocrfmz9Gokd\n";
            artName[0] = "el yapımı kart";
        } else if (inputString.toLowerCase().trim().equals("c8")) {
            inputString = "9Ghf0JXzaesTLv5nDjad\n";
            artName[0] = "Bakır Gravür";
        } else if (inputString.toLowerCase().trim().equals("c9")) {
            inputString = "BRpp3Y17hiHNbXq7B499";
            artName[0] = "kamış kalem";
        } else if (inputString.toLowerCase().trim().equals("c11")) {
            inputString = "BVhoT9izFBPivBymOjxu";
            artName[0] = "mumlu yazı tahtası";
        } else if (inputString.toLowerCase().trim().equals("c12")) {
            inputString = "EpRis1XKi4vGjbN9u834";
            artName[0] = "dünyanın en ince kağıdı";
        }
        return inputString;
    }


}