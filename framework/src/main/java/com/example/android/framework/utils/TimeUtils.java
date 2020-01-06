package com.example.android.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    /**
     * 转换时间
     *  将时间戳转换为时间
     * @param ms
     */
    public static String formatDuring(long ms) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(ms);

    }
}
