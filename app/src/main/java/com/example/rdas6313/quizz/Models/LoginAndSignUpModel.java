package com.example.rdas6313.quizz.Models;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rdas6313.quizz.Interfaces.LoginSignUpModelCallback;
import com.example.rdas6313.quizz.Interfaces.LoginSignUpModelConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by rdas6313 on 25/4/18.
 */

public class LoginAndSignUpModel implements LoginSignUpModelConnection{

    private final String TAG = LoginAndSignUpModel.class.getName();
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;

    public LoginAndSignUpModel(){
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
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
                            Log.e(TAG,task.getException()+"");
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException e){
                                if(callback != null)
                                    callback.loginRespose(true,"Invalid Username Or Password");
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                if(callback != null)
                                    callback.loginRespose(true,"Invalid Username Or Password");
                            }catch (Exception e){
                                if(callback != null)
                                    callback.loginRespose(true,e.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public String getCurrentUserId() {
        FirebaseUser currentUser = null;
        if(mAuth != null){
            currentUser = mAuth.getCurrentUser();
        }
        if(currentUser != null)
            return currentUser.getUid();
        return null;
    }

    @Override
    public void resetPassword(String email, final LoginSignUpModelCallback callback) {
        if(mAuth == null){
            callback.resetPasswordResponse(true,"Internal Error");
            return;
        }
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(callback != null)
                        callback.resetPasswordResponse(false,"Password Reset Successfully.Check Your Email");
                }else{
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        if(callback != null)
                            callback.resetPasswordResponse(true,"Invalid Email Address.Check Your Email Address");
                    }catch (Exception e){
                        Log.e(TAG,task.getException()+"");
                        if(callback != null)
                            callback.resetPasswordResponse(true,"Unable To Reset Password.Try again later");
                    }

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
                            final FirebaseUser user =  mAuth.getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            user.updateProfile(profileChangeRequest).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        sendVerificationMail(user,callback,activity);
                                    }else {
                                        String msg = "Sign Up Successfully But unable to update your profile";
                                        Log.e(TAG,task.getException()+"");
                                        if(callback != null)
                                            callback.signUpResponse(false,msg);
                                    }
                                }
                            });
                        }else{
                            String msg = task.getException().getMessage();
                            Log.e(TAG,msg);
                            if(callback != null)
                                callback.signUpResponse(true,msg);
                        }
                    }
                });
    }

    private void sendVerificationMail(FirebaseUser user, final LoginSignUpModelCallback callback, Activity activity){
        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String msg = "";
                    if(task.isSuccessful()){
                        msg = "Sign Up Successfully.Verification Mail has been sent to your email address";
                    }else{
                        msg = "Sign Up Successfully";
                    }
                    if(callback != null)
                        callback.signUpResponse(false,msg);
                }
            });
        }else{
            String msg = "Sign Up Successfully";
            if(callback != null)
                callback.signUpResponse(false,msg);
        }
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

    @Override
    public void updateProfilePic(String uri_path, final LoginSignUpModelCallback callback) {
        String userId = getCurrentUserId();
        if(userId == null) {
            if(callback != null)
                callback.onResponseUpdateProfilePic(true,"Internal Error",null);
            return;
        }
        StorageReference rootStorageRef = firebaseStorage.getReference();
        Uri uri = Uri.parse(uri_path);
        StorageReference user_imageRef = rootStorageRef.child("user_profile_images/"+userId+".jpg");
        UploadTask uploadTask = user_imageRef.putFile(uri);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    UploadTask.TaskSnapshot snapshot = task.getResult();
                    updateProfilePhotolink(snapshot.getDownloadUrl().toString(),callback);
                }else{
                    if(callback != null)
                        callback.onResponseUpdateProfilePic(true,"Profile Pic updation Failed",null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,e.getMessage());
                if(callback != null)
                    callback.onResponseUpdateProfilePic(true,"Profile Pic updation Failed",null);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                long totalSize = taskSnapshot.getTotalByteCount();
                long uploadedSize = taskSnapshot.getBytesTransferred();
                int progress = (int)((100*uploadedSize)/totalSize);
                if(callback != null)
                    callback.onProgressUpdateProfilePic(progress);
            }

        });
    }

    @Override
    public void updateName(final String name, final LoginSignUpModelCallback callback) {
        if(mAuth == null){
            if(callback != null)
                callback.onUpdateNameResponse(true,null);
            return;
        }
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            if(callback != null)
                callback.onUpdateNameResponse(true,null);
            return;
        }

        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();
        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(callback != null)
                        callback.onUpdateNameResponse(false,name);
                }else{
                    if(callback != null)
                        callback.onUpdateNameResponse(true,user.getDisplayName());
                }
            }
        });
    }

    @Override
    public String getProfilePhotoUrl() {
        if(mAuth == null)
            return null;
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null)
            return null;
        else{
            Uri uri = user.getPhotoUrl();
            if(uri == null)
                return null;
            else
               return uri.toString();
        }

    }

    @Override
    public String getProfileName() {
        if(mAuth == null)
            return null;
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null)
            return null;
        else
            return user.getDisplayName();

    }

    @Override
    public String getEmail() {
        if(mAuth == null)
            return null;
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null)
            return null;
        else
            return user.getEmail();
    }

    @Override
    public boolean isEmailVerified() {
        if(mAuth == null)
            return false;
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null)
            return false;
        else
            return user.isEmailVerified();
    }

    private void deletePhoto(){
        String userId = getCurrentUserId();
        if(userId == null)
            return;
        StorageReference rootRef = firebaseStorage.getReference();
        StorageReference imageRef = rootRef.child("user_profile_images/"+userId+".jpg");
        imageRef.delete();
    }

    private void updateProfilePhotolink(final String url, final LoginSignUpModelCallback callback){
        if(mAuth == null) {
            return;
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            return;
        }
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(url))
                .build();
        currentUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (callback != null) {
                        callback.onResponseUpdateProfilePic(false, "Profile Pic updated Successfully", url);
                    } else {

                        deletePhoto();

                        if (callback != null) {
                            callback.onResponseUpdateProfilePic(false, "Unable To Update Profile Pic", null);
                        }
                    }
                }
            }
        });
    }
}
