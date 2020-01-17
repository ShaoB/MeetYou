package com.example.android.meetyou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.android.framework.base.BaseActivity;
import com.example.android.framework.bmob.BmobManager;
import com.example.android.framework.entity.Constant;
import com.example.android.framework.utils.JumpUtils;
import com.example.android.framework.utils.LogUtils;
import com.example.android.framework.utils.SpUtils;
import com.example.android.framework.utils.ToastUtils;
import com.example.android.meetyou.MainActivity;
import com.example.android.meetyou.R;
import com.example.android.meetyou.networkUtils.NetWorkUtils;

import androidx.annotation.NonNull;

public class IndexActivity extends BaseActivity implements View.OnClickListener {


    private static final int TYPE_MAIN = 1000;

    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case TYPE_MAIN:
                    startMain();
                    break;
            }
            return false;
        }
    });
    /**
     * 跳过
     */
    private TextView mTvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();

        //判断网络是否可用
        if(!NetWorkUtils.isNetworkConnected(this)){
            ToastUtils.show(this,"网络不可用");
        }
    }

    /**
     * 进入主页
     */
    private void startMain() {
        //判断APP是不是第一次启动
        boolean is_first = SpUtils.getInstance().getBoolean(Constant.SP_IS_FIRST_APP, true);

        if (is_first) {
            //跳转引导页
            SpUtils.getInstance().putBoolean(Constant.SP_IS_FIRST_APP, false);
            JumpUtils.goNext(this, GuideActivity.class, true);
        } else {
            //判断是否登录过（是否记住密码）
            String sp_token = SpUtils.getInstance().getString(Constant.SP_TOKEN, "");
            LogUtils.e("token:"+sp_token);
            if (TextUtils.isEmpty(sp_token)) {
                //判断bmob是否登录
                if(BmobManager.getInstance().islogin()){
                    JumpUtils.goNext(this, MainActivity.class, true);
                }else{
                    //没有登陆过 跳转登录页
                    JumpUtils.goNext(this, LoginActivity.class, true);
                }

            } else {
                //跳转主页
                JumpUtils.goNext(this, MainActivity.class, true);
            }
        }
    }

    private void initView() {
        mTvSkip = (TextView) findViewById(R.id.tv_skip);
        mTvSkip.setOnClickListener(this);

        myHandler.sendEmptyMessageDelayed(TYPE_MAIN, 2 * 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_skip:
                myHandler.removeMessages(TYPE_MAIN);
                startMain();
                break;
        }
    }
}
