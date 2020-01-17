package com.example.android.meetyou.cloud;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.android.framework.cloud.CloudManager;
import com.example.android.framework.entity.Constant;
import com.example.android.framework.utils.LogUtils;
import com.example.android.framework.utils.SpUtils;

import androidx.annotation.Nullable;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

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

    @Override
    public void onCreate() {
        super.onCreate();

        linCloudServer();
    }

    /**
     * 链接云服务
     */
    private void linCloudServer() {
        String token = SpUtils.getInstance().getString(Constant.SP_TOKEN, "");
        LogUtils.e("链接云服务，获取到的Token："+ token);
        CloudManager.getInstance().connect(token);
        CloudManager.getInstance().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                LogUtils.e("接收的消息：" + message);
                return false;
            }
        });
    }
}
