package com.idyll.mutualcomm.event;

import android.text.TextUtils;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.event
 * @description 事件助手类
 * @date 16/2/3
 */
public class EventHelper {

    /**
     * 获取事件描述
     * @param code
     * @param playerNum
     * @return
     */
    public static final String getEventDes(int code, String playerNum) {
        if (!TextUtils.isEmpty(playerNum) && (code < EventCode.Ctrl || code >= EventCode.penaltyShotOnTarget)) { //有球员事件
            return playerNum + "号" + EventCode.sEventNameMap.get(code);
        } else if (code >= EventCode.Begin && code <= EventCode.Finish) {
            return EventCode.sEventNameMap.get(code);
        } else {
            return "";
        }
    }

}
