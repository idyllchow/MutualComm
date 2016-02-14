package com.idyll.mutualcomm.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.idyll.mutualcomm.comm.MCConstants;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.utils
 * @description 设备信息
 * @date 16/2/2
 */
public class DeviceUtil {

    /**
     * 获取手机IMEI号 需要权限：READ_PHONE_STATE
     *
     * @return
     */
    public static String getIMEI() {
        String imei = null;
        imei = ((TelephonyManager) MCConstants.application
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        return imei;

    }

    /**
     * 获取手机IMSI号 需要权限：READ_PHONE_STATE
     *
     * @return
     */
    public static String getIMSI() {
        String imsi = ((TelephonyManager) MCConstants.application
                .getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        return imsi;
    }

    /**
     * 获取手机ICCID号 需要权限：READ_PHONE_STATE
     *
     * @return
     */
    public static String getICCID() {
        return ((TelephonyManager) MCConstants.application
                .getSystemService(Context.TELEPHONY_SERVICE))
                .getSimSerialNumber();
    }

    /**
     * 获取手机号 需要权限：READ_PHONE_STATE
     *
     * @return
     */
    public static String getMSISDN() {
        return ((TelephonyManager) MCConstants.application
                .getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
    }

    /**
     * wifi-SSID 需要权限：ACCESS_WIFI_STATE
     *
     * @return
     */
    public static String getSSID() {
        WifiManager wifiManager = (WifiManager) MCConstants.application
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    /**
     * wifi-BSSID 需要权限：ACCESS_WIFI_STATE
     *
     * @return
     */
    public static String getBSSID() {
        WifiManager wifiManager = (WifiManager) MCConstants.application
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getBSSID();
    }

    /**
     * mac 地址 需要权限：ACCESS_WIFI_STATE
     *
     * @return
     */
    public static String getWifiMacAddress() {
        WifiManager wifi = (WifiManager) MCConstants.application
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }
}
