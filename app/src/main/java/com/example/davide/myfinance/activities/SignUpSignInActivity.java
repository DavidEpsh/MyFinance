package com.example.davide.myfinance.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


import com.example.davide.myfinance.R;
import com.example.davide.myfinance.fragments.FragmentSignInSignUp;

public class SignUpSignInActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_sign_up);

		openFragment(new FragmentSignInSignUp());
    }

	private void openFragment(final Fragment fragment){

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		if(getSupportFragmentManager().getBackStackEntryCount() == 1) {
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		}
		transaction.replace(R.id.activity_sign_in_sign_up, fragment)
				.addToBackStack(null)
				.commit();
	}

	@Override
	public void onBackPressed() {

		if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
			getSupportFragmentManager().popBackStack();
		} else {
			finish();
		}
	}
    
}
