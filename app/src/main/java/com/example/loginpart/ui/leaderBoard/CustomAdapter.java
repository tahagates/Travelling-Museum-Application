package com.example.loginpart.ui.leaderBoard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.loginpart.R;
import com.example.loginpart.model.UserModel;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<UserModel> userList;

    public CustomAdapter (Activity activity, ArrayList<UserModel> userList)
    {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserModel user = userList.get(position);
        View item = inflater.inflate(R.layout.custom_layout,null);

        TextView tv_fullName = (TextView) item.findViewById(R.id.tv_fullName);
        TextView tv_point = (TextView) item.findViewById(R.id.tv_point);
        TextView tv_reward = (TextView) item.findViewById(R.id.tv_reward);

        tv_fullName.setText(user.getFullName());
        tv_point.setText(String.valueOf(user.getPoint()));
        tv_reward.setText(String.valueOf(user.getReward()));

        return item;
    }
}
