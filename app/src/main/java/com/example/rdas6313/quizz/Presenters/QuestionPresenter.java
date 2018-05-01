package com.example.rdas6313.quizz.Presenters;

import com.example.rdas6313.quizz.Interfaces.QuestionModelConnection;
import com.example.rdas6313.quizz.Interfaces.QuestionModelResponse;
import com.example.rdas6313.quizz.Interfaces.QuestionPresenterConnection;
import com.example.rdas6313.quizz.Interfaces.QuestionPresenterResponse;
import com.example.rdas6313.quizz.Models.QuestionModel;
import com.example.rdas6313.quizz.Models.Questions;
import com.example.rdas6313.quizz.Models.Questiontype;
import com.firebase.ui.database.FirebaseRecyclerOptions;

/**
 * Created by rdas6313 on 26/4/18.
 */

public class QuestionPresenter implements QuestionPresenterConnection,QuestionModelResponse {

    private QuestionModelConnection modelConnection;
    private QuestionPresenterResponse questionPresenterResponse;

    public QuestionPresenter(){
        modelConnection = new QuestionModel();
    }

    @Override
    public FirebaseRecyclerOptions<Questions> getDataForQuestions(String key) {
        if(modelConnection != null)
            return modelConnection.getFirebaseOptionsForQuestions(key);
        return null;
    }

    @Override
    public void addCurrentUserToQuestionSet(String questionSet_Key, QuestionPresenterResponse presenterResponse) {
        questionPresenterResponse = presenterResponse;
        if(modelConnection != null){
            modelConnection.addCurrentUserToQuestionSetSelection(questionSet_Key,this);
        }else{
            if(presenterResponse != null)
                presenterResponse.onAddUserToQuestionSetResponse(false);
        }

    }

    @Override
    public FirebaseRecyclerOptions<Questiontype> getDataForQuestionSet() {
        if(modelConnection != null)
           return modelConnection.getFirebaseOptionsForQuestionSet();
        return null;
    }

    @Override
    public void onSelectionResponse(boolean isSuccessfull) {
        if(questionPresenterResponse != null)
            questionPresenterResponse.onAddUserToQuestionSetResponse(isSuccessfull);
    }
}
