package com.example.rdas6313.quizz.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rdas6313.quizz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreBoardFragment extends Fragment {


    public ScoreBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score_board, container, false);
    }

}
