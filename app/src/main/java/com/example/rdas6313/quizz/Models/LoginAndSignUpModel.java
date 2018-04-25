package com.example.rdas6313.quizz.Models;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rdas6313.quizz.Interfaces.LoginSignUpModelCallback;
import com.example.rdas6313.quizz.Interfaces.LoginSignUpModelConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by rdas6313 on 25/4/18.
 */

public class LoginAndSignUpModel implements LoginSignUpModelConnection{

    private final String TAG = LoginAndSignUpModel.class.getName();
    private FirebaseAuth mAuth;

    public LoginAndSignUpModel(){
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void loginUser(String email, String password, Activity activity, final LoginSignUpModelCallback callback) {
        if(mAuth == null && callback != null){
            callback.loginRespose(true,"Internal Error! login Failed");
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(callback != null)
                                callback.loginRespose(false,"Welcome");
                        }else{
                            String msg = task.getException().getMessage();
                            Log.e(TAG,msg);
                            if(callback != null)
                                callback.loginRespose(true,msg);
                        }
                    }
                });
    }

    @Override
    public void signUpUser(String email, String password, final String name, final Activity activity, final LoginSignUpModelCallback callback) {
        if(mAuth == null){
            callback.signUpResponse(true,"Internal Error ! Sign up Failed");
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user =  mAuth.getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            user.updateProfile(profileChangeRequest).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        callback.signUpResponse(false,"Sign Up Successfully");
                                    else {
                                        String msg = task.getException().getMessage();
                                        Log.e(TAG,msg);
                                        callback.signUpResponse(true,msg);
                                    }
                                }
                            });
                        }else{
                            String msg = task.getException().getMessage();
                            Log.e(TAG,msg);
                            callback.signUpResponse(true,msg);
                        }
                    }
                });
    }

    @Override
    public boolean isUserLoggedIn() {
        FirebaseUser currentUser = null;
        if(mAuth != null)
            currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
            return true;
        else
            return false;
    }

    @Override
    public void signOutUser() {
        if(mAuth != null)
            mAuth.signOut();
    }
}
