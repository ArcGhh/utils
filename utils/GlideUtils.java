package com.meetacg.util.glide;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.base.Optional;
import com.meetacg.GlideApp;
import com.meetacg.R;
import com.meetacg.util.SizeUtils;
import com.xy51.libcommon.utils.glide.GlideBlurFormation;
import com.xy51.libcommon.utils.glide.GlideRoundTransform;

import java.io.File;

/**
 * @author gaoxin 2019/10/24 下午 12:03
 * @version V1.0.0
 * @name GlideUtlis
 * @mail godfeer@aliyun.com
 * @description TODO
 */
public class GlideUtils {

    //    ==========================================  建议使统一使用 下面这些方法，加载本地图片，适配了AndroidQ ===================================================


    /**
     * 适配AndroidQ
     * 可以加载任意类型图片 不带圆角
     */
    public static void withNetOrLocalImage(@NonNull ImageView image, String imagePath) {
        withRoundImage(image,imagePath,0);
    }

    /**
     * 适配AndroidQ
     * 加载任意类型图片 带圆角的
     */
    public static void withRoundImage(@NonNull ImageView image, String imagePath, int dp) {
        if (TextUtils.isEmpty(imagePath)) return;
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

    public static void withInsideRoundImage(@NonNull ImageView image, String imagePath, int dp) {
        if (imagePath.endsWith(".gif")) {
            withInsideRoundImageGif(image, imagePath, dp);
            return;
        }
        withUrlInsideRoundImage(image, imagePath, dp);//网络图片
    }


    //    =================================================  Gif图片  ===============================================================


    /***
     * 需要服务器配置
     */
    public static void withRoundImageGif(@NonNull ImageView image, String imagePath, int dp) {
        withObjectRoundImageGif(image,imagePath,dp);
    }

    /***
     * 需要服务器配置
     * @param image
     * @param imagePath
     */
    public static void withRoundImageGif(@NonNull ImageView image, Uri imagePath, int dp) {
        withObjectRoundImageGif(image,imagePath,dp);
    }

    /***
     * 加载任意类型的gif图片
     */
    public static void withObjectRoundImageGif(@NonNull ImageView image, Object imagePath, int dp) {
        GlideApp.with(image.getContext()).asGif().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .load(imagePath)
                .apply(glideRoundeds(dp))
                .into(image);
    }

    /***
     * 加载Inside类型的gif图片
     */
    public static void withInsideRoundImageGif(@NonNull ImageView image, String imagePath, int dp) {
        withObjectInsideRoundImageGif(image,Uri.parse(Optional.fromNullable(imagePath).or("")),dp);
    }

    public static void withUrlInsideRoundImage(@NonNull ImageView image, String imagePath, int dp) {
        if (TextUtils.isEmpty(imagePath)) return;
        withObjectInsideRoundImageGif(image,imagePath,dp);
    }

    /***
     * 加载任意类型的gif图片
     */
    public static void withObjectInsideRoundImageGif(@NonNull ImageView image, Object imagePath, int dp) {
        GlideApp.with(image.getContext()).asGif().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .load(imagePath)
                .apply(glideInsideRoundeds(dp))
                .into(image);
    }

//    =================================================  普通图片  ===============================================================


    /**
     * 加载网络图片，带圆角
     */
    private static void withUrlRoundImage(@NonNull ImageView image, String imagePath, int dp) {
        withObjectRoundImage(image,imagePath,dp);
    }

    /**
     * 加载Uri图片，带圆角
     */
    private static void withUriRoundImage(@NonNull ImageView image, Uri imagePath, int dp) {
        withObjectRoundImage(image,imagePath,dp);
    }

    /**
     * 加载多种类型图片，带圆角
     */
    private static void withObjectRoundImage(@NonNull ImageView image, Object imagePath, int dp) {
        GlideApp.with(image.getContext())
                .load(imagePath)
                .apply(glideRoundeds(dp))
                .into(image);
    }

//    ===============================================================================================

    public static Uri getImageContentUri(Context context, String path) {
        if (path.startsWith("content")) {
            return Uri.parse(path);
        } else {
           return Uri.fromFile(new File(path));
        }
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
                .transform(new GlideBlurFormation(30));
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
