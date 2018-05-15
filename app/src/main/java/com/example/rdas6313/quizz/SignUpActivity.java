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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rdas6313.quizz.Interfaces.PresenterCallBack;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.example.rdas6313.quizz.Presenters.LoginAndSignUp;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,PresenterCallBack{

    private EditText nameView,emailView,passwordView,confirmPasswordView;
    private Button signupBtn;
    private CoordinatorLayout coordinatorLayout;
    private PresenterConnection connection;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        connection = new LoginAndSignUp();
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        nameView = (EditText)findViewById(R.id.name);
        emailView = (EditText)findViewById(R.id.email);
        passwordView = (EditText)findViewById(R.id.password);
        confirmPasswordView = (EditText)findViewById(R.id.confirm_password);
        signupBtn = (Button)findViewById(R.id.signup);
        signupBtn.setOnClickListener(this);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_sign_up_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView textView = (TextView)dialog.findViewById(R.id.dialogText);
        textView.setText(R.string.dialog_sign_up);
    }

    private void signUp(){
        String email = emailView.getText().toString();
        String name = nameView.getText().toString();
        String password = passwordView.getText().toString();
        String confirm_password = confirmPasswordView.getText().toString();
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
        if(TextUtils.isEmpty(name)){
            nameView.setError(getString(R.string.empty_editText_Error));
            isError = true;
        }
        if(TextUtils.isEmpty(password)){
            passwordView.setError(getString(R.string.empty_editText_Error));
            isError = true;
        }
        if(TextUtils.isEmpty(confirm_password)){
            confirmPasswordView.setError(getString(R.string.empty_editText_Error));
            isError = true;
        }
        if(isError)
            return;
        else if(!password.equals(confirm_password)){
            Snackbar.make(coordinatorLayout, R.string.passoword_not_matched,Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(connection != null) {
            changeView(1);
            connection.signUpUser(email, password, name, this);
        }else{
            Snackbar.make(coordinatorLayout, getString(R.string.Internal_Error),Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onChageProfilePicResponse(boolean isError, String msg, String download_link) {}

    @Override
    public void onProgressProfilePic(int progress) {}

    @Override
    public void onUpdateName(boolean isError) {}

    @Override
    public void onChangePassword(boolean isError, String msg) {}

    private void showDialog(){
       if(dialog != null)
            dialog.show();
    }

    private void cancelDialog(){
        if(dialog != null)
            dialog.dismiss();
    }

    private void changeView(int v){
        switch (v){
            case 1:
                showDialog();
                break;
            case 2:
                cancelDialog();
                break;
        }
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
        changeView(2);
        if(isError){
            Snackbar.make(coordinatorLayout,msg,Snackbar.LENGTH_SHORT).show();
            //Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onLoginResponse(boolean isError, String msg) {}

    @Override
    public void onForgotPasswordResponse(boolean isError, String msg) {}
}
