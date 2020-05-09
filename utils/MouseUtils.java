package com.arcghh.utilslibs;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

public class MouseUtils {
    /**
     * 获取当前进程名
     */
    private static String getCurrentProcessName(Application application) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) application.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (manager !=null){
            for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
                if (process.pid == pid) {
                    processName = process.processName;
                }
            }
        }

        return processName;
    }
    /**
     * 包名判断是否为主进程
     *
     * @param
     * @return
     */
    public static boolean isMainProcess(Application application) {
        return application.getApplicationContext().getPackageName().equals(getCurrentProcessName(application));
    }
}
