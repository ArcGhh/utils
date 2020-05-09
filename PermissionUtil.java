package com.arcghh.utilslibs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

public class PermissionUtil {

    public static String[] READ_WRITE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};

    public static String[] READ_PHONE_STATE = {
            Manifest.permission.READ_PHONE_STATE};

    public static String[] READ_WRITE_PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    public static final int CODE_PERMISSION_REQUEST_DOWNLOAD = 10;
    public static final int CODE_PERMISSION_REQUEST_SHARE = 20;

    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示有该权限  false-表示无权限
     */
    public static boolean hasPermissions(Context mContexts, String[] permissions) {
        for (String permission : permissions) {
            if (lacksPermission(mContexts, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否缺少权限
     */
    private static boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    /**
     * 请求权限
     *
     * @param activity
     * @param permissions
     */
    public static void requestPermissions(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, 0);
    }

    /**
     * 请求权限
     *
     * @param activity
     * @param permissions
     */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }


    /**
     * 请求权限
     *
     * @param fragment
     * @param permissions
     */
    public static void requestPermissions(Fragment fragment, String[] permissions, int requestCode) {
        fragment.requestPermissions(permissions,requestCode);
    }

}
