package com.sponia.foundationmoudle.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.sponia.foundationmoudle.common.Common;

/**
 * @author shibo
 * @packageName com.sponia.foundationmoudle.utils
 * @description 像素密度类
 * @date 15/9/24
 */
public class DensityUtil {
    private final static DisplayMetrics dm = Common.application.getResources().getDisplayMetrics();

    public final static float density = dm.density;

    public final static float scaledDensity = dm.scaledDensity;

    public final static int widthPixels = dm.widthPixels;

    public final static int heightPixels = dm.heightPixels;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        return (int) (dpValue * density);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / density);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        return (int) (pxValue / scaledDensity);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        return (int) (spValue * scaledDensity);
    }

    /**获取屏幕宽度*/
    public static int getWindowWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
    /**获取屏幕高度*/
    public static int getWindowHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
