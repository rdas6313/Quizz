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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rdas6313.quizz.Interfaces.FragmentCallbacks;
import com.example.rdas6313.quizz.Interfaces.QuestionPresenterConnection;
import com.example.rdas6313.quizz.Models.Questiontype;
import com.example.rdas6313.quizz.Presenters.QuestionPresenter;
import com.example.rdas6313.quizz.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;

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
    private FragmentCallbacks fragmentCallbacks;

    public QuestionSetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_question_set, container, false);
        recyclerView = (RecyclerView)root.findViewById(R.id.recylerView);
        progressBar = (ProgressBar)root.findViewById(R.id.progressBar);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentCallbacks = (FragmentCallbacks) getContext();
        if(fragmentCallbacks != null) {
            fragmentCallbacks.setBottomNavigartionBarVisibility(true);
            fragmentCallbacks.ActionBarElevation(true);
        }
        questionPresenterConnection = new QuestionPresenter();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        if(questionPresenterConnection != null)
            options = questionPresenterConnection.getDataForQuestionSet();
        adapter = new FirebaseRecyclerAdapter<Questiontype, QuestionTypeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull QuestionTypeViewHolder holder, int position, @NonNull Questiontype model) {
                if(holder != null)
                    holder.bindView(model);
            }

            @NonNull
            @Override
            public QuestionTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_set_layout,parent,false);
                QuestionTypeViewHolder vh = new QuestionTypeViewHolder(root);
                return vh;
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(adapter.getItemCount()>0)
                    flipView(false);
                else if(adapter.getItemCount() == 0){
                    offAllView();
                    Toast.makeText(getContext(),"No Question set Found",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
                Log.e(TAG,error.getMessage());
                offAllView();
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void flipView(boolean showProgressBar){
        if(!showProgressBar){
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void offAllView(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
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

    private class QuestionTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView questionType,scoreLabel,scoreView;
        private ImageView doneBtn;

        public QuestionTypeViewHolder(View root){
            super(root);
            root.setOnClickListener(this);
            questionType = (TextView)root.findViewById(R.id.questiontype);
            scoreLabel = (TextView)root.findViewById(R.id.score_label);
            scoreView = (TextView)root.findViewById(R.id.score);
            doneBtn = (ImageView) root.findViewById(R.id.done);
        }
        public void bindView(Questiontype model){
            if(model.isAlreadSelected()) {
                doneBtn.setImageResource(R.drawable.ic_done_green_a700_24dp);
                scoreLabel.setText(R.string.question_set_score_label);
                scoreView.setText(model.getPoint()*model.rightAns()+"");
            }
            else {
                doneBtn.setImageResource(R.drawable.ic_keyboard_arrow_right_light_blue_a100_24dp);
                scoreLabel.setText(R.string.question_set_point);
                scoreView.setText(model.getPoint()+"");
            }
            questionType.setText(model.getQuestionType());
        }

        @Override
        public void onClick(View v) {
            Questiontype questiontype = adapter.getItem(getAdapterPosition());
            if(questiontype == null)
                return;
            if(!questiontype.isAlreadSelected()){
                fragmentCallbacks.QuestionSetFragmentCallbacks(questiontype.getId(),questiontype.getQuestionType(),questiontype.getPoint());
            }

        }
    }
}
