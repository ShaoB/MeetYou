package com.example.android.meetyou.activity;

import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.framework.base.BaseUIActivity;
import com.example.android.framework.base.BaseViewPageAdapter;
import com.example.android.framework.manager.MediaPlayManager;
import com.example.android.framework.utils.AnimUtils;
import com.example.android.framework.utils.JumpUtils;
import com.example.android.meetyou.R;

import java.util.ArrayList;

import androidx.viewpager.widget.ViewPager;

public class GuideActivity extends BaseUIActivity implements View.OnClickListener {

    private ViewPager mMViewpager;
    private ImageView mIvMusic;
    private TextView mTvSkip;
    private ImageView mIvPoint1;
    private ImageView mIvPoint2;
    private ImageView mIvPoint3;

    private ImageView mIvStart;
    private ImageView mIvNight;
    private ImageView mIvSmlie;


    private View view1,view2,view3;
    private ArrayList<View> mList = new ArrayList<>();
    private BaseViewPageAdapter mPageAdapter;

    private MediaPlayManager mMediaPlay;
    private ObjectAnimator mAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    private void initView() {
        mMViewpager = (ViewPager) findViewById(R.id.mViewpager);
        mIvMusic = (ImageView) findViewById(R.id.iv_music);
        mIvMusic.setOnClickListener(this);
        mTvSkip = (TextView) findViewById(R.id.tv_skip);
        mTvSkip.setOnClickListener(this);
        mIvPoint1 = (ImageView) findViewById(R.id.iv_point_1);
        mIvPoint2 = (ImageView) findViewById(R.id.iv_point_2);
        mIvPoint3 = (ImageView) findViewById(R.id.iv_point_3);

        view1 = View.inflate(this, R.layout.layout_page_guide1,null);
        view2 = View.inflate(this, R.layout.layout_page_guide2,null);
        view3 = View.inflate(this, R.layout.layout_page_guide3,null);
        mList.add(view1);
        mList.add(view2);
        mList.add(view3);

        //预加载
        mMViewpager.setOffscreenPageLimit(mList.size());
        mPageAdapter = new BaseViewPageAdapter(mList);
        mMViewpager.setAdapter(mPageAdapter);

        //帧动画
        mIvStart = view1.findViewById(R.id.iv_start);
        mIvNight = view2.findViewById(R.id.iv_night);
        mIvSmlie = view3.findViewById(R.id.iv_smile);
        //播放
        AnimationDrawable animatStart = (AnimationDrawable) mIvStart.getBackground();
        animatStart.start();
        AnimationDrawable animatNight = (AnimationDrawable) mIvNight.getBackground();
        animatNight.start();
        AnimationDrawable animatSmile = (AnimationDrawable) mIvSmlie.getBackground();
        animatSmile.start();

        mMViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //播放音乐
        startMusic();
    }

    private void startMusic() {
        mMediaPlay = new MediaPlayManager();
        mMediaPlay.startPlay(getResources().openRawResourceFd(R.raw.music));
        mMediaPlay.setLooping(true);

        //旋转动画
        mAnim = AnimUtils.rotation(mIvMusic, 2 * 1000);
        mAnim.start();
    }

    private void selectPoint(int position) {
        switch (position){
            case 0:
                mIvPoint1.setImageResource(R.mipmap.img_guide_point_p);
                mIvPoint2.setImageResource(R.mipmap.img_guide_point);
                mIvPoint3.setImageResource(R.mipmap.img_guide_point);
                break;
            case 1:
                mIvPoint1.setImageResource(R.mipmap.img_guide_point);
                mIvPoint2.setImageResource(R.mipmap.img_guide_point_p);
                mIvPoint3.setImageResource(R.mipmap.img_guide_point);
                break;
            case 2:
                mIvPoint1.setImageResource(R.mipmap.img_guide_point);
                mIvPoint2.setImageResource(R.mipmap.img_guide_point);
                mIvPoint3.setImageResource(R.mipmap.img_guide_point_p);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_music:
                if(mMediaPlay.MEDIA_STATUS == MediaPlayManager.MEDIA_STATUS_PLAY){
                    mAnim.pause();
                    mMediaPlay.pausePlay();
                    mIvMusic.setImageResource(R.mipmap.img_guide_music_off);
                }else if(mMediaPlay.MEDIA_STATUS == MediaPlayManager.MEDIA_STATUS_PAUSE){
                    mAnim.start();
                    mMediaPlay.continuePlay();
                    mIvMusic.setImageResource(R.mipmap.img_guide_music);
                }
                break;
            case R.id.tv_skip:
                JumpUtils.goNext(this,LoginActivity.class,true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlay.stopPlay();
    }
}
