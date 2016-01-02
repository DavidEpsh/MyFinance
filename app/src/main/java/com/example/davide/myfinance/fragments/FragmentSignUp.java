package com.example.davide.myfinance.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.davide.myfinance.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class FragmentSignUp extends Fragment {

    private View mRootView;
    EditText email, pass;

    public FragmentSignUp() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_sign_up_screen, container, false);

        email = (EditText) mRootView.findViewById(R.id.et_SignUp_Email);
        pass = (EditText) mRootView.findViewById(R.id.et_SignUp_Pass);
        Button signUp = (Button) mRootView.findViewById(R.id.btnSignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fieldsCompleted()){
                    saveNewUser();
                }
            }
        });

        return mRootView;
    }

    public boolean fieldsCompleted(){
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            Toast.makeText(getActivity(),"Invalid Email Address" , Toast.LENGTH_SHORT).show();
        }else if(pass.getText().length() < 6){
            Toast.makeText(getActivity(),"Password must be at least 6 characters long" , Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    public void saveNewUser(){
        ParseUser newUser = new ParseUser();
        newUser.setUsername(email.getText().toString());
        newUser.setPassword(pass.getText().toString());

        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

            }
        });

        ProgressBar bar = (ProgressBar) mRootView.findViewById(R.id.loader_sign_up);
        bar.setVisibility(View.VISIBLE);

    }
}