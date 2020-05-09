package com.arcghh.utilslibs.wallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author ganhuanhui
 * 时间：2019/10/21
 * 描述：壁纸工具类 需要声明权限 SET_WALLPAPER
 */
public class ImageWallerPaper {

    /**
     * 设置桌面壁纸
     *
     * @param context
     * @param imageFilesPath
     */
    public static void setWallPaper(Context context, String imageFilesPath) {
        WallpaperManager mWallManager = WallpaperManager.getInstance(context);
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilesPath);
            mWallManager.setBitmap(bitmap);
            Toast.makeText(context, "壁纸设置成功", Toast.LENGTH_SHORT)
                    .show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置锁屏壁纸
     */
    public static void setLockWallPaper(Context context, String imageFilesPath) {
        try {

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setStream(new FileInputStream(new File(imageFilesPath)), null, true, WallpaperManager.FLAG_LOCK);
            } else {
                WallpaperManager mWallManager = WallpaperManager.getInstance(context);
                Class class1 = mWallManager.getClass();//获取类名
                Method setWallPaperMethod = class1.getMethod("setBitmapToLockWallpaper", Bitmap.class);//获取设置锁屏壁纸的函数
                setWallPaperMethod.invoke(mWallManager, BitmapFactory.decodeFile(imageFilesPath));//调用锁屏壁纸的函数，并指定壁纸的路径imageFilesPath
            }
            Toast.makeText(context, "锁屏壁纸设置成功", Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * 调用手机系统设置壁纸
     * @param context
     * @param path
     * @param authority
     */
    public static void setWallpaper(Context context, String path, String authority) {
        if (context == null || TextUtils.isEmpty(path) || TextUtils.isEmpty(authority)) {
            return;
        }
        Uri uriPath = getUriWithPath(context, path, authority);
        Intent intent;
        if (RomUtil.isHuaweiRom()) {
            try {
                ComponentName componentName =
                        new ComponentName("com.android.gallery3d", "com.android.gallery3d.app.Wallpaper");
                intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uriPath, "image/*");
                intent.putExtra("mimeType", "image/*");
                intent.setComponent(componentName);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    WallpaperManager.getInstance(context.getApplicationContext())
                            .setBitmap(BitmapFactory.decodeFile(path));
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Toast.makeText(context,"未找到图片资源",Toast.LENGTH_SHORT).show();
                }
            }
        } else if (RomUtil.isMiuiRom()) {
            try {
                ComponentName componentName = new ComponentName("com.android.thememanager",
                        "com.android.thememanager.activity_book_read.WallpaperDetailActivity");
                intent = new Intent("miui.intent.action.START_WALLPAPER_DETAIL");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uriPath, "image/*");
                intent.putExtra("mimeType", "image/*");
                intent.setComponent(componentName);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    WallpaperManager.getInstance(context.getApplicationContext())
                            .setBitmap(BitmapFactory.decodeFile(path));
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Toast.makeText(context,"未找到图片资源",Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    intent = WallpaperManager.getInstance(context.getApplicationContext()).getCropAndSetWallpaperIntent(uriPath);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(intent);
                } catch (IllegalArgumentException e) {
                    try {
                        Uri uri = getImageContentUri(context,path);
                        intent = WallpaperManager.getInstance(context.getApplicationContext()).getCropAndSetWallpaperIntent(uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), uriPath);
                            if (bitmap != null) {
                                WallpaperManager.getInstance(context.getApplicationContext()).setBitmap(bitmap);
                                return;
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        Toast.makeText(context,"未找到图片资源",Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                try {
                    WallpaperManager.getInstance(context.getApplicationContext())
                            .setBitmap(BitmapFactory.decodeFile(path));
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Toast.makeText(context,"未找到图片资源",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static Uri getImageContentUri(Context context, String absPath) {

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[] { MediaStore.Images.Media._ID }
                , MediaStore.Images.Media.DATA + "=? "
                , new String[] { absPath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , Integer.toString(id));

        } else if (!absPath.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, absPath);
            return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }

    public static Uri getUriWithPath(Context context, String filepath, String authority) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0以上的读取文件uri要用这种方式了
            return FileProvider.getUriForFile(context.getApplicationContext(), authority, new File(filepath));
        } else {
            return Uri.fromFile(new File(filepath));
        }
    }
}
