package com.example.rdas6313.quizz.Interfaces;

import com.example.rdas6313.quizz.Models.Questiontype;
import com.firebase.ui.database.FirebaseRecyclerOptions;

/**
 * Created by rdas6313 on 26/4/18.
 */

public interface QuestionModelConnection {
    public FirebaseRecyclerOptions<Questiontype> getFirebaseOptionsForQuestionSet();
}