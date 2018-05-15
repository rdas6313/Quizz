package com.example.rdas6313.quizz.Interfaces;

/**
 * Created by rdas6313 on 24/4/18.
 */

public interface PresenterCallBack {
    public void onSignUpResponse(boolean isError,String msg);
    public void onLoginResponse(boolean isError,String msg);
    public void onForgotPasswordResponse(boolean isError,String msg);
    public void onChageProfilePicResponse(boolean isError,String msg,String download_link);
    public void onProgressProfilePic(int progress);
    public void onUpdateName(boolean isError);
    public void onChangePassword(boolean isError,String msg);
}
