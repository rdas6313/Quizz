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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,PresenterCallBack{

    private EditText nameView,emailView,passwordView;
    private Button signupBtn;
    PresenterConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        connection = new LoginAndSignUp();
        nameView = (EditText)findViewById(R.id.name);
        emailView = (EditText)findViewById(R.id.email);
        passwordView = (EditText)findViewById(R.id.password);
        signupBtn = (Button)findViewById(R.id.signup);
        signupBtn.setOnClickListener(this);
    }

    private void signUp(){
        String email = emailView.getText().toString();
        String name = nameView.getText().toString();
        String password = passwordView.getText().toString();
        boolean isError = false;
        if(TextUtils.isEmpty(email)){
            emailView.setError("this Field can't be empty");
            isError = true;
        }
        if(TextUtils.isEmpty(name)){
            nameView.setError("this Field can't be empty");
            isError = true;
        }
        if(TextUtils.isEmpty(password)){
            passwordView.setError("this Field can't be empty");
            isError = true;
        }
        if(isError)
            return;
        if(connection != null)
            connection.signUpUser(email,password,name,this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup:
                signUp();
                break;
        }
    }

    @Override
    public void onSignUpResponse(boolean isError, String msg) {
        if(isError){
            Toast.makeText(this,"Sign Up Failed",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Sign Up Successfully",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onLoginResponse(boolean isError, String msg) {}
}
