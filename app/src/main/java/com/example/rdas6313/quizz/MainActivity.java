package com.example.rdas6313.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rdas6313.quizz.Fragments.QuestionSetFragment;
import com.example.rdas6313.quizz.Fragments.QuestionsFragment;
import com.example.rdas6313.quizz.Fragments.ScoreBoardFragment;
import com.example.rdas6313.quizz.Interfaces.FragmentCallbacks;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.example.rdas6313.quizz.Presenters.LoginAndSignUp;

public class MainActivity extends AppCompatActivity implements FragmentCallbacks{
    private final String TAG = MainActivity.class.getName();
    private PresenterConnection mConnection;
  //  private QuestionSetFragment questionSetFragment;
    private final String QUESTION_SET_FRAGMENT = "question_set_fragment";
   // private QuestionsFragment questionsFragment;
    private final String QUESTION_FRAGMENT = "question_fragment";
   // private ScoreBoardFragment scoreBoardFragment;
    private final String SCORE_BOARD_FRAGMENT = "score_board_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConnection = new LoginAndSignUp();
        QuestionSetFragment questionSetFragment = new QuestionSetFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,questionSetFragment,QUESTION_SET_FRAGMENT)
                .commit();

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
    public void QuestionSetFragmentCallbacks(String key) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.QUESTION_SET_KEY),key);
        QuestionsFragment questionsFragment = new QuestionsFragment();
        questionsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,questionsFragment,QUESTION_FRAGMENT)
                .commit();
    }

    @Override
    public void ScoreBoardFragmentCallback() {
        QuestionSetFragment questionSetFragment = new QuestionSetFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,questionSetFragment,QUESTION_SET_FRAGMENT)
                .commit();
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
}
