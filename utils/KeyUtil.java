package com.arcghh.utilslibs;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 加密密钥Key
 *
 * @author: LiYanChao
 * @create: 2019-04-26 15:37
 */
public class KeyUtil {

    private KeyUtil() {
    }

    /*
     * @Author Renqiang
     * @Description MD5二次加密 用于接口校验加密参数
     * @Param [userNum, timeStr]
     * @Date 2019/9/6 17:05
     * @return java.lang.String
     **/
    public static String getToken(String userNum, String timeStr) {
        StringBuilder builder = new StringBuilder(userNum);
        builder.append("!_").append(timeStr).append("@!");
        //MD5二次加密
        return stringToMD5(stringToMD5(builder.toString()));
    }

    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            return "";
            //throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;

    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(final EditText editText) {

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                String speChat2 = "[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]";
                Pattern pattern = Pattern.compile(speChat2);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    Toast.makeText(editText.getContext(),"暂时不支持输入表情",Toast.LENGTH_SHORT).show();
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
}
