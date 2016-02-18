package com.sponia.foundationmoudle.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.text.TextUtils;

import java.io.InputStream;

/**
 * @packageName com.sponia.foundationmoudle.utils
 * @description
 * @date 15/9/9
 * @auther shibo
 */
public final class SponiaBitmapUtil {
    private static final Paint paint_comm = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * @Description 计算图片所占内存大小width * height * perPixelsSize
     *
     * @param bitmap
     * @return int
     * @Description 单位是：byte
     */
    public static final int calcBitmapMemorySize(Bitmap bitmap) {
        if (null == bitmap) {
            return 0;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Config config = bitmap.getConfig();
        int perPixelsSize = 0;// 每像素所占的大小
        if (Config.ALPHA_8 == config) {// 此时图片只有alpha值，没有RGB值，一个像素占用一个字节
            perPixelsSize = 1;
        } else if (config == Config.ARGB_4444) {
            perPixelsSize = 2;
        } else if (config == Config.ARGB_8888) {
            perPixelsSize = 4;
        } else if (config == Config.RGB_565) {
            perPixelsSize = 2;
        }

        return width * height * perPixelsSize;
    }

    /**
     *
     * @Description 图片缩放,会返回newWidth*newHeight大小的图片，会将传入的bitmap进行recycle
     *
     * @param bitmap
     *            需要缩放的图片
     * @param newWidth
     *            图片新宽度
     * @param newHeight
     *            图片新高度
     * @return bitmap
     */
    public static final Bitmap getBitmapScale(Bitmap bitmap, int newWidth,
                                              int newHeight) {
        if (null == bitmap || newWidth <= 0 || newHeight <= 0) {
            return null;
        }
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(newWidth / (float) bitmapWidth, newHeight
                / (float) bitmapHeight);
        Bitmap resizedBitmap = bitmap;
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight,
                matrix, true);
        if (bitmap != resizedBitmap) {
            resizedBitmap.recycle();
            resizedBitmap = null;
        } else {
            LogUtil.defaultLog("------------ the same bitmap address");
        }
        return bitmap;
    }

    /**
     *
     * @Description
     *              图片缩放,会返回(Width*newWidth)*(Height*newHeight)大小的图片，会将传入的bitmap进行recycle
     *              ，newWidth、newHeight参数必须大于0
     * @param bitmap
     *            需要缩放的图片
     * @param scaleX
     *            横向缩放
     * @param scaleY
     *            纵向缩放
     * @return bitmap
     */
    public static final Bitmap getBitmapScale(Bitmap bitmap, float scaleX,
                                              float scaleY) {
        if (null == bitmap || scaleX <= 0f || scaleY <= 0f) {
            return null;
        }
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scaleX, scaleY);
        Bitmap resizedBitmap = bitmap;
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight,
                matrix, false);
        if (bitmap != resizedBitmap) {
            resizedBitmap.recycle();
            resizedBitmap = null;
        } else {
            LogUtil.defaultLog("------------ the same bitmap address");
        }
        return bitmap;
    }

    /**
     * @Description
     *              图片缩放,会返回newWidth*newHeight大小的图片，不会将传入的bitmap进行recycle，参数newWidth
     *              、newHeight参数必须大于0
     *
     * @param bitmap
     *            需要缩放的图片
     * @param newWidth
     *            图片新宽度
     * @param newHeight
     *            图片新高度
     * @return bitmap
     */
    public static final Bitmap getBitmapScaleNoRecycle(Bitmap bitmap,
                                                       int newWidth, int newHeight) {
        if (null == bitmap || newWidth <= 0 || newWidth <= 0) {
            return null;
        }
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(newWidth / (float) bitmapWidth, newHeight
                / (float) bitmapHeight);
        if (!bitmap.isRecycled()) {

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth,
                    bitmapHeight, matrix, true);
            return bitmap;
        }
        return null;
    }

    /**
     * @Description 图片缩放,会返回(Width*newWidt)*(Height*newHeight)大小的图片，
     *              不会将传入的bitmap进行recycle，参数newWidth、newHeight参数必须大于0
     *
     * @param bitmap
     *            需要缩放的图片
     * @param scaleX
     *            横向缩放
     * @param scaleY
     *            纵向缩放
     * @return bitmap
     */
    public static final Bitmap getBitmapScaleNoRecycle(Bitmap bitmap,
                                                       float scaleX, float scaleY) {
        if (null == bitmap || scaleX <= 0 || scaleY <= 0) {
            return null;
        }

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scaleX, scaleY);

        if (!bitmap.isRecycled()) {
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth,
                    bitmapHeight, matrix, false);
            return bitmap;
        }
        return null;
    }

    /**
     * @Description 两张一样大小的图片合并成一张图片,从左上角开始合成，如果不一样大小，会按照上面的bitmap大小尺寸进行合成
     *
     * @param bmp1
     *            在底下的图片
     * @param bmp2
     *            在上层的图片
     * @return 合成后的图片
     */
    public static final Bitmap merge(Bitmap bmp1, Bitmap bmp2) {
        if (null == bmp1 || null == bmp2) {
            return null;
        }
        Bitmap bmOverlay = Bitmap.createBitmap(bmp2.getWidth(),
                bmp2.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOverlay);

        float x = (bmp2.getWidth() - bmp1.getWidth()) / 2f;
        float y = (bmp2.getHeight() - bmp1.getHeight()) / 2f;

        x = x > 0 ? x : 0;
        y = y > 0 ? y : 0;

        canvas.drawBitmap(bmp1, x, y, paint_comm);
        canvas.drawBitmap(bmp2, 0, 0, paint_comm);
        return bmOverlay;
    }

    /**
     * @Description 将bitmap切成圆形图
     *
     * @param bitmap
     * @return Bitmap
     */
    public static final Bitmap screenshotCycle(Bitmap bitmap) {
        if (null != bitmap) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            /**
             * Rect src：是对图片进行裁截，若是空null则显示整个图片,
             * RectF dst：是图片在Canvas画布中显示的位置区域.
             *
             * */
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());//矩形对象
            final RectF rectF = new RectF(rect);//矩形对象,与上面那个的区别是精度不同 这是float类型.
            final float roundPx = bitmap.getWidth() / 2;//位图宽度作为半径
            paint.setAntiAlias(true);//抗锯齿
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);//绘制圆角矩形,当x方向半径与y方向半径一直,绘制出来的就是圆形
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));//设置两层绘制的混合模式,取两层绘制的交集,即是在圆形包含于矩形的交集,最终交集就是圆形
            canvas.drawBitmap(bitmap, rect, rect, paint);//绘制出一个传入的bitmap图像与上面圆形的交集

            return output;
        }
        return null;
    }

    /**
     * @Description 将bitmap按照传入的弧度切成圆形图
     *
     * @param bitmap
     * @return Bitmap
     */
    public static final Bitmap screenshotRoundedPic(Bitmap bitmap, float radian) {
        if (null != bitmap) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            // final float roundPx = bitmap.getWidth() / 2;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, radian, radian, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        }
        return null;
    }

    /**
     * @Description 将图片周围用透明的像素补全成正方形
     *
     * @param bitmap
     * @return Bitmap
     */
    public static final Bitmap complementedBitmapByAlpha(Bitmap bitmap) {
        if (null == bitmap) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width == height) {
            return bitmap;
        }

        int len = width > height ? width : height;
        Bitmap output = Bitmap.createBitmap(len, len, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAlpha(0);
        canvas.drawPaint(paint);

        if (width > height) {
            int devide = (width - height) / 2;
            canvas.drawBitmap(bitmap, 0, devide, paint_comm);
        } else {
            int devide = (height - width) / 2;
            canvas.drawBitmap(bitmap, devide, 0, paint_comm);
        }
        return output;
    }

    /**
     * @Description 将图片周围用传入的透明颜色值像素补全成正方形
     * @param bitmap
     * @param color
     *            传入的透明颜色值
     * @return Bitmap
     */
    public static final Bitmap complementedBitmapByColor(Bitmap bitmap,
                                                         int color) {
        if (null == bitmap) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width == height) {
            return bitmap;
        }

        int len = width > height ? width : height;
        Bitmap output = Bitmap.createBitmap(len, len, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // paint.setAlpha(0);
        paint.setColor(color);
        canvas.drawPaint(paint);

        if (width > height) {
            int devide = (width - height) / 2;
            canvas.drawBitmap(bitmap, 0, devide, paint_comm);
        } else {
            int devide = (height - width) / 2;
            canvas.drawBitmap(bitmap, devide, 0, paint_comm);
        }
        return output;
    }

    /**
     * @Description 已最省内存的方式读取图片资源文件，但这样读出来的图片是图片的原始大小，不会根据机器的分辨率来自动适应。
     *
     * @param context
     * @param resId
     * @return Bitmap
     */
    public static final Bitmap readBitmapFromResWithLowerMemory(
            Context context, int resId) {
        return readBitmapFromResByOptions(context, resId, getOptions());
    }

    /**
     * @Description 以最省内存的方式读取图片资源文件通过指定的配置项。
     *              尽量不要使用setImageBitmap或setImageResource或BitmapFactory
     *              .decodeResource()方法，最终都会通过Java层的createBitmap方法，则会消耗更多的内存。
     *              但是这种方式读出来的图片大小与原始图片大小是一样的，与在res下的位置无关。
     *              如果要与Bitmapfactory.decodeFile读出图片大小一致
     *              ，则需要对读出的图片做缩放＝getResources().getDisplayMetrics().densityDpi
     *              </br> 文件所属drawable下的dpi drawable、drawable-mdpi为160dpi
     *              ，drawable-ldpi为120dpi,drawable－hdpi为240dpi,drawable为320dpi
     *
     * @param context
     * @param resId
     *            本地资源的id
     * @param options
     * @return Bitmap
     */
    public static final Bitmap readBitmapFromResByOptions(Context context,
                                                          int resId, BitmapFactory.Options options) {
        return BitmapFactory.decodeStream(context.getResources()
                .openRawResource(resId), null, options);
    }

    /**
     * 获取默认的配置项
     *
     * @return
     */
    private static BitmapFactory.Options getOptions() {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPurgeable = true;// true 内存不足的情况下可被回收，再次访问的时候，可被重新创建。
        opt.inInputShareable = true;// 是否深拷贝
        return opt;
    }

    /**
     * @Description
     *              以最省内存的方式读取图片资源文件，不带alpha通道。较之readBitmapFromResWithLowerMemory更省内存
     *              ， 但是读出的图片无透明通道,适合背景图片。
     *
     * @param context
     * @param resId
     *            本地资源的id
     * @return Bitmap
     */
    public static final Bitmap readBitmapNoAlphaFromResWithLowerMemory(
            Context context, int resId) {
        BitmapFactory.Options opt = getOptions();
        /**
         * ALPHA_8 8位位图 ARGB_4444 4＋4+4+4＝ 16 位位图 ARGB_8888 8+8+8+8 ＝ 32 位位图
         * RGB_565 5+6+5 ＝ 16 位位图
         */
        opt.inPreferredConfig = Config.RGB_565;
        return readBitmapFromResByOptions(context, resId, opt);
    }

    /**
     * @Description 以最省内存的方式读取图片资源文件,携带缩放比例inSampleSize，
     * @param context
     * @param resId
     *            本地资源的id
     * @param inSampleSize
     *            缩放比例
     * @return Bitmap
     */
    public static final Bitmap readBitmapFromResWithSampleSize(Context context,
                                                               int resId, int inSampleSize) {
        BitmapFactory.Options opt = getOptions();
        opt.inSampleSize = inSampleSize;
        return readBitmapFromResByOptions(context, resId, opt);
    }

    /**
     * @Description 最省内存的方式读取本地资源的图片
     * @param context
     * @param resId
     *            本地资源的id
     * @return Bitmap
     */
    public static Bitmap getResIdBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /***
     * @Description 设置图片倒影生成带倒影的bitmap，不会将传入的bitmap进行recycle
     * @param originalBitmap
     * @param spacing
     *            原图与倒影的间距
     * @return Bitmap
     */
    public static final Bitmap createReflectedImage(Bitmap originalBitmap,
                                                    int spacing) {
        // 图片与倒影间隔距离
        final int reflectionGap = spacing;
        // 图片的宽度
        int width = originalBitmap.getWidth();
        // 图片的高度
        int height = originalBitmap.getHeight();

        Matrix matrix = new Matrix();
        // 图片缩放，x轴变为原来的1倍，y轴为-1倍,实现图片的反转
        matrix.preScale(1, -1);
        // 创建反转后的图片Bitmap对象，图片高是原图的一半。
        Bitmap reflectionBitmap = Bitmap.createBitmap(originalBitmap, 0,
                height / 2, width, height / 2, matrix, false);
        // 创建标准的Bitmap对象，宽和原图一致，高是原图的1.5倍。
        Bitmap withReflectionBitmap = Bitmap.createBitmap(width, (height
                + height / 2 + reflectionGap), Config.ARGB_8888);

        // 构造函数传入Bitmap对象，为了在图片上画图
        Canvas canvas = new Canvas(withReflectionBitmap);
        // 画原始图片
        canvas.drawBitmap(originalBitmap, 0, 0, null);

        // 画间隔矩形
        Paint defaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);

        // 画倒影图片
        canvas.drawBitmap(reflectionBitmap, 0, height + reflectionGap, null);

        // 实现倒影效果
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                originalBitmap.getHeight(), 0,
                withReflectionBitmap.getHeight(), 0x70ffffff, 0x00ffffff,
                TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

        // 覆盖效果
        canvas.drawRect(0, height, width, withReflectionBitmap.getHeight(),
                paint);

        return withReflectionBitmap;
    }

    /**
     * drawable转bitmap
     * @param context
     * @param d
     * @return
     */
    public static Bitmap drawable2Bitmap(Context context, int d) {
        return BitmapFactory.decodeResource(context.getResources(), d);
    }


    static int binarySearch(int[] srcArray, int des, boolean higher) {

        int low = 0;
        int high = srcArray.length - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (des == srcArray[middle]) {
                return middle;
            } else if (des < srcArray[middle]) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        if (high < 0)
            return 0;
        if (higher) {
            if (des > srcArray[high] && high + 1 <= srcArray.length - 1) {
                high = high + 1;
            }
        } else {
            if (des < srcArray[high] && high - 1 >= 0)
                high = high - 1;
        }
        return high;
    }

    private static final int[] URL_SIZE = {16, 20, 30, 40, 50, 60, 70, 80, 90, 100,
            120, 160, 200, 300, 400, 500, 600};

    public static String onDecide(String originUrl, int width) {
        if (TextUtils.isEmpty(originUrl)) {
            return "";
        }
        int index = binarySearch(URL_SIZE, width, true);
        return originUrl + "-w" + URL_SIZE[index];
    }

}
