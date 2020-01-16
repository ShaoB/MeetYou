package com.example.android.framework.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Founder: shaobin
 * Create Date: 2020/1/9
 * Profile:
 */
public class BaseActivity extends AppCompatActivity {

    //申请所需要的权限
    protected String[] mPermisssions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS
    };
    //没有申请的权限组
    private List<String> mPerList = new ArrayList<>();
    //失败的权限组
    private List<String> mPerNoList = new ArrayList<>();
    private OnPermissionsRequest onPermissionsRequest;

    private int mRequestCode;


    /**
     * 一个方法请求权限
     * @param requestcode
     * @param onPermissionsRequest
     */
    protected void request(int requestcode,OnPermissionsRequest onPermissionsRequest){
        if(!checkPermissionsAll()){
            requestPermissionAll(requestcode,onPermissionsRequest);
        }
    }
    /**
     * 判断单个权限
     *
     * @param permission 权限
     * @return
     */
    protected boolean checkPermissions(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int check = checkSelfPermission(permission);
            return check == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    /**
     * 查询全部权限 true表示 mPerList里有需要请求的权限组
     * @return
     */
    protected boolean checkPermissionsAll() {
        mPerList.clear();
        for (int i = 0; i < mPermisssions.length; i++) {
            boolean check = checkPermissions(mPermisssions[i]);
            if (!check) {
                mPerList.add(mPermisssions[i]);
            }
        }
        return mPerList.size() == 0;
    }

    /**
     * 请求权限
     *
     * @param permission  权限组
     * @param requestcode 请求码
     */
    protected void requestPermission(String[] permission, int requestcode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, requestcode);
        }
    }

    /**
     * 请求全部权限
     * @param requestcode 请求码
     */
    protected void requestPermissionAll(int requestcode,OnPermissionsRequest onPermissionsRequest){
        this.mRequestCode = requestcode;
        this.onPermissionsRequest = onPermissionsRequest;
        requestPermission(mPerList.toArray(new String[mPerList.size()]),requestcode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPerNoList.clear();
        if(requestCode == mRequestCode){
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++ ){
                    if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                        //如果有失败的权限
                        mPerNoList.add(permissions[i]);
                    }
                }
                if(onPermissionsRequest != null){
                    if(mPerNoList.size() == 0){
                        onPermissionsRequest.onSuccess();
                    }else{
                        onPermissionsRequest.onFail(mPerNoList);
                    }
                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected interface OnPermissionsRequest{
        void onSuccess();
        void onFail(List<String> mNolist);
    }
}
