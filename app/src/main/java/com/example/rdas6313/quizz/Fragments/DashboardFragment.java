package com.example.rdas6313.quizz.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rdas6313.quizz.Interfaces.PresenterCallBack;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.example.rdas6313.quizz.Presenters.LoginAndSignUp;
import com.example.rdas6313.quizz.R;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * Created by rdas6313 on 23/5/18.
 */

public class DashboardFragment extends Fragment{

    private TextView nameView,scoreView,questionSetView,solvedView;
    private ImageView profilePicView;

    private PresenterConnection loginConnection;
    private final String TAG = DashboardFragment.class.getName();

    public DashboardFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dashboard_layout,container,false);
        nameView = (TextView)root.findViewById(R.id.name);
        scoreView = (TextView)root.findViewById(R.id.score);
        questionSetView = (TextView)root.findViewById(R.id.total_question_set);
        solvedView = (TextView)root.findViewById(R.id.solved_set);
        profilePicView = (ImageView)root.findViewById(R.id.profilePic);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginConnection = new LoginAndSignUp();

    }

    private void loadUserData(){
        Map<String,Object> userData = null;
        if(loginConnection != null)
            userData = loginConnection.getUserInfo();
        if(userData == null)
            return;
        nameView.setText((String)userData.get(PresenterConnection.USER_NAME));
        Picasso.get().load((String)userData.get(PresenterConnection.USER_PHOTO_URL)).resize(300,300).centerCrop().onlyScaleDown().placeholder(R.drawable.avatar).into(profilePicView);
        scoreView.setText("1050");
        questionSetView.setText("30");
        solvedView.setText("21");
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }
}
