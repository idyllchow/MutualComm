package com.idyll.mutualcomm.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.global.MCConstants;
import com.idyll.mutualcomm.global.SpCode;
import com.idyll.mutualcomm.entity.StatsMatchFormationBean;
import com.idyll.mutualcomm.event.EventCode;
import com.idyll.mutualcomm.event.RecordFactory;
import com.idyll.mutualcomm.fragment.statistics.StatsMatchFragment;
import com.idyll.mutualcomm.fragment.statistics.StatsTransitionFragment;
import com.idyll.mutualcomm.talk.GroupTalkHelper;
import com.idyll.mutualcomm.utils.AnimationUtil;
import com.idyll.mutualcomm.view.PopTipView.PopTipRelativeLayout;
import com.idyll.mutualcomm.view.PopTipView.PopTipView;
import com.sponia.foundationmoudle.BaseActivity;
import com.sponia.foundationmoudle.net.NetMessage;
import com.sponia.foundationmoudle.utils.CellphoneUtil;
import com.sponia.foundationmoudle.utils.CollectionUtil;
import com.sponia.foundationmoudle.utils.DensityUtil;
import com.sponia.foundationmoudle.utils.LogUtil;
import com.sponia.foundationmoudle.utils.SponiaSpUtil;
import com.sponia.foundationmoudle.utils.SponiaToastUtil;
import com.sponia.foundationmoudle.utils.TimeUtil;
import com.sponia.foundationmoudle.view.sweetalert.ProgressHelper;
import com.sponia.foundationmoudle.view.sweetalert.ProgressWheel;
import com.sponia.foundationmoudle.view.sweetalert.SweetAlertDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * @packageName com.sponia.soccerstats.activities
 * @description 统计操作类
 * @date 15/9/11
 * @auther shibo
 */
public class StatsOperateActivity extends BaseActivity {

    private static final String MATCH_FRAGMENT = "StatsMatchFragment";
    private static final String TRANSITION_FRAGMENT = "StatsTransitionFragment";
    private StatsMatchFragment mMatchFragment;
    private StatsTransitionFragment mTransitionFragment;
    private FragmentTransaction fragmentTransaction;
    //比赛matchId
    private String matchId;
    //球队ID
    private String teamId;
    //title上显示事件
//    private TextView mEventTv;
    //显示在titlebar上事件名
    private String mTitleState;
    //导出和登pw
    private PopupWindow mSettingPopupWindow;
    //保持手机恒亮
    private PowerManager.WakeLock mWakeLock;
    //球队阵容
    private ArrayList<StatsMatchFormationBean> playerList = new ArrayList<>();
    //比赛时间
//    private TextView tvMatchTime;
    //比赛计时
    private static final int MSG_MATCH_TIMING = 0x0001;
    //事件间隔1s
    private static final int INTERVAL_TIME = 1000;
    //比赛时间timer
    private final Timer timer = new Timer();
    //比赛时间task
    private TimerTask task = null;
    //是否开始比赛
    private boolean isPlaying = false;
    //初始时间
    private long matchTime = 0;
    //task运行
    private boolean taskAlready = false;
    //统计模式
    private int statsMode;
    //比赛几人制
    private int matchType;
    //顶部弹出框
    private PopTipRelativeLayout ptrlyMatchProgress;
    //弹出框视图
    private PopTipView tipView;
    //比赛进程
    private int matchProcess;
    //是否有加时赛
    private boolean hasExtraTime = false;
    //是否有点球大战
    private boolean hasPenalty = false;
    //离开统计页面时的时间
    private long leaveStatsTime = 0l;
    //换人之前场上球员,用作换人逻辑
    private ArrayList<StatsMatchFormationBean> lastOnField = new ArrayList<>();
    //存比赛进程对应时间,供撤销时用
    private HashMap<Integer, Long> timeProcessMap = new HashMap<>();
    //上半场开始时时间
    private long firstTime;
    //下半场开始时时间
    private long secondTime;
    //加时上开始时时间
    private long extralFirstTime;
    //加时下开始时时间
    private long extralSecondTime;
    //缓存文件名
    private String spName;
    //撤销事件编码
    private int eventCode = -1;
    //常规半场时间
    private int halfTime;
    //加时半场时间
    private int extraTime;
    //是否点球大战
    private boolean isPenalty = false;
    //所有球员
    ArrayList<StatsMatchFormationBean> allList = new ArrayList<>();
    //场上球员
    ArrayList<StatsMatchFormationBean> onFieldsList = new ArrayList<>();
    private FrameLayout flyLayout;
    //loading pw
    private PopupWindow pw;
    //比赛阵容
    private static final String TAG_GET_MATCH_FORMATION = "TAG_GET_MATCH_FORMATION";
    //比赛阵容获取成功
    private static final int MSG_GET_MATCH_FORMATION_SUCCESS = 0x0003;
    //比赛阵容获取失败
    private static final int MSG_GET_MATCH_FORMATION_FAILED = 0x0004;
    private static final int MSG_SOCKET_INIT = 0x0005;
    //球队阵容
    private ArrayList<StatsMatchFormationBean> matchList = new ArrayList();
    //取消刷新
    private boolean cancelRefresh = false;
    //换人
    private boolean ensurePlayer = false;
    //服务器与本地时间差
    private long timeDiff = 0;

    private boolean showSetting = false;
    private boolean showRefresh = false;
    private boolean showCancel = false;
    private boolean showTrans = false;
    private boolean showDone = false;

    private Socket mSocket;
    private boolean isStartRecieveMsg;
    private BufferedReader mReader;
    private BufferedWriter mWriter;
    private String ip;
    private int port;

    private OperateHandler handler;
    private TextView tv_stats_title1;
    private TextView tv_stats_title2;
    /**
     * 上一次事件描述
     */
    private String preEventDes = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMidView(R.layout.activity_stats_operate);
        initUI();
        initData(savedInstanceState);
        addMatchTimeView();
        lastOnField.clear();
        lastOnField.addAll(onFieldsList);
        //存最近场上球员 防止换人时数据被清除
        SponiaSpUtil.setDefaultSpValue(SpCode.MATCH_LINEUPS, lastOnField);
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        flyLayout = (FrameLayout) findViewById(R.id.fly_layout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setActionBarBackground(R.color.black);
        initTitleMiddleView();
        handler = new OperateHandler();
    }

    /**
     * 初始化数据
     */
    private void initData(Bundle savedInstanceState) {
        matchProcess = EventCode.Begin;
        ptrlyMatchProgress = (PopTipRelativeLayout) findViewById(R.id.ptrly_match_progress);
        statsMode = getIntent().getIntExtra("statsMode", 1);
        matchId = getIntent().getStringExtra("matchId");
        teamId = getIntent().getStringExtra("teamId");
        halfTime = getIntent().getIntExtra("halfTime", 45);
        extraTime = getIntent().getIntExtra("extraTime", 15);
        timeDiff = getIntent().getLongExtra("timeDiff", 0);
        playerList = getIntent().getParcelableArrayListExtra("playerList");
        //matchType确定场上最多人数
        matchType = getIntent().getIntExtra("matchType", 9);
        ip = getIntent().getStringExtra("ip");
        port = getIntent().getIntExtra("port", 7000);
        spName = teamId + matchId;
        initSocket();
        //只有第一次进入统计页面中时才会从SharedPreferences中读取数据，若从后台切换到前台则从内存恢复数据(在各自Fragment中完成)
        restoreMatchProgress();
        distributionPlayer(false);

        if (null != savedInstanceState) { //activity被kill后从内存中恢复数据
            fragmentTransaction.hide(mTransitionFragment);
            mTitleState = savedInstanceState.getString("titleState");
            matchProcess = savedInstanceState.getInt("matchProcess");
            isPlaying = savedInstanceState.getBoolean("isPlaying");
            matchTime = savedInstanceState.getLong("matchTime");
            firstTime = savedInstanceState.getLong("firstTime");
            secondTime = savedInstanceState.getLong("secondTime");
            extralFirstTime = savedInstanceState.getLong("extralFirstTime");
            extralSecondTime = savedInstanceState.getLong("extralSecondTime");
            hasExtraTime = savedInstanceState.getBoolean("hasExtraTime");
            hasPenalty = savedInstanceState.getBoolean("hasPenalty");
            //如果比赛是在进行中,场上时间需要加上离开统计页面的时间
            leaveStatsTime = savedInstanceState.getLong(SpCode.MatchProgress.LEAVE_STATS_TIME);
            if (leaveStatsTime != 0) {
                leaveStatsTime = System.currentTimeMillis() - leaveStatsTime;
                matchTime = matchTime + leaveStatsTime;
            }
            onFieldsList = (ArrayList<StatsMatchFormationBean>) savedInstanceState.getSerializable("onFieldsList");
            showActionUI();
        }
        addFragment();
        if (savedInstanceState == null) {
            if (null != onFieldsList && !onFieldsList.isEmpty()) { //直接进入统计界面
                fragmentTransaction.hide(mTransitionFragment);
                showActionUI();
            } else { //换人界面
                showTransitionUI();
            }
        }
    }

    private void addFragment() {
        if (fragmentTransaction == null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
        }
        addMatchFragment();
        addSubstitutionFragment();
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void initTitleMiddleView() {
        View view = getLayoutInflater().inflate(R.layout.item_title_middle, null);
        tv_stats_title1 = (TextView) view.findViewById(R.id.tv_event_des1);
        tv_stats_title2 = (TextView) view.findViewById(R.id.tv_event_des2);
        setTitleMiddleView(view);
    }

    /**
     * 刷新事件描述
     *
     * @param eventDes
     * @param isUndo
     */
    public void updateEventDes(final String eventDes, boolean isUndo) {
        if (!TextUtils.isEmpty(eventDes)) {
            if (isUndo) {
                tv_stats_title2.clearAnimation();
                tv_stats_title2.setText(eventDes);
                tv_stats_title1.setText(preEventDes);
                preEventDes = eventDes;
                tv_stats_title1.setAnimation(AnimationUtil.transAndAlaph(-1000));
            } else {
                tv_stats_title1.clearAnimation();
                tv_stats_title1.setText(eventDes);
                tv_stats_title2.setText(preEventDes);
                preEventDes = eventDes;
                tv_stats_title2.setAnimation(AnimationUtil.transAndAlaph(1000));
            }
        }
    }


    /**
     * 分配场上场下队员
     */
    private void distributionPlayer(boolean isRefresh) {
        Map<String, Integer> onFieldMap;
        Map<String, Integer> offFieldMap;
        if (isRefresh) {
            savePlayers("OnFieldPlayers_" + matchId + "_" + teamId, mMatchFragment.getOnFieldPlayers());
            savePlayers("OffFieldPlayers_" + matchId + "_" + teamId, mTransitionFragment.getOffFieldList());
        }
        onFieldMap = restorePlayers("OnFieldPlayers_" + matchId + "_" + teamId);
        offFieldMap = restorePlayers("OffFieldPlayers_" + matchId + "_" + teamId);
        ArrayList<Integer> positionIndex = new ArrayList<>();
        int size = playerList.size();
        for (int i = 0; i < size; i++) {
            positionIndex.add(i);
        }
        //后台临时增加球员
        ArrayList<StatsMatchFormationBean> tempAddPlayer = new ArrayList<>();
        if (onFieldMap != null && onFieldMap.size() > 0 && offFieldMap != null && offFieldMap.size() > 0) {
            onFieldsList.clear();
            allList.clear();
            for (StatsMatchFormationBean player : playerList) {
                if (onFieldMap.containsKey(player.id + "")) {
                    player.onField = true;
                    player.index = onFieldMap.get(player.id + "");
                    onFieldsList.add(player);
                    allList.add(player);
                } else if (offFieldMap.containsKey(player.id + "")) {
                    player.onField = false;
                    player.index = offFieldMap.get(player.id + "");
                    int index = -1;
                    for (int i = 0; i < positionIndex.size(); i++) {
                        if (positionIndex.get(i) == (player.index)) {
                            index = i;
                            continue;
                        }
                    }
                    if (index != -1) {
                        positionIndex.remove(index);
                    }
                    allList.add(player);
                } else { //后台改球员情况
                    tempAddPlayer.add(player);
                }
            }
            //处理后台临场更换球员号码
            int sizeTemp = tempAddPlayer.size();
            for (int i = 0; i < sizeTemp; i++) {
                tempAddPlayer.get(i).onField = false;
                tempAddPlayer.get(i).index = positionIndex.get(i);
                allList.add(tempAddPlayer.get(i));
            }
        } else {
            int position = 0;
            for (StatsMatchFormationBean player : playerList) {
                player.index = position;
                position++;
            }
            allList.clear();
            allList.addAll(playerList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getName());
        }
        mWakeLock.acquire();
        if (mMatchFragment != null) { //通知球员号码背景颜色变化
            mMatchFragment.changePlayerBackground(isPlaying, false);
        }
    }

    @Override
    protected void onPause() {
        mWakeLock.release();
        super.onPause();
    }

    /**
     * 获取比赛进程
     *
     * @return
     */
    public int getMatchProcess() {
        return matchProcess;
    }

    /**
     * 获取是否点球
     *
     * @return
     */
    public boolean isPenalty() {
        return isPenalty;
    }

    /**
     * 添加比赛时间视图
     */
    private void addMatchTimeView() {
        if (matchProcess == EventCode.Begin) {
            setTitleRight1Text(getString(R.string.match_begin));
            setTitleRight1TextColor(Color.parseColor("#FFFFFF"));
            setTitleRight1Background(R.drawable.bg_op_red);
        }
        if (task != null) {
            task.cancel();
        }

        task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_MATCH_TIMING);
            }
        };
        if (!taskAlready) {
            timer.schedule(task, INTERVAL_TIME, INTERVAL_TIME);
        }
    }

    /**
     * 比赛fragment
     */
    private void addMatchFragment() {
        if (mMatchFragment == null) {
            mMatchFragment = StatsMatchFragment.getInstance(statsMode, matchType, matchId, teamId, timeDiff, onFieldsList);
            fragmentTransaction.add(R.id.content_layout, mMatchFragment, MATCH_FRAGMENT);
        }
    }

    /**
     * 换人fragment
     */
    private void addSubstitutionFragment() {
        if (mTransitionFragment == null) {
            mTransitionFragment = StatsTransitionFragment.getInstance(allList, matchType);
            fragmentTransaction.add(R.id.content_layout, mTransitionFragment, TRANSITION_FRAGMENT);
        }
    }

    class OperateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_MATCH_TIMING:
                    setMatchTime();
                    break;
                case MSG_GET_MATCH_FORMATION_SUCCESS:
                    if (ensurePlayer) {
                        showRefresh = false;
                        showCancel = false;
                    } else {
                        showRefresh = true;
                        showCancel = false;
                    }
                    if (!cancelRefresh) {
                        StatsMatchFormationBean temp = new StatsMatchFormationBean(teamId, "-1");
                        matchList.add(matchList.size(), temp);
                        hideLoadingPW();
                        playerList.clear();
                        playerList.addAll(matchList);
                        distributionPlayer(true);
                        mTransitionFragment.refreshOnFieldPlayer(playerList);
                    }
                    break;
                case MSG_GET_MATCH_FORMATION_FAILED:
                    SponiaToastUtil.showShortToast(getString(R.string.failure_get_match_info));
                    showRefresh = true;
                    showCancel = false;
                    break;
                case MSG_SOCKET_INIT:
                    try {
                        JSONObject json = new JSONObject((String) msg.obj);
//                        mConsoleStr.append(json.getString("from") + ":" + json.getString("msg") + "   " + "\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:

                    tv_stats_title1.setText((String) msg.obj);
                    tv_stats_title2.setText("");
                    break;
                default:
                    break;
            }
        }
    }

    ;

    /**
     * 根据阶段设置比赛时间
     */
    private void setMatchTime() {
        taskAlready = true;
        matchTime += INTERVAL_TIME;
        if (matchProcess == EventCode.Begin) {
            setTitleRight1Text(getString(R.string.match_begin));
        } else if (matchProcess == EventCode.KickOffFirstHalf) {
            matchTime = System.currentTimeMillis() - firstTime;
            setTitleRight1Text(getString(R.string.match_first_half) + TimeUtil.getMSTimeFormat(matchTime));
        } else if (matchProcess == EventCode.HalfTime) {
            setTitleRight1Text(getString(R.string.match_intermission));
        } else if (matchProcess == EventCode.KickOffSecondHalf) {
            matchTime = System.currentTimeMillis() - secondTime;
            setTitleRight1Text(getString(R.string.match_second_half) + TimeUtil.getMSTimeFormat(matchTime));
        } else if (matchProcess == EventCode.FullTime) {
            setTitleRight1Text(getString(R.string.match_second_half_end)); //match_second_half_end
        } else if (matchProcess == EventCode.KickOffExtraTimeFirstHalf) {
            matchTime = System.currentTimeMillis() - extralFirstTime;
            setTitleRight1Text(getString(R.string.match_extra_time_first_half) + TimeUtil.getMSTimeFormat(matchTime)); //match_extra_time_first_half
        } else if (matchProcess == EventCode.ExtraTimeHalfTime) {
            setTitleRight1Text(getString(R.string.match_extra_time_half_time)); //match_extra_time_half_time
        } else if (matchProcess == EventCode.KickOffExtraTimeSecondHalf) {
            matchTime = System.currentTimeMillis() - extralSecondTime;
            setTitleRight1Text(getString(R.string.match_extra_time_second_half) + TimeUtil.getMSTimeFormat(matchTime)); //match_extra_time_second_half
        } else if (matchProcess == EventCode.ExtraTimeFullTime) {
            setTitleRight1Text(getString(R.string.match_extra_time_full_time));
        } else if (matchProcess == EventCode.PenaltyShootOutBegin) {
            setTitleRight1Text(getString(R.string.match_penalty_shoot));  //match_penalty_shoot
        } else if (matchProcess == EventCode.Finish) {
            setTitleRight1Text(getString(R.string.match_finish)); //match_finish
        }
    }

    /**
     * 获取比赛时间
     *
     * @return
     */
    public long getMatchTime() {
        String matchTimeStr = tvRight1.getText().toString().trim();
        if (matchTimeStr.contains(getString(R.string.match_second_half).trim())) { //下半场
            return matchTime + halfTime * 60000;
        } else if (matchTimeStr.contains(getString(R.string.match_extra_time_first_half).trim())) { //加时上
            return matchTime + (halfTime * 2) * 60000;
        } else if (matchTimeStr.contains(getString(R.string.match_extra_time_second_half).trim())) { //加时下
            return matchTime + (halfTime * 2) * 60000 + extraTime * 60000;
        }
        return matchTime;
    }

    public String getShowMatchTime() {
        return tvRight1.getText().toString().trim();
    }

    /**
     * 清除计时器
     */
    private void cleanTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtil.defaultLog("Stats operate activity onSaveInstanceState isPlaying " + isPlaying + "; matchProcess: " + matchProcess);
        outState.putString("titleState", mTitleState);
        outState.putInt("matchProcess", matchProcess);
        outState.putLong("matchTime", matchTime);
        outState.putLong("firstTime", firstTime);
        outState.putLong("secondTime", secondTime);
        outState.putLong("extralFirstTime", extralFirstTime);
        outState.putLong("extralSecondTime", extralSecondTime);
        outState.putBoolean("isPlaying", isPlaying);
        outState.putBoolean("hasExtraTime", hasExtraTime);
        outState.putBoolean("hasPenalty", hasPenalty);
        if (isPlaying) {
            outState.putLong(SpCode.MatchProgress.LEAVE_STATS_TIME, System.currentTimeMillis());
        }
        saveMatchProgress();
        savePlayers("OnFieldPlayers_" + matchId + "_" + teamId, mMatchFragment.getOnFieldPlayers());
        savePlayers("OffFieldPlayers_" + matchId + "_" + teamId, mTransitionFragment.getOffFieldList());
        outState.putSerializable("onFieldsList", mMatchFragment.getOnFieldPlayers());
        super.onSaveInstanceState(outState);
    }

    /**
     * 从sp文件中读取已存比赛进度
     */
    private void restoreMatchProgress() {
        isPlaying = SponiaSpUtil.getValue(spName, SpCode.MatchProgress.IS_PLAYING, isPlaying);
        matchTime = SponiaSpUtil.getValue(spName, SpCode.MatchProgress.MATCH_TIME, matchTime);
        firstTime = SponiaSpUtil.getValue(spName, SpCode.MatchProgress.FIRST_TIME, firstTime);
        secondTime = SponiaSpUtil.getValue(spName, SpCode.MatchProgress.SECOND_TIME, secondTime);
        extralFirstTime = SponiaSpUtil.getValue(spName, SpCode.MatchProgress.EXTRAL_FIRST_TIME, extralFirstTime);
        extralSecondTime = SponiaSpUtil.getValue(spName, SpCode.MatchProgress.EXTRAL_SECOND_TIME, extralSecondTime);
        matchProcess = SponiaSpUtil.getValue(spName, SpCode.MatchProgress.MATCH_PROCESS, matchProcess);
        hasExtraTime = SponiaSpUtil.getValue(spName, SpCode.MatchProgress.HAS_EXTRA_TIME, hasExtraTime);
        hasPenalty = SponiaSpUtil.getValue(spName, SpCode.MatchProgress.HAS_PENALTY, hasPenalty);
        //如果比赛是在进行中,场上时间需要加上离开统计页面的时间
        leaveStatsTime = SponiaSpUtil.getValue(spName, SpCode.MatchProgress.LEAVE_STATS_TIME, 0l);
        if (leaveStatsTime != 0l) {
            leaveStatsTime = System.currentTimeMillis() - leaveStatsTime;
            matchTime = matchTime + leaveStatsTime;
        }
    }

    /**
     * 存比赛进度
     */
    private void saveMatchProgress() {
        if (isPlaying) {
            SponiaSpUtil.setValue(spName, SpCode.MatchProgress.LEAVE_STATS_TIME, System.currentTimeMillis());
        }
        SponiaSpUtil.setValue(spName, SpCode.MatchProgress.MATCH_TIME, matchTime);
        SponiaSpUtil.setValue(spName, SpCode.MatchProgress.FIRST_TIME, firstTime);
        SponiaSpUtil.setValue(spName, SpCode.MatchProgress.SECOND_TIME, secondTime);
        SponiaSpUtil.setValue(spName, SpCode.MatchProgress.EXTRAL_FIRST_TIME, extralFirstTime);
        SponiaSpUtil.setValue(spName, SpCode.MatchProgress.EXTRAL_SECOND_TIME, extralSecondTime);
        SponiaSpUtil.setValue(spName, SpCode.MatchProgress.IS_PLAYING, isPlaying);
        SponiaSpUtil.setValue(spName, SpCode.MatchProgress.MATCH_PROCESS, matchProcess);
        SponiaSpUtil.setValue(spName, SpCode.MatchProgress.HAS_EXTRA_TIME, hasExtraTime);
        SponiaSpUtil.setValue(spName, SpCode.MatchProgress.HAS_PENALTY, hasPenalty);
    }

    /**
     * 读取之前退出时场上数据
     *
     * @param key
     * @return
     */
    private Map<String, Integer> restorePlayers(String key) {
        String json = SponiaSpUtil.getDefaultSpValue(key, "");
        if (!TextUtils.isEmpty(json)) {
            return (Map<String, Integer>) JSON.parse(json);
        }
        return null;
    }

    /**
     * 统计过程中退出统计存储场上状态
     *
     * @param key
     * @param players
     */
    private void savePlayers(String key, List<StatsMatchFormationBean> players) {
        if (null != players && 0 != players.size()) {
            HashMap<String, Integer> map = new HashMap<>();
            for (StatsMatchFormationBean player : players) {
                map.put(player.id + "", player.index);
            }
            SponiaSpUtil.setDefaultSpValue(key, JSON.toJSONString(map, true));
        } else {
            SponiaSpUtil.setDefaultSpValue(key, "");
        }
    }

    /**
     * PW loading
     */
    private void showLoadingPW() {
        flyLayout.setVisibility(View.VISIBLE);
        flyLayout.setOnClickListener(null);
        flyLayout.setOnTouchListener(null);
        View view = LayoutInflater.from(this).inflate(R.layout.pw_loading_dialog, null);
        ProgressHelper mProgressHelper = new ProgressHelper(this);
        mProgressHelper.setProgressWheel((ProgressWheel) view.findViewById(R.id.progressWheel));
        pw = new PopupWindow(view, 500,
                400, true);
        pw.setFocusable(false);
        pw.setOutsideTouchable(true);
        pw.showAtLocation(findViewById(R.id.content_layout), Gravity.CENTER, 0, 0);
    }

    /**
     * 隐藏loading PW
     */
    private void hideLoadingPW() {
        if (pw != null && pw.isShowing()) {
            flyLayout.setVisibility(View.GONE);
            pw.dismiss();
        }
    }

    /**
     * 换人,包含开场球员上场
     */
    private void substitution() {
        if (null != mTransitionFragment && mTransitionFragment.isVisible()) {
            ArrayList<StatsMatchFormationBean> onFieldList = mTransitionFragment.getOnFieldList();

            long eventTime = System.currentTimeMillis();
            if (lastOnField.size() <= 0) { //比赛开始出场阵容
                for (StatsMatchFormationBean matchFormationBean : onFieldList) {
                    mMatchFragment.generateEvent(EventCode.EnterThePitch, MCConstants.EventType.EVENT_ENTERTHEPITCH, matchFormationBean.id, "", -1, eventTime);
                }
                lastOnField.addAll(onFieldList);
            } else { //比赛过程中换人事件,需对比球场上球员,确定变动
                ArrayList<String> onFieldPlayerNum = new ArrayList<>();
                for (StatsMatchFormationBean formationBean : onFieldList) {
                    onFieldPlayerNum.add(formationBean.Player_Num);
                    LogUtil.defaultLog("---onFieldList number " + formationBean.Player_Num);
                }
                ArrayList<String> lastOnFieldPlayerNum = new ArrayList<>();
                for (StatsMatchFormationBean formationBean : lastOnField) {
                    lastOnFieldPlayerNum.add(formationBean.Player_Num);
                    LogUtil.defaultLog("---onFieldList lastOnField number " + formationBean.Player_Num);
                }
                ArrayList<Integer> diffNum = (ArrayList<Integer>) CollectionUtil.getDiffent(onFieldPlayerNum, lastOnFieldPlayerNum);
                if (diffNum.size() > 0) {
                    for (StatsMatchFormationBean formationBean : lastOnField) {
                        if (diffNum.contains(formationBean.Player_Num)) {
                            mMatchFragment.generateEvent(EventCode.LeaveThePitch, MCConstants.EventType.EVENT_LEAVETHEPITCH, formationBean.id, "", -1, eventTime);
                        }
                    }
                    for (StatsMatchFormationBean formationBean : onFieldList) {
                        if (diffNum.contains(formationBean.Player_Num)) {
                            mMatchFragment.generateEvent(EventCode.EnterThePitch, MCConstants.EventType.EVENT_ENTERTHEPITCH, formationBean.id, "", -1, eventTime);
                        }
                    }
                }
                lastOnField.clear();
                lastOnField.addAll(onFieldList);
            }
        }
    }

    private void undoEvent() {
        hideTipView();
        eventCode = mMatchFragment.undoLastAction();
        if (EventCode.KickOffFirstHalf == eventCode) { //撤销上半场开始联通比赛开始一起撤销
            eventCode = mMatchFragment.undoLastAction();
        }
        LogUtil.defaultLog("click undo button eventCode before == " + eventCode);
        if (EventCode.Finish == eventCode && hasPenalty) { //点球大战－全场结束－记两个事件［点球大战结束，全场结束（按撤销时一并撤销）］
            eventCode = mMatchFragment.undoLastAction();
        }
        LogUtil.defaultLog("click undo button eventCode == " + eventCode);
        if (eventCode == EventCode.Begin) {
            matchProcess = EventCode.Begin;
            isPlaying = false;
        } else if (eventCode == EventCode.KickOffFirstHalf) {
            matchProcess = EventCode.Begin;
            isPlaying = false;
        } else if (eventCode == EventCode.HalfTime) {
            matchProcess = EventCode.KickOffFirstHalf;
            isPlaying = true;
        } else if (eventCode == EventCode.KickOffSecondHalf) {
            matchProcess = EventCode.HalfTime;
            isPlaying = false;
        } else if (eventCode == EventCode.FullTime) {
            matchProcess = EventCode.KickOffSecondHalf;
            isPlaying = true;
        } else if (eventCode == EventCode.KickOffExtraTimeFirstHalf) {
            matchProcess = EventCode.FullTime;
            isPlaying = false;
        } else if (eventCode == EventCode.ExtraTimeHalfTime) {
            matchProcess = EventCode.KickOffExtraTimeFirstHalf;
            isPlaying = true;
        } else if (eventCode == EventCode.KickOffExtraTimeSecondHalf) {
            matchProcess = EventCode.ExtraTimeHalfTime;
            isPlaying = false;
        } else if (eventCode == EventCode.ExtraTimeFullTime) {
            matchProcess = EventCode.KickOffExtraTimeSecondHalf;
            isPlaying = true;
        } else if (eventCode == EventCode.PenaltyShootOutBegin) {
            if (hasExtraTime) {
                matchProcess = EventCode.ExtraTimeFullTime;
                isPlaying = false;
                isPenalty = false;
            } else {
                matchProcess = EventCode.FullTime;
                isPlaying = false;
                isPenalty = false;
            }
        } else if (eventCode == EventCode.PenaltyShootOutFinish) {
            if (hasPenalty) { //有点球战
                matchProcess = EventCode.PenaltyShootOutBegin;
                isPenalty = true;
                isPlaying = true;
            }
        } else if (eventCode == EventCode.Finish) {
            if (hasPenalty) { //有点球战
                matchProcess = EventCode.PenaltyShootOutBegin;
                isPenalty = true;
                isPlaying = true;
            } else {
                if (hasExtraTime) { //没有点球大战,但有加时赛
                    matchProcess = EventCode.ExtraTimeFullTime;
                    isPlaying = false;
                    isPenalty = false;
                } else {
                    matchProcess = EventCode.FullTime;
                    isPlaying = false;
                    isPenalty = false;
                }
            }
        }
        if (eventCode < EventCode.KickOffExtraTimeFirstHalf) { //撤销到点球加时后初始化点球加时状态
            hasExtraTime = false;
        }
        if (eventCode < EventCode.PenaltyShootOutBegin) { //撤销到点球加时后初始化点球加时状态
            hasPenalty = false;
        }
        //撤销时同步比赛时间
        if ((matchProcess > EventCode.Begin) && (matchProcess < EventCode.PenaltyShootOutFinish) && timeProcessMap.size() > 0) {
            if (timeProcessMap.containsKey(matchProcess)) {
                matchTime = timeProcessMap.get(matchProcess);
                timeProcessMap.remove(matchProcess);
            }
        }

        mMatchFragment.changePlayerBackground(isPlaying, true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fly_title_left:
                undoEvent();
                break;
            case R.id.export_data_action:
                if (null != mSettingPopupWindow && mSettingPopupWindow.isShowing()) {
                    mSettingPopupWindow.dismiss();
                }
                hideTipView();
                show2BtnDialog(SweetAlertDialog.NORMAL_TYPE, "提示", "确定需要导出数据吗？", null, exportDataListener);
                break;
            case R.id.exit_action:
                hideTipView();
                if (null != mSettingPopupWindow && mSettingPopupWindow.isShowing()) {
                    mSettingPopupWindow.dismiss();
                }
                show2BtnDialog(SweetAlertDialog.NORMAL_TYPE, "提示", "确定需要退出当前统计吗？", null, exitStatsListener);
                break;
            case R.id.fly_title_right1:
                //比赛时间
                if (!taskAlready) {
                    timer.schedule(task, INTERVAL_TIME, INTERVAL_TIME);
                }
                if (tipView != null && tipView.isShown()) {
                    tipView.hide();
                    break;
                }

                if (matchProcess == EventCode.Finish) {

                } else if (matchProcess == EventCode.PenaltyShootOutBegin) {
                    showMatchProgressTip(getString(R.string.match_finish));
                } else if (matchProcess == EventCode.Begin) {
                    showMatchProgressTip(getString(R.string.match_kick_off_first_half));
                } else if (matchProcess == EventCode.KickOffFirstHalf) {
                    showMatchProgressTip(getString(R.string.match_half_time));
                } else if (matchProcess == EventCode.HalfTime) {
                    showMatchProgressTip(getString(R.string.match_kick_off_second_half));
                } else if (matchProcess == EventCode.KickOffSecondHalf) {
                    showMatchProgressTip(getString(R.string.match_second_half_end), getString(R.string.match_finish));
                } else if (matchProcess == EventCode.FullTime) {
                    showMatchProgressTip(getString(R.string.match_extra_time), getString(R.string.match_penalty_shoot), getString(R.string.match_finish));
                } else if (matchProcess == EventCode.KickOffExtraTimeFirstHalf) {
                    showMatchProgressTip(getString(R.string.match_extra_time_half_time));
                } else if (matchProcess == EventCode.ExtraTimeHalfTime) {
                    showMatchProgressTip(getString(R.string.match_kick_off_extra_time_second_half));
                } else if (matchProcess == EventCode.KickOffExtraTimeSecondHalf) {
                    showMatchProgressTip(getString(R.string.match_extra_time_full_time), getString(R.string.match_finish));
                } else if (matchProcess == EventCode.ExtraTimeFullTime) {
                    showMatchProgressTip(getString(R.string.match_penalty_shoot), getString(R.string.match_finish));
                }
                break;
            case R.id.fly_title_right2:
                if (showTrans) {
                    ensurePlayer = false;
                    hideTipView();
                    toggleTransitionFragment();
                }
                if (showRefresh) {
                    showRefresh = false;
                    showCancel = true;
//                getStatsMatchFormation();
                    cancelRefresh = false;
                    showLoadingPW();
                }
                if (showCancel) {
                    dismissLoading();
                    cancelRefresh = true;
                    hideLoadingPW();
                    showRefresh = true;
                    showCancel = false;
                }
                break;
            case R.id.fly_title_right3:
                if (showSetting) { //需放在showDone前面
                    hideTipView();
                    showSettingMenu();
                }
                if (showDone) {
                    ensurePlayer = true;
                    hideLoadingPW();
                    substitution();
                    hideTipView();
                    if (mMatchFragment != null && mTransitionFragment != null && mTransitionFragment.isVisible()) {
                        mMatchFragment.setOnFieldPlayers(mTransitionFragment.getOnFieldList());
                        toggleTransitionFragment();
                    }
                    showRefresh = false;
                    showCancel = false;
                }

                break;
            default:
                break;
        }
    }

    /**
     * 导出数据监听
     */
    private SweetAlertDialog.OnSweetClickListener exportDataListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            sweetAlertDialog.dismiss();
            exportData();
        }
    };

    /**
     * 退出统计button事件监听
     */
    private SweetAlertDialog.OnSweetClickListener exitStatsListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    savePlayers("OnFieldPlayers_" + matchId + "_" + teamId, mMatchFragment.getOnFieldPlayers());
                    savePlayers("OffFieldPlayers_" + matchId + "_" + teamId, mTransitionFragment.getOffFieldList());
                    saveMatchProgress();
                    StatsOperateActivity.this.finish();
                }
            });
            sweetAlertDialog.dismiss();
        }
    };

    /**
     * 展示比赛进程popview
     */
    private void showMatchProgressTip(String... showStr) {
        tipView = new PopTipView(this, tvRight1);
        tipView.setArrowWidth(30);
        tipView.setArrowPointOffset(0, 80);
        tipView.setArrowLocation(PopTipView.ArrowLocation.top);
        tipView.setBackgroud(getResources().getColor(
                R.color.white));

        if (showStr.length == 1) {
            tipView.setTvPoptipContent1Text(showStr[0]);
        } else if (showStr.length == 2) {
            tipView.setTvPoptipContent1Text(showStr[0]);
            tipView.setTvPoptipContent2Text(showStr[1]);
        } else if (showStr.length == 3) {
            tipView.setTvPoptipContent1Text(showStr[0]);
            tipView.setTvPoptipContent2Text(showStr[1]);
            tipView.setTvPoptipContent3Text(showStr[2]);
        }
        tipView.setPopViewContentClickedListener(new PopTipView.OnPopViewContentClickedListener() {
            int code = -1;

            @Override
            public void onPopViewTv1Clicked() {
                if (getString(R.string.match_kick_off_first_half).equals(tipView.getTv1Text())) {
                    firstTime = System.currentTimeMillis();
                    isPlaying = true;
                    code = EventCode.KickOffFirstHalf;
                } else if (getString(R.string.match_half_time).equals(tipView.getTv1Text())) {
                    isPlaying = false;
                    code = EventCode.HalfTime;
                } else if (getString(R.string.match_kick_off_second_half).equals(tipView.getTv1Text())) {
                    secondTime = System.currentTimeMillis();
                    isPlaying = true;
                    code = EventCode.KickOffSecondHalf;
                } else if (getString(R.string.match_second_half_end).equals(tipView.getTv1Text())) {
                    isPlaying = false;
                    code = EventCode.FullTime;
                } else if (getString(R.string.match_extra_time).equals(tipView.getTv1Text())) {
                    extralFirstTime = System.currentTimeMillis();
                    isPlaying = true;
                    code = EventCode.KickOffExtraTimeFirstHalf;
                    hasExtraTime = true;
                } else if (getString(R.string.match_extra_time_half_time).equals(tipView.getTv1Text())) {
                    isPlaying = false;
                    code = EventCode.ExtraTimeHalfTime;
                } else if (getString(R.string.match_kick_off_extra_time_second_half).equals(tipView.getTv1Text())) {
                    extralSecondTime = System.currentTimeMillis();
                    isPlaying = true;
                    code = EventCode.KickOffExtraTimeSecondHalf;
                } else if (getString(R.string.match_extra_time_full_time).equals(tipView.getTv1Text())) {
                    isPlaying = false;
                    code = EventCode.ExtraTimeFullTime;
                } else if (getString(R.string.match_penalty_shoot).equals(tipView.getTv1Text())) {
                    isPenalty = true;
                    isPlaying = true;
                    code = EventCode.PenaltyShootOutBegin;
                    hasPenalty = true;
                } else if (getString(R.string.match_finish).equals(tipView.getTv1Text())) {
                    isPenalty = false;
                    isPlaying = false;
                    code = EventCode.Finish;
                }
                matchProcess = code; //放在前是因在fragment中实时获取比赛进程
                timeProcessMap.put(code - 1, matchTime);
                if (mMatchFragment != null) { //通知球员号码背景颜色变化
                    long eventTime = System.currentTimeMillis();
                    if (code == EventCode.KickOffFirstHalf) { //比赛开始传比赛开始和上半场开始两个事件
                        mMatchFragment.generateEvent(EventCode.Begin, MCConstants.EventType.EVENT_PROCESS, null, null, -1, eventTime);
                    }
                    if (code == EventCode.Finish && hasPenalty) { //记两个事件［点球大战结束，全场结束（按撤销时一并撤销
                        matchProcess = EventCode.PenaltyShootOutFinish;
                        mMatchFragment.generateEvent(EventCode.PenaltyShootOutFinish, MCConstants.EventType.EVENT_PROCESS, null, null, -1, eventTime);
                    }
                    mMatchFragment.changePlayerBackground(isPlaying, false);
                    matchProcess = code;
                    mMatchFragment.generateEvent(code, MCConstants.EventType.EVENT_PROCESS, null, null, -1, eventTime);
                }
            }

            @Override
            public void onPopViewTv2Clicked() {
                if (getString(R.string.match_penalty_shoot).equals(tipView.getTv2Text())) {
                    isPenalty = true;
                    isPlaying = true;
                    code = EventCode.PenaltyShootOutBegin;
                    hasPenalty = true;
                    hasExtraTime = false;
                } else if (getString(R.string.match_finish).equals(tipView.getTv2Text())) { //此时分为有加时和无加时情况
                    isPenalty = false;
                    isPlaying = false;
                    code = EventCode.Finish;
                    hasPenalty = false;
                    if (hasExtraTime) {
                        timeProcessMap.put(EventCode.ExtraTimeHalfTime, matchTime);
                    } else {
                        timeProcessMap.put(EventCode.FullTime, matchTime);
                    }
                }
                matchProcess = code;
                long eventTime = System.currentTimeMillis();
                if (mMatchFragment != null) { //通知球员号码背景颜色变化
                    mMatchFragment.changePlayerBackground(isPlaying, false);
                    if (code == EventCode.Finish && !hasExtraTime && !hasPenalty) { //全场结束记两个事件［下半场结束、全场结束
                        mMatchFragment.generateEvent(EventCode.FullTime, MCConstants.EventType.EVENT_PROCESS, null, null, -1, eventTime);
                    } else if (code == EventCode.Finish && hasExtraTime && !hasPenalty) { //加时下结束后点击全场结束不需要多传事件
                        if (!tvRight1.getText().equals(getString(R.string.match_extra_time_full_time))) { //加时下点全场结束记两个事件［加时赛下半场结束、全场结束]
                            mMatchFragment.generateEvent(EventCode.ExtraTimeFullTime, MCConstants.EventType.EVENT_PROCESS, null, null, -1, eventTime);
                        }
                    }
                    mMatchFragment.generateEvent(code, MCConstants.EventType.EVENT_PROCESS, null, null, -1, eventTime);
                }
            }

            @Override
            public void onPopViewTv3Clicked() {
                if (getString(R.string.match_finish).equals(tipView.getTv3Text())) {
                    isPlaying = false;
                    code = EventCode.Finish;
                    hasPenalty = false;
                    hasExtraTime = false;
                    isPenalty = false;
                }
                matchProcess = code;
                timeProcessMap.put(code, matchTime);
                if (mMatchFragment != null) { //通知球员号码背景颜色变化
                    mMatchFragment.changePlayerBackground(isPlaying, false);
                    long eventTime = System.currentTimeMillis();
                    mMatchFragment.generateEvent(code, MCConstants.EventType.EVENT_PROCESS, null, null, -1, eventTime);
                }
            }
        });
        ptrlyMatchProgress.removeAllViews();
        ptrlyMatchProgress.show(tipView);
    }

    /**
     * 隐藏弹出框
     */
    public void hideTipView() {
        if (tipView != null && tipView.isShown()) {
            tipView.hide();
        }
    }

    /**
     * 获取比赛是否开始
     *
     * @return
     */
    public boolean getMatchIsPlaying() {
        return isPlaying;
    }

    /**
     * 右上角导出数据和登出
     */
    private void showSettingMenu() {
        View menuView = LayoutInflater.from(this).inflate(R.layout.export_data_menu_layout, null, false);
        menuView.findViewById(R.id.export_data_action).setOnClickListener(this);
        menuView.findViewById(R.id.exit_action).setOnClickListener(this);
        menuView.findViewById(R.id.clear_action).setVisibility(View.GONE);
        mSettingPopupWindow = new PopupWindow(this);
        mSettingPopupWindow.setContentView(menuView);
        mSettingPopupWindow.setWidth(DensityUtil.dip2px(240));
        mSettingPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mSettingPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mSettingPopupWindow.setFocusable(true);
        mSettingPopupWindow.showAtLocation(menuView, Gravity.TOP | Gravity.RIGHT, 0, DensityUtil.dip2px(48));
        mSettingPopupWindow.update();
        performDismissAnimation(menuView);
    }

    private void performDismissAnimation(final View menuView) {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.3f, 1);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.3f, 1);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0, 1);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(menuView, scaleX, scaleY, alpha);
        animator.setDuration(150);
        animator.start();
    }

    /**
     * 导出本地数据
     */
    private void exportData() {
        String filePath = CellphoneUtil.getSDPath() + File.separator + "OpenPlay" + File.separator + "stats" + File.separator + "csv";
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        LogUtil.defaultLog("开始导出本地数据:" + System.currentTimeMillis());
        RecordFactory.exportToCSV(this, filePath, matchId, teamId);
        LogUtil.defaultLog("export data size  " + RecordFactory.queryRecords(matchId, teamId).size() + "--->" + RecordFactory.queryRecords(matchId, teamId));
        SponiaToastUtil.showShortToast("正在导出本地数据库中的数据...");
        //注册本地广播，监听返回的结果
        registerBroadcast();
    }


    /**
     * 监听导出数据返回结果
     */
    private void registerBroadcast() {
        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int result = intent.getIntExtra(RecordFactory.RESULT_CODE, RecordFactory.SUCCESS);
                if (RecordFactory.SUCCESS == result) {
                    SponiaToastUtil.showShortToast("数据成功导出在OpenPlay文件夹中");
                } else if (RecordFactory.FAILED == result) {
                    SponiaToastUtil.showShortToast("磁盘空间不够，请清理后再尝试！");
                } else if (RecordFactory.EMPTY == result) {
                    SponiaToastUtil.showShortToast("比赛事件为空，请先录入数据再导出！");
                }
                broadcastManager.unregisterReceiver(this);
            }
        }, new IntentFilter(RecordFactory.INTENT_EXPORT_DATA));
    }

    private void toggleTransitionFragment() {
        if (null != mTransitionFragment && !mTransitionFragment.isDetached()) {
            toggleToolBar();
            mTransitionFragment.toggle();
        }
    }

    /**
     * 控制显示toolbar和球员界面
     */
    private void toggleToolBar() {
        if (null != mTransitionFragment && !mTransitionFragment.isDetached() && mTransitionFragment.isVisible()) {
            showActionUI();
        } else {
            showTransitionUI();
        }
    }

    private void showActionUI() {
        setActionbarTitleVisble();
        setTitleRight1TextColor(Color.parseColor("#FFFFFF"));
        setTitleRight1Background(R.drawable.bg_op_red);
        setTitleRight2Image(R.mipmap.ic_transition);
        setTitleRight3Image(R.mipmap.ic_data_menu);

        imgTitleLeft.setVisibility(View.VISIBLE);
        showSetting = true;
        showTrans = true;
        tvRight1.setVisibility(View.VISIBLE);
        showDone = false;
        showRefresh = false;
        showCancel = false;

        setTitleLeftText("撤销");
        setTitleLeftBackground(R.drawable.bg_op_red);
        if (!TextUtils.isEmpty(mTitleState)) {
//            setActionbarTitle(mTitleState);
        }
    }

    /**
     * 显示换人界面
     */
    private void showTransitionUI() {
        setActionbarTitleGone();
        setTitleRight1Gone();
        setTitleRight2Image(R.mipmap.ic_refresh);
        setTitleRight3Image(R.mipmap.ic_done);

        imgTitleLeft.setVisibility(View.GONE);
        showTrans = false;
        showSetting = false;
        showDone = true;
        showCancel = false;
        showRefresh = true;
    }

    @Override
    public void onHttpSuccess(NetMessage netMsg, String responceData) {
        if (netMsg == null) {
            return;
        }
        if (TAG_GET_MATCH_FORMATION.equalsIgnoreCase(netMsg.getMessageId())) {
            matchList.clear();
            matchList.addAll(JSON.parseArray(responceData, StatsMatchFormationBean.class));
            Collections.sort(matchList);

            if (matchList != null && matchList.size() > 0) {
                handler.sendEmptyMessage(MSG_GET_MATCH_FORMATION_SUCCESS);
            } else {
                handler.sendEmptyMessage(MSG_GET_MATCH_FORMATION_FAILED);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mMatchFragment != null && mTransitionFragment != null && !mTransitionFragment.isDetached() && mTransitionFragment.isVisible()) {
            mMatchFragment.setOnFieldPlayers(mTransitionFragment.getOnFieldList());
            toggleTransitionFragment();
        }
        //屏蔽返回键
    }

    private void initSocket() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    isStartRecieveMsg = true;
                    mSocket = new Socket(ip, port);
                    mSocket.setReuseAddress(true);
                    mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "utf-8"));
                    mWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "utf-8"));
                    while (isStartRecieveMsg) {
                        if (mReader.ready()) {
                            handler.obtainMessage(MSG_SOCKET_INIT, mReader.readLine()).sendToTarget();
                        }
                        Thread.sleep(200);
                    }
                    mWriter.close();
                    mReader.close();
                    mSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void send(final String msg, final String playerNum, final String eventCode, final String matchTime, final String clientStartAt) {
        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                sendMsg(msg, playerNum, eventCode, matchTime, clientStartAt);
                return null;
            }
        }.execute();

    }

    private void sendMsg(String msg, String playerNum, String eventCode, String matchTime, String clientStartAt) {
        Set socketIDSet = (Set) SponiaSpUtil.getDefaultSpValue(SpCode.SOCKET_ID);
        LogUtil.defaultLog("socketIDSet == " + socketIDSet);
        try {
            JSONObject json = new JSONObject();
            Iterator<String> it = socketIDSet.iterator();
            String uuid = UUID.randomUUID().toString();
            while (it.hasNext()) {
                json.put("to", it.next());
                json.put("msg", msg);
                json.put("playerNum", playerNum);
                json.put("eventCode", eventCode);
                json.put("matchTime", matchTime);
                json.put("clientStartAt", clientStartAt);
                json.put("id", uuid);
                mWriter.write(json.toString() + "\n");
                mWriter.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        if (mTransitionFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mTransitionFragment).commitAllowingStateLoss();
            mTransitionFragment = null;
        }
        if (mMatchFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mMatchFragment).commitAllowingStateLoss();
            mMatchFragment = null;
        }

        if (null != mSettingPopupWindow && mSettingPopupWindow.isShowing()) {
            mSettingPopupWindow.dismiss();
            mSettingPopupWindow = null;
        }
        cleanTimer();
        super.onDestroy();
    }
}
