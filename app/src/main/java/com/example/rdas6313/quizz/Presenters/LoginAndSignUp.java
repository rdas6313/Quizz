package com.example.rdas6313.quizz.Presenters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rdas6313.quizz.Interfaces.LoginSignUpModelCallback;
import com.example.rdas6313.quizz.Interfaces.LoginSignUpModelConnection;
import com.example.rdas6313.quizz.Interfaces.PresenterCallBack;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.example.rdas6313.quizz.Models.LoginAndSignUpModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rdas6313 on 24/4/18.
 */

public class LoginAndSignUp implements PresenterConnection,LoginSignUpModelCallback{
    @Override
    public Map<String, Object> getUserInfo() {
        Map<String,Object> data = null;
        if(connection != null){
            data = new HashMap<>();
            data.put(PresenterConnection.USER_NAME, connection.getProfileName());
            data.put(PresenterConnection.USER_EMAIL,connection.getEmail());
            data.put(PresenterConnection.USER_PHOTO_URL,connection.getProfilePhotoUrl());
            data.put(PresenterConnection.USER_EMAIL_VERIFIED,connection.isEmailVerified());
        }
        return data;
    }

    @Override
    public void changeName(String name, PresenterCallBack callBack) {
        this.callBack = callBack;
        if(connection != null)
            connection.updateName(name,this);
        else
            callBack.onUpdateName(true);
    }

    @Override
    public void changePassword(String old_password, String new_password, PresenterCallBack callBack) {
        this.callBack = callBack;
        if(connection != null) {
            connection.changePassword(old_password, new_password, this);
        }else{
            if(callBack != null)
                callBack.onChangePassword(true,"Internal Error");
        }
    }

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
    public void changeProfilePic(String pic_uri,PresenterCallBack callBack) {
        this.callBack = callBack;
        if(connection != null)
            connection.updateProfilePic(pic_uri,this);
    }

    @Override
    public void resetPasswordResponse(boolean isError, String msg) {
        if(callBack != null)
            callBack.onForgotPasswordResponse(isError,msg);
    }

    @Override
    public void onResponseUpdateProfilePic(boolean isError, String msg, String download_link) {
        if(callBack != null){
            callBack.onChageProfilePicResponse(isError,msg,download_link);
        }
    }

    @Override
    public void onProgressUpdateProfilePic(int progress) {
        if(callBack != null)
            callBack.onProgressProfilePic(progress);
    }

    @Override
    public void onUpdateNameResponse(boolean isError, String name) {
        if(callBack != null)
            callBack.onUpdateName(isError);
    }

    @Override
    public void onUpdatePassword(boolean isError, String msg) {
        if(callBack != null)
            callBack.onChangePassword(isError,msg);
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
