package com.example.android.framework.base;

import android.os.Bundle;

import com.example.android.framework.utils.SystemUI;

/**
 * 沉浸式
 */
public class BaseUIActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemUI.fixSystemUI(this);

    }
}
