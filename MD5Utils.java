package com.arcghh.utilslibs;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author gaoxin 2019-05-10 17:54
 * @version V1.0.0
 * @name MD5Utils
 * @mail godfeer@aliyun.com
 * @description  MD5相关
 */
public class MD5Utils {

    public static final String TAG = MD5Utils.class.getSimpleName();

    /**
     * 获得str的md5值
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        String md5String = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            md5String = buf.toString();
            Log.d(TAG, buf.toString());// 32位的加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());// 32位的加密
        }
        return md5String;
    }

}