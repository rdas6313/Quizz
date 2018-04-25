package com.example.rdas6313.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rdas6313.quizz.Interfaces.PresenterCallBack;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.example.rdas6313.quizz.Presenters.LoginAndSignUp;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,PresenterCallBack{

    private PresenterConnection connection;
    private Button signUpBtn,loginBtn;
    private EditText emailView,passwordView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connection = new LoginAndSignUp();
        signUpBtn = (Button)findViewById(R.id.signup);
        loginBtn = (Button)findViewById(R.id.login);
        emailView = (EditText)findViewById(R.id.email);
        passwordView = (EditText)findViewById(R.id.password);
        signUpBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(connection.isUserAlreadyLoggedIn()){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void signUp(){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void login(){
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        boolean isError = false;
        if(TextUtils.isEmpty(email)){
            emailView.setError("this field can't be empty");
            isError = true;
        }
        if(TextUtils.isEmpty(password)){
            passwordView.setError("this field can't be empty");
            isError = true;
        }
        if(isError)
            return;
        if(connection != null)
            connection.loginUser(email,password,this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup:
                signUp();
                break;
            case R.id.login:
                login();
                break;
        }
    }

    @Override
    public void onLoginResponse(boolean isError, String msg) {
        if(isError){
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onSignUpResponse(boolean isError, String msg) {}
}
