package com.example.android.meetyou.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.android.framework.base.BaseUIActivity;
import com.example.android.framework.bmob.BmobManager;
import com.example.android.framework.bmob.User;
import com.example.android.framework.utils.CommonUtils;
import com.example.android.framework.utils.ToastUtils;
import com.example.android.meetyou.Bean.AddFriendModel;
import com.example.android.meetyou.R;
import com.example.android.meetyou.adapter.AddFriendAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AddFriendActivity extends BaseUIActivity implements View.OnClickListener {

    private LinearLayout mLlToContact;
    /**
     * 电话号码
     */
    private EditText mEtPhone;
    private ImageView mIvSearch;
    private RecyclerView mMSearchResultView;
    private View include_empty_view;

    private AddFriendAdapter mAddFriendAdapter;
    private List<AddFriendModel> mlist = new ArrayList<>();
    private String mobilePhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        initView();
    }

    private void initView() {
        mLlToContact = (LinearLayout) findViewById(R.id.ll_to_contact);
        mLlToContact.setOnClickListener(this);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mIvSearch = (ImageView) findViewById(R.id.iv_search);
        mIvSearch.setOnClickListener(this);
        mMSearchResultView = (RecyclerView) findViewById(R.id.mSearchResultView);
        include_empty_view = (View) findViewById(R.id.include_empty_view);

        mMSearchResultView.setLayoutManager(new LinearLayoutManager(this));
        mMSearchResultView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAddFriendAdapter = new AddFriendAdapter(this, mlist);
        mMSearchResultView.setAdapter(mAddFriendAdapter);
        mAddFriendAdapter.setOnClickListener(new AddFriendAdapter.OnClickListener() {
            @Override
            public void setOnclickListener(int postion) {
                ToastUtils.show(AddFriendActivity.this, postion + "");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.ll_to_contact:
                break;
            case R.id.iv_search:
                queryPhoneUser();
                break;
        }
    }

    /**
     * 查询用户
     */
    private void queryPhoneUser() {
        //获取电话号码
        String mPhone = mEtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mPhone)) {
            ToastUtils.show(this, "查询用户为空");
            return;
        }
        //过滤自己
        mobilePhoneNumber = BmobManager.getInstance().getUser().getMobilePhoneNumber();
        if(mPhone.equals(mobilePhoneNumber)){
            ToastUtils.show(this,"不能查询自己");
            mEtPhone.setText("");
            return;
        }
        //查询用户
        BmobManager.getInstance().queryPhoneUser(mPhone, new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    mEtPhone.setText("");
                    if (CommonUtils.isEmpty(list)) {
                        User user = list.get(0);
                        include_empty_view.setVisibility(View.GONE);
                        mMSearchResultView.setVisibility(View.VISIBLE);
                        //每次查询有数据的话清空
                        mlist.clear();

                        addTitle("查询结果");
                        addContent(user);
                        mAddFriendAdapter.notifyDataSetChanged();
                        //推荐
                        pushUser();
                    } else {
                        //显示空数据
                        include_empty_view.setVisibility(View.VISIBLE);
                        mMSearchResultView.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /**
     * 推荐好友
     */
    private void pushUser() {
        //查询所有好友 推荐10个
        BmobManager.getInstance().queryAllUser(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (CommonUtils.isEmpty(list)) {
                        addTitle("推荐好友");
                        int num = (list.size() <= 100) ? list.size() : 100;
                        for (int i = 0; i < num; i++) {
                            if(list.get(i).getMobilePhoneNumber().equals(mobilePhoneNumber)){
                                //如果是自己跳过本次循环
                                continue;
                            }
                            addContent(list.get(i));
                        }
                        mAddFriendAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /**
     * 添加头部
     *
     * @param title
     */
    private void addTitle(String title) {
        AddFriendModel addFriendModel = new AddFriendModel();
        addFriendModel.setType(AddFriendAdapter.TYPE_TITLE);
        addFriendModel.setTitle(title);
        mlist.add(addFriendModel);
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
        mlist.add(addFriendModel);
    }
}
