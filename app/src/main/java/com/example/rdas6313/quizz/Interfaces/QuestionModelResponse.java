package com.example.rdas6313.quizz.Interfaces;

/**
 * Created by rdas6313 on 1/5/18.
 */

public interface QuestionModelResponse {
    public void onSelectionResponse(boolean isSuccessfull);
    public void onUserQuestionSetResponse(long totalQuestionSet,long attempt,long score);
}
