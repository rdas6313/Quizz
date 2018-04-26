package com.example.rdas6313.quizz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rdas6313.quizz.Fragments.QuestionSetFragment;
import com.example.rdas6313.quizz.Interfaces.PresenterCallBack;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.example.rdas6313.quizz.Presenters.LoginAndSignUp;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity{
    private final String TAG = MainActivity.class.getName();
    private PresenterConnection mConnection;
    private QuestionSetFragment questionSetFragment;
    private final String QUESTION_SET_FRAGMENT = "question_set_fragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConnection = new LoginAndSignUp();
        questionSetFragment = new QuestionSetFragment();
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



}
