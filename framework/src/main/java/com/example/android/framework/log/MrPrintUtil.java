package com.example.android.framework.log;

import android.text.TextUtils;
import android.util.Log;

/**
 * Founder: shaobin
 * Create Date: 2020/1/19
 * Profile:
 */
public class MrPrintUtil {
    public static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }

    public static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.e(tag, "╔═══════════════════════════════════════════════════════");
        } else {
            Log.e(tag, "╚═══════════════════════════════════════════════════════");
        }
    }
}
