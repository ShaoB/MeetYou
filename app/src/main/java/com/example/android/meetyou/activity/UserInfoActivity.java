package com.example.android.meetyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.framework.base.BaseUIActivity;
import com.example.android.framework.bmob.BmobManager;
import com.example.android.framework.bmob.User;
import com.example.android.framework.helper.GlideHelper;
import com.example.android.framework.utils.CommonUtils;
import com.example.android.framework.utils.ToastUtils;
import com.example.android.meetyou.R;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends BaseUIActivity implements View.OnClickListener {

    private RelativeLayout mLlBack;
    private CircleImageView mIvUserPhoto;
    private TextView mTvNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        String phone = intent.getStringExtra("paras");

        mLlBack = (RelativeLayout) findViewById(R.id.ll_back);
        mLlBack.setOnClickListener(this);
        mIvUserPhoto = (CircleImageView) findViewById(R.id.iv_user_photo);
        mTvNickname = (TextView) findViewById(R.id.tv_nickname);

        BmobManager.getInstance().queryPhoneUser(phone, new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e == null){
                    if(CommonUtils.isEmpty(list)){
                        GlideHelper.loadUrl(UserInfoActivity.this,list.get(0).getPhoto(),mIvUserPhoto);
                        mTvNickname.setText(list.get(0).getNickName());
                    }
                }else{
                    ToastUtils.show(UserInfoActivity.this,"e:"+e);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.ll_back:
                break;
        }
    }
}
