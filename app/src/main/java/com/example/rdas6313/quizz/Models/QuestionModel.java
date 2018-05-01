package com.example.rdas6313.quizz.Models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rdas6313.quizz.Interfaces.LoginSignUpModelConnection;
import com.example.rdas6313.quizz.Interfaces.QuestionModelConnection;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by rdas6313 on 26/4/18.
 */

public class QuestionModel implements QuestionModelConnection {

    private LoginSignUpModelConnection loginSignUpModelConnection = new LoginAndSignUpModel();
    private final String TAG = QuestionModel.class.getName();

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
                    //    Log.e(TAG,snapshot.getKey()+" "+snapshot.getValue());
                        Map<String,Object> map = (Map<String,Object>)snapshot.getValue();
                        boolean alredySelected = checkIfUserIdExist(map);
                        Questiontype questiontype = new Questiontype(snapshot.getKey(),(String)map.get("name"),alredySelected);
                        return questiontype;
                    }
                })
                .build();
        return options;
    }

    private boolean checkIfUserIdExist(Map<String,Object> response){
        String userId = null;
        if(loginSignUpModelConnection != null)
            userId = loginSignUpModelConnection.getCurrentUserId();
        if(userId == null)
            return false;
        ArrayList<String> list = (ArrayList<String>) response.get("users");
        if(list != null)
            return list.contains(userId);
        return false;
    }
}
