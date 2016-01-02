package com.example.davide.myfinance.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.davide.myfinance.R;


public class FragmentSignIn extends Fragment {

    private View mRootView;

    public FragmentSignIn() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_sign_in_screen, container, false);

        EditText userName = (EditText) mRootView.findViewById(R.id.et_SignIn_UserName);
        EditText pass = (EditText) mRootView.findViewById(R.id.et_SignUp_Pass);
        Button signIn = (Button) mRootView.findViewById(R.id.btnSignIn);
        final ProgressBar loader = (ProgressBar) mRootView.findViewById(R.id.loader_sign_in);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
            }
        });

        return mRootView;
    }
}