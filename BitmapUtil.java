package com.arcghh.utilslibs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @description 图片工具类
 */
public class BitmapUtil {

    /**
     * 将Drawable对象转化为Bitmap对象
     *
     * @param drawable Drawable对象
     * @return 对应的Bitmap对象
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        //如果本身就是BitmapDrawable类型 直接转换即可
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        //取得Drawable固有宽高
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            //创建一个1x1像素的单位色图
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            //直接设置一下宽高和ARGB
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        //重新绘制Bitmap
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap addGradient(Bitmap originalBitmap, int[] colors) {//给originalBitmap着渐变色
        if (colors == null || colors.length == 0) {//默认色处理
            colors = new int[]{Color.parseColor("#989898"), Color.parseColor("#f8f8f8")};
        } else if (colors.length == 1) {//单色处理
            int[] newColor = new int[]{colors[0], colors[0]};
            colors = newColor;
        }
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Canvas canvas = new Canvas(originalBitmap);//Canvas中Bitmap是用来保存像素，相当于画纸
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, width, height, colors, null, Shader.TileMode.CLAMP);//shader:着色器，线性着色器设置渐变从左上坐标到右下坐标
        paint.setShader(shader);//设置着色器
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//设置图像混合模式
        canvas.drawRect(0, 0, width, height, paint);
        return originalBitmap;
    }

    public static Bitmap getGradientBitmap(Context context, int drawableId, int[] colors) {
        //android不允许直接修改res里面的图片，所以要用copy方法
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);
        addGradient(bitmap, colors);
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）
     */
    public static Bitmap compress(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            Bitmap.CompressFormat Type =  Bitmap.CompressFormat.PNG;
            //image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
            image.compress(Type, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 质量压缩
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap.CompressFormat Type = Bitmap.CompressFormat.PNG;
        //image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Type, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}
