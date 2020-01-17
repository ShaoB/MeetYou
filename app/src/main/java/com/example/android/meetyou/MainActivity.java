package com.example.android.meetyou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.framework.base.BaseUIActivity;
import com.example.android.framework.bmob.BmobManager;
import com.example.android.framework.entity.Constant;
import com.example.android.framework.manager.DialogManager;
import com.example.android.framework.utils.JumpUtils;
import com.example.android.framework.utils.LogUtils;
import com.example.android.framework.utils.SpUtils;
import com.example.android.framework.utils.ToastUtils;
import com.example.android.framework.view.DialogView;
import com.example.android.meetyou.Bean.TokenModel;
import com.example.android.meetyou.activity.FirstUploadActivity;
import com.example.android.meetyou.fragment.ChatFragment;
import com.example.android.meetyou.fragment.MeFragment;
import com.example.android.meetyou.fragment.SquareFragment;
import com.example.android.meetyou.fragment.StarFragment;
import com.example.android.meetyou.networkUtils.ApiService;
import com.example.android.meetyou.networkUtils.RetrofitUtil;
import com.example.android.meetyou.networkUtils.RetryFunction;
import com.example.android.meetyou.cloud.CloudService;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseUIActivity implements View.OnClickListener {

    private FrameLayout mMMainLayout;

    /**
     * 星球
     */
    private TextView mTvStar;
    private ImageView mIvStar;
    private LinearLayout mLlStar;
    private StarFragment mStarFragment = null;
    private FragmentTransaction mStarTransaction = null;
    /**
     * 广场
     */
    private TextView mTvSquare;
    private ImageView mIvSquare;
    private LinearLayout mLlSquare;
    private SquareFragment mSquareFragment = null;
    private FragmentTransaction mSquareTransaction = null;
    /**
     * 聊天
     */
    private TextView mTvChat;
    private ImageView mIvChat;
    private LinearLayout mLlChat;
    private ChatFragment mChatFragment = null;
    private FragmentTransaction mChatTransaction = null;

    /**
     * 我的
     */
    private TextView mTvMe;
    private ImageView mIvMe;
    private LinearLayout mLlMe;
    private MeFragment mMeFragment = null;
    private FragmentTransaction mMeTransaction = null;

    private static final int PERMISSION_CODE = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        request(PERMISSION_CODE, new OnPermissionsRequest() {
            @Override
            public void onSuccess() {
                ToastUtils.show(MainActivity.this, R.string.toast_success);
            }

            @Override
            public void onFail(List<String> mNolist) {
                ToastUtils.show(MainActivity.this, mNolist.toString());
            }
        });

        mMMainLayout = (FrameLayout) findViewById(R.id.mMainLayout);
        mIvStar = (ImageView) findViewById(R.id.iv_star);
        mTvStar = (TextView) findViewById(R.id.tv_star);
        mLlStar = (LinearLayout) findViewById(R.id.ll_star);
        mLlStar.setOnClickListener(this);
        mIvSquare = (ImageView) findViewById(R.id.iv_square);
        mTvSquare = (TextView) findViewById(R.id.tv_square);
        mLlSquare = (LinearLayout) findViewById(R.id.ll_square);
        mLlSquare.setOnClickListener(this);
        mIvChat = (ImageView) findViewById(R.id.iv_chat);
        mTvChat = (TextView) findViewById(R.id.tv_chat);
        mLlChat = (LinearLayout) findViewById(R.id.ll_chat);
        mLlChat.setOnClickListener(this);
        mIvMe = (ImageView) findViewById(R.id.iv_me);
        mTvMe = (TextView) findViewById(R.id.tv_me);
        mLlMe = (LinearLayout) findViewById(R.id.ll_me);
        mLlMe.setOnClickListener(this);


        initFragment();
    }

    private void initFragment() {
        //星球
        if (mStarFragment == null) {
            mStarFragment = new StarFragment();
            mStarTransaction = getSupportFragmentManager().beginTransaction();
            mStarTransaction.add(R.id.mMainLayout, mStarFragment, "star");
            mStarTransaction.commit();
        }
        //广场
        if (mSquareFragment == null) {
            mSquareFragment = new SquareFragment();
            mSquareTransaction = getSupportFragmentManager().beginTransaction();
            mSquareTransaction.add(R.id.mMainLayout, mSquareFragment, "square");
            mSquareTransaction.commit();
        }
        //聊天
        if (mChatFragment == null) {
            mChatFragment = new ChatFragment();
            mChatTransaction = getSupportFragmentManager().beginTransaction();
            mChatTransaction.add(R.id.mMainLayout, mChatFragment, "chat");
            mChatTransaction.commit();
        }
        //我的
        if (mMeFragment == null) {
            mMeFragment = new MeFragment();
            mMeTransaction = getSupportFragmentManager().beginTransaction();
            mMeTransaction.add(R.id.mMainLayout, mMeFragment, "me");
            mMeTransaction.commit();
        }

        //默认选项卡
        checkMainTap(0);

        checkToken();
    }

    /**
     * 检查token
     */
    private void checkToken() {
        //获取Token需要三个参数 1.用户ID 2.头像地址 3.昵称
        String sp_token = SpUtils.getInstance().getString(Constant.SP_TOKEN, "");
        if (!TextUtils.isEmpty(sp_token)) {
             //启动云服务去连接融云服务
            startService(new Intent(this, CloudService.class));
        }else{
            //有3个参数
            String tokenPhoto = BmobManager.getInstance().getUser().getTokenPhoto();
            String tokenNickName = BmobManager.getInstance().getUser().getTokenNickName();
            if(!TextUtils.isEmpty(tokenPhoto) && !TextUtils.isEmpty(tokenNickName)){
                //创建Token
                createToken();
            }else{
                createUploadDialog();
            }
        }
    }

    /**
     * 创建上传提示框
     */
    private void createUploadDialog() {
        final DialogView mUploadDialogView = DialogManager.getInstance().initView(this, R.layout.dialog_first_upload);
        mUploadDialogView.setCancelable(false);
        ImageView mIvGoUpload = mUploadDialogView.findViewById(R.id.iv_go_upload);
        mIvGoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().hide(mUploadDialogView);
                JumpUtils.invokeActivity(MainActivity.this, FirstUploadActivity.class);
            }
        });
        DialogManager.getInstance().show(mUploadDialogView);
    }

    /**
     * 创建Token
     */
    private void createToken() {
        LogUtils.e("createToken");
        if( BmobManager.getInstance().getUser() == null){
            ToastUtils.show(this, "登录异常");
            return;
        }
        /**
         * 1.去融云后台获取Token
         * 2.连接融云
         */
        final HashMap<String, String> map = new HashMap<>();
        map.put("userId", BmobManager.getInstance().getUser().getObjectId());
        map.put("name", BmobManager.getInstance().getUser().getTokenNickName());
        map.put("portraitUri", BmobManager.getInstance().getUser().getTokenPhoto());

        ApiService apiService = RetrofitUtil.getInstance().create(ApiService.class);
        Observable<TokenModel> observable = apiService.getCloudToken(map);

        observable.retryWhen(new RetryFunction(3,3))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TokenModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TokenModel tokenModel) {
                        if(tokenModel.getCode() == 200){
                            //存入 sp
                            SpUtils.getInstance().putString(Constant.SP_TOKEN,tokenModel.getToken());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 显示fragment
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            transaction.show(fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 隐藏所有fragment
     *
     * @param transaction
     */
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mStarFragment != null) {
            transaction.hide(mStarFragment);
        }
        if (mSquareFragment != null) {
            transaction.hide(mSquareFragment);
        }
        if (mChatFragment != null) {
            transaction.hide(mChatFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }

    /**
     * 切换主题选项卡
     *
     * @param index 0：星球
     *              1：广场
     *              2：聊天
     *              3：我的
     */
    private void checkMainTap(int index) {
        switch (index) {
            case 0:
                showFragment(mStarFragment);
                mIvStar.setImageResource(R.mipmap.img_star_p);
                mIvSquare.setImageResource(R.mipmap.img_square);
                mIvChat.setImageResource(R.mipmap.img_chat);
                mIvMe.setImageResource(R.mipmap.img_me);
                break;
            case 1:
                showFragment(mSquareFragment);
                mIvStar.setImageResource(R.mipmap.img_star);
                mIvSquare.setImageResource(R.mipmap.img_square_p);
                mIvChat.setImageResource(R.mipmap.img_chat);
                mIvMe.setImageResource(R.mipmap.img_me);
                break;
            case 2:
                showFragment(mChatFragment);
                mIvStar.setImageResource(R.mipmap.img_star);
                mIvSquare.setImageResource(R.mipmap.img_square);
                mIvChat.setImageResource(R.mipmap.img_chat_p);
                mIvMe.setImageResource(R.mipmap.img_me);
                break;
            case 3:
                showFragment(mMeFragment);
                mIvStar.setImageResource(R.mipmap.img_star);
                mIvSquare.setImageResource(R.mipmap.img_square);
                mIvChat.setImageResource(R.mipmap.img_chat);
                mIvMe.setImageResource(R.mipmap.img_me_p);
                break;
        }
    }

    /**
     * 防止重叠
     * 当应用的内存紧张的时候，系统会回收掉Fragment对象
     * 再一次进入的时候会重新创建Fragment
     * 非原来对象，我们无法控制，导致重叠
     *
     * @param fragment
     */
    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (mStarFragment == null && fragment instanceof StarFragment) {
            mStarFragment = (StarFragment) fragment;
        }
        if (mSquareFragment == null && fragment instanceof SquareFragment) {
            mSquareFragment = (SquareFragment) fragment;
        }
        if (mChatFragment == null && fragment instanceof ChatFragment) {
            mChatFragment = (ChatFragment) fragment;
        }
        if (mMeFragment == null && fragment instanceof MeFragment) {
            mMeFragment = (MeFragment) fragment;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.ll_star:
                checkMainTap(0);
                break;
            case R.id.ll_square:
                checkMainTap(1);
                break;
            case R.id.ll_chat:
                checkMainTap(2);
                break;
            case R.id.ll_me:
                checkMainTap(3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == JumpUtils.REQ_FOR_FORWARD){
                checkToken();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

