package com.arcghh.utilslibs.wallpaper;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.text.TextUtils;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * @author ganhuanhui
 * 时间：2019/11/8
 * 描述：设置本地视频为动态壁纸
 */
public class VideoWallpaperService extends WallpaperService {

    public static final String VIDEO_PARAMS_CONTROL_ACTION = "com.meetacg.videowallpager";
    public static final String ACTION = "action";
    public static final int ACTION_VOICE_SILENCE = 0x101;
    public static final int ACTION_VOICE_NORMAL = 0x102;
    public static final String IS_SILENCE = "is_silence";
    private static final String TAG = VideoWallpaperService.class.getName();

    /**
     * 设置静音
     *
     * @param context
     */
    public static void setVoiceSilence(Context context) {
        Intent intent = new Intent(VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(ACTION, ACTION_VOICE_SILENCE);
        context.sendBroadcast(intent);
    }

    /**
     * 设置有声音
     *
     * @param context
     */
    public static void setVoiceNormal(Context context) {
        Intent intent = new Intent(VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(ACTION, ACTION_VOICE_NORMAL);
        context.sendBroadcast(intent);
    }

    /**
     * 设置壁纸
     *
     * @param context
     */
    public void setToWallPaper(Context context) {
        try {
            context.clearWallpaper();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(context, VideoWallpaperService.class));
        context.startActivity(intent);
    }


    @Override
    public Engine onCreateEngine() {
        return new VideoWallpagerEngine();
    }

    class VideoWallpagerEngine extends Engine {
        private MediaPlayer mMediaPlayer;
        private BroadcastReceiver mVideoVoiceControlReceiver;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            IntentFilter intentFilter = new IntentFilter(VIDEO_PARAMS_CONTROL_ACTION);
            mVideoVoiceControlReceiver = new VideoVoiceControlReceiver();
            registerReceiver(mVideoVoiceControlReceiver, intentFilter);
        }

        @Override
        public void onDestroy() {
            unregisterReceiver(mVideoVoiceControlReceiver);
            super.onDestroy();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (null == mMediaPlayer) return;
            if (visible) {
                mMediaPlayer.start();
            } else {
                mMediaPlayer.pause();
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            String sVideoPath = WallpaperController.getInstance().getVideoPath();
            if (TextUtils.isEmpty(sVideoPath)) return;
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setSurface(holder.getSurface());

            try {

                mMediaPlayer.setDataSource(sVideoPath);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.setVolume(0f, 0f);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        }

        class VideoVoiceControlReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                int action = intent.getIntExtra(ACTION, -1);
                switch (action) {
                    case ACTION_VOICE_NORMAL:
                        mMediaPlayer.setVolume(1.0f, 1.0f);
                        break;
                    case ACTION_VOICE_SILENCE:
                        mMediaPlayer.setVolume(0, 0);
                        break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
