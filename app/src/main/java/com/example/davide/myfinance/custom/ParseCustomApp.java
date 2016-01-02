package com.example.davide.myfinance.custom;

import android.app.Application;

import com.example.davide.myfinance.models.Model;
import com.parse.Parse;

public class ParseCustomApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Model.instance().initParse(getApplicationContext());
    }
}