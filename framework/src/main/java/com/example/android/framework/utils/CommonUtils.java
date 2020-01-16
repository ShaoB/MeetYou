package com.example.android.framework.utils;

import java.util.List;

/**
 * Founder: shaobin
 * Create Date: 2020/1/15
 * Profile: 通用方法
 */
public class CommonUtils {

    /**
     * 检查list是否可用
     *
     * @param list
     */
    public static boolean isEmpty(List list) {
        return list != null && list.size() > 0;
    }
}
