package com.example.davide.myfinance.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.davide.myfinance.R;


public class FragmentSignInSignUp extends Fragment {

    private View mRootView;
    Button btnSignIn, btnSignUp;

    public FragmentSignInSignUp() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_sign_in_sign_up, container, false);

        btnSignIn = (Button) mRootView.findViewById(R.id.btnSignIn);
        btnSignUp = (Button) mRootView.findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new FragmentSignIn());
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new FragmentSignUp());
            }
        });

        return mRootView;
    }

    private void openFragment(final Fragment fragment){

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        if(getActivity().getSupportFragmentManager().getBackStackEntryCount() == 1) {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        transaction.replace(R.id.container_sign_up_sign_in, fragment)
                .addToBackStack(null)
                .commit();
    }
}