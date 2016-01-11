package com.example.davide.myfinance.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.davide.myfinance.R;
import com.example.davide.myfinance.activities.MainActivity;
import com.example.davide.myfinance.models.Model;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.HashMap;


public class FragmentSignIn extends Fragment {

    private View mRootView;
    ViewFlipper vf;
    EditText email, pass;

    public FragmentSignIn() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_sign_in_screen, container, false);

        email = (EditText) mRootView.findViewById(R.id.et_SignIn_UserName);
        pass = (EditText) mRootView.findViewById(R.id.et_SignIn_Pass);
        Button signIn = (Button) mRootView.findViewById(R.id.btnSignIn);
        vf = (ViewFlipper)mRootView.findViewById(R.id.view_flipper_sign_in);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fieldsCompleted()) {
                    vf.setDisplayedChild(1);
                    saveNewUser();
                }
            }
        });

        return mRootView;
    }

    public boolean fieldsCompleted(){
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            Toast.makeText(getActivity(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
            return false;

        }else if(pass.getText().length() < 6){
            Toast.makeText(getActivity(),"Password must be at least 6 characters long" , Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;
    }

    public void saveNewUser(){

        ParseUser.logInInBackground(email.getText().toString(), pass.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    startUpdatingData();
                }else{
                    Toast.makeText(getActivity(),"Wrong username or password", Toast.LENGTH_SHORT).show();
                    vf.setDisplayedChild(0);
                }
            }
        });

    }

    public void startUpdatingData(){
        Model.instance().getAllExpensesOrUpdateAsync(false, new Model.GetAllExpensesOrUpdateAsync() {
            @Override
            public void onResult() {
                finishAndSetResult();
            }
        });
    }

    public void finishAndSetResult(){

        if(!Model.instance().returnMySheets().containsKey("My Account")){
            String userName = ParseUser.getCurrentUser().getUsername();
            Model.instance().addSheets(userName, "My Account", true);
            Model.instance().addUserSheets(userName, userName, true);
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", MainActivity.RESULT_LOG_IN_SIGN_UP);
        getActivity().setResult(MainActivity.RESULT_OK, returnIntent);
        getActivity().finish();
    }
}