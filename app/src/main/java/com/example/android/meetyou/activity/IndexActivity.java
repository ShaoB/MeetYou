package com.example.android.meetyou.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.android.framework.base.BaseUIActivity;
import com.example.android.framework.entity.Constant;
import com.example.android.framework.utils.JumpUtils;
import com.example.android.framework.utils.SpUtils;
import com.example.android.meetyou.MainActivity;
import com.example.android.meetyou.R;

public class IndexActivity extends BaseUIActivity {


    private static final int TYPE_MAIN = 1000;

    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case TYPE_MAIN :
                    startMain();
                    break;
            }
            return false;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        myHandler.sendEmptyMessageDelayed(TYPE_MAIN,2 * 1000);
    }

    /**
     * 进入主页
     */
    private void startMain() {
        SpUtils.getInstance().initSp(this);
        //判断APP是不是第一次启动
        boolean is_first = SpUtils.getInstance().getBoolean(Constant.SP_IS_FIRST_APP, true);

        if(is_first){
            //跳转引导页
            JumpUtils.goNext(this,GuideActivity.class,true);
        }else{
            //判断是否登录过（是否记住密码）
            String sp_token = SpUtils.getInstance().getString(Constant.SP_TOKEN, "");
            if(TextUtils.isEmpty(sp_token)){
                //没有登陆过 跳转登录页
                JumpUtils.goNext(this,LoginActivity.class,true);
            }else{
                //跳转主页
                JumpUtils.goNext(this,MainActivity.class,true);
            }
        }
    }
}
