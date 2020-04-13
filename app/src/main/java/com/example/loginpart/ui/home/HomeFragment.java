package com.example.loginpart.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.loginpart.R;
import com.google.android.gms.maps.MapFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import javax.annotation.Nullable;

public class HomeFragment extends Fragment {

    private Context context;

    Button artifact1, artifact2, artifact3, artifact4, artifact5, artifact6, artifact7;
    Button artifact8, artifact9, artifact11, artifact12;
    Button playerButton;
    Button btnMove,btnReset;

    private String artifactInput;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;

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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        playerButton = view.findViewById(R.id.btnPlayer);

        artifact1 = view.findViewById(R.id.btnCollection1);
        artifact2 = view.findViewById(R.id.btnCollection2);

        btnMove = view.findViewById(R.id.btnArtifact);
        btnReset = view.findViewById(R.id.btnReset);



        artifact1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Point p = getPointOfView(artifact1);
                Log.d("Coordinates","Coordinates x:" + p.x + " and y: " + p.y);

                playerButton.setX(p.x);
                playerButton.setY(p.y + 1);
            }
        });

        playerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Point p = getPointOfView(playerButton);


                Log.d("Coordinates","Coordinates x:" + p.x + " and y: " + p.y);

            }
        });

        //                Point p = getPointOfView(artifact1);
////                //Point player = getPointOfView(btnMove);
////                Log.d("Coordinates","Coordinates x:" + p.x + " and y: " + p.y);
////                playerButton.setX(p.x+10);
////                playerButton.setY(p.y-170);

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
}