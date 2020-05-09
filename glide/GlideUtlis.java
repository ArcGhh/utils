package com.arcghh.utilslibs.glide;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.arcghh.utilslibs.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.base.Optional;GlideBlurFormation
        GlideRoundTransform

import java.io.File;

/**
 * @author gaoxin 2019/10/24 下午 12:03
 * @version V1.0.0
 * @name GlideUtlis
 * @mail godfeer@aliyun.com
 * @description TODO
 */
public class GlideUtlis {

    public static void withImage(@NonNull ImageView image, String imagePath) {
        GlideApp.with(image.getContext())
                .load(Optional.fromNullable(imagePath).or(""))
                .apply(glideDefault())
                .into(image);
    }

    public static void withImages(@NonNull ImageView image, String imagePath) {
        GlideApp.with(image.getContext())
                .load(imagePath)
                .apply(glideDefault())
                .into(image);
    }

    public static void withImage(@NonNull ImageView image, String imagePath, int w, int h) {
        GlideApp.with(image.getContext())
                .load(Optional.fromNullable(imagePath).or(""))
                .override(w, h)
                .apply(glideDefault())
                .into(image);
    }

    public static void withImage(@NonNull ImageView image, int imagePath) {
        GlideApp.with(image.getContext())
                .load(imagePath)
                .apply(glideDefault())
                .into(image);
    }

    public static void withRoundImage(@NonNull ImageView image, int imagePath, int dp) {
        GlideApp.with(image.getContext())
                .load(imagePath)
                .apply(glideRoundeds(dp))
                .into(image);
    }

    public static void withUrlInsideRoundImage(@NonNull ImageView image, String imagePath, int dp) {
        GlideApp.with(image.getContext())
                .load(imagePath)
                .apply(glideInsideRoundeds(dp))
                .into(image);
    }

    public static void withUrlRoundImageDefault(@NonNull ImageView image, String imagePath, int dp) {
        GlideApp.with(image.getContext())
                .load(imagePath)
                .apply(glideRound(dp))
                .into(image);
    }

    public static void withUrlRoundImage(@NonNull ImageView image, String imagePath, int dp) {
        GlideApp.with(image.getContext())
                .load(imagePath)
                .apply(glideRoundeds(dp))
                .into(image);
    }

    public static void withUriRoundImage(@NonNull ImageView image, Uri imagePath, int dp) {
        GlideApp.with(image.getContext())
                .load(imagePath)
                .apply(glideRoundeds(dp))
                .into(image);
    }

    public static void withInsideRoundImage(@NonNull ImageView image, String imagePath, int dp) {
        if (imagePath.endsWith(".gif")) {
            withInsideRoundImageGif(image, imagePath, dp);
            return;
        }
        withUrlInsideRoundImage(image, imagePath, dp);//网络图片
    }

    public static void withRoundImage(@NonNull ImageView image, String imagePath, int dp) {
        if (imagePath.startsWith("http")) {
            if (imagePath.endsWith(".gif")) {
                withRoundImageGif(image, imagePath, dp);
                return;
            }
            withUrlRoundImage(image, imagePath, dp);//网络图片
        } else {
            if (imagePath.endsWith(".gif")) {
                withRoundImageGif(image, getImageContentUri(image.getContext(),imagePath), dp);
                return;
            }
            //本地图片
            withUriRoundImage(image, getImageContentUri(image.getContext(),imagePath), dp);
        }
    }

    public static void withRoundImage(@NonNull ImageView image, String imagePath, int dp, boolean isGif) {
        if (isGif) {
            withRoundImageGif(image, getImageContentUri(image.getContext(),imagePath), dp);
            return;
        }
        withUriRoundImage(image,getImageContentUri(image.getContext(),imagePath), dp);
    }

    public static Uri getImageContentUri(Context context, String path) {
        if (path.startsWith("content")) {
            return Uri.parse(path);
        } else {
           return Uri.fromFile(new File(path));
        }
    }

    /***
     * 需要服务器配置
     * @param image
     * @param imagePath
     */
    public static void withImageGif(@NonNull ImageView image, String imagePath) {
        GlideApp.with(image.getContext()).asGif().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .load(Uri.parse(Optional.fromNullable(imagePath).or("")))
                .apply(glideDefault())
                .into(image);
    }

    /***
     * 需要服务器配置
     * @param image
     * @param imagePath
     */
    public static void withImageGif(@NonNull ImageView image, int imagePath) {
        GlideApp.with(image.getContext()).asGif().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .load(imagePath)
                .apply(glideDefault())
                .into(image);
    }

    /***
     * 需要服务器配置
     * @param image
     * @param imagePath
     */
    public static void withRoundImageGif(@NonNull ImageView image, String imagePath, int dp) {
        GlideApp.with(image.getContext()).asGif().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .load(Uri.parse(Optional.fromNullable(imagePath).or("")))
                .apply(glideRoundeds(dp))
                .into(image);
    }

    /***
     * 需要服务器配置
     * @param image
     * @param imagePath
     */
    public static void withInsideRoundImageGif(@NonNull ImageView image, String imagePath, int dp) {
        GlideApp.with(image.getContext()).asGif().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .load(Uri.parse(Optional.fromNullable(imagePath).or("")))
                .apply(glideInsideRoundeds(dp))
                .into(image);
    }

    /***
     * 需要服务器配置
     * @param image
     * @param imagePath
     */
    public static void withRoundImageGif(@NonNull ImageView image, Uri imagePath, int dp) {
        GlideApp.with(image.getContext()).asGif().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .load(imagePath)
                .apply(glideRoundeds(dp))
                .into(image);
    }

    public RequestOptions glide() {
        return new RequestOptions().centerCrop().error(R.mipmap.img_placeholder);
    }

    /***
     * 高斯模糊
     * @return
     */
    public static RequestOptions glideTransform() {
        return new RequestOptions()
                .centerCrop()
                .error(R.mipmap.img_placeholder)
                .transform(new GlideBlurFormation(20));
    }

    public static RequestOptions glideDefault() {
        return new RequestOptions().placeholder(R.mipmap.img_placeholder)
                .error(R.mipmap.img_placeholder);
    }

    public static RequestOptions glideRounded() {
        return new RequestOptions()
                .centerCrop()
                .error(R.mipmap.img_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.color_eeeeee)
                .disallowHardwareConfig()
                .transform(new GlideRoundedCornersTransform(SizeUtils.dp2px(6),
                        GlideRoundedCornersTransform.CornerType.ALL));
    }

    public static RequestOptions glideRoundeds(int dp) {
        return new RequestOptions()
                .error(R.mipmap.img_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.color_eeeeee)
                .disallowHardwareConfig()
                .transform(new CenterCrop(), new GlideRoundTransform(dp));
    }

    public static RequestOptions glideRound(int dp) {
        return new RequestOptions()
                .error(R.mipmap.img_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.color_eeeeee)
                .disallowHardwareConfig()
                .transform(new GlideRoundTransform(dp));
    }

    public static RequestOptions glideInsideRoundeds(int dp) {
        return new RequestOptions()
                .error(R.mipmap.img_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.color_eeeeee)
                .disallowHardwareConfig()
                .transform(new CenterInside(), new GlideRoundTransform(dp));
    }

    public RequestOptions glideBanner() {
        return new RequestOptions()
                .error(R.mipmap.img_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.color_eeeeee);
    }

    public RequestOptions glideRoundedHead() {
        return new RequestOptions()
                .centerCrop()
                .error(R.mipmap.img_placeholder)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.color.color_eeeeee)
                .disallowHardwareConfig()
                .transform(new GlideRoundedCornersTransform(10,
                        GlideRoundedCornersTransform.CornerType.ALL));
    }


}
