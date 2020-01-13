package com.example.android.framework.bmob;

import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Founder: shaobin
 * Create Date: 2020/1/10
 * Profile: Bmob管理类
 */
public class BmobManager {

    private static final String BMOB_APP_ID = "eeb75e7daf854cebe6fd89b528c8e91b";

    private volatile static BmobManager mInstance = null;

    private BmobManager() {

    }

    public static BmobManager getInstance() {
        if (mInstance == null) {
            synchronized (BmobManager.class) {
                if (mInstance == null) {
                    mInstance = new BmobManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void initBomb(Context context) {
        Bmob.initialize(context, BMOB_APP_ID);
    }

    /**
     * 发送短信
     *
     * @param phone    手机号
     * @param listener 回调
     */
    public void requestSMS(String phone, QueryListener<Integer> listener) {
        BmobSMS.requestSMSCode(phone, "", listener);
    }

    /**
     * 通过手机号和验证码注册登录
     *
     * @param phone    手机号
     * @param smsCode  验证码
     * @param listener 回调
     */
    public void signOrLoginByMobilePhone(String phone, String smsCode, LogInListener<User> listener) {
        BmobUser.signOrLoginByMobilePhone(phone, smsCode, listener);
    }

    /**
     * 获取用户实体
     * @return
     */
    public User getUser(){
        return BmobUser.getCurrentUser(User.class);
    }

    public boolean islogin(){
        return BmobUser.isLogin();
    }
}
