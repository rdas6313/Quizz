package com.example.rdas6313.quizz.Presenters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rdas6313.quizz.Interfaces.LoginSignUpModelCallback;
import com.example.rdas6313.quizz.Interfaces.LoginSignUpModelConnection;
import com.example.rdas6313.quizz.Interfaces.PresenterCallBack;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.example.rdas6313.quizz.Models.LoginAndSignUpModel;

/**
 * Created by rdas6313 on 24/4/18.
 */

public class LoginAndSignUp implements PresenterConnection,LoginSignUpModelCallback{

    private PresenterCallBack callBack;
    private final String TAG = LoginAndSignUp.class.getName();
    private LoginSignUpModelConnection connection;

    public LoginAndSignUp(){
        connection = new LoginAndSignUpModel();
    }

    @Override
    public boolean isUserAlreadyLoggedIn() {
        if(connection != null)
            return connection.isUserLoggedIn();
        return false;
    }

    @Override
    public void signOutUser() {
        if(connection != null)
            connection.signOutUser();
    }

    @Override
    public void signUpUser(String email, String password, final String name, final Activity activity) {
        callBack = (PresenterCallBack)activity;
        if(connection != null)
            connection.signUpUser(email,password,name,activity,this);
    }

    @Override
    public void loginRespose(boolean isError, String msg) {
        if(callBack != null)
            callBack.onLoginResponse(isError,msg);
    }

    @Override
    public void resetPasswordResponse(boolean isError, String msg) {
        if(callBack != null)
            callBack.onForgotPasswordResponse(isError,msg);
    }

    @Override
    public void loginUser(String email, String password, Activity activity) {
        callBack = (PresenterCallBack)activity;
        if(connection != null)
            connection.loginUser(email,password,activity,this);
    }

    @Override
    public void forgotPassword(String email, PresenterCallBack callBack) {
        this.callBack = callBack;
        if(connection != null){
            connection.resetPassword(email,this);
        }else{
            callBack.onForgotPasswordResponse(true,"Internal Error");
        }
    }

    @Override
    public void signUpResponse(boolean isError, String msg) {
        if(callBack != null)
            callBack.onSignUpResponse(isError,msg);
    }
}
