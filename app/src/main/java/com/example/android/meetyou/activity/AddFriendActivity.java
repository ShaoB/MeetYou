package com.example.android.meetyou.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.android.framework.base.BaseUIActivity;
import com.example.android.framework.base.CommonAdapter;
import com.example.android.framework.base.CommonViewHolder;
import com.example.android.framework.bmob.BmobManager;
import com.example.android.framework.bmob.User;
import com.example.android.framework.utils.CommonUtils;
import com.example.android.framework.utils.JumpUtils;
import com.example.android.framework.utils.ToastUtils;
import com.example.android.meetyou.Bean.AddFriendModel;
import com.example.android.meetyou.R;
import com.example.android.meetyou.adapter.AddFriendAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AddFriendActivity extends BaseUIActivity implements View.OnClickListener {

    //标题
    public static final int TYPE_TITLE = 0;
    //内容
    public static final int TYPE_CONTENT = 1;

    private LinearLayout mLlToContact;
    /**
     * 电话号码
     */
    private EditText mEtPhone;
    private ImageView mIvSearch;
    private RecyclerView mMSearchResultView;
    private View include_empty_view;

    private CommonAdapter<AddFriendModel> mAddFriendAdapter;
    private List<AddFriendModel> mlist = new ArrayList<>();
    private String mobilePhoneNumber;

    private final int ADD_BOOK_REQUEST_CODE = 1009;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        initView();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.ll_to_contact:
                //处理权限
                if(checkPermissions(Manifest.permission.READ_CONTACTS)){
                    JumpUtils.goNext(this,ContactFriendActivity.class);
                }else{
                    requestPermission(new String[]{Manifest.permission.READ_CONTACTS},ADD_BOOK_REQUEST_CODE);
                }
                break;
            case R.id.iv_search:
                queryPhoneUser();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == ADD_BOOK_REQUEST_CODE){
                //拒绝
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    ToastUtils.show(AddFriendActivity.this,"用户拒绝");
                }else{
                    JumpUtils.goNext(this,ContactFriendActivity.class);
                }


        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

        mAddFriendAdapter = new CommonAdapter<AddFriendModel>(mlist, new CommonAdapter.OnBindMoreDataListener<AddFriendModel>() {
            @Override
            public int getItemType(int position) {
                return mlist.get(position).getType();
            }

            @Override
            public void onBindViewHolder(AddFriendModel model, CommonViewHolder viewHolder, int type, int position) {
                if(type == TYPE_TITLE){
                    viewHolder.setText(R.id.tv_title,model.getTitle());
                }else if(type == TYPE_CONTENT){
                    viewHolder.setImageUrl(AddFriendActivity.this,R.id.iv_photo,model.getPhoto());
                    viewHolder.setImageResource(R.id.iv_sex,model.isSex() ? R.mipmap.img_boy_icon : R.mipmap.img_girl_icon);
                    viewHolder.setText(R.id.tv_nickname,model.getNickName());
                    viewHolder.setText(R.id.tv_age,model.getAge()+"岁");
                    viewHolder.setText(R.id.tv_desc,model.getDesc());
                }
            }

            @Override
            public int getLayoutId(int type) {
                if(type == TYPE_TITLE){
                    return R.layout.layout_search_title_item;
                }else if(type == TYPE_CONTENT){
                    return R.layout.layout_search_user_item;
                }
                return 0;
            }
        });

        mMSearchResultView.setAdapter(mAddFriendAdapter);
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
                }else{
                    ToastUtils.show(AddFriendActivity.this,"bmob报错：" + e.toString());
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
