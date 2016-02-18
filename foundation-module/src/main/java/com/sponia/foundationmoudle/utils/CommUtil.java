package com.sponia.foundationmoudle.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.sponia.foundationmoudle.common.Common;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @author shibo
 * @packageName com.sponia.foundationmoudle.utils
 * @description 获取应用基本信息类
 * @date 15/9/24
 */
public class CommUtil {

    /**
     * 获取已完整安装签名信息
     *
     * @return
     */
    public static String getSignInfo(Context context) {

        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            return parseSignature(sign.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String parseSignature(byte[] signature) {

        try {
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String signNumber = cert.getSerialNumber().toString();
            return getMd5UpperCase(signNumber
                    + cert.getSubjectDN().toString());
        } catch (CertificateException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMd5UpperCase(String string) {
        byte[] hash;
        if (null == string) {
            return null;
        }
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString().toUpperCase();
    }

    /**
     * 隐藏系统键盘
     */
    public static void hideSyskeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) Common.application
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
