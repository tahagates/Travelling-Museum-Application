package com.example.loginpart.ui.leaderBoard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.loginpart.R;

import java.util.ArrayList;

public class GridBaseAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context ctx;
    private ArrayList<UserModel> userList;
    private TextView tv_fullName;
    private TextView tv_point;
    private TextView tv_reward;

    public GridBaseAdapter(Context ctx, ArrayList<UserModel> userList) {
        this.ctx = ctx;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row=inflater.inflate(R.layout.grid_item,viewGroup,false);

        tv_fullName = (TextView) view.findViewById(R.id.tv_fullName);
        tv_point = (TextView) view.findViewById(R.id.tv_point);
        tv_reward = (TextView) view.findViewById(R.id.tv_reward);

        tv_fullName.setText(userList.get(i).getFullName());
        tv_point.setText(userList.get(i).getPoint());
        tv_reward.setText("0");

        return row;
    }
}
