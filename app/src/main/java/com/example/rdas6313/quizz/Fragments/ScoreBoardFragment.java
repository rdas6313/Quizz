package com.example.rdas6313.quizz.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rdas6313.quizz.Interfaces.FragmentCallbacks;
import com.example.rdas6313.quizz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreBoardFragment extends Fragment implements View.OnClickListener{

    private int total_question = 0;
    private int right_ans = 0;

    private TextView totalquestionView,rightAnsView;
    private Button okBtn;

    private FragmentCallbacks fragmentCallbacks;

    public ScoreBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_score_board, container, false);
        totalquestionView = (TextView)root.findViewById(R.id.total_score);
        rightAnsView = (TextView)root.findViewById(R.id.right_ans);
        okBtn = (Button)root.findViewById(R.id.okBtn);
        okBtn.setOnClickListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            total_question = bundle.getInt(getString(R.string.total_question));
            right_ans = bundle.getInt(getString(R.string.right_ans));
        }
        totalquestionView.setText(getString(R.string.set_total_question,total_question));
        rightAnsView.setText(getString(R.string.set_right_ans,right_ans));
        fragmentCallbacks = (FragmentCallbacks)getActivity();
    }

    private void openQuestionSet(){
        if(fragmentCallbacks != null)
            fragmentCallbacks.ScoreBoardFragmentCallback();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.okBtn:
                openQuestionSet();
                break;
        }
    }
}
