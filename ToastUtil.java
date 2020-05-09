package com.arcghh.utilslibs;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * ToastUtil  提示工具类,处理整个应用的toast提示，使用单例实现，避免在大量Toast消息弹出的时候某些消息无法显示
 *
 * @author
 */
public class ToastUtil {
    private static Toast toast;
    private static long time;
    private static String mMessage = "";

    public static void init(Context context) {
        toast = Toast.makeText(context, mMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        time = System.currentTimeMillis();
    }

    /**
     * 显示消息
     *
     * @param message 消息
     */
    public static void show(String message) {
        if (toast == null) {
            throw new IllegalStateException("ToastUtil is not inited");
        } else {
            long now = System.currentTimeMillis();

            if (message.equals(mMessage) && !isNeedShow(now)) {
                return;
            }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && mContext!=null) {
//                    // 9.0以后已经优化了Toast 所以不会出现一瞬间重复显示并且弹出多个的情况
//                    // 为了避免有些机型在9.0以后 一瞬间执行了多次show而导致Toast会消失不显示的情adb况，所以这里每次show都重新初始化
//                    toast = Toast.makeText(mContext,mMessage, Toast.LENGTH_SHORT);
//                }
            toast.setText(message);
            toast.show();
            mMessage = message;
            time = now;


        }
    }

    /**
     * 显示消息
     *
     * @param message 消息
     */
    public static void show(String message, boolean falg) {
        if (toast == null) {
            throw new IllegalStateException("ToastUtil is not inited");
        } else if (falg) {
            toast.setText(message);
            toast.show();
        } else {
            long now = System.currentTimeMillis();

            if (message.equals(mMessage) && !isNeedShow(now)) {
                return;
            }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && mContext!=null) {
//                    // 9.0以后已经优化了Toast 所以不会出现一瞬间重复显示并且弹出多个的情况
//                    // 为了避免有些机型在9.0以后 一瞬间执行了多次show而导致Toast会消失不显示的情况，所以这里每次show都重新初始化
//                    toast = Toast.makeText(mContext,mMessage, Toast.LENGTH_SHORT);
//                }
            toast.setText(message);
            toast.show();
            mMessage = message;
            time = now;

        }
    }


    /**
     * 判断是否需要显示消息
     *
     * @param now 现在时间
     */
    private static boolean isNeedShow(long now) {
        return time == 0 || (now - time) > 2000;
    }

    /**
     * 取消显示的toast
     */
    public static void cancelToast() {

        if (toast != null) {
            toast.cancel();
        }
    }


}
