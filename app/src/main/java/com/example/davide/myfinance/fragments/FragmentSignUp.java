package com.example.davide.myfinance.fragments;


import android.content.Intent;
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
import android.widget.ViewFlipper;

import com.example.davide.myfinance.R;
import com.example.davide.myfinance.activities.MainActivity;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class FragmentSignUp extends Fragment {

    private View mRootView;
    ViewFlipper vf;
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
        vf = (ViewFlipper) mRootView.findViewById(R.id.view_flipper_sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fieldsCompleted()){
                    vf.setDisplayedChild(1);
                    saveNewUser();
                }
            }
        });

        return mRootView;
    }

    public boolean fieldsCompleted(){
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            Toast.makeText(getActivity(),"Invalid Email Address" , Toast.LENGTH_SHORT).show();
            return false;
        }else if(pass.getText().length() < 6){
            Toast.makeText(getActivity(),"Password must be at least 6 characters long" , Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void saveNewUser(){
        ParseUser newUser = new ParseUser();
        newUser.setUsername(email.getText().toString());
        newUser.setPassword(pass.getText().toString());

        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getActivity(), "Sign Up successful", Toast.LENGTH_SHORT).show();
                    finishAndSetResult();
                }else{
                    Toast.makeText(getActivity(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_SHORT).show();
                    vf.setDisplayedChild(0);
//                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

    }

    public void finishAndSetResult(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", MainActivity.RESULT_LOG_IN_SIGN_UP);
        getActivity().setResult(MainActivity.RESULT_OK, returnIntent);
        getActivity().finish();

    }
}