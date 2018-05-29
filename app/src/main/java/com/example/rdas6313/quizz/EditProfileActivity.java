package com.example.rdas6313.quizz;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rdas6313.quizz.Fragments.CustomDialogFragment;
import com.example.rdas6313.quizz.Interfaces.FragmentCallbacks;
import com.example.rdas6313.quizz.Interfaces.PresenterCallBack;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.example.rdas6313.quizz.Presenters.LoginAndSignUp;
import com.example.rdas6313.quizz.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener,PresenterCallBack,FragmentCallbacks{

    private ImageView profileImageView,verifiedEmail;
    private ImageButton nameIcon,cancelBtn;
    private ImageButton addProfilePicBtn;
    private final int PHOTO_REQUEST = 1;
    private PresenterConnection loginConnection;
    private Map<String,Object> userData;
    private CoordinatorLayout coordinatorLayout;
    private EditText nameView;
    private CardView changePasswordBtn;
    private final String TAG = EditProfileActivity.class.getName();
    private boolean isInEditMode;
    private CustomDialogFragment dialogFragment;
    private Dialog dialog;
    private TextView dialogTextView,emailView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.edit_profile);
            actionBar.setElevation(0);
        }
        loginConnection = new LoginAndSignUp();

        verifiedEmail = (ImageView)findViewById(R.id.verifiedEmail);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        profileImageView = findViewById(R.id.profilePic);
        profileImageView.setImageResource(R.drawable.avatar);
        nameView = (EditText)findViewById(R.id.name);
        nameIcon = (ImageButton)findViewById(R.id.nameIcon);
        //cancelBtn = (ImageButton)findViewById(R.id.cancel);
        //cancelBtn.setOnClickListener(this);
        nameIcon.setOnClickListener(this);
        changeEditText(false);
        emailView = (TextView)findViewById(R.id.email);
        //makeRoundImage();
        addProfilePicBtn = findViewById(R.id.addProfilePicBtn);
        addProfilePicBtn.setOnClickListener(this);
        //changePasswordBtn = (Button)findViewById(R.id.changePasswordBtn);
        changePasswordBtn = (CardView)findViewById(R.id.changePasswordBtn);
        changePasswordBtn.setOnClickListener(this);
        loadUserData();
        dialogFragment = new CustomDialogFragment();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.photo_upload_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialogTextView = (TextView)dialog.findViewById(R.id.progressText);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    private void showDialog(){
        if(dialog != null) {
            dialog.show();
            if(dialogTextView != null)
                dialogTextView.setText("0%");
        }
    }

    private void hideDialog(){
        if(dialog != null)
            dialog.hide();
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
                    if(loginConnection != null) {
                      //  showDialog();
                        progressBar.setVisibility(View.VISIBLE);
                        loginConnection.changeProfilePic(uri.toString(), this);
                    }
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
        Picasso.get().load(uri).noFade().resize(300,300).centerCrop().onlyScaleDown().placeholder(R.drawable.avatar).into(profileImageView, new Callback() {
            @Override
            public void onSuccess() {
              // makeRoundImage();
            }

            @Override
            public void onError(Exception e) {
                profileImageView.setImageResource(R.drawable.avatar);
               // makeRoundImage();
            }
        });
    }

    private void changeProfilePic(){
        getPhotoFromGallary();
    }

    private void changePassword(){
        getSupportFragmentManager().beginTransaction().add(R.id.coordinatorLayout,dialogFragment).commit();
    }

    private void close(){
        finish();
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
            case R.id.changePasswordBtn:
                changePassword();
                break;
            case R.id.cancel:
                close();
                break;
        }
    }

    private void saveName(String name){
        if(loginConnection != null){
            loginConnection.changeName(name,this);
        }
    }

    private void changeEditText(boolean enable){
        if(enable){
            nameView.setEnabled(true);
            nameView.setFocusable(true);
            nameView.setFocusableInTouchMode(true);
            nameView.requestFocus();
        }else{
            nameView.setEnabled(false);
            //nameView.setInputType(InputType.TYPE_NULL);
            nameView.setFocusable(false);
            nameView.setFocusableInTouchMode(false);
        }
    }

    private void editName(){
        if(!isInEditMode){
            changeEditText(true);
            isInEditMode = true;
            nameIcon.setImageResource(R.drawable.ic_done_black_24dp);
        }else{
            String name = nameView.getText().toString();
            if(TextUtils.isEmpty(name)){
                nameView.setError(getString(R.string.empty_editText_Error));
                return;
            }
            saveName(name);
            changeEditText(false);
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
       // hideDialog();
        progressBar.setVisibility(View.GONE);
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
        Log.e(TAG,"Progress "+progress);
       /* if(dialogProgressBar != null && dialogTextView != null) {
            dialogProgressBar.setProgress(progress);
            dialogTextView.setText(progress+"%");
        }*/
       if(progressBar != null)
           progressBar.setProgress(progress);
    }

    @Override
    public void onUpdateName(boolean isError) {
        if(isError){
            nameView.setText((String)userData.get(PresenterConnection.USER_NAME));
            Snackbar.make(coordinatorLayout,"Unable To Change Name",Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(coordinatorLayout,"Name Changed Successfully",Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onChangePassword(boolean isError, String msg) {

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
                verifiedEmail.setVisibility(View.VISIBLE);
            }
        }

    }


    @Override
    public void QuestionSetFragmentCallbacks(String key, String name, long point) {}

    @Override
    public void QuestionFrgmentCallbacks(int total_question, int right_ans, long each_question_point, String question_key) {

    }

    @Override
    public void ScoreBoardFragmentCallback() {}

    @Override
    public void CustomDialogFragmentCallback(String msg) {
        Snackbar.make(coordinatorLayout,msg,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void ActionBarElevation(boolean needToShow) {}

    @Override
    public void setBottomNavigartionBarVisibility(boolean visibility) {}
}
