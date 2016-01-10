package com.example.davide.myfinance.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class AdapterTabLayout extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public AdapterTabLayout(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "My Account";
            case 1:
                return "Home";
            case 2:
                return "Trip";
            default:
                return "Home";
        }
    }
}
