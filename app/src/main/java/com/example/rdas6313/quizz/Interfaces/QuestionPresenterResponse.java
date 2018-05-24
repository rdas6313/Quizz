package com.example.rdas6313.quizz.Interfaces;

/**
 * Created by rdas6313 on 1/5/18.
 */

public interface QuestionPresenterResponse {
    public void onAddUserToQuestionSetResponse(boolean isSuccessfull);
    public void onGettingUserScoreinfo(long totalQuestionSet,long attemptSet,long Score);
}
