package com.idyll.mutualcomm.comm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * @packageName com.sponia.soccerstats.utils
 * @description
 * @date 15/9/9
 * @auther shibo
 */
public class SpCode {

    public interface IDefault {

        SharedPreferences SP = getDefaultSpInstance();

        /**
         * token
         */
        String TOKEN = "token";

        /**
         * 统计员ID
         */
        String STATS_ID = "stats_od";

        /**
         * 用户头像路径
         */
        String USER_HEAD_PATH = "user_head_path";

        /**
         * 七牛用户头像token
         */
        String USER_HEAD_TOKEN = "user_head_token";

        /**
         * 七牛用户头像host
         */
        String USER_HEAD_HOST = "user_head_host";

        /**
         * 是否隐藏侧滑栏
         */
        String IS_HIDE_DRAWER = "is_hide_drawer";
    }

    /**
     * 比赛状态
     */
    public interface MatchProgress {
        /**
         * 比赛进程
         */
        String MATCH_PROCESS = "matchProcess";
        /**
         * 初始时间
         */
        String MATCH_TIME = "matchTime";
        /**
         * 加时下开始时间
         */
        String FIRST_TIME = "firstTime";
        /**
         * 加时下开始时间
         */
        String SECOND_TIME = "secondTime";
        /**
         * 加时下开始时间
         */
        String EXTRAL_FIRST_TIME = "extralFirstTime";
        /**
         * 加时下开始时间
         */
        String EXTRAL_SECOND_TIME = "extralSecondTime";
        /**
         * 是否开始比赛
         */
        String IS_PLAYING = "isPlaying";
        /**
         * 是否有加时赛
         */
        String HAS_EXTRA_TIME = "hasExtraTime";
        /**
         * 是否有点球
         */
        String HAS_PENALTY = "hasPenalty";
        /**
         * 离开统计页面的时间
         */
        String LEAVE_STATS_TIME = "leaveStatsTime";
    }

    /**
     * 比赛阵容
     */
    public static String MATCH_LINEUPS = "matchLineups";
    public static String SOCKET_ID = "socket_id";
    public static String SOCKET_ADD = "socket_add";

    private static SharedPreferences defaultSP = null;

    public static synchronized SharedPreferences getDefaultSpInstance() {
        if (null == defaultSP) {
            defaultSP = PreferenceManager
                    .getDefaultSharedPreferences(MCConstants.application);
        }
        return defaultSP;
    }

}
