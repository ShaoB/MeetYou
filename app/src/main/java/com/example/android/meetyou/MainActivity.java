package com.example.android.meetyou;

import android.os.Bundle;

import com.example.android.framework.base.BaseUIActivity;
import com.example.android.framework.bmob.BmobManager;
import com.example.android.framework.utils.LogUtils;

public class MainActivity extends BaseUIActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtils.e(BmobManager.getInstance().getUser().getMobilePhoneNumber());
    }
}
