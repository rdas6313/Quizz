package com.example.rdas6313.quizz.Models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rdas6313.quizz.Interfaces.LoginSignUpModelConnection;
import com.example.rdas6313.quizz.Interfaces.QuestionModelConnection;
import com.example.rdas6313.quizz.Interfaces.QuestionModelResponse;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by rdas6313 on 26/4/18.
 */

public class QuestionModel implements QuestionModelConnection {

    private LoginSignUpModelConnection loginSignUpModelConnection = new LoginAndSignUpModel();
    private final String TAG = QuestionModel.class.getName();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

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
    public void addCurrentUserToQuestionSetSelection(String questionSet_key, final QuestionModelResponse listener) {
        String uid = null;
        if(loginSignUpModelConnection != null)
            uid = loginSignUpModelConnection.getCurrentUserId();
        if(databaseReference != null && uid != null){
            databaseReference.child("questionset").child(questionSet_key).child("users").child(uid).setValue(0)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                listener.onSelectionResponse(true);
                            }else{
                                listener.onSelectionResponse(false);
                            }
                        }
                    });
        }else{
            listener.onSelectionResponse(false);
        }
    }

    @Override
    public void getUserQuestionSetInfo(final QuestionModelResponse modelResponse) {
        if(databaseReference == null)
            return;
        databaseReference.child("questionset").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long score = 0,rightAns,attempt=0;
                long tota_set = dataSnapshot.getChildrenCount();
                for (DataSnapshot data:dataSnapshot.getChildren()) {
                    Map<String,Object>map = (Map<String, Object>) data.getValue();
                    long each_question_point = (long)data.child("point").getValue();
                    rightAns = getUserRightAns(map);
                    if(rightAns > -1){
                        score += each_question_point*rightAns;
                        attempt++;
                    }
                }
                if(modelResponse != null)
                    modelResponse.onUserQuestionSetResponse(tota_set,attempt,score);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
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
                        long rightAns = getUserRightAns(map);
                        boolean alredySelected = true;
                        if(rightAns == -1)
                            alredySelected = false;
                        /*boolean alredySelected = checkIfUserIdExist(map);*/
                        Questiontype questiontype = new Questiontype(snapshot.getKey(),(String)map.get("name"),alredySelected,(long)map.get("point"),rightAns);
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
        if(response.containsKey("users")){
            Map<String,Object> map = (Map<String,Object>)response.get("users");
            if(map.containsKey(userId))
                return true;
        }
        return false;
    }

    private long getUserRightAns(Map<String,Object> response){
        String userId = null;
        if(loginSignUpModelConnection != null)
            userId = loginSignUpModelConnection.getCurrentUserId();
        if(userId == null)
            return -1;
        if(response.containsKey("users")){
            Map<String,Object> map = (Map<String,Object>)response.get("users");
            if(map.containsKey(userId))
                return (long)map.get(userId);
        }
        return -1;
    }
}
