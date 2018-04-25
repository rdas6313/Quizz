package com.example.rdas6313.quizz.Presenters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rdas6313.quizz.Interfaces.PresenterCallBack;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by rdas6313 on 24/4/18.
 */

public class LoginAndSignUp implements PresenterConnection{

    private FirebaseAuth mAuth;
    private final String TAG = LoginAndSignUp.class.getName();

    public LoginAndSignUp(){
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean isUserAlreadyLoggedIn() {
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

    @Override
    public void signUpUser(String email, String password, final String name, final Activity activity) {
        final PresenterCallBack presenterCallBack = (PresenterCallBack) activity;
        if(mAuth == null){
            presenterCallBack.onSignUpResponse(true,"SignUp Not Possible");
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
                                    presenterCallBack.onSignUpResponse(false,"SignUp Successfully");
                                }
                            });
                        }else{
                            Log.e(TAG,task.getException()+"");
                            presenterCallBack.onSignUpResponse(true,"SignUp Failed");
                        }
                    }
                });
    }
}
