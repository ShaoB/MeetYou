package com.example.android.framework.cloud;

import android.content.Context;

import com.example.android.framework.utils.LogUtils;

import io.rong.imlib.RongIMClient;

/**
 * Founder: shaobin
 * Create Date: 2020/1/17
 * Profile:
 */
public class CloudManager {
    //令牌
    public static final String CLOUD_SECRET = "c2pPJeL2qcYne";
    //key
    public static final String CLOUD_KEY = "k51hidwqkvnpb";

    private static volatile CloudManager mInstnce = null;

    private CloudManager() {

    }

    public static CloudManager getInstance() {
        if (mInstnce == null) {
            synchronized (CloudManager.class) {
                if (mInstnce == null) {
                    mInstnce = new CloudManager();
                }
            }
        }
        return mInstnce;
    }

    /**
     * 初始化SDK
     *
     * @param mContext
     */
    public void initCloud(Context mContext) {
        RongIMClient.init(mContext);
    }

    /**
     * 连接融云服务
     *
     * @param token
     */
    public void connect(String token) {
        LogUtils.e("连接融云服务");
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                LogUtils.e("Token Error");
            }

            @Override
            public void onSuccess(String s) {
                LogUtils.e("融云服务连接成功：" + s);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtils.e("融云服务连接失败：" + errorCode);
            }
        });
    }


    /**
     * 断开连接
     */
    public void disconnect() {
        RongIMClient.getInstance().disconnect();
    }

    /**
     * 退出登录
     */
    public void logout() {
        RongIMClient.getInstance().logout();
    }

    /**
     * 接收消息的监听器
     *
     * @param listener
     */
    public void setOnReceiveMessageListener(RongIMClient.OnReceiveMessageListener listener) {
        RongIMClient.setOnReceiveMessageListener(listener);
    }
}
