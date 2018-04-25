package com.example.rdas6313.quizz.Interfaces;

import android.app.Activity;

/**
 * Created by rdas6313 on 24/4/18.
 */

public interface PresenterConnection {
    public boolean isUserAlreadyLoggedIn();
    public void signUpUser(String email, String password, String name, Activity activity);
    public void signOutUser();
    public void loginUser(String email,String password,Activity activity);
}
