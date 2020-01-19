package com.example.android.framework.log;

import android.util.Log;

import com.example.android.framework.utils.LogUtils;

/**
 * Founder: shaobin
 * Create Date: 2020/1/19
 * Profile:
 */
public class MrBaseLog {
    public static void printDefault(int type, String tag, String msg) {

        int index = 0;
        int maxLength = 4000;
        int countOfSub = msg.length() / maxLength;

        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                printSub(type, tag, sub);
                index += maxLength;
            }
            printSub(type, tag, msg.substring(index, msg.length()));
        } else {
            printSub(type, tag, msg);
        }
    }

    private static void printSub(int type, String tag, String sub) {
        tag = LogUtils.PREFIX + tag;

        switch (type) {
            case LogUtils.V:
                Log.v(tag, sub);
                break;
            case LogUtils.D:
                Log.d(tag, sub);
                break;
            case LogUtils.I:
                Log.i(tag, sub);
                break;
            case LogUtils.W:
                Log.w(tag, sub);
                break;
            case LogUtils.E:
                Log.e(tag, sub);
                break;
            case LogUtils.A:
                Log.wtf(tag, sub);
                break;
        }
    }
}
