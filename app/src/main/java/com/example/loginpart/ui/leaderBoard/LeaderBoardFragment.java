package com.example.loginpart.ui.leaderBoard;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.loginpart.R;

import com.example.loginpart.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class LeaderBoardFragment extends Fragment {

    public static final String TAG = "TAG";
    public ArrayList<UserModel> userList = new ArrayList<>();
    public ListView listView;
    private FirebaseFirestore firebaseFirestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        listView = (ListView) view.findViewById(R.id.listView);

        firebaseFirestore = FirebaseFirestore.getInstance();


        Task<QuerySnapshot> documentReference = firebaseFirestore.collection("users")
                .orderBy("point", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int index = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> user = new HashMap<>();
                                user = document.getData();

                                String fullName = (String) user.get("fullName");
                                int point = Integer.parseInt(user.get("point").toString());
                                int reward; //An impression is made in the table for the reward
                                if(index == 1)
                                {
                                    reward = 500;
                                }
                                else if(index == 2)
                                {
                                    reward = 250;
                                }
                                else if(index == 3)
                                {
                                    reward = 100;
                                }
                                else
                                {
                                    reward = 0;
                                }
                                userList.add(new UserModel(fullName,point,reward));
                                index = index + 1;
                            }
                            CustomAdapter adapter = new CustomAdapter(getActivity(), userList);
                            listView.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                }
                );
        return view;
    }
}


