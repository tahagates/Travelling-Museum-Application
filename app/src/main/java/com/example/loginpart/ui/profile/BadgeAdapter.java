package com.example.loginpart.ui.profile;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;

public class BadgeAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<BadgeModel> badgeList;

    public BadgeAdapter (Activity activity, ArrayList<BadgeModel> badgeList)
    {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.badgeList = badgeList;
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
        //şu değişecek
        View item = inflater.inflate(R.layout.badge_layout,null);

        TextView tv_badgeName = (TextView) item.findViewById(R.id.tv_badge_name);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageview_badge);

        tv_badgeName.setText(badge.getName());

        if(badge.getName() == "Kağıt Değirmeni Avcısı")
        {
            imageView.setImageResource(R.drawable.background);
        }
        else if(badge.getName() == "Minyatür Meraklısı")
        {
            imageView.setImageResource(R.drawable.kedi);
        }
        else if(badge.getName() == "Organik Meraklısı")
        {
            imageView.setImageResource(R.drawable.profile);
        }
        /*
        //String point = String.valueOf(user.getPoint());
        tv_point.setText(String.valueOf(user.getPoint()));
        tv_reward.setText("0");*/

        return item;
    }
}
