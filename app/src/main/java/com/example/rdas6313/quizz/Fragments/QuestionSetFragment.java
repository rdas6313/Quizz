package com.example.rdas6313.quizz.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rdas6313.quizz.Interfaces.QuestionPresenterConnection;
import com.example.rdas6313.quizz.Models.Questiontype;
import com.example.rdas6313.quizz.Presenters.QuestionPresenter;
import com.example.rdas6313.quizz.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionSetFragment extends Fragment {

    private final String TAG = QuestionSetFragment.class.getName();
    private RecyclerView.LayoutManager layoutManager = null;
    private FirebaseRecyclerOptions<Questiontype> options = null;
    private RecyclerView recyclerView = null;
    private FirebaseRecyclerAdapter<Questiontype,QuestionTypeViewHolder>adapter = null;
    private QuestionPresenterConnection questionPresenterConnection = null;
    private ProgressBar progressBar;
    private boolean isStopProgressBar = false;

    public QuestionSetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_question_set, container, false);
        recyclerView = (RecyclerView)root.findViewById(R.id.recylerView);
        recyclerView.setVisibility(View.GONE);
        progressBar = (ProgressBar)root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        questionPresenterConnection = new QuestionPresenter();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        if(questionPresenterConnection != null)
            options = questionPresenterConnection.getDataForQuestionSet();
        adapter = new FirebaseRecyclerAdapter<Questiontype, QuestionTypeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull QuestionTypeViewHolder holder, int position, @NonNull Questiontype model) {
                if(holder != null)
                    holder.bindView(model.getQuestionType(),true);
            }

            @NonNull
            @Override
            public QuestionTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_set_layout,parent,false);
                QuestionTypeViewHolder vh = new QuestionTypeViewHolder(root);
                return vh;
            }
        };
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                //Log.e(TAG,"Calling On Insert "+itemCount);
                checkIfEmpty();
            }

        });
        recyclerView.setAdapter(adapter);
    }

    private void checkIfEmpty(){
        if(adapter.getItemCount()>0){
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.stopListening();
    }

    private class QuestionTypeViewHolder extends RecyclerView.ViewHolder{
        private TextView questionType;
        private ImageButton doneBtn;

        public QuestionTypeViewHolder(View root){
            super(root);
            questionType = (TextView)root.findViewById(R.id.questiontype);
            doneBtn = (ImageButton)root.findViewById(R.id.done);
        }
        public void bindView(String q_type,boolean shouldMark){
            if(shouldMark)
                doneBtn.setVisibility(View.VISIBLE);
            else
                doneBtn.setVisibility(View.GONE);
            questionType.setText(q_type);
        }
    }
}
