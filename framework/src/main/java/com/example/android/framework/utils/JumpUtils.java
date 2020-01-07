package com.example.android.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Founder: shaobin
 * Create Date: 2020/1/7
 * Profile: 跳转工具类
 */
public class JumpUtils {
    // 通用请求码
    public static final int REQ_FOR_FORWARD = 0;


    /**
     * Activity跳转，无参数,父Activity不销毁,动画
     *
     * @param to
     *            目的Activity
     * @param enterAnim
     *            进入动画
     * @param exitAnim
     *            退出动画
     */
    public static void goNext(Context context, Class<?> to, int enterAnim, int exitAnim) {
        goNext(context, to, null, null, false);
        ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * Activity跳转，无参数,父Activity 不销毁
     *
     * @param to
     *            目的Activity
     */
    public static void goNext(Context context, Class<?> to) {
        goNext(context, to, null, null, false);
    }
    /**
     * Activity跳转，无参数
     *
     * @param to
     *            目的Activity
     * @param finished
     *            父Activity是否销毁，finished=true销毁
     */
    public static void goNext(Context context, Class<?> to, boolean finished) {
        goNext(context, to, null, null, finished);
    }
    /**
     * Activity跳转，未指定参数名，参数名为paras
     *
     * @param to
     *            目的Activity
     * @param paraValues
     *            参数
     * @param finished
     *            父Activity是否销毁，finished=true销毁
     */
    public static void goNext(Context context, Class<?> to, Object paraValues, boolean finished) {
        goNext(context, to, null, paraValues, finished);
    }

    /**
     * 延迟Activity跳转，无参数
     *
     * @param context
     * @param to
     * @param delay
     * @param finished
     *            父Activity是否销毁，finished=true销毁
     */
    public static void goNextDelay(final Context context, final Class<?> to, long delay, final boolean finished) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                goNext(context, to, null, null, finished);
            }
        }, delay);
    }

    /**
     * 延迟Activity跳转，无参数
     *
     * @param context
     * @param to
     * @param paraValues
     *            参数
     * @param delay
     * @param finished
     *            父Activity是否销毁，finished=true销毁
     */
    public static void goNextDelay(final Context context, final Class<?> to, final Object paraValues, long delay, final boolean finished) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                goNext(context, to, null, paraValues, finished);
            }
        }, delay);
    }


    /**
     * 延迟Activity跳转，无参数
     *
     * @param context
     * @param to
     * @param paraValues
     *            参数
     * @param delay
     * @param finished
     *            父Activity是否销毁，finished=true销毁
     */
    public static void goNextDelay(final Context context, final Class<?> to, final String paraName, final Object paraValues, long delay, final boolean finished) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                goNext(context, to, paraName, paraValues, finished);
            }
        }, delay);
    }

    /**
     * Activity跳转，如果是多个参数，建议用map<String, object>
     *
     * @param to
     * @param paraName
     * @param paraValues
     * @param finished
     */
    public static void goNext(Context context, Class<?> to, String paraName, Object paraValues, boolean finished) {
        Intent intent = new Intent(context, to);
        if (paraValues != null)
            initParas(intent, paraName, paraValues);
        context.startActivity(intent);
        if (finished)
            ((Activity) context).finish();
    }

    /**
     * 延迟Activity跳转，无参数
     *
     * @param context
     * @param to
     * @param delay
     *            request code
     */
    public static void invokeActivityDelay(final Context context, final Class<?> to, long delay) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                invokeActivity(context, to, null, null);
            }
        }, delay);
    }

    /**
     * 调用另一Activity处理，处理完成后可以返回值，实现类中需要重载方法：onActivityResult(int requestCode,
     * int resultCode, Intent data)。
     *
     * @param to
     *            另一Activity
     */
    public static void invokeActivity(Context context, Class<?> to) {
        invokeActivity(context, to, null, null);
    }

    /**
     * 延迟Activity跳转，无参数
     *
     * @param context
     * @param to
     * @param paraName
     *            参数名
     * @param paraValues
     *            参数值
     * @param delay
     *            request code
     */
    public static void invokeActivityDelay(final Context context, final Class<?> to, final String paraName, final Object paraValues, long delay) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                invokeActivity(context, to, paraName, paraValues);
            }
        }, delay);
    }

    /**
     * 调用另一Activity处理，处理完成后可以返回值，实现类中需要重载方法：onActivityResult(int requestCode,
     * int resultCode, Intent data)。
     *
     * @param to
     *            另一Activity
     * @param paraName
     *            参数名
     * @param paraValues
     *            参数值
     */
    public static void invokeActivity(Context context, Class<?> to, String paraName, Object paraValues) {
        Intent intent = new Intent();
        intent.setClass(context, to);
        if (paraValues != null)
            initParas(intent, paraName, paraValues);
        ((Activity) context).startActivityForResult(intent, REQ_FOR_FORWARD);
    }

    /**
     * 把activity之间的参数保存在intent中，如果是多个参数，建议用map<String, object>.
     *
     * @param intent
     * @param parasName
     *            参数名
     * @param value
     *            参数值
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected static void initParas(Intent intent, String parasName, Object value) {
        if (value == null) {
            return;
        }
        if (parasName == null)
            parasName = "paras";
        if (value instanceof Integer) {
            intent.putExtra(parasName, (Integer) value);
        } else if (value instanceof String) {
            intent.putExtra(parasName, (String) value);
        } else if (value instanceof Boolean) {
            intent.putExtra(parasName, (Boolean) value);
        } else if (value instanceof Character) {
            intent.putExtra(parasName, (Character) value);
        } else if (value instanceof Byte) {
            intent.putExtra(parasName, (Byte) value);
        } else if (value instanceof Double) {
            intent.putExtra(parasName, (Double) value);
        } else if (value instanceof Float) {
            intent.putExtra(parasName, (Float) value);
        } else if (value instanceof Long) {
            intent.putExtra(parasName, (Long) value);
        } else if (value instanceof StringBuffer) {
            intent.putExtra(parasName, ((StringBuffer) value).toString());
        } else if (value instanceof Bundle) {
            intent.putExtras((Bundle) value);
        } else if (value instanceof java.util.ArrayList) {
            java.util.ArrayList al = (java.util.ArrayList) value;
            if (al == null || al.size() == 0)
                return;

            Object v = al.get(al.size() - 1);
            if (v instanceof Integer)
                intent.putIntegerArrayListExtra(parasName, al);
            else
                intent.putStringArrayListExtra(parasName, (java.util.ArrayList<String>) value);
        } else if (value instanceof java.util.Map) {
            /** 传递过来的是map */
            java.util.Map<String, Object> p = (java.util.Map<String, Object>) value;
            java.util.Iterator<String> iter = ((java.util.Map) value).keySet().iterator();
            Bundle bundle = new Bundle();
            while (iter.hasNext()) {
                String key = iter.next();
                Object val = (Object) p.get(key);
                if (val instanceof Integer) {
                    bundle.putInt(key, (Integer) val);
                }
                if (val instanceof String) {
                    bundle.putString(key, (String) val);
                }
                if (val instanceof Boolean) {
                    bundle.putBoolean(key, (Boolean) val);
                }
                if (val instanceof Byte) {
                    bundle.putByte(key, (Byte) val);
                }
                if (val instanceof Double) {
                    bundle.putDouble(key, (Double) val);
                }
                if (val instanceof Float) {
                    bundle.putFloat(key, (Float) val);
                }
                if (val instanceof Long) {
                    bundle.putLong(key, (Long) val);
                }
                if (val instanceof StringBuffer) {
                    bundle.putString(key, ((StringBuffer) val).toString());
                }
                if (val instanceof java.util.ArrayList) {
                    java.util.ArrayList al = (java.util.ArrayList) val;
                    if (al == null || al.size() == 0)
                        return;

                    Object v = al.get(al.size() - 1);
                    if (v instanceof Integer)
                        bundle.putIntegerArrayList(key, al);
                    else
                        bundle.putStringArrayList(key, (java.util.ArrayList<String>) value);
                }
            }
            intent.putExtras(bundle);
        }
    }
}
