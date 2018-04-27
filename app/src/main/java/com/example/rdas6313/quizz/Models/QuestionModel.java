package com.example.rdas6313.quizz.Models;

import android.support.annotation.NonNull;

import com.example.rdas6313.quizz.Interfaces.QuestionModelConnection;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by rdas6313 on 26/4/18.
 */

public class QuestionModel implements QuestionModelConnection {

    @Override
    public FirebaseRecyclerOptions<Questions> getFirebaseOptionsForQuestions(String question_set_Key) {
        Query query = FirebaseDatabase.getInstance().getReference().child("questions").orderByChild("type").equalTo(question_set_Key);
        FirebaseRecyclerOptions<Questions> moptions = new FirebaseRecyclerOptions.Builder<Questions>()
                .setQuery(query, new SnapshotParser<Questions>() {
                    @NonNull
                    @Override
                    public Questions parseSnapshot(@NonNull DataSnapshot snapshot) {
                        // Log.e(TAG,snapshot.getKey()+" "+snapshot.getValue());
                        Questions questions = snapshot.getValue(Questions.class);
                        questions.setId(snapshot.getKey());
                        // Log.e(TAG,questions.getQuestion()+" "+questions.getOptions() +" "+questions.getId());
                        return questions;
                    }

                }).build();
        return moptions;
    }

    @Override
    public FirebaseRecyclerOptions<Questiontype> getFirebaseOptionsForQuestionSet() {
        Query query = FirebaseDatabase.getInstance().getReference().child("questionset");
        FirebaseRecyclerOptions<Questiontype> options = new FirebaseRecyclerOptions.Builder<Questiontype>()
                .setQuery(query, new SnapshotParser<Questiontype>() {
                    @NonNull
                    @Override
                    public Questiontype parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Questiontype questiontype = new Questiontype(snapshot.getKey(),snapshot.getValue()+"");
                        return questiontype;
                    }
                })
                .build();
        return options;
    }
}
