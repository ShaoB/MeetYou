package com.example.android.meetyou.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.framework.base.BaseFragment;
import com.example.android.framework.utils.ToastUtils;
import com.example.android.meetyou.R;
import com.example.android.meetyou.adapter.CloudTagAdapter;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Founder: shaobin
 * Create Date: 2020/1/14
 * Profile: 星球
 */
public class StarFragment extends BaseFragment implements View.OnClickListener{


    private TextView mTvStartTitle;
    private TextView mTvConnectStatus;
    private ImageView mIvCamera1;
    private ImageView mIvAdd;
    private TagCloudView mCloudView;
    private LinearLayout mllRandom;
    private LinearLayout mllSoul;
    private LinearLayout mllFate;
    private LinearLayout mllLove;

    private CloudTagAdapter mCloudTagAdapter;
    private List<String> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.fragment_star, null);
        initView(mview);
        return mview;
    }

    private void initView(View view) {
        mTvStartTitle = view.findViewById(R.id.tv_star_title);
        mTvConnectStatus = view.findViewById(R.id.tv_connect_status);
        mIvCamera1 = view.findViewById(R.id.iv_camera);
        mIvAdd = view.findViewById(R.id.iv_add);
        mCloudView = view.findViewById(R.id.mCloudView);
        mllRandom = view.findViewById(R.id.ll_random);
        mllSoul = view.findViewById(R.id.ll_soul);
        mllFate = view.findViewById(R.id.ll_fate);
        mllLove = view.findViewById(R.id.ll_love);

        mIvCamera1.setOnClickListener(this);
        mIvAdd.setOnClickListener(this);

        mllRandom.setOnClickListener(this);
        mllSoul.setOnClickListener(this);
        mllFate.setOnClickListener(this);
        mllLove.setOnClickListener(this);

        for (int i = 0; i < 100; i++) {
            mList.add("name"+i);
        }
        mCloudTagAdapter = new CloudTagAdapter(getActivity(),mList);
        mCloudView.setAdapter(mCloudTagAdapter);
        mCloudView.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                ToastUtils.show(getActivity(),"position:"+position);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_camera://相机

                break;
            case R.id.iv_add://添加好友

                break;
            case R.id.ll_random://随机

                break;
            case R.id.ll_soul://灵魂

                break;
            case R.id.ll_fate://缘分

                break;
            case R.id.ll_love://恋爱

                break;
        }
    }
}
