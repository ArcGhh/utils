package com.arcghh.utilslibs.wallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ganhuanhui
 * 时间：2020/1/3 0003
 * 描述：
 */
public class GIfWallPaperService extends WallpaperService {

    private Movie movie;
    private Handler handler;
    private boolean isVisible;
    private int height;
    private int width;
    private int gifWidth;
    private int gifHeigth;

    /**
     * 设置壁纸
     *
     * @param context
     */
    public void setGifToWallPaper(Context context) {
        try {
            context.clearWallpaper();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(context, GIfWallPaperService.class));
        context.startActivity(intent);
    }

    @Override
    public Engine onCreateEngine() {
        return new GifEngine();
    }


    class GifEngine extends Engine {

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            Uri sGifPath = WallpaperController.getInstance().getGifPath();
            if (sGifPath == null) return;
            handler = new Handler();
            try {
                InputStream inputStream = getContentResolver().openInputStream(sGifPath);
                movie = Movie.decodeStream(inputStream);
                if (null == movie) return;
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                height = displayMetrics.heightPixels;
                width = displayMetrics.widthPixels;
                gifWidth = movie.width();
                gifHeigth = movie.height();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (handler == null) handler = new Handler();
            if (visible) {
                isVisible = true;
                handler.post(runnable);
            } else {
                isVisible = false;
                handler.removeCallbacks(runnable);
            }

        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (handler != null) handler.removeCallbacks(runnable);
        }

        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isVisible && null != movie) {
                    Canvas canvas = getSurfaceHolder().lockCanvas();
                    canvas.save();
                    canvas.scale(2, 2);
                    movie.draw(canvas, 0, 0);
                    movie.setTime((int) (System.currentTimeMillis() % movie.duration()));
                    canvas.restore();
                    getSurfaceHolder().unlockCanvasAndPost(canvas);
                    handler.postDelayed(runnable, 50);   //50ms表示每50m绘制一次
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != handler) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
