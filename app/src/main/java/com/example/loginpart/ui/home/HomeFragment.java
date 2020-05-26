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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginpart.model.ArtifactModel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.loginpart.R;
import com.example.loginpart.model.*;
import com.example.loginpart.ui.profile.ProfileFragment;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
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
    Button btnMove;
    Button[] artifacts = {artifact1, artifact2, artifact3, artifact4, artifact5, artifact6,
            artifact7,artifact8, artifact9, artifact11, artifact12};

    ImageView floorPlan;

    TextView tv_level;

    ProgressBar progressBar;

    String userID = "";

    Object userPoint;
    Object userLevel;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
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

        floorPlan = view.findViewById(R.id.imgFloorPlan);

        btnMove = view.findViewById(R.id.btnArtifact);

        tv_level = view.findViewById(R.id.tv_level);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Progress bar function --------------------------------------------------------------------

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
                    //leveling up
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
        //------------------------------------------------------------------------------------------

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
                        movementFunction2(inputString);


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
    }

    //Toast.makeText(context, "No artifact",Toast.LENGTH_LONG);
    final String[] artName = {""}; //??
    List<String> artPath = new ArrayList<String>(); //The path that holds artifacts' ids

    private void movementFunction(final String inputString) {
        final Object[] artifactPoint = new Object[1];

        //final String artString = findArt(inputString);
        artPath.add(inputString);
        documentReference = firebaseFirestore.collection("artifacts").document(inputString);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    //adding user artifact point
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

    private void movementFunction2(String inputString){
        final int index = Integer.parseInt(inputString) - 1;

        CollectionReference mapLocation = firebaseFirestore.collection("mapLocations");
        Query query = mapLocation.whereEqualTo("artifactID", inputString);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                        Map<String,Object> currentArtifact = new HashMap<>();
                        currentArtifact = queryDocumentSnapshot.getData();

                        int cX = Integer.parseInt(currentArtifact.get("X").toString());
                        int cY = Integer.parseInt(currentArtifact.get("Y").toString());

                        artifacts[index].setBackgroundColor(artifacts[index].getContext().getResources().getColor(R.color.Green));
                        artifacts[index].setX(cX);
                        artifacts[index].setY(cY);

                        documentReferenceUser.update("path",artPath);

                    }
                }
                else{
                    Toast.makeText(context,"Failure",Toast.LENGTH_LONG);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                return;
            }
        });
    }




}