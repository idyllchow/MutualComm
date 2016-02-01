package com.idyll.mutualcomm.comm;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.sponia.foundationmoudle.common.Common;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.comm
 * @description
 * @date 16/1/25
 */
public class MCConstants {

    /**
     * 全局context
     */
    public static Context application = Common.application;

    private static DisplayMetrics metrics = new DisplayMetrics();

    /**
     * 场上最多12人
     */
    public static final int MAX_ONFIELD_TWELVE = 12;
    /**
     * 场上最多9人
     */
    public static final int MAX_ONFIELD_NINE = 9;

    public interface TeamData {
        String TOTAL_PLAYERS = "total_players";
        String FIELD_PLAYERS = "field_players";
        String MATCH_ID = "match_id";
        String TEAM_ID = "team_id";
    }

    /**
     * 传递事件类型
     */
    public interface EventType {
        /**
         * 球员上场事件
         */
        int EVENT_ENTERTHEPITCH = 1;
        /**
         * 球员下场事件
         */
        int EVENT_LEAVETHEPITCH = 2;
        /**
         * 比赛进程事件
         */
        int EVENT_PROCESS = 3;
        /**
         * 比赛具体动作事件
         */
        int EVENT_ACTION = 4;
    }

    public interface EditEventType {
        /**
         * 此版只记录射门区域
         */
        int LOCATION_EVENT = 0;
        /**
         * 射门方式
         */
        int MODE_EVENT = 1;
        /**
         * 有否助攻
         */
        int ASSIST_EVENT = 2;
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) application
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getScreenWidth() {

        WindowManager windowManager = (WindowManager) application
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

}
