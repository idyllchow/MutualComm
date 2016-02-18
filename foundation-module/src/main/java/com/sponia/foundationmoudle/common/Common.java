package com.sponia.foundationmoudle.common;

import android.app.Application;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

/**
 * @author shibo
 * @packageName com.sponia.foundationmoudle.common
 * @description 通用常量
 * @date 15/9/29
 */
public final class Common {

    /**
     * 全局context
     */
    public static Application application;

    /**
     * 当前屏幕宽度
     */
    private static int screenWidth = -1;

    /**
     * 当前屏幕密度
     */
    private static int densityDip = -1;

    /**
     * 当前屏幕高度
     */
    private static int screenHeight = -1;

    /**
     * 当前版本状态
     */
    private static boolean isRelease = false;

    /**
     * 记录是否已经设置过当前版本状态
     */
    private static boolean isSetReleaseFlag = false;
    /***
     * 版本号
     */
    private static String ver;
    /***
     * 屏幕密度
     */
    private static float density;

    /**
     * 设置为Debug版本
     * 仅允许设置一次
     */
    public static void setVersionToDebug() {
        if (!isSetReleaseFlag) {
            isRelease = false;
            isSetReleaseFlag = true;
        }
    }

    /**
     * 设置为Release版本
     * 仅允许设置一次
     */
    public static void setVersionToRelease() {
        if (!isSetReleaseFlag) {
            isRelease = true;
            isSetReleaseFlag = true;
        }
    }

    /**
     * 判断是否为Debug版本
     *
     * @return
     */
    public static boolean isDebugVersion() {
        return !isRelease;
    }

    /**
     * 判断是否为Release版本
     *
     * @return
     */
    public static boolean isReleaseVersion() {
        return isRelease;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        if (screenHeight < 0) {
            initScreenSize();
        }
        return screenHeight;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        if (screenWidth < 0) {
            initScreenSize();
        }
        return screenWidth;
    }

    public static int getDensityDip() {
        if (densityDip < 0) {
            initScreenSize();
        }
        return densityDip;
    }

    public static float getDensity() {
        return density;
    }

    /**
     * 获取versionCode
     * @return
     */
    public static String getAppVersionCode() {
        String versionCode = "";
        try {
            versionCode = Common.application.getPackageManager().getPackageInfo(Common.application.getPackageName(), 0).versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取versionName
     * @return
     */
    public static String getAppVersionName() {
        String versionName = "";
        try {
            versionName = Common.application.getPackageManager().getPackageInfo(Common.application.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 初始化屏幕尺寸
     */
    private static void initScreenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = application.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        densityDip = dm.densityDpi;
        density = dm.density;
    }
}

