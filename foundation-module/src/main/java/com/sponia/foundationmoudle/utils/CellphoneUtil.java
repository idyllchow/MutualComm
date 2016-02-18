package com.sponia.foundationmoudle.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.sponia.foundationmoudle.common.Common;

import java.io.File;

/**
 * @author shibo
 * @packageName com.sponia.stats.utils
 * @description 手机相关信息检查
 * 提供了网络状态的检查与SD卡的检查
 * 需要判断网络连接权限android.permission.ACCESS_NETWORK_STATE
 * @date 15/10/14
 */
public class CellphoneUtil {

    public static final int LIANTONGWAP = 0x0001;// 联通
    public static final int DIANXINWAP = 0x0002;// 电信
    public static final int YIDONGWAP = 0x0003;// 移动
    public static final int WIFI = 0x0004;

    /**
     * 检查网络状态是否可用(非蓝牙)
     *
     * @return true 可用 false不可用
     */
    public static boolean checkNetWorkAvailable() {
        ConnectivityManager mConenctivity = (ConnectivityManager) Common.application
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConenctivity.getActiveNetworkInfo();
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    /**
     * 检查网络状态 判断网络WAP代理专用
     *
     * @return -1为网络不可用，其他为联通，移动，电信，WIFI四种状态
     */
    @Deprecated
    public static int checkNetWorkStatus() {
        ConnectivityManager mConenctivity = (ConnectivityManager) Common.application.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConenctivity.getActiveNetworkInfo();
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                final String netType = info.getExtraInfo();
                if (netType.contains("wap")) {
                    if (netType.contains("uniwap")) {
                        return LIANTONGWAP;
                    } else if (netType.contains("ctwap")) {
                        return DIANXINWAP;
                    } else {
                        return YIDONGWAP;
                    }
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return WIFI;
            }
        }
        return -1;
    }

    /**
     * 判断网络制式
     *
     * @return 网络制式
     */
    public static String checkNetWorkStatusV1() {
        if (checkNetWorkAvailable()) {
            ConnectivityManager mConenctivity = (ConnectivityManager) Common.application
                   .getSystemService(
                            Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = mConenctivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // 判断为是手机网络,返回手机网络具体制式
                    final String netType = info.getExtraInfo();
                    return netType;
                } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    // 判断为是WIFI网络,返回WIFI网络制式
                    final String netType = info.getTypeName();
                    return netType;
                }
            }
        }
        return null;
    }


    /**
     * @功能：获取保存路径
     */
    public static String getImagePath(Context context){
        String imageDirString = FileUtil.getStorageDirectory() + File.separator + "images";

        File imageDir = new File(imageDirString);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        return imageDirString;
    }


    /**
     * @功能：删除图片
     */
    public static boolean delete(String fName) {
        File f = new File(fName);
        if (f.exists()) {
            return f.delete();
        } else {
            return false;
        }
    }

    /**
     * @功能： 判断sdcard的状态
     */
    public static boolean isCardExist() {
        String status = Environment.getExternalStorageState();
        //SD已经挂载,可以使用
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else if (status.equals(Environment.MEDIA_REMOVED)) {
            //SD卡已经已经移除
            LogUtil.defaultLog("SD卡已经移除或不存在");
            return false;

        } else if (status.equals(Environment.MEDIA_SHARED)) {
            //SD卡正在使用中
            LogUtil.defaultLog("SD卡正在使用中");
            return false;

        } else if (status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            //SD卡只能读，不能写
            LogUtil.defaultLog("SD卡只能读，不能写");
            return false;

        } else {
            //SD卡的其它情况
            LogUtil.defaultLog("SD卡已经移除或不存在");
            return false;
        }
    }

    /**
     * 获取SD卡路径
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        if (isCardExist()) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            return sdDir.toString();
        }
        return null;
    }

}
