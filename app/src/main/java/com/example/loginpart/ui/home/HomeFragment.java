package com.example.loginpart.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.loginpart.Login;
import com.example.loginpart.R;
import com.example.loginpart.model.BadgeModel;
import com.example.loginpart.model.QuestionModel;
import com.example.loginpart.ui.profile.BadgeAdapter;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;
import com.example.loginpart.model.artMapLocModel;

import static com.example.loginpart.ui.leaderBoard.LeaderBoardFragment.TAG;




public class HomeFragment extends Fragment {

    private Context context;

    Button artifact1, artifact2, artifact3, artifact4, artifact5, artifact6, artifact7;
    Button artifact8, artifact9, artifact11, artifact12;
    Button playerButton;
    Button btnMove;
    protected ArrayList<Button> artifactButtons = new ArrayList<Button>();
    protected List<artMapLocModel> artifactLocations = new ArrayList<artMapLocModel>();
    TextView tv_level;
    ProgressBar progressBar;

    String userID = "";
    String inputString;

    Object userPoint;
    Object userLevel;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DocumentReference documentReference;
    private DocumentReference documentReferenceUser;

    public ArrayList<QuestionModel> questionList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.context = getContext();
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        //Creating necessary objects
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
        tv_level = view.findViewById(R.id.tv_level);


        //Adding artifacts buttons to list
        artifactButtons.add(artifact1);
        artifactButtons.add(artifact2);
        artifactButtons.add(artifact3);
        artifactButtons.add(artifact4);
        artifactButtons.add(artifact5);
        artifactButtons.add(artifact6);
        artifactButtons.add(artifact7);
        artifactButtons.add(artifact8);
        artifactButtons.add(artifact9);
        artifactButtons.add(artifact11);
        artifactButtons.add(artifact12);

        //Creating necessary firebase objects
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Coordinate functions call
        Log.d("x and y",artifact1.getX() + " "+ artifact1.getY());
        fillLocation(artifactButtons);
        updateCoordinates();

        //full QuestionList
        Task<QuerySnapshot> documentReference = firebaseFirestore.collection("questions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                                       Log.d(TAG, document.getId() + " => " + document.getData());
                                                       Map<String, Object> quiz = new HashMap<>();
                                                       quiz = document.getData();

                                                       String question = (String) quiz.get("question");
                                                       int artID = Integer.parseInt(quiz.get("artifactID").toString());

                                                       questionList.add(new QuestionModel(artID,question));

                                                   }
                                               } else {
                                                   Log.d(TAG, "Error getting documents: ", task.getException());
                                               }

                                           }
                                       }
                );


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
                    progressBar.setProgress((Integer.parseInt(userPoint.toString())) % 50);
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
        //-----------------------------------------------------------------------------------------




        //Main function of the homepage
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
                        inputString = inputArtifact.getText().toString();
                        pointFunction(inputString);
                        movementFunction(inputString);
                        quizFunction(inputString);
                        Log.d("empty", "this is an empty message");



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



    final String[] artName = {""}; //??
    List<String> artPath = new ArrayList<String>(); //The path that holds artifacts' ids

    private void pointFunction(final String inputString) {
        final Object[] artifactPoint = new Object[1];

        //final String artString = findArt(inputString);
        //artPath.add(inputString);
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

    @SuppressLint("ResourceAsColor")
    private void movementFunction(final String inputString){
        final int index = Integer.parseInt(inputString);
        for (int i = 0; i < artifactLocations.size(); i++){
            int currentID = (int)artifactLocations.get(i).getArtifactID();
            if (index == currentID){
                int x = (int)artifactLocations.get(i).getCoordinateX();
                int y = (int)artifactLocations.get(i).getCoordinateY();
//                int id = (int)artifactLocations.get(i).getArtifactID();
                artifactButtons.get(index - 1).setBackgroundColor(R.color.Green);
                playerButton.setX(x);
                playerButton.setY(y);
            }
        }

    }

    private Point getPointOfView(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new Point(location[0], location[1]);
    }


    private void updateCoordinates(){
        Map<Float,Object> data = new HashMap<>();
        for(float i = 0; i < artifactLocations.size(); i++){
            Task<Void> collectionReference = firebaseFirestore.collection("mapLocations").document(Integer.toString(Math.round(i)))
                    .update(
                            "X", artifactLocations.get((int)i).getCoordinateX(),
                            "Y",artifactLocations.get((int)i).getCoordinateY()
                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Task successful
                        }
                    });
        }

    }

    private void quizFunction(final String inputString) {

        final EditText answer = new EditText(context);

        QuestionModel question = new QuestionModel(0,"a");
        for(int i = 0;i < questionList.size();i++)
        {
            if(questionList.get(i).getArtifactID() == Integer.parseInt(inputString))
            {
                question = questionList.get(i);
                break;
            }
        }

        if(!question.getQuestion().equals("a"))
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("QUESTION");
            alertDialog.setView(answer);
            alertDialog.setMessage(question.getQuestion())
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userPoint = (Integer.parseInt(userPoint.toString()) + 20);
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

                            Toast.makeText(context,"Well done! Your score is on the rise.",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(context,"You could increase your score by answering the question :(",Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }

    }
    private void fillLocation(List<Button> buttons){
       for(int i = 0; i < buttons.size(); i++){
           Point p = getPointOfView(buttons.get(i));
           Log.d("x and y of ", i + ": " + p.x + " " + p.y);

       }
    }
}

