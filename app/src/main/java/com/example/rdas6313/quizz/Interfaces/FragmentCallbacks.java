package com.example.rdas6313.quizz.Interfaces;

/**
 * Created by rdas6313 on 27/4/18.
 */

public interface FragmentCallbacks {
    public void QuestionSetFragmentCallbacks(String key);
    public void QuestionFrgmentCallbacks(int total_question,int right_ans);
    public void ScoreBoardFragmentCallback();
    public void CustomDialogFragmentCallback(String msg);
}
