package com.example.rdas6313.quizz;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.rdas6313.quizz.Interfaces.PresenterCallBack;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.example.rdas6313.quizz.Presenters.LoginAndSignUp;
import com.example.rdas6313.quizz.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener,PresenterCallBack{

    private ImageView profileImageView;
    private ImageButton nameIcon;
    private ImageButton addProfilePicBtn;
    private final int PHOTO_REQUEST = 1;
    private PresenterConnection loginConnection;
    private Map<String,Object> userData;
    private CoordinatorLayout coordinatorLayout;
    private EditText emailView,nameView;
    private final String TAG = EditProfileActivity.class.getName();
    private boolean isInEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Edit Profile");
        }
        loginConnection = new LoginAndSignUp();
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        profileImageView = findViewById(R.id.profilePic);
        profileImageView.setImageResource(R.drawable.avatar);
        nameView = (EditText)findViewById(R.id.name);
        nameIcon = (ImageButton)findViewById(R.id.nameIcon);
        nameIcon.setOnClickListener(this);
        nameView.setEnabled(false);
        emailView = (EditText)findViewById(R.id.email);
        makeRoundImage();
        addProfilePicBtn = findViewById(R.id.addProfilePicBtn);
        addProfilePicBtn.setOnClickListener(this);
        loadUserData();

    }

    private void getPhotoFromGallary(){
        String mimeTypes[] = {"image/jpeg"};
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Choose Photo"),PHOTO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PHOTO_REQUEST:
                if(resultCode == RESULT_OK && data != null){
                    Uri uri = data.getData();
                    if(loginConnection != null)
                        loginConnection.changeProfilePic(uri.toString(),this);
                }
                break;
        }
    }

    private void makeRoundImage(){
        Bitmap imageBitmap = ((BitmapDrawable) profileImageView.getDrawable()).getBitmap();
        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
        imageDrawable.setCircular(true);
        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
        profileImageView.setImageDrawable(imageDrawable);
    }

    private void loadProfilePic(Uri uri){
        Picasso.get().load(uri).placeholder(R.mipmap.ic_launcher_round).into(profileImageView, new Callback() {
            @Override
            public void onSuccess() {
               makeRoundImage();
            }

            @Override
            public void onError(Exception e) {
                profileImageView.setImageResource(R.drawable.avatar);
                makeRoundImage();
            }
        });
    }

    private void changeProfilePic(){
        getPhotoFromGallary();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addProfilePicBtn:
                changeProfilePic();
                break;
            case R.id.nameIcon:
                editName();
                break;
        }
    }

    private void saveName(String name){
        if(loginConnection != null){
            loginConnection.changeName(name,this);
        }
    }

    private void editName(){
        if(!isInEditMode){
            nameView.setEnabled(true);
            isInEditMode = true;
            nameIcon.setImageResource(R.drawable.ic_done_black_24dp);
        }else{
            String name = nameView.getText().toString();
            if(TextUtils.isEmpty(name)){
                nameView.setError(getString(R.string.empty_editText_Error));
                return;
            }
            saveName(name);
            nameView.setEnabled(false);
            nameIcon.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            isInEditMode = false;
        }
    }

    @Override
    public void onSignUpResponse(boolean isError, String msg) {}

    @Override
    public void onLoginResponse(boolean isError, String msg) {}

    @Override
    public void onForgotPasswordResponse(boolean isError, String msg) {}

    @Override
    public void onChageProfilePicResponse(boolean isError, String msg, String download_link) {
        if(!isError){
            //success
            //loadProfilePic(Uri.parse(download_link));
            loadUserData();
        }else{
            //error
        }
    }

    @Override
    public void onProgressProfilePic(int progress) {

    }

    @Override
    public void onUpdateName(boolean isError) {
        if(isError){
            nameView.setText((String)userData.get(PresenterConnection.USER_NAME));
            Snackbar.make(coordinatorLayout,"Unable To Change Name",Snackbar.LENGTH_SHORT).show();
        }
    }

    private void loadUserData(){
        if(loginConnection != null)
            userData = loginConnection.getUserInfo();
        if(userData == null)
            return;
        String link = (String)userData.get(PresenterConnection.USER_PHOTO_URL);
        String name = (String)userData.get(PresenterConnection.USER_NAME);
        String email = (String)userData.get(PresenterConnection.USER_EMAIL);
        boolean isEmailVerified = (boolean)userData.get(PresenterConnection.USER_EMAIL_VERIFIED);
        if(link != null)
            loadProfilePic(Uri.parse(link));
        if(name != null)
            nameView.setText(name);
        if(email != null) {
            emailView.setText(email);
            emailView.setEnabled(false);
            if(isEmailVerified) {
                emailView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_black_24dp, 0, R.drawable.ic_done_green_a700_24dp, 0);
            }
        }

    }



}
