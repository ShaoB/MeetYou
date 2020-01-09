package com.example.android.framework.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Founder: shaobin
 * Create Date: 2020/1/9
 * Profile:
 */
public class AnimUtils {

    private static ObjectAnimator mAnim;

    /**
     * 旋转动画
     * @param view
     * @return
     */
    public static ObjectAnimator rotation(View view,int duration) {
        mAnim = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        mAnim.setDuration(duration);
        mAnim.setRepeatMode(ValueAnimator.RESTART);
        mAnim.setRepeatCount(ValueAnimator.INFINITE);
        mAnim.setInterpolator(new LinearInterpolator());
        return mAnim;
    }
}
