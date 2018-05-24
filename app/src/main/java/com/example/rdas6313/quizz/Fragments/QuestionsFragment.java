package com.example.rdas6313.quizz.Fragments;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rdas6313.quizz.Interfaces.FragmentCallbacks;
import com.example.rdas6313.quizz.Interfaces.QuestionPresenterConnection;
import com.example.rdas6313.quizz.Interfaces.QuestionPresenterResponse;
import com.example.rdas6313.quizz.Models.Questions;
import com.example.rdas6313.quizz.Models.Questiontype;
import com.example.rdas6313.quizz.Presenters.QuestionPresenter;
import com.example.rdas6313.quizz.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionsFragment extends Fragment implements View.OnClickListener,QuestionPresenterResponse{
    String questionSetKey = null;
    private final String TAG = QuestionSetFragment.class.getName();
    private final int SHOW_START_BTN = 1;
    private final int SHOW_QUESTION = 2;
    private boolean alreadyShowingBtn = false;
    private RecyclerView recyclerView;
    private LinearLayout containerView;
    private Button startBtn;
    private ProgressBar progressBar;
    private TextView timerView,readyTextView,logoTextView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Questions,MyViewHolder>adapter;
    private QuestionPresenterConnection questionPresenterConnection;
    private CountDownTimer timer;
    private final int TOTAL_TIME_IN_MIN = 1;
    private FragmentCallbacks fragmentCallbacks;
    private boolean alreadyFinished = false;
    private boolean alreadyStartedQuizz = false;
    private int rightAns;


    public QuestionsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_questions, container, false);
        recyclerView = (RecyclerView)root.findViewById(R.id.recylerView);
        progressBar = (ProgressBar)root.findViewById(R.id.progressBar);
        timerView = (TextView)root.findViewById(R.id.timerView);
        containerView = (LinearLayout)root.findViewById(R.id.quizzContainer);
        startBtn = (Button)root.findViewById(R.id.startButton);
        startBtn.setOnClickListener(this);
        readyTextView = (TextView)root.findViewById(R.id.readyText);
        logoTextView = (TextView)root.findViewById(R.id.logo);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null) {
            questionSetKey = bundle.getString(getString(R.string.QUESTION_SET_KEY));
        }
        else
            return;
        rightAns = 0;
        fragmentCallbacks = (FragmentCallbacks)getActivity();
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
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
                Log.e(TAG,error.getMessage());
                changeView(4);
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(adapter.getItemCount()>0 && !alreadyShowingBtn) {
                    Log.e(TAG, "Data Changed");
                    changeView(SHOW_START_BTN);
                    alreadyShowingBtn = true;
                }else if(adapter.getItemCount() == 0){
                    changeView(4);
                    Toast.makeText(getContext(),"Question Set Empty", Toast.LENGTH_LONG).show();
                }

            }

        };
        recyclerView.setAdapter(adapter);
    }

    private void changeView(int view_type){
        switch (view_type){
            case 1:
                progressBar.setVisibility(View.GONE);
                startBtn.setVisibility(View.VISIBLE);
                logoTextView.setVisibility(View.VISIBLE);
                readyTextView.setVisibility(View.VISIBLE);
                break;
            case 2:
                logoTextView.setVisibility(View.GONE);
                readyTextView.setVisibility(View.GONE);
                startBtn.setVisibility(View.GONE);
                containerView.setVisibility(View.VISIBLE);
                break;
            default:
                progressBar.setVisibility(View.GONE);
                logoTextView.setVisibility(View.GONE);
                readyTextView.setVisibility(View.GONE);
                startBtn.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
        }
    }

    private void changeTimerView(long millis){
        long sec = (millis/1000);
        long min = (sec/60);
        long hours = (min/60);
        timerView.setText(getString(R.string.timer,hours+"",min+"",sec+""));
    }

    private void startTimer(){
        long totalTimeInMillis = TOTAL_TIME_IN_MIN*60*1000;
        long intervalInMillis = 1000;
        changeTimerView(totalTimeInMillis);
        timer = new CountDownTimer(totalTimeInMillis,intervalInMillis){
            @Override
            public void onFinish() {
                changeTimerView(0);
                finishedQuizz();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                changeTimerView(millisUntilFinished);
            }
        }.start();
    }

    private void stopTimer(){
        if(timer != null)
            timer.cancel();
    }

    private void finishedQuizz(){
        Log.e(TAG,"Right Ans "+rightAns);
        stopTimer();
        if(fragmentCallbacks != null)
            fragmentCallbacks.QuestionFrgmentCallbacks(adapter.getItemCount(),rightAns);
        alreadyFinished = true;

    }

    private void startQuizz(){
        alreadyStartedQuizz = true;

        if(questionPresenterConnection != null && questionSetKey != null)
            questionPresenterConnection.addCurrentUserToQuestionSet(questionSetKey,this);

        changeView(SHOW_QUESTION);
        startTimer();
        setHasOptionsMenu(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null)
            adapter.startListening();

    }

    @Override
    public void onPause() {
        super.onPause();
        if(adapter != null)
            adapter.stopListening();
        if(!alreadyFinished && alreadyStartedQuizz)
            finishedQuizz();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startButton:
                startQuizz();
                break;
        }
    }

    @Override
    public void onAddUserToQuestionSetResponse(boolean isSuccessfull) {
        if(!isSuccessfull)
            Log.e(TAG,"Update user to Question set Error");
    }

    @Override
    public void onGettingUserScoreinfo(long totalQuestionSet, long attemptSet, long Score) {}

    private class MyViewHolder extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener{
        private TextView questionView;
        private RadioButton r1,r2,r3;
        private RadioGroup radioGroup;

        public MyViewHolder(View root){
            super(root);
            questionView = (TextView)root.findViewById(R.id.question);
            radioGroup = (RadioGroup)root.findViewById(R.id.optionGroup);
            radioGroup.setOnCheckedChangeListener(this);
            r1 = (RadioButton)root.findViewById(R.id.option1);
            r2 = (RadioButton)root.findViewById(R.id.option2);
            r3 = (RadioButton)root.findViewById(R.id.option3);
        }
        public void bindView(String question, List<String>options){
            questionView.setText(question);
            if(options == null)
                return;
            r1.setText(options.get(0));
            r1.setTag(0);
            r2.setText(options.get(1));
            r2.setTag(1);
            r3.setText(options.get(2));
            r3.setTag(2);
        }

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            RadioButton r = group.findViewById(checkedId);
            Questions questions = adapter.getItem(getAdapterPosition());
            if((int)r.getTag() == questions.getAns()){
                rightAns++;
                radioGroup.setTag(true);
            }else{
                if(radioGroup.getTag() != null)
                    rightAns--;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.question_menu_layout,menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.logout);
        item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.doneQuizz:
                finishedQuizz();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
