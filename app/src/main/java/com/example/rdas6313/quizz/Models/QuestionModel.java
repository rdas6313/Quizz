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
