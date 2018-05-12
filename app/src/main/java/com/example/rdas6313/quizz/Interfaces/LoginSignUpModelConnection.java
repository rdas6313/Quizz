package com.example.rdas6313.quizz.Interfaces;

import android.app.Activity;

/**
 * Created by rdas6313 on 25/4/18.
 */

public interface LoginSignUpModelConnection {
    public void signUpUser(String email, String password, String name, Activity activity,LoginSignUpModelCallback callback);
    public boolean isUserLoggedIn();
    public void signOutUser();
    public void loginUser(String email,String password,Activity activity,LoginSignUpModelCallback callback);
    public String getCurrentUserId();
    public void resetPassword(String email,LoginSignUpModelCallback callback);
    public void updateProfilePic(String path,LoginSignUpModelCallback callback);
    public String getProfilePhotoUrl();
    public String getProfileName();
    public String getEmail();
    public boolean isEmailVerified();
}
