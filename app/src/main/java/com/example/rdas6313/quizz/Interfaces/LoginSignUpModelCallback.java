package com.example.rdas6313.quizz.Interfaces;

/**
 * Created by rdas6313 on 25/4/18.
 */

public interface LoginSignUpModelCallback {
    public void signUpResponse(boolean isError,String msg);
    public void loginRespose(boolean isError,String msg);
    public void resetPasswordResponse(boolean isError,String msg);
    public void onResponseUpdateProfilePic(boolean isError,String msg,String download_link);
    public void onProgressUpdateProfilePic(int progress);
    public void onUpdateNameResponse(boolean isError,String name);
}
