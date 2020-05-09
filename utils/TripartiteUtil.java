package com.arcghh.utilslibs;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.util.List;

/***
 * app检测判断
 */
public class TripartiteUtil {

    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }else if (pn.equals("com.tencent.tim")){
                    return true;
                }
            }
        }
        return false;
    }

    public static void getQQMessage(Context activity, String qqNumber){
        if(isQQClientAvailable(activity)){
            final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin="+qqNumber+"&version=1";
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
        }else{
            Toast.makeText(activity,"请安装QQ客户端", Toast.LENGTH_SHORT).show();
        }
    }
    public static void getQQGroup(Context activity, String key){
        if(isQQClientAvailable(activity)){
            Intent intent = new Intent();
            intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                activity.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(activity,"未安装手Q或安装的版本不支持", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(activity,"未安装手Q或安装的版本不支持", Toast.LENGTH_SHORT).show();
        }
    }

}
