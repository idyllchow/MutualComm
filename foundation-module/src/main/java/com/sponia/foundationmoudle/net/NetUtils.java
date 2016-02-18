package com.sponia.foundationmoudle.net;

import android.text.TextUtils;

import com.sponia.foundationmoudle.utils.CellphoneUtil;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map.Entry;


/**
 * com.sponia.stats.net
 * 网络相关工具类
 * 15/9/7
 * shibo
 */
public class NetUtils {

    /**
     * 将map形式的url参数拼接成url中的参数形式
     *
     * @param params
     * @return
     */
    public static String handlerURLParams(HashMap<String, String> params) {
        if (params == null || params.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : params.entrySet()) {
            String value = entry.getValue();
            if (TextUtils.isEmpty(value)) {
                value = "";
            }
            sb.append(entry.getKey() + "=" + value + "&");
        }
        if (sb.length() > 0) {//删除最后一个&符号
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 根据当前的网络状态，获取代理
     *
     * @return
     */
    public static final Proxy getProxy() {
        Proxy proxy = null;
        switch (CellphoneUtil.checkNetWorkStatus()) {
            case CellphoneUtil.DIANXINWAP:
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.200", 80));
                break;
            case CellphoneUtil.LIANTONGWAP:
            case CellphoneUtil.YIDONGWAP:
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
                break;
            case CellphoneUtil.WIFI:
                String proHost = android.net.Proxy.getDefaultHost();
                int proPort = android.net.Proxy.getDefaultPort();
                if (proHost != null && proPort != -1) {  //设置了代理
                    InetSocketAddress inetAddress = new InetSocketAddress(proHost, proPort);
                    proxy = new Proxy(Proxy.Type.HTTP, inetAddress);
                }
                break;
        }
        return proxy;
    }

}
