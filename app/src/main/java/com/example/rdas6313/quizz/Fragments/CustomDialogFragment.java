package com.example.rdas6313.quizz.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rdas6313.quizz.Interfaces.FragmentCallbacks;
import com.example.rdas6313.quizz.Interfaces.PresenterCallBack;
import com.example.rdas6313.quizz.Interfaces.PresenterConnection;
import com.example.rdas6313.quizz.Presenters.LoginAndSignUp;
import com.example.rdas6313.quizz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomDialogFragment extends Fragment implements View.OnClickListener,PresenterCallBack{

    private EditText oldPasswordView,newPasswordView,confirmPasswordView;
    private Button doneBtn;
    private ImageButton cancelBtn;
    private PresenterConnection connection;
    private CoordinatorLayout coordinatorLayout;
    private FragmentCallbacks fragmentCallbacks;
    private Dialog dialog;

    private TextInputLayout currentpasswordInputLayout,newpasswordInputLayout,confirmpasswordInputLayout;

    public CustomDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.edit_profile_layout2, container, false);
        connection = new LoginAndSignUp();
        coordinatorLayout = root.findViewById(R.id.coordinatorLayout);
        oldPasswordView = (EditText)root.findViewById(R.id.old_password);
        newPasswordView = (EditText)root.findViewById(R.id.new_passowrd);
        confirmPasswordView = (EditText)root.findViewById(R.id.confirm_password);
        //doneBtn = (Button)root.findViewById(R.id.done);
        //doneBtn.setOnClickListener(this);
        //cancelBtn = (ImageButton) root.findViewById(R.id.cancel);
        //cancelBtn.setOnClickListener(this);
       // ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        currentpasswordInputLayout = (TextInputLayout)root.findViewById(R.id.currentPasswordInputLayout);
        newpasswordInputLayout = (TextInputLayout)root.findViewById(R.id.New_PasswordInputLayout2);
        confirmpasswordInputLayout = (TextInputLayout)root.findViewById(R.id.confirm_passwordInputLayout);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        connection = new LoginAndSignUp();
        fragmentCallbacks = (FragmentCallbacks)getActivity();
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_sign_up_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView textView = dialog.findViewById(R.id.dialogText);
        textView.setText(R.string.password_change_dialog_text);
    }

    private void showDialog(){
        if(dialog != null){
            dialog.show();
        }
    }

    private void hideDialog(){
        if(dialog != null)
            dialog.dismiss();
    }

   /* private void done(){
        String old_pasword = oldPasswordView.getText().toString();
        String new_password = newPasswordView.getText().toString();
        String confirm_password = confirmPasswordView.getText().toString();
        boolean isError = false;
        if(TextUtils.isEmpty(old_pasword)){
            oldPasswordView.setError(getString(R.string.empty_editText_Error));
            isError = true;
        }
        if(TextUtils.isEmpty(new_password)){
            newPasswordView.setError(getString(R.string.empty_editText_Error));
            isError = true;
        }
        if(TextUtils.isEmpty(confirm_password)){
            confirmPasswordView.setError(getString(R.string.empty_editText_Error));
            isError = true;
        }

        if(isError)
            return;

        if(!new_password.equals(confirm_password)){
            String msg = "New Password and Re-type Password are Not Matching";
            if(fragmentCallbacks != null)
                fragmentCallbacks.CustomDialogFragmentCallback(msg);
            return;
        }

        if(connection != null) {
            showDialog();
            connection.changePassword(old_pasword, new_password, this);
        }
    }*/

   private void done(){
       String old_pasword = oldPasswordView.getText().toString();
       String new_password = newPasswordView.getText().toString();
       String confirm_password = confirmPasswordView.getText().toString();
       boolean isError = false;
       if(TextUtils.isEmpty(old_pasword)){
           currentpasswordInputLayout.setError(getString(R.string.empty_editText_Error));
           isError = true;
       }
       if(TextUtils.isEmpty(new_password)){
           newpasswordInputLayout.setError(getString(R.string.empty_editText_Error));
           isError = true;
       }
       if(TextUtils.isEmpty(confirm_password)){
           confirmpasswordInputLayout.setError(getString(R.string.empty_editText_Error));
           isError = true;
       }

       if(isError)
           return;

       if(!new_password.equals(confirm_password)){
           String msg = "New Password and Re-type Password are Not Matching";
           if(fragmentCallbacks != null)
               fragmentCallbacks.CustomDialogFragmentCallback(msg);
           return;
       }

       if(connection != null) {
           showDialog();
           connection.changePassword(old_pasword, new_password, this);
       }
   }

    private void cancel(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.done:
                done();
                break;
            case R.id.cancel:
                cancel();
                break;
        }
    }

    @Override
    public void onSignUpResponse(boolean isError, String msg) {}

    @Override
    public void onLoginResponse(boolean isError, String msg) {}

    @Override
    public void onForgotPasswordResponse(boolean isError, String msg) {}

    @Override
    public void onChageProfilePicResponse(boolean isError, String msg, String download_link) {}

    @Override
    public void onProgressProfilePic(int progress) {}

    @Override
    public void onUpdateName(boolean isError) {}

    @Override
    public void onChangePassword(boolean isError, String msg) {
        hideDialog();
        if(fragmentCallbacks != null)
            fragmentCallbacks.CustomDialogFragmentCallback(msg);
        if(!isError){
            cancel();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.change_password_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.done:
                done();
                return true;
            case R.id.cancel:
                cancel();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
