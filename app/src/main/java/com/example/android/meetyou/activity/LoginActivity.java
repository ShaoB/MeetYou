package com.example.android.meetyou.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.android.framework.base.BaseUIActivity;
import com.example.android.meetyou.R;

public class LoginActivity extends BaseUIActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
