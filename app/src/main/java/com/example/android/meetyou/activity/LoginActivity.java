package com.example.android.meetyou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.framework.base.BaseActivity;
import com.example.android.framework.bmob.BmobManager;
import com.example.android.framework.bmob.User;
import com.example.android.framework.manager.DialogManager;
import com.example.android.framework.utils.JumpUtils;
import com.example.android.framework.utils.ToastUtils;
import com.example.android.framework.view.DialogView;
import com.example.android.framework.view.TouchPictureV;
import com.example.android.meetyou.MainActivity;
import com.example.android.meetyou.R;

import androidx.annotation.NonNull;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 电话号码
     */
    private EditText mEtPhone;
    /**
     * 验证码
     */
    private EditText mEtCode;
    /**
     * 发送
     */
    private Button mBtnSendCode;
    /**
     * 登录
     */
    private Button mBtnLogin;
    /**
     * 测试登录
     */
    private TextView mTvTestLogin;
    /**
     * <u>用户协议</u>
     */
    private TextView mTvUserAgreement;
    private final int SEND_LOGIN_MESSAGE = 1001;
    private int code = 10;

    private Handler myhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case SEND_LOGIN_MESSAGE:
                    code -- ;
                    mBtnSendCode.setText(code+"s");
                    if(code > 0){
                        myhandler.sendEmptyMessageDelayed(SEND_LOGIN_MESSAGE,1000);
                    }else{
                        mBtnSendCode.setBackground(getResources().getDrawable(R.drawable.login_btn_bg));
                        mBtnSendCode.setEnabled(true);
                        mBtnSendCode.setText(getString(R.string.text_login_send_again));
                    }
                    break;
            }
            return false;
        }
    });
    private DialogView mDialog;
    private TouchPictureV mtouchPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initDialog();
    }

    private void initDialog() {
        mDialog = DialogManager.getInstance().initView(this, R.layout.dialog_login);
        mtouchPic = mDialog.findViewById(R.id.touchPic);
        mtouchPic.setViewResultListener(new TouchPictureV.OnViewResultListener() {
            @Override
            public void onResult() {
                mDialog.hide();
                sendSMS();
            }
        });
    }

    private void initView() {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtCode = (EditText) findViewById(R.id.et_code);
        mBtnSendCode = (Button) findViewById(R.id.btn_send_code);
        mBtnSendCode.setOnClickListener(this);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mTvTestLogin = (TextView) findViewById(R.id.tv_test_login);
        mTvTestLogin.setOnClickListener(this);
        mTvUserAgreement = (TextView) findViewById(R.id.tv_user_agreement);
        mTvUserAgreement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_send_code:
                mDialog.show();
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_test_login:
                break;
            case R.id.tv_user_agreement:
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        String phone = mEtPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.show(this, R.string.toast_login_phone_null);
            return;
        }
        String code = mEtCode.getText().toString().trim();
        if(TextUtils.isEmpty(code)){
            ToastUtils.show(this, R.string.toast_login_code_null);
            return;
        }

        BmobManager.getInstance().signOrLoginByMobilePhone(phone, code, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e == null){
                    ToastUtils.show(LoginActivity.this, R.string.toast_success);
                    JumpUtils.goNext(LoginActivity.this, MainActivity.class,true);
                }else{
                    ToastUtils.show(LoginActivity.this, R.string.toast_error);
                }
            }
        });
    }

    /**
     * 发送验证码
     */
    private void sendSMS() {
        String phone = mEtPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.show(this, R.string.toast_login_phone_null);
            return;
        }
        BmobManager.getInstance().requestSMS(phone, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e == null){
                    mBtnSendCode.setEnabled(false);
                    mBtnSendCode.setBackground(getResources().getDrawable(R.drawable.login_btn_bg_no));
                    myhandler.sendEmptyMessage(SEND_LOGIN_MESSAGE);
                    ToastUtils.show(LoginActivity.this, R.string.toast_success);
                }else {
                    ToastUtils.show(LoginActivity.this, R.string.toast_error);
                }
            }
        });
    }
}
