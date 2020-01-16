package com.example.android.meetyou.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.example.android.framework.bmob.BmobManager;
import com.example.android.framework.bmob.User;
import com.example.android.framework.utils.CommonUtils;
import com.example.android.framework.utils.LogUtils;
import com.example.android.framework.utils.ToastUtils;
import com.example.android.meetyou.Bean.AddFriendModel;
import com.example.android.meetyou.R;
import com.example.android.meetyou.adapter.AddFriendAdapter;

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

    private AddFriendAdapter mAddFriendAdapter;
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
        mAddFriendAdapter = new AddFriendAdapter(this,mList);
        mRvContact.setAdapter(mAddFriendAdapter);
        mAddFriendAdapter.setOnClickListener(new AddFriendAdapter.OnClickListener() {
            @Override
            public void setOnclickListener(int postion) {
                ToastUtils.show(ContactFriendActivity.this, "" + postion);
            }
        });

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
        addFriendModel.setType(AddFriendAdapter.TYPE_CONTENT);
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
