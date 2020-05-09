package com.arcghh.utilslibs.wallpaper;

import android.content.Context;
import android.net.Uri;

/**
 * @author ganhuanhui
 * 时间：2020/1/3 0003
 * 描述：
 */
public class WallpaperController {

    private Uri mGifPath;
    private String mVideoPath;

    private WallpaperController() {
    }

    private static class InnerClass {
        private static final WallpaperController instance = new WallpaperController();
    }

    public static WallpaperController getInstance() {
        return InnerClass.instance;
    }

    public void startVideoWallpaperService(Context context, String path) {
        mVideoPath = path;
        VideoWallpaperService mVideoWallpaperService = new VideoWallpaperService();
        VideoWallpaperService.setVoiceSilence(context);
        mVideoWallpaperService.setToWallPaper(context);
    }

    public void startGIfWallPaperService(Context context, Uri path) {
        mGifPath = path;
        GIfWallPaperService mGIfWallPaperService = new GIfWallPaperService();
        mGIfWallPaperService.setGifToWallPaper(context);
    }

    public Uri getGifPath() {
        return mGifPath;
    }

    public String getVideoPath() {
        return mVideoPath;
    }

    public void destroy(){
        mGifPath = null;
        mVideoPath = null;
    }
}
