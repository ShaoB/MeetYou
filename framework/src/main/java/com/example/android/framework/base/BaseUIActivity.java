package com.example.android.framework.base;

import android.os.Bundle;

import com.example.android.framework.utils.SystemUI;

import androidx.appcompat.app.AppCompatActivity;

public class BaseUIActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemUI.fixSystemUI(this);

    }
}
