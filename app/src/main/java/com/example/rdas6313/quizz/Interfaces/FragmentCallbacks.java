package com.example.rdas6313.quizz.Interfaces;

/**
 * Created by rdas6313 on 27/4/18.
 */

public interface FragmentCallbacks {
    public void QuestionSetFragmentCallbacks(String key,String name,long point);
    public void QuestionFrgmentCallbacks(int total_question,int right_ans,long each_question_point,String question_key);
    public void ScoreBoardFragmentCallback();
    public void CustomDialogFragmentCallback(String msg);
    public void ActionBarElevation(boolean needToShow);
    public void setBottomNavigartionBarVisibility(boolean visibility);
    public void clearBackStack();
}
