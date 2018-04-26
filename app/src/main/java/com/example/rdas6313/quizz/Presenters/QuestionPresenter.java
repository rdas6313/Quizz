package com.example.rdas6313.quizz.Presenters;

import com.example.rdas6313.quizz.Interfaces.QuestionModelConnection;
import com.example.rdas6313.quizz.Interfaces.QuestionPresenterConnection;
import com.example.rdas6313.quizz.Models.QuestionModel;
import com.example.rdas6313.quizz.Models.Questiontype;
import com.firebase.ui.database.FirebaseRecyclerOptions;

/**
 * Created by rdas6313 on 26/4/18.
 */

public class QuestionPresenter implements QuestionPresenterConnection {

    private QuestionModelConnection modelConnection;

    public QuestionPresenter(){
        modelConnection = new QuestionModel();
    }
    @Override
    public FirebaseRecyclerOptions<Questiontype> getDataForQuestionSet() {
        if(modelConnection != null)
           return modelConnection.getFirebaseOptionsForQuestionSet();
        return null;
    }
}
