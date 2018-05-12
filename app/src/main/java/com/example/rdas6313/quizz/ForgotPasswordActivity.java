package com.example.rdas6313.quizz;

import android.app.Dialog;
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

import com.example.rdas6313.quizz.Interfaces.PresenterCallBack;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.example.rdas6313.quizz.Presenters.LoginAndSignUp;

import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity implements PresenterCallBack,View.OnClickListener {

    private PresenterConnection connection;
    private EditText emailView;
    private Button resetBtn;
    private CoordinatorLayout coordinatorLayout;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        connection = new LoginAndSignUp();
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        emailView = (EditText)findViewById(R.id.email);
        resetBtn = (Button)findViewById(R.id.resetPasswordBtn);
        resetBtn.setOnClickListener(this);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.login_sign_up_dialog);
        TextView textView = (TextView)dialog.findViewById(R.id.dialogText);
        textView.setText(R.string.reset_password_dialog);
    }

    @Override
    public void onSignUpResponse(boolean isError, String msg) {}

    @Override
    public void onLoginResponse(boolean isError, String msg) {}

    @Override
    public void onForgotPasswordResponse(boolean isError, String msg) {
        cancelDialog();
        Snackbar.make(coordinatorLayout,msg,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onChageProfilePicResponse(boolean isError, String msg, String download_link) {}

    @Override
    public void onProgressProfilePic(int progress) {}

    private void resetPassword(){
        String email = emailView.getText().toString();
        String regx = "\\S+@\\S+\\.\\S+";
        if(TextUtils.isEmpty(email)){
            emailView.setError(getString(R.string.empty_editText_Error));
            return;
        }else if(!Pattern.matches(regx,email)){
            emailView.setError(getString(R.string.email_formatting_error));
            return;
        }

        if(connection != null){
            showDialog();
            connection.forgotPassword(email,this);
        }else{
            Snackbar.make(coordinatorLayout,R.string.Internal_Error,Snackbar.LENGTH_SHORT).show();
        }
    }


    private void showDialog(){
        if(dialog != null)
            dialog.show();
    }

    private void cancelDialog(){
        if(dialog != null)
            dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetPasswordBtn:
                resetPassword();
                break;
        }
    }
}
