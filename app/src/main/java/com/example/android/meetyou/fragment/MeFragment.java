package com.example.android.meetyou.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.framework.base.BaseFragment;
import com.example.android.framework.bmob.BmobManager;
import com.example.android.framework.bmob.User;
import com.example.android.framework.entity.Constant;
import com.example.android.framework.helper.GlideHelper;
import com.example.android.framework.utils.SpUtils;
import com.example.android.meetyou.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Founder: shaobin
 * Create Date: 2020/1/14
 * Profile: 我的
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    private View mview;

    private CircleImageView mIvMePhoto;
    private TextView mTvNickName;
    private TextView mTvServerStatus;
    private LinearLayout mllMeInfo;
    private LinearLayout mllNewFriend;
    private LinearLayout mllPrivateSet;
    private LinearLayout mllShare;
    private LinearLayout mllNotice;
    private LinearLayout mllSetting;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_me, null);
        initview();
        return mview;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                break;
            case R.id.iv_me_photo:

                break;
            case R.id.ll_me_info:

                break;
            case R.id.ll_new_friend:

                break;
            case R.id.ll_private_set:

                break;
            case R.id.ll_share:

                break;
            case R.id.ll_notice:

                break;
            case R.id.ll_setting:
                BmobUser.logOut();
                SpUtils.getInstance().deleteKey(Constant.SP_IS_FIRST_APP);
                SpUtils.getInstance().deleteKey(Constant.SP_TOKEN);
                break;
        }
    }
    private void initview() {
        mIvMePhoto = mview.findViewById(R.id.iv_me_photo);
        mIvMePhoto.setOnClickListener(this);
        mTvNickName = mview.findViewById(R.id.tv_nickname);
        mTvServerStatus  = mview.findViewById(R.id.tv_server_status);
        mllMeInfo  = mview.findViewById(R.id.ll_me_info);
        mllMeInfo.setOnClickListener(this);
        mllNewFriend  = mview.findViewById(R.id.ll_new_friend);
        mllNewFriend.setOnClickListener(this);
        mllPrivateSet  = mview.findViewById(R.id.ll_private_set);
        mllPrivateSet.setOnClickListener(this);
        mllShare  = mview.findViewById(R.id.ll_share);
        mllShare.setOnClickListener(this);
        mllNotice = mview.findViewById(R.id.ll_notice);
        mllNotice.setOnClickListener(this);
        mllSetting = mview.findViewById(R.id.ll_setting);
        mllSetting.setOnClickListener(this);

        loadMeInfo();
    }

    /**
     * 加载个人信息
     */
    private void loadMeInfo() {
        User user = BmobManager.getInstance().getUser();
        GlideHelper.loadUrl(getActivity(),user.getPhoto(),mIvMePhoto);
        mTvNickName.setText(user.getNickName());
    }

}
