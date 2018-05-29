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
import android.widget.Toast;

import com.example.rdas6313.quizz.Interfaces.FragmentCallbacks;
import com.example.rdas6313.quizz.Interfaces.QuestionPresenterConnection;
import com.example.rdas6313.quizz.Interfaces.QuestionPresenterResponse;
import com.example.rdas6313.quizz.Presenters.QuestionPresenter;
import com.example.rdas6313.quizz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreBoardFragment extends Fragment implements View.OnClickListener,QuestionPresenterResponse{

    private int total_question = 0;
    private int right_ans = 0;
    private long each_qeustion_point = 0;
    private String question_key;

    private TextView totalquestionView,rightAnsView,scoreView;
    private Button okBtn;

    private FragmentCallbacks fragmentCallbacks;

    private QuestionPresenterConnection questionConnection;

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
        scoreView = (TextView)root.findViewById(R.id.score);
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
            each_qeustion_point = bundle.getLong(getString(R.string.question_set_point));
            question_key = bundle.getString(getString(R.string.questionSet_key));
        }
        totalquestionView.setText(total_question+"");
        rightAnsView.setText(right_ans+"");
        long score = right_ans*each_qeustion_point;
        scoreView.setText(score+"");
        fragmentCallbacks = (FragmentCallbacks)getActivity();
        questionConnection = new QuestionPresenter();
        if(questionConnection != null){
            questionConnection.addCurrentUserToQuestionSet(question_key,this,right_ans);
        }
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

    @Override
    public void onAddUserToQuestionSetResponse(boolean isSuccessfull) {
        if(!isSuccessfull)
            Toast.makeText(getContext(),"Unable to update your score",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGettingUserScoreinfo(long totalQuestionSet, long attemptSet, long Score) {}
}
