package com.example.android.meetyou.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.framework.base.BaseFragment;
import com.example.android.meetyou.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Founder: shaobin
 * Create Date: 2020/1/14
 * Profile: 我的
 */
public class MeFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.fragment_me,null);
        return mview;
    }
}
