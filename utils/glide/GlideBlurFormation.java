package com.arcghh.utilslibs.glide;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.xy51.libcommon.utils.FastBlur;

import java.security.MessageDigest;

/***
 * 模糊类
 */
public class GlideBlurFormation extends BitmapTransformation {

    private int radius;

    public GlideBlurFormation(int radius) {
        this.radius = radius;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return FastBlur.doBlur(toTransform, radius, false);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
    }
}