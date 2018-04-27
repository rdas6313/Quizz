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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.rdas6313.quizz.Interfaces.QuestionPresenterConnection;
import com.example.rdas6313.quizz.Models.Questions;
import com.example.rdas6313.quizz.Models.Questiontype;
import com.example.rdas6313.quizz.Presenters.QuestionPresenter;
import com.example.rdas6313.quizz.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionsFragment extends Fragment {

    private final String TAG = QuestionSetFragment.class.getName();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Questions,MyViewHolder>adapter;
    private QuestionPresenterConnection questionPresenterConnection;

    public QuestionsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_questions, container, false);
        recyclerView = (RecyclerView)root.findViewById(R.id.recylerView);
        progressBar = (ProgressBar)root.findViewById(R.id.progressBar);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String questionSetKey = null;
        Bundle bundle = getArguments();
        if(bundle != null)
            questionSetKey = bundle.getString(getString(R.string.QUESTION_SET_KEY));

        Log.e(TAG,"QUESTION_KEY "+questionSetKey);
        questionPresenterConnection = new QuestionPresenter();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<Questions> moptions = null;
        if(questionPresenterConnection != null)
            moptions = questionPresenterConnection.getDataForQuestions(questionSetKey);

        adapter = new FirebaseRecyclerAdapter<Questions, MyViewHolder>(moptions) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Questions model) {
                if(holder != null && model != null)
                    holder.bindView(model.getQuestion(),model.getOptions());
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout,parent,false);
                MyViewHolder vh = new MyViewHolder(root);
                return vh;
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(adapter.getItemCount()>0)
                    flipView(false);

            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void flipView(boolean showProgressBar){
        if(showProgressBar){
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null)
            adapter.startListening();
        flipView(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(adapter != null)
            adapter.stopListening();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView questionView;
        private RadioButton r1,r2,r3;
        private RadioGroup radioGroup;

        public MyViewHolder(View root){
            super(root);
            questionView = (TextView)root.findViewById(R.id.question);
            radioGroup = (RadioGroup)root.findViewById(R.id.optionGroup);
            r1 = (RadioButton)root.findViewById(R.id.option1);
            r2 = (RadioButton)root.findViewById(R.id.option2);
            r3 = (RadioButton)root.findViewById(R.id.option3);
        }
        public void bindView(String question, List<String>options){
            questionView.setText(question);
            if(options == null)
                return;
            r1.setText(options.get(0));
            r2.setText(options.get(1));
            r3.setText(options.get(2));
        }
    }
}
