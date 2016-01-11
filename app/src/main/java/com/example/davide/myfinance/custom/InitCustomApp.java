package com.example.davide.myfinance.custom;

import android.app.Application;

import com.example.davide.myfinance.models.Model;
import com.parse.Parse;

public class InitCustomApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Model.instance().initParse(getApplicationContext());
        Model.instance().init(getApplicationContext());
    }
}