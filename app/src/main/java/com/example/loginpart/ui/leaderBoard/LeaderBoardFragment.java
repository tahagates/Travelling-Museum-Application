package com.example.loginpart.ui.leaderBoard;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.loginpart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class LeaderBoardFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }
    public static final String TAG = "TAG";
    final ArrayList<UserModel> userList = new ArrayList<>();

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        //GridBaseAdapter gridBaseAdapter;
        //GridView userGrid = null;
        final Map<String, Object>[] user = new Map[]{new HashMap<>()};


        Task<QuerySnapshot> documentReference = firebaseFirestore.collection("users")
                .orderBy("point", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                user[0] = document.getData();
                                //addToList(user[0]);
                                /*userModel[0] = new UserModel();
                                userModel[0].setFullName((String)user[0].get("fullName"));
                                userModel[0].setPoint((int)user[0].get("point"));
                                //userList.add(userModel[0]);*/
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        /*gridBaseAdapter = new GridBaseAdapter(getContext(),userList);
        userGrid.setAdapter(gridBaseAdapter);*/

    }

    public void addToList(Map<String, Object> a){
        UserModel userModel = new UserModel();

        userModel.setFullName((String)a.get("fullName"));
        userModel.setPoint((int)a.get("point"));
        userList.add(userModel);
    }
}