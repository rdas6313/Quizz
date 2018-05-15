package com.example.rdas6313.quizz;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rdas6313.quizz.Interfaces.PresenterCallBack;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.example.rdas6313.quizz.Presenters.LoginAndSignUp;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,PresenterCallBack{

    private PresenterConnection connection;
    private Button loginBtn;
    private EditText emailView,passwordView;
    private TextView forgotPasswordView,signUpBtn;
    private CoordinatorLayout coordinatorLayout;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connection = new LoginAndSignUp();
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        signUpBtn = (TextView) findViewById(R.id.signup);
        loginBtn = (Button)findViewById(R.id.login);
        emailView = (EditText)findViewById(R.id.email);
        passwordView = (EditText)findViewById(R.id.password);
        signUpBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        forgotPasswordView = (TextView)findViewById(R.id.forgotPassword);
        forgotPasswordView.setOnClickListener(this);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.login_sign_up_dialog);
        TextView textView = (TextView)dialog.findViewById(R.id.dialogText);
        textView.setText(R.string.login_dialog_text);
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

    private void forgotPassword(){
        //forgot password
        Intent intent = new Intent(this,ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void login(){
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        boolean isError = false;
        if(TextUtils.isEmpty(email)){
            emailView.setError(getString(R.string.empty_editText_Error));
            isError = true;
        }else{
            String regx = "\\S+@\\S+\\.\\S+";
            if(!Pattern.matches(regx,email)){
                emailView.setError(getString(R.string.email_formatting_error));
                isError = true;
            }
        }
        if(TextUtils.isEmpty(password)){
            passwordView.setError(getString(R.string.empty_editText_Error));
            isError = true;
        }
        if(isError)
            return;
        if(connection != null) {
            showDialog();
            connection.loginUser(email, password, this);
        }else{
            Snackbar.make(coordinatorLayout,getString(R.string.Internal_Error),Snackbar.LENGTH_SHORT).show();
        }
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
            case R.id.forgotPassword:
                forgotPassword();
                break;
        }
    }

    private void showDialog(){
        if(dialog != null)
            dialog.show();
    }

    private void dismissDialog(){
        if(dialog != null){
            dialog.dismiss();
        }
    }

    @Override
    public void onLoginResponse(boolean isError, String msg) {
        dismissDialog();
        if(isError){
            Snackbar.make(coordinatorLayout,msg,Snackbar.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onForgotPasswordResponse(boolean isError, String msg) {}

    @Override
    public void onChageProfilePicResponse(boolean isError, String msg, String download_link) {}

    @Override
    public void onProgressProfilePic(int progress) {}

    @Override
    public void onUpdateName(boolean isError) {}

    @Override
    public void onChangePassword(boolean isError, String msg) {}

    @Override
    public void onSignUpResponse(boolean isError, String msg) {}
}
