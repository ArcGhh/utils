package com.arcghh.utilslibs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

public class IntentUtils {
    private final Context context;

    public IntentUtils(Context context) {
        this.context = context;
    }

    public static void StartNoIntent(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    /**
     * 一个参数的跳转
     *
     * @param context
     * @param cls
     * @param key
     * @param val
     */

    public static void startSignIntent(Context context, Class cls, String key, String val) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(key, val);
        context.startActivity(intent);
    }

    /**
     * 一个参数的跳转
     *
     * @param context
     * @param cls
     * @param key
     * @param val
     */

    public static void startSignIntent(Context context, Class cls, String key, int val) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(key, val);
        context.startActivity(intent);
    }

    /**
     * 一个参数的跳转
     *
     * @param context
     * @param cls
     * @param key
     * @param val
     */

    public static void startSignIntent(Context context, Class cls, String key, long val) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(key, val);
        context.startActivity(intent);
    }

    /**
     * 两个参数的跳转
     *
     * @param context
     * @param cls
     * @param key
     * @param val
     * @param key2
     * @param val2
     */

    public static void StartDoubleIntent(Context context, Class cls, String key, String val, String key2, String val2) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(key, val);
        intent.putExtra(key2, val2);
        context.startActivity(intent);
    }

    /**
     * 三个参数的跳转
     *
     * @param context
     * @param cls
     * @param key
     * @param val
     * @param key2
     * @param val2
     */


    public static void StartThreeIntent(Context context, Class cls, String key, String val, String key2, String val2, String key3, String val3) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(key, val);
        intent.putExtra(key2, val2);
        intent.putExtra(key3, val3);
        context.startActivity(intent);
    }

    /**
     * 四个参数的跳转
     *
     * @param context
     * @param cls
     * @param key
     * @param val
     * @param key2
     * @param val2
     */
    public static void StartFourIntent(Context context, Class cls, String key, String val, String key2, String val2, String key3, String val3, String key4, String val4) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(key, val);
        intent.putExtra(key2, val2);
        intent.putExtra(key3, val3);
        intent.putExtra(key4, val4);
        context.startActivity(intent);
    }

    /**
     * 对象跳转
     *
     * @param context
     * @param cls
     * @param key
     * @param val
     */

    public static void StartBundleIntent(Context context, Class cls, String key, Class val) {
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, val);
        context.startActivity(intent);
    }

    /**
     * 集合跳转
     *
     * @param context
     * @param cls
     * @param key
     */
    public static void StartListIntent(Context context, Class cls, String key, List list) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(key, (Serializable) list);
        context.startActivity(intent);
    }

    /**
     * 集合带一个参数
     *
     * @param context
     * @param cls
     * @param key
     * @param list
     * @param key2
     * @param val
     */
    public static void StartListSignIntent(Context context, Class cls, String key, List list, String key2, String val) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(key, (Serializable) list);
        intent.putExtra(key2, val);
        context.startActivity(intent);
    }


}
