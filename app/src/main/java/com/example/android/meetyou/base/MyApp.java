package com.example.android.meetyou.base;

import android.app.Application;

import com.example.android.framework.Framework;

/**
 * Founder: shaobin
 * Create Date: 2020/1/10
 * Profile:
 */
public class MyApp extends Application {
    public static MyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Framework.getFramework().initFramework(this);
    }

    public static MyApp getInstance() {
        return mInstance;
    }
}
