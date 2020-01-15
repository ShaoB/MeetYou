package com.example.android.meetyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.framework.base.BaseBackActivity;
import com.example.android.framework.bmob.BmobManager;
import com.example.android.framework.helper.FileHelper;
import com.example.android.framework.manager.DialogManager;
import com.example.android.framework.utils.LogUtils;
import com.example.android.framework.view.DialogView;
import com.example.android.framework.view.LodingView;
import com.example.android.meetyou.R;

import java.io.File;

import androidx.annotation.Nullable;
import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

public class FirstUploadActivity extends BaseBackActivity implements View.OnClickListener {

    /**
     * 头像
     */
    private CircleImageView mIvPhoto;
    /**
     * 昵称
     */
    private EditText mEtNickname;
    /**
     * 完成
     */
    private Button mBtnUpload;

    private DialogView mPhotoView;
    /**
     * 拍一张
     */
    private TextView mTvCamera;
    /**
     * 从相册选择
     */
    private TextView mTvAblum;
    /**
     * 返回
     */
    private TextView mTvCancel;
    /**
     * 头像
     */
    private File uploadFile = null;
    private LodingView mlodingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_upload);
        initView();
        initPhotoView();
    }

    private void initView() {
        mIvPhoto = (CircleImageView) findViewById(R.id.iv_photo);
        mIvPhoto.setOnClickListener(this);
        mEtNickname = (EditText) findViewById(R.id.et_nickname);
        mBtnUpload = (Button) findViewById(R.id.btn_upload);
        mBtnUpload.setOnClickListener(this);

        mBtnUpload.setEnabled(false);

        mlodingView = new LodingView(this);
        mlodingView.setLodingText("正在上传头像。。。");
        mlodingView.setCancelable(false);
    }

    /**
     * 初始化选择框
     */
    private void initPhotoView() {
        mPhotoView = DialogManager.getInstance().initView(this, R.layout.dialog_select_photo, Gravity.BOTTOM);
        mTvCamera = (TextView) mPhotoView.findViewById(R.id.tv_camera);
        mTvCamera.setOnClickListener(this);
        mTvAblum = (TextView) mPhotoView.findViewById(R.id.tv_ablum);
        mTvAblum.setOnClickListener(this);
        mTvCancel = (TextView) mPhotoView.findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(this);

        mEtNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    mBtnUpload.setEnabled(uploadFile != null);
                }else{
                    mBtnUpload.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_photo:
                DialogManager.getInstance().show(mPhotoView);
                break;
            case R.id.btn_upload:
                uploadPhoto();
                break;
            case R.id.tv_camera:
                DialogManager.getInstance().hide(mPhotoView);
                //跳转到相机
                FileHelper.getInstance().toCamera(this);
                break;
            case R.id.tv_ablum:
                DialogManager.getInstance().hide(mPhotoView);
                //跳转到相册
                FileHelper.getInstance().toAlbum(this);
                break;
            case R.id.tv_cancel:
                DialogManager.getInstance().hide(mPhotoView);
                break;
        }
    }

    /**
     * 上传头像，昵称
     */
    private void uploadPhoto() {
        mlodingView.show();
        String nickName = mEtNickname.getText().toString().trim();
        BmobManager.getInstance().uploadFirstPhoto(nickName, uploadFile, new BmobManager.OnUploadPhotoListener() {
            @Override
            public void OnUploadDone() {
                mlodingView.hide();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void OnUploadFail(BmobException e) {
                mlodingView.hide();
                LogUtils.e(e.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            LogUtils.e(requestCode+"");
            if(requestCode == FileHelper.CAMEAR_REQUEST_CODE){
                uploadFile = FileHelper.getInstance().getTempFile();
            }else if(requestCode == FileHelper.ALBUM_REQUEST_CODE){
                Uri uri = data.getData();
                if(uri != null){
                    String path = FileHelper.getInstance().getRealPathFromURI(this, uri);
                    if(!TextUtils.isEmpty(path)){
                        uploadFile = new File(path);
                    }
                }
            }
            if(uploadFile != null){
                Bitmap headPortrait = BitmapFactory.decodeFile(uploadFile.getPath());
                mIvPhoto.setImageBitmap(headPortrait);

                //判断按钮是否可以点击
                mBtnUpload.setEnabled(!TextUtils.isEmpty(mEtNickname.getText().toString().trim()));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
