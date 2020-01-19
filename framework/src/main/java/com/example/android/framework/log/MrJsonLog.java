package com.example.android.framework.log;

import android.util.Log;

import com.example.android.framework.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Founder: shaobin
 * Create Date: 2020/1/19
 * Profile: Json Log
 */
public class MrJsonLog {
    public static void printJson(String tag, String msg, String headString) {

        String message;

        tag = LogUtils.PREFIX + tag;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(LogUtils.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(LogUtils.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        MrPrintUtil.printLine(tag, true);
        message = headString + LogUtils.LINE_SEPARATOR + message;
        String[] lines = message.split(LogUtils.LINE_SEPARATOR);
        for (String line : lines) {
            Log.e(tag, "║ " + line);
        }
        MrPrintUtil.printLine(tag, false);
    }
}
