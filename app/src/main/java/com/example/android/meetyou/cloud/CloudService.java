package com.example.android.meetyou.cloud;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Founder: shaobin
 * Create Date: 2020/1/15
 * Profile:
 */
public class CloudService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
