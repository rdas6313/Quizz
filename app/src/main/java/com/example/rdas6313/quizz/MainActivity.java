package com.example.rdas6313.quizz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rdas6313.quizz.Fragments.DashboardFragment;
import com.example.rdas6313.quizz.Fragments.QuestionSetFragment;
import com.example.rdas6313.quizz.Fragments.QuestionsFragment;
import com.example.rdas6313.quizz.Fragments.ScoreBoardFragment;
import com.example.rdas6313.quizz.Interfaces.FragmentCallbacks;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.example.rdas6313.quizz.Presenters.LoginAndSignUp;

public class MainActivity extends AppCompatActivity implements FragmentCallbacks,BottomNavigationView.OnNavigationItemSelectedListener{
    private final String TAG = MainActivity.class.getName();
    private PresenterConnection mConnection;
  //  private QuestionSetFragment questionSetFragment;
    private final String QUESTION_SET_FRAGMENT = "question_set_fragment";
   // private QuestionsFragment questionsFragment;
    private final String QUESTION_FRAGMENT = "question_fragment";
   // private ScoreBoardFragment scoreBoardFragment;
    private final String SCORE_BOARD_FRAGMENT = "score_board_fragment";

    private BottomNavigationView navigationView;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();

        mConnection = new LoginAndSignUp();
        QuestionSetFragment questionSetFragment = new QuestionSetFragment();
        navigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        start_home();
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(!mConnection.isUserAlreadyLoggedIn()){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void editProfile(){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                return true;
            case R.id.editProfile:
                editProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){
        if(mConnection != null)
            mConnection.signOutUser();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void QuestionSetFragmentCallbacks(String key,String name) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.QUESTION_SET_KEY),key);
        if(key != null && name != null)
            getSupportActionBar().setTitle(name);
        QuestionsFragment questionsFragment = new QuestionsFragment();
        questionsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,questionsFragment,QUESTION_FRAGMENT)
                .commit();
    }

    @Override
    public void ScoreBoardFragmentCallback() {
        getSupportActionBar().setTitle("Question sets");
        QuestionSetFragment questionSetFragment = new QuestionSetFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,questionSetFragment,QUESTION_SET_FRAGMENT)
                .commit();
    }

    private void start_questionSet(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new QuestionSetFragment())
                .commit();
    }

    private void start_home(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment())
                .commit();
    }

    @Override
    public void CustomDialogFragmentCallback(String msg) {}

    @Override
    public void ActionBarElevation(boolean needToShow) {
        if(needToShow){
            if(actionBar != null){
                actionBar.setElevation((float) 8.0);
            }
        }else{
            if(actionBar != null){
                actionBar.setElevation(0);
            }
        }
    }

    @Override
    public void QuestionFrgmentCallbacks(int total_question, int right_ans) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.total_question),total_question);
        bundle.putInt(getString(R.string.right_ans),right_ans);
        ScoreBoardFragment scoreBoardFragment = new ScoreBoardFragment();
        scoreBoardFragment.setArguments(bundle);
       getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,scoreBoardFragment,SCORE_BOARD_FRAGMENT)
               .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.question_set:
                start_questionSet();
                return true;
            case R.id.homeView:
                start_home();
                return true;
        }
        return false;
    }
}
