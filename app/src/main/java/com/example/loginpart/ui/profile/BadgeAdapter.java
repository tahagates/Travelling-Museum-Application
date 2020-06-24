package com.example.loginpart.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.loginpart.R;
import com.example.loginpart.model.BadgeModel;
import com.example.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import static android.graphics.Color.rgb;

public class BadgeAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<BadgeModel> badgeList;
    ArrayList<String> userPath;
    public static final String TAG = "TAG";
    DocumentReference documentReferenceArtifact;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ArrayList<String> categories = new ArrayList<>();

    public BadgeAdapter (Activity activity, ArrayList<BadgeModel> badgeList, ArrayList<String> userPath,ArrayList<String> categories)
    {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.badgeList = badgeList;
        this.userPath = userPath;
        this.categories = categories;
    }



    @Override
    public int getCount() {
        return badgeList.size();
    }

    @Override
    public Object getItem(int position) {
        return badgeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BadgeModel badge = badgeList.get(position);

        View item = inflater.inflate(R.layout.badge_layout,null);


        TextView tv_badgeName = (TextView) item.findViewById(R.id.tv_badge_name);
        ImageView imageView = (ImageView) item.findViewById(R.id.imageview_badge);

        tv_badgeName.setText(badge.getName());


        boolean isVisibleBadge = true;
        ArrayList<String> badgePath = badge.getPath();

        if(badge.getName().equals("Kağıt Değirmeni Avcısı") || badge.getName().equals("Minyatür Meraklısı") || badge.getName().equals("Organik Meraklısı"))
        {
            for (int i = 0;i < badgePath.size(); i++)
            {
                for(int j = 0;j < userPath.size(); j++)
                {
                    if(!badgePath.get(i).equals(userPath.get(j)))
                    {
                        isVisibleBadge = false;
                        break;
                    }
                }
                if(isVisibleBadge == false)
                {
                    tv_badgeName.setVisibility(View.INVISIBLE);
                    imageView.setImageResource(R.drawable.engel_edit);
                    item.setClickable(true);
                    break;
                }
                else
                {
                    if(badge.getName().equals("Kağıt Değirmeni Avcısı"))
                    {
                        imageView.setImageResource(R.drawable.kagit_avcisi);
                    }
                    else if(badge.getName().equals("Minyatür Meraklısı"))
                    {
                        imageView.setImageResource(R.drawable.minyatur);
                    }
                    else if(badge.getName().equals("Organik Meraklısı"))
                    {
                        imageView.setImageResource(R.drawable.organik);
                    }
                }
            }
        }
        else if(badge.getName().equals("Gezgin"))
        {
            if(userPath.size() < 10)
            {
                imageView.setImageResource(R.drawable.engel_edit);
                tv_badgeName.setVisibility(View.INVISIBLE);
                item.setClickable(true);
            }
            else
            {
                imageView.setImageResource(R.drawable.gezgin);
            }
        }
        else if(badge.getName().equals("Meraklı"))
        {
            if(categories.size() < 3)
            {
                imageView.setImageResource(R.drawable.engel_edit);
                tv_badgeName.setVisibility(View.INVISIBLE);
                item.setClickable(true);
            }
            else
            {
                imageView.setImageResource(R.drawable.merakli);
            }
        }
        return item;
    }
}
