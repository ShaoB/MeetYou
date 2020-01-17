package com.example.android.meetyou.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.framework.base.BaseFragment;
import com.example.android.framework.bmob.BmobManager;
import com.example.android.framework.bmob.User;
import com.example.android.framework.utils.CommonUtils;
import com.example.android.framework.utils.JumpUtils;
import com.example.android.framework.utils.ToastUtils;
import com.example.android.meetyou.Bean.AddFriendModel;
import com.example.android.meetyou.R;
import com.example.android.meetyou.activity.AddFriendActivity;
import com.example.android.meetyou.activity.UserInfoActivity;
import com.example.android.meetyou.adapter.AddFriendAdapter;
import com.example.android.meetyou.adapter.CloudTagAdapter;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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
    private List<AddFriendModel> mList = new ArrayList<>();

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

       //添加数据
        BmobManager.getInstance().queryAllUser(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e == null){
                    if(CommonUtils.isEmpty(list)){
                        mList.clear();
                        int num = (list.size() <= 100) ? list.size() : 100;
                        for (int i = 0; i < num; i++) {
                            addContent(list.get(i));
                        }
                        mCloudTagAdapter.notifyDataSetChanged();
                    }
                }else{
                    ToastUtils.show(getActivity(),"e:"+e);
                }
            }
        });
        mCloudTagAdapter = new CloudTagAdapter(getActivity(),mList);
        mCloudView.setAdapter(mCloudTagAdapter);
        mCloudView.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                String contactPhone = mList.get(position).getContactPhone();
                JumpUtils.goNext(getActivity(), UserInfoActivity.class,contactPhone,false);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_camera://相机

                break;
            case R.id.iv_add://添加好友
                JumpUtils.goNext(getActivity(), AddFriendActivity.class);
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

    /**
     * 添加内容
     *
     * @param user
     */
    private void addContent(User user) {
        AddFriendModel addFriendModel = new AddFriendModel();
        addFriendModel.setType(AddFriendAdapter.TYPE_CONTENT);
        addFriendModel.setUserId(user.getObjectId());
        addFriendModel.setPhoto(user.getPhoto());
        addFriendModel.setSex(user.isSex());
        addFriendModel.setAge(user.getAge());
        addFriendModel.setNickName(user.getNickName());
        addFriendModel.setDesc(user.getDesc());
        addFriendModel.setContact(true);
        addFriendModel.setContactName(user.getNickName());
        addFriendModel.setContactPhone(user.getMobilePhoneNumber());
        mList.add(addFriendModel);
    }
}
