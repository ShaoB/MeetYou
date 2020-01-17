package com.example.android.framework;

import android.content.Context;

import com.example.android.framework.bmob.BmobManager;
import com.example.android.framework.cloud.CloudManager;
import com.example.android.framework.utils.SpUtils;

public class Framework {
    private volatile static  Framework mframework;

    private Framework() {

    }

    public static Framework getFramework(){
        if(mframework == null){
            synchronized (Framework.class){
                if(mframework == null){
                    mframework = new Framework();
                }
            }
        }
        return mframework;
    }

    /**
     * 初始化框架
     * @param context
     */
    public void initFramework(Context context){
        SpUtils.getInstance().initSp(context);
        BmobManager.getInstance().initBomb(context);
        CloudManager.getInstance().initCloud(context);
    }
}
