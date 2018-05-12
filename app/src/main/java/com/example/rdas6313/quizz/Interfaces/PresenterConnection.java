package com.example.rdas6313.quizz.Interfaces;

import android.app.Activity;

import java.util.Map;

/**
 * Created by rdas6313 on 24/4/18.
 */

public interface PresenterConnection {

    public final String USER_NAME = "user_name";
    public final String USER_EMAIL = "user_email";
    public final String USER_PHOTO_URL = "user_photo_url";
    public final String USER_EMAIL_VERIFIED = "user_email_verified";

    public boolean isUserAlreadyLoggedIn();
    public void signUpUser(String email, String password, String name, Activity activity);
    public void signOutUser();
    public void loginUser(String email,String password,Activity activity);
    public void forgotPassword(String email,PresenterCallBack callBack);
    public void changeProfilePic(String pic_uri,PresenterCallBack callBack);
    public Map<String,Object> getUserInfo();
}
