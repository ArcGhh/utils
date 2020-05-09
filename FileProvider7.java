package com.arcghh.utilslibs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.io.File;

/**
 * @author ganhuanhui
 * @date 2020/5/9 0009
 * @desc Android 7.0 行为变更 通过FileProvider在应用间共享文件
 *
 * AndroidManifest下配置
  <provider
          android:name="android.support.v4.content.FileProvider"
          android:authorities="${applicationId}.android7.fileprovider"
          android:exported="false"
          android:grantUriPermissions="true">
          <meta-data
             android:name="android.support.FILE_PROVIDER_PATHS"
              android:resource="@xml/file_paths" />
      </provider>

file_paths：
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
<root-path
name="root"
path="" />
<files-path
name="files"
path="" />

<cache-path
name="cache"
path="" />

<external-path
name="external"
path="" />

<external-files-path
name="external_file_path"
path="" />
<external-cache-path
name="external_cache_path"
path="" />

</paths>


 *
 *
 *
 */
public class FileProvider7 {

    /**
     * 拍照-使用的Uri
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = getUriForFile24(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    public static Uri getUriForFile24(Context context, File file) {
        Uri fileUri = android.support.v4.content.FileProvider.getUriForFile(context,
                context.getPackageName() + ".android7.fileprovider",
                file);
        return fileUri;
    }

    /**
     * 安装apk等等需要的Uri
     * @param context
     * @param intent
     * @param type
     * @param file
     * @param writeAble
     */
    public static void setIntentDataAndType(Context context,
                                            Intent intent,
                                            String type,
                                            File file,
                                            boolean writeAble) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(getUriForFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }
}

