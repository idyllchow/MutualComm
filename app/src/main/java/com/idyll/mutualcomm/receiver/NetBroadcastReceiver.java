package com.idyll.mutualcomm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sponia.foundationmoudle.utils.NetUtil;

import java.util.ArrayList;

public class NetBroadcastReceiver extends BroadcastReceiver {
    private static ArrayList<NetEventHandler> mListeners = new ArrayList<NetEventHandler>();
    public static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NET_CHANGE_ACTION)) {

            if (mListeners.size() > 0)// 通知接口完成加载
                for (NetEventHandler handler : mListeners) {
                    handler.onNetChange(NetUtil.getNetworkState(context));
                }
        }
    }

    public void addNetEventHandler(NetEventHandler handler) {
        if (!mListeners.contains(handler)) {
            mListeners.add(handler);
        }
    }

    public void removeNetEventHandler(NetEventHandler handler) {
        mListeners.remove(handler);
    }

    public interface NetEventHandler {
        void onNetChange(int state);
    }
}