package com.example.android.meetyou.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.example.android.framework.base.CommonAdapter;
import com.example.android.framework.base.CommonViewHolder;
import com.example.android.framework.bmob.BmobManager;
import com.example.android.framework.bmob.User;
import com.example.android.framework.utils.CommonUtils;
import com.example.android.framework.utils.LogUtils;
import com.example.android.meetyou.Bean.AddFriendModel;
import com.example.android.meetyou.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 从通讯录导入
 */
public class ContactFriendActivity extends AppCompatActivity {

    private RecyclerView mRvContact;
    private Map<String, String> mContactMap = new HashMap<>();

    private CommonAdapter<AddFriendModel> mAddFriendAdapter;
    private List<AddFriendModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_friend);
        initView();
    }

    private void initView() {
        mRvContact = (RecyclerView) findViewById(R.id.rv_contact);
        mRvContact.setLayoutManager(new LinearLayoutManager(this));
        mRvContact.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mAddFriendAdapter = new CommonAdapter<AddFriendModel>(mList, new CommonAdapter.OnBindDataListener<AddFriendModel>() {
            @Override
            public void onBindViewHolder(AddFriendModel model, CommonViewHolder viewHolder, int type, int position) {
                viewHolder.setImageUrl(ContactFriendActivity.this,R.id.iv_photo,model.getPhoto());
                viewHolder.setImageResource(R.id.iv_sex,model.isSex() ? R.mipmap.img_boy_icon : R.mipmap.img_girl_icon);
                viewHolder.setText(R.id.tv_nickname,model.getNickName());
                viewHolder.setText(R.id.tv_age,model.getAge()+"岁");
                viewHolder.setText(R.id.tv_desc,model.getDesc());
                viewHolder.setVisibility(R.id.ll_contact_info,View.VISIBLE);
                viewHolder.setText(R.id.tv_contact_name,model.getContactName());
                viewHolder.setText(R.id.tv_contact_phone,model.getContactPhone());
            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_search_user_item;
            }
        });

        mRvContact.setAdapter(mAddFriendAdapter);

        loadContact();
        loadUser();
    }

    /**
     * 加载用户
     */
    private void loadUser() {
        LogUtils.e(mContactMap.entrySet().toString());
        if (mContactMap.size() > 0) {
            for (final Map.Entry<String, String> entry :
                    mContactMap.entrySet()) {
                BmobManager.getInstance().queryPhoneUser(entry.getValue(), new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            if (CommonUtils.isEmpty(list)) {
                                User user = list.get(0);
                                addContent(user,entry.getKey(),entry.getValue());
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * 加载通讯录
     */
    private void loadContact() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , null, null, null, null);
        String name;
        String phone;
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phone = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone = phone.replace(" ", "").replace("-", "");
            mContactMap.put(name, phone);
        }
    }

    /**
     * 添加内容
     * @param user
     * @param name
     * @param phone
     */
    private void addContent(User user, String name, String phone) {
        AddFriendModel addFriendModel = new AddFriendModel();
        addFriendModel.setType(AddFriendActivity.TYPE_CONTENT);
        addFriendModel.setUserId(user.getObjectId());
        addFriendModel.setPhoto(user.getPhoto());
        addFriendModel.setSex(user.isSex());
        addFriendModel.setAge(user.getAge());
        addFriendModel.setNickName(user.getNickName());
        addFriendModel.setDesc(user.getDesc());

        addFriendModel.setContact(true);
        addFriendModel.setContactName(name);
        addFriendModel.setContactPhone(phone);
        mList.add(addFriendModel);

        mAddFriendAdapter.notifyDataSetChanged();
    }
}
