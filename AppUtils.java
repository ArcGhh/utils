package com.arcghh.utilslibs;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * @author gaoxin 2019-07-03 18:01
 * @version V1.0.0
 * @name AppUtils
 * @mail godfeer@aliyun.com
 * @description App相关的辅助类
 */
public class AppUtils {

    private Context context;
    private boolean installFalg;

    public static final String TAG = AppUtils.class.getSimpleName();

    private AppUtils() {
        /**cannot be instantiated **/
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    public AppUtils(Context context) {
        this.context = context;
    }

    //根据apk获取应用包名
    public static String getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        ApplicationInfo appInfo = null;
        if (info != null) {
            appInfo = info.applicationInfo;
            return appInfo.packageName;
        } else {
            return null;
        }
    }
    /**
     * 判断网络连接情况
     */
    public static boolean isThereInternetConnection(Context context) {
        boolean isConnected;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }

    /***
     * 获取apk信息
     * @param context
     * @param path
     * @return
     */
    public static PackageInfo getApkMessage(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        return info;
    }

    /***
     * 判断是否安装
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        //取得所有的PackageInfo
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        //判断包名是否在系统包名列表中
        return pName.contains(packageName);
    }

    public static boolean openAPK(Context mContext, String packagename) {
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = new Intent();
        intent = packageManager.getLaunchIntentForPackage(packagename);
        if (intent == null) {
            Toast.makeText(mContext, "未安装", Toast.LENGTH_LONG).show();
            return false;
        } else {
            mContext.startActivity(intent);
            return true;
        }

    }

    /**
     * 复制文件到SD卡
     *
     * @param context
     * @param fileName 复制的文件名
     * @param path     保存的目录路径
     * @return
     */
    public static File copyAssetsFile(Context context, String fileName, String path) {
        try {
            InputStream mInputStream = context.getAssets().open(fileName);
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            File mFile = new File(path + File.separator + fileName);
            if (!mFile.exists()) {
                mFile.createNewFile();
            }
            Log.e("*****", "开始拷贝");
            FileOutputStream mFileOutputStream = new FileOutputStream(mFile);
            byte[] mbyte = new byte[1024];
            int i = 0;
            while ((i = mInputStream.read(mbyte)) > 0) {
                mFileOutputStream.write(mbyte, 0, i);
            }
            mInputStream.close();
            mFileOutputStream.close();

            return mFile;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("*****", fileName + "not exists" + "or write err");
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断网络是否可用
     *
     * @param context Context对象
     */
    public static Boolean isNetworkReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = cm.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }
        return (current.isAvailable());
    }

    /**
     * 获取view的bitmap
     *
     * @param v
     * @return
     */
    public static Bitmap getBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(c);
        } else {
            c.drawColor(Color.WHITE);
        }
        // Draw view to canvas
        v.draw(c);
        return b;
    }

    /***
     * 修改字体大小
     * @param va 值
     * @param fontSize 字体大小
     * @param start 从第几位开始
     * @param fontLength 从第几位结束
     * @return
     */
    public static Spannable modifyFontSize(String va, int fontSize, int start, int fontLength) {
        Spannable sp = new SpannableString(va);
        sp.setSpan(new AbsoluteSizeSpan(fontSize, true), start, fontLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sp;
    }


    /**
     * get the version of the application
     *
     * @param mContext
     * @return version code.
     */
    public static int getAppVersionCode(Context mContext) {
        PackageInfo pInfo = null;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo.versionCode;
    }

    /**
     * 获得本机imei md5小写值
     *
     * @param context
     * @return
     */
    public static String getImeiMd5(Context context) {
        return MD5Utils.md5(getImei(context)).toLowerCase();
    }

    /**
     * 获取mac
     *
     * @param context
     * @return
     */
    public static String getMac(Context context) {
        android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifi.getConnectionInfo().getMacAddress();
    }

    /**
     * 获得imei
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        if (deviceId == null) {
            //android.provider.Settings;
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    /**
     * 获取包信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return new PackageInfo();
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL; // 手机型号
    }

    /**
     * 获取系统版本
     *
     * @return
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }


    // 手机制造商
    public static String getProduct() {
        return Build.PRODUCT;
    }

    // 系统定制商 手机品牌
    public static String getBrand() {
        return Build.BRAND;
    }

    // 硬件制造商
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    // 平台信息
    public static String getHardWare() {
        String result = Build.HARDWARE;
        if (result.matches("qcom")) {
            // LogUtils.i(TAG, "Qualcomm platform");
            result = "Qualcomm_" + result;
        } else if (result.matches("mt[0-9]*")) {
            result = "MTK_" + result;
        }
        return result;
    }

    // 型号
    public static String getMode() {
        return Build.MODEL;
    }

    /**
     * 判断是否平板设备
     *
     * @param context
     * @return true:平板,false:手机
     */
    public static boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    // CPU 指令集，可以查看是否支持64位
    public static String getCpuAbi() {
        return Build.CPU_ABI;
    }

    public static boolean isCpu64() {
        boolean result = false;

        if (Build.CPU_ABI.contains("arm64")) {
            result = true;
        }

        return result;
    }

    // 显示模块
    public static String getDisplay() {
        return Build.DISPLAY;
    }

    // SDK 当前版本号
    public static int getCurSDK() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机号
     *
     * @param context
     * @return
     */
//    public static String getPhoneNumber(Context context) {
//        android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        return tm.getLine1Number(); // 手机号码，有的可得，有的不可得
//    }

    /**
     * 获取AndroidID
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
    }


    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String isDevice(Context context) {
        if (isPad(context)) {
            return "平板";
        } else {
            return "手机";
        }
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }


    /**
     * 获取系统属性
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(c, key, "unknown"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    /**
     * 设置系统属性
     *
     * @param key
     * @param value
     */
    public static void setProperty(String key, String value) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(c, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否是cts模式
     *
     * @return
     */
    public static boolean isCtsEnable() {
        return getProperty("ro.cts.enable", "false").equals("true");
    }

    /**
     * install Apk
     *
     * @param context
     * @param filePath
     */
    public static void installApp(Context context, String filePath) {

        File _file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(_file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static boolean getUninatllApkInfo(Context context, String filePath) {
        boolean result = false;
        try {
            PackageManager pm = context.getPackageManager();
            Log.e("archiveFilePath", filePath);
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                //完整
                result = true;
            }
        } catch (Exception e) {
            //不完整
            result = false;
        }
        return result;
    }

    private static long lastClickTime = 0;

    /***
     * APP 退出逻辑
     * @param context
     */
    public static boolean exitApp(Context context) {
        if (lastClickTime <= 0) {
            Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT).show();
            lastClickTime = System.currentTimeMillis();
        } else {
            long currentClickTime = System.currentTimeMillis();
            if (currentClickTime - lastClickTime < 1000) {
                lastClickTime = 0;
                System.exit(0);
            } else {
                lastClickTime = currentClickTime;
                Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT).show();

            }

        }
        return true;

    }


    /**
     * Return the application's version code.
     *
     * @param packageName The name of the package.
     * @return the application's version code
     */
    public static int getAppVersionCode(Context ctx, final String packageName) {
        if (isSpace(packageName)) return -1;
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * Return the application's version name.
     *
     * @param packageName The name of the package.
     * @return the application's version name
     */
    public static String getAppVersionName(Context context,final String packageName) {
        if (isSpace(packageName)) return "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param filePath The path of file.
     */
    public static void installApp(Context ctx, final String filePath, final String authority) {
        installApp(ctx, getFileByPath(filePath), authority);
    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file The file.
     */
    public static void installApp(Context ctx, final File file, final String authority) {
        if (!isFileExists(file)) return;
        ctx.startActivity(getInstallAppIntent(ctx, file, authority, true));
    }

    private static Intent getInstallAppIntent(Context ctx, final File file, String authority,
                                              final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file);
        } else {
            data = FileProvider.getUriForFile(ctx, authority, file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        ctx.grantUriPermission(ctx.getPackageName(), data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(data, type);
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }


    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }
}