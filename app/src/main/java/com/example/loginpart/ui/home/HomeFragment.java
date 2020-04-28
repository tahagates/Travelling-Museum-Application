package com.example.loginpart.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.graphics.Point;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.loginpart.R;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Collection;

import javax.annotation.Nullable;

import static com.example.loginpart.ui.leaderBoard.LeaderBoardFragment.TAG;

public class HomeFragment extends Fragment {

    private Context context;

    Button artifact1, artifact2, artifact3, artifact4, artifact5, artifact6, artifact7;
    Button artifact8, artifact9, artifact11, artifact12;
    Button playerButton;
    Button btnMove,btnReset;
    Button btnDeneme;

    private String artifactInput;



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
                } else{
                    Log.d("No document","Document error");
                    //Toast.makeText(context,"No document",Toast.LENGTH_LONG);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,e.toString());
            }
        });

    }
}