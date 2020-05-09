package com.arcghh.utilslibs;

import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import java.util.HashMap;

public class VideoUtil {

    /**
     * get Local video duration
     * 获取本地视频时长
     *
     * @return
     */
    public static int getLocalVideoDuration(String videoPath) {
        if (TextUtils.isEmpty(videoPath)) return 0;
        int duration = 0;
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(videoPath);
            String dStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            duration = Integer.parseInt(dStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duration;
    }

    /**
     * 获取网络视频时长 单位秒
     * @param mUri
     * @return
     */
    public static long getVideoDuration(String mUri) {
        if (TextUtils.isEmpty(mUri)) return 0;
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
        try {

            HashMap<String, String> headers = null;
            if (headers == null) {
                headers = new HashMap<String, String>();
                headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
            }
            mmr.setDataSource(mUri, headers);

            String duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);//时长(毫秒)

            return Long.parseLong(duration);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mmr.release();
        }

        return 0;
    }
}
