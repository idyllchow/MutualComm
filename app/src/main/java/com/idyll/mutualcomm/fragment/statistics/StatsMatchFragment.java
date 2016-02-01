package com.idyll.mutualcomm.fragment.statistics;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.adapter.StatsActionAdapter;
import com.idyll.mutualcomm.activity.StatsOperateActivity;
import com.idyll.mutualcomm.adapter.StatsPlayerAdapter;
import com.idyll.mutualcomm.comm.MCConstants;
import com.idyll.mutualcomm.entity.MCPlayerTextItem;
import com.idyll.mutualcomm.entity.StatsActionItem;
import com.idyll.mutualcomm.entity.StatsMatchFormationBean;
import com.idyll.mutualcomm.event.EventCode;
import com.idyll.mutualcomm.event.MCRecordData;
import com.idyll.mutualcomm.event.RecordFactory;
import com.idyll.mutualcomm.fragment.StatsBaseFragment;
import com.idyll.mutualcomm.receiver.NetBroadcastReceiver;
import com.sponia.foundationmoudle.utils.BigDecimalUtil;
import com.sponia.foundationmoudle.utils.LogUtil;
import com.sponia.foundationmoudle.utils.NetUtil;
import com.sponia.foundationmoudle.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @packageName com.sponia.soccerstats.fragment
 * @description 比赛统计fragment
 * @date 15/9/14
 * @auther shibo
 */
public class StatsMatchFragment extends StatsBaseFragment implements AdapterView.OnItemClickListener, View.OnTouchListener {
    private final String TAG = "StatsMatchFragment";
    //是否实时发送数据
    private boolean isRealTime = true;
    //队员
    private ArrayList<MCPlayerTextItem> mItems = new ArrayList<>();
    //场上球员
    private ArrayList<StatsMatchFormationBean> mOnFieldPlayers;
    //等待上传的操作
    private final ArrayList<MCRecordData> mRecordWaitUploadDataList = new ArrayList<>();
    //保存所有的操作，用于撤销操作
    private final ArrayList<MCRecordData> mRecordBackUpCancelList = new ArrayList<>();
    //删除事件
    private final ArrayList<String> mDeleteEventList = new ArrayList<>();
    //比赛ID
    private String matchId;
    //球队ID
    private String teamId;
    //球队bean
//    private StatsTeamBean mTeam;
    //所选中球员
    private StatsMatchFormationBean mSelectedPlayer;
    //标识选中球员
    private int mSelectedPosition = -1;
    //场上球员view
    private GridView mPlayerGridView;
    //场下名单及action view
    private GridView mActionGridView;
    //球员adapter
    private StatsPlayerAdapter mPlayerAdapter;
    //事件
    private StatsActionAdapter mActionAdapter;

    private Vibrator mVibrator;
    //声效播放
    final private SoundPool sp = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
    private int mActionMusic;
    private int mNumberMusic;
    //选中球员时间
    private long mLastSelectedPlayerTime = 0l;
    //toolbar回调
    private UpdateToolBarCallback mUpdateCallback;
    //上传事件完成回调
    private UploadCompleteCallback mUploadCompleteCallback;
    //上传事件list
//    private ArrayList<StatsUploadEventBean> uploadEventBeans;
//    private ArrayList<StatsDeleteBean> deleteBean = new ArrayList();
    private ArrayList<String> deleteList = new ArrayList<>();
    //比赛模式
    private int statsMode;
    //比赛几人制
    private int matchType;
    //删除事件完成
    private boolean isDeleteEventFinished = true;
    private NetBroadcastReceiver mReceiver;
    private NetBroadcastReceiver.NetEventHandler mNetEventHandler;
    //比赛上传事件tag
    private static final String TAG_UPLOAD_EVENTS = "TAG_UPLOAD_EVENTS";
    //比赛删除事件tag
    private static final String TAG_DELETE_EVENTS = "TAG_DELETE_EVENTS";
    //上传事件获取成功
    private static final int MSG_UPLOAD_EVENTS_SUCCESS = 0x0001;
    //上传事件失败
    private static final int MSG_UPLOAD_EVENTS_FAILED = 0x0002;
    //删除事件获取成功
    private static final int MSG_DELETE_EVENTS_SUCCESS = 0x0003;
    //删除事件失败
    private static final int MSG_DELETE_EVENTS_FAILED = 0x0004;
    //服务器与本地时间差
    private long timeDiff = 0;

    public interface UpdateToolBarCallback {
        void update(MCRecordData data);
    }

    public interface UploadCompleteCallback {
        void complete(boolean isSuccess);
    }

    public void setUpdateCallback(UpdateToolBarCallback callback) {
        this.mUpdateCallback = callback;
    }

    public void setUploadCompleteCallback(UploadCompleteCallback callback) {
        this.mUploadCompleteCallback = callback;
    }

    /**
     * 用实体方式传值
     *
     * @param matchType  比赛几人制
     * @param matchId    比赛Id
     * @param timeDiff   服务器与本地时间差
     * @param playerList 球员list
     * @return
     */
    public static StatsMatchFragment getInstance(int statsMode, int matchType, String matchId, long timeDiff, ArrayList<StatsMatchFormationBean> playerList) {
        StatsMatchFragment fragment = new StatsMatchFragment();
        Bundle args = new Bundle();
        args.putInt("statsMode", statsMode);
        args.putInt("matchType", matchType);
        args.putParcelableArrayList("playerList", playerList);
        args.putString("matchId", matchId);
        args.putLong("timeDiff", timeDiff);
//        args.putParcelable("team", team);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            statsMode = savedInstanceState.getInt("statsMode");
            matchType = savedInstanceState.getInt("matchType");
            matchId = savedInstanceState.getString("matchId");
            timeDiff = savedInstanceState.getLong("timeDiff");
            mSelectedPlayer = savedInstanceState.getParcelable("selectedPlayer");
            mSelectedPosition = savedInstanceState.getInt("selectedPosition", -1);
            mLastSelectedPlayerTime = savedInstanceState.getLong("lastSelectedTime");
            ArrayList<MCRecordData> temp = savedInstanceState.getParcelableArrayList("MCRecordDataList");
            if (null != temp) {
                mRecordWaitUploadDataList.clear();
                mRecordWaitUploadDataList.addAll(temp);
            }
            temp = savedInstanceState.getParcelableArrayList("recordBackUpList");
            if (null != temp) {
                mRecordBackUpCancelList.addAll(temp);
            }
            ArrayList<String> deleteEvents = savedInstanceState.getStringArrayList("deleteEventList");
            if (null != deleteEvents) {
                mDeleteEventList.addAll(deleteEvents);
            }
            mOnFieldPlayers = savedInstanceState.getParcelableArrayList("playerList");
            if (null != mOnFieldPlayers && !mOnFieldPlayers.isEmpty()) {
                mItems.addAll(generateDatas(mOnFieldPlayers));
            }
        } else if (null != getArguments()) {
            statsMode = getArguments().getInt("statsMode");
            matchType = getArguments().getInt("matchType");
            matchId = getArguments().getString("match");
            timeDiff = getArguments().getLong("timeDiff");
            mOnFieldPlayers = getArguments().getParcelableArrayList("playerList");
            if (null != mOnFieldPlayers && !mOnFieldPlayers.isEmpty()) {
                mItems.addAll(generateDatas(mOnFieldPlayers));
            }
        }
        teamId = "sponia";
    }

    @Override
    public void onSaveInstanceState(Bundle outState) { //保存数据用以防止activity被销毁
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("playerList", mOnFieldPlayers);
        outState.putInt("statsMode", statsMode);
        outState.putInt("matchType", matchType);
        outState.putString("matchId", matchId);
        outState.putLong("timeDiff", timeDiff);
        outState.putParcelable("selectedPlayer", mSelectedPlayer);
        outState.putInt("selectedPosition", mSelectedPosition);
        outState.putLong("lastSelectedTime", mLastSelectedPlayerTime);
        outState.putParcelableArrayList("MCRecordDataList", mRecordWaitUploadDataList);
        outState.putParcelableArrayList("recordBackUpList", mRecordBackUpCancelList);
        outState.putStringArrayList("deleteEventList", mDeleteEventList);
    }

    /**
     * 生成参赛队员,确定阵容后的视图
     *
     * @param players
     * @return
     */
    private List<MCPlayerTextItem> generateDatas(ArrayList<StatsMatchFormationBean> players) {
        MCPlayerTextItem[] items = new MCPlayerTextItem[matchType];
        if (null != players && !players.isEmpty()) {
            int redColor = getResources().getColor(R.color.R);
            String selectNumber = null == mSelectedPlayer ? "" : mSelectedPlayer.Player_Num + "";
            for (StatsMatchFormationBean player : players) {
                int pos = player.position;
                int color = (0 == (pos / 3 + pos % 3) % 2) ? Color.WHITE : redColor;
                if (pos < matchType) {
                    items[pos] = new MCPlayerTextItem(player, color, selectNumber.equals(player.Player_Num));
                }
            }
        }
        return Arrays.asList(items);
    }

    /**
     * 设置场上队员
     *
     * @param players
     */
    public void setOnFieldPlayers(ArrayList<StatsMatchFormationBean> players) {
        LogUtil.defaultLog("current mSelectedPlayer: " + mSelectedPlayer);
        if (null != mSelectedPlayer) {
            int selectedPos = -1;
            if (null != players) {
                for (StatsMatchFormationBean player : players) {
                    if (mSelectedPlayer.Player_Num == player.Player_Num) {
//                        selectedPos = player.position;
                        mSelectedPosition = selectedPos;
                        mSelectedPlayer = player;
                        LogUtil.defaultLog("current choosed player: " + player.Player_Num + ", pos: " + selectedPos);
                    }
                }
            }

            if (-1 == selectedPos) {
                mSelectedPosition = -1;
                mSelectedPlayer = null;
                if (isRealTime) {
                    handleRecordEvent();
                }
            }
        }

        mOnFieldPlayers = players;
        mItems.clear();
        mItems.addAll(generateDatas(players));
        changePlayerBackground(((StatsOperateActivity) getActivity()).getMatchIsPlaying(), false);
    }

    public ArrayList<StatsMatchFormationBean> getOnFieldPlayers() {
        return mOnFieldPlayers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match, null, false);
        mPlayerGridView = (GridView) rootView.findViewById(R.id.gridView_left);
        mActionGridView = (GridView) rootView.findViewById(R.id.gridView_right);
        //此处设置在选中确定后生效
        int windowHeight = MCConstants.getScreenHeight() - getResources().getDimensionPixelOffset(R.dimen.tab_height);
        int viewWidth = MCConstants.getScreenWidth() / 2;
        int playerItemWidth = viewWidth / 3 - getResources().getDimensionPixelOffset(R.dimen.player_reduce_height);

        if (windowHeight < viewWidth) {
            viewWidth = windowHeight;
        }
        int leftItemWidth = viewWidth / 3;

        if (matchType == MCConstants.MAX_ONFIELD_NINE) {
            mPlayerGridView.setVerticalSpacing(windowHeight / 3 - leftItemWidth);
        } else if (matchType == MCConstants.MAX_ONFIELD_TWELVE) {
            mPlayerGridView.setVerticalSpacing(windowHeight / 4 - leftItemWidth);
        }

//        mPlayerGridView.getLayoutParams().width = viewWidth;
        mPlayerGridView.getLayoutParams().width = MCConstants.getScreenWidth() - viewWidth;
        mPlayerGridView.getLayoutParams().height = windowHeight;
        mActionGridView.getLayoutParams().width = viewWidth;
        mActionGridView.getLayoutParams().height = windowHeight;

        mPlayerAdapter = new StatsPlayerAdapter(getActivity(), mItems);
        mActionAdapter = new StatsActionAdapter(getActivity(), generateActions(), matchType);
        mPlayerGridView.setAdapter(mPlayerAdapter);
        mActionGridView.setAdapter(mActionAdapter);

        mPlayerGridView.setOnItemClickListener(this);
        mPlayerGridView.setOnTouchListener(this);
        mActionGridView.setOnTouchListener(this);
        int itemWidth = viewWidth / 4;
        int itemHeight = windowHeight / 4;
//        mPlayerAdapter.setItemWidthAndHeight(playerItemWidth, playerItemWidth);
        mActionAdapter.setItemWidthAndHeight(itemWidth, itemHeight);
        LogUtil.d(TAG, "onCreateView mPlayerGridView playerItemWidth: " + playerItemWidth + "; leftItemWidth: " + leftItemWidth);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        mActionMusic = sp.load(getActivity(), R.raw.op_action, 1);
        mNumberMusic = sp.load(getActivity(), R.raw.op_number, 1);
        LogUtil.d(TAG, "onActivityCreated upload size " + mRecordWaitUploadDataList.size());
        if (null == savedInstanceState) {
            handleDbDatas();
        }
        registerBroadcast();
    }

    /**
     * 删除事件
     */
//    private void deleteEvent(ArrayList<String> eventId) {
//        ArrayList<DeleteBean> deleteBeans = new ArrayList<>();
//        int size = eventId.size();
//        for (int i = 0; i < size; i++) {
//            DeleteBean deleteBean = new DeleteBean(eventId.get(i));
//            deleteBeans.add(deleteBean);
//        }
//        String params = JSON.toJSONString(deleteBeans, true);
//        LogUtil.defaultLog("deleteEvent size: " + size + "; params: " + params);
//        new NetAction(this).excuteDelete(StatsHostConst.DELETE_EVENTS.replace("match_id", matchId).replace("team_id", mTeam.id), params, new NetMessage(TAG_DELETE_EVENTS), true);
//    }

    /**
     * 上传事件
     */
//    private void uploadEvent(ArrayList<?> event) {
//        String params = JSON.toJSONString(event);
//        LogUtil.defaultLog("uploadEvent params: " + params);
//        new NetAction(this).excutePost(StatsHostConst.UPLOAD_EVENTS.replace("match_id", matchId).replace("team_id", mTeam.id), params, new NetMessage(TAG_UPLOAD_EVENTS), true);
//    }

//    @Override
//    public void onHttpError(NetMessage netMsg, int errorCode, String errorMsg) {
//        super.onHttpError(netMsg, errorCode, errorMsg);
//        if (TAG_UPLOAD_EVENTS.equalsIgnoreCase(netMsg.getMessageId())) {
//            handler.sendEmptyMessage(MSG_UPLOAD_EVENTS_FAILED);
//        } else if (TAG_DELETE_EVENTS.equalsIgnoreCase(netMsg.getMessageId())) {
//            handler.sendEmptyMessage(MSG_DELETE_EVENTS_FAILED);
//        }
//    }

//    @Override
//    public void onHttpSuccess(NetMessage netMsg, String responceData) {
//        if (netMsg == null) {
//            return;
//        }
//        if (TAG_UPLOAD_EVENTS.equalsIgnoreCase(netMsg.getMessageId())) {
//            uploadEventBeans = (ArrayList) JSON.parseArray(responceData, StatsUploadEventBean.class);
//            if (uploadEventBeans != null && uploadEventBeans.size() > 0) {
//                handler.sendEmptyMessage(MSG_UPLOAD_EVENTS_SUCCESS);
//            } else {
//                handler.sendEmptyMessage(MSG_UPLOAD_EVENTS_FAILED);
//            }
//        } else if (TAG_DELETE_EVENTS.equalsIgnoreCase(netMsg.getMessageId())) {
//            deleteBean = (ArrayList) JSON.parseArray(responceData, StatsDeleteBean.class);
//            if (deleteBean != null && deleteBean.size() >= 0) {
//                handler.sendEmptyMessage(MSG_DELETE_EVENTS_SUCCESS);
//            } else {
//                handler.sendEmptyMessage(MSG_DELETE_EVENTS_FAILED);
//            }
//        }
//    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_DELETE_EVENTS_SUCCESS:
                    dealEventDeleteSuccess();
                    break;
                case MSG_DELETE_EVENTS_FAILED:
                    isDeleteEventFinished = true;
                    LogUtil.defaultLog("删除事件失败");
                    break;
                case MSG_UPLOAD_EVENTS_SUCCESS:
                    dealUploadEventSuccess();
                    break;
                case MSG_UPLOAD_EVENTS_FAILED:
                    dealUploadEventFailed();
                    break;
            }
        }
    };

    /**
     * 上传事件成功后的操作
     */
    private void dealUploadEventSuccess() {}

    /**
     * 上传事件失败后的操作
     */
    private void dealUploadEventFailed() {}

    /**
     * 删除事件成功后的操作
     */
    private void dealEventDeleteSuccess() {}

    private void registerBroadcast() {
        if (null == mNetEventHandler) {
            mNetEventHandler = new NetBroadcastReceiver.NetEventHandler() {
                @Override
                public void onNetChange(int state) {
                    if (NetUtil.NETWORN_NONE != state) {
                        //网络连通
                        LogUtil.defaultLog("网络状态连通，开始发送数据...");
                        if (0 != mDeleteEventList.size()) {
                            isDeleteEventFinished = false;
                            LogUtil.defaultLog("撤销队列不为空，正在发送网络请求...");
//                            deleteEvent(mDeleteEventList);
                        }
                        if (0 != mRecordWaitUploadDataList.size()) {
//                            LogUtil.defaultLog("register球员动作队列不为空,正在发送网络请求...");
//                            LogUtil.defaultLog("registerBroadcast upload size " + mRecordWaitUploadDataList.size());
//                            isHandleRecordEventFinished = false;
//                            uploadEvent(mRecordWaitUploadDataList);
                        }
                    }
                }
            };
        }
        if (null == mReceiver) {
            mReceiver = new NetBroadcastReceiver();
            mReceiver.addNetEventHandler(mNetEventHandler);
        }
        getActivity().registerReceiver(mReceiver, new IntentFilter(NetBroadcastReceiver.NET_CHANGE_ACTION));
    }

    private void unRegisterBroadcast() {
        if (null != mReceiver) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver.removeNetEventHandler(mNetEventHandler);
            mReceiver = null;
            mNetEventHandler = null;
        }
    }

    /**
     * 处理数据库中的事件
     */
    private void handleDbDatas() {
        if (TextUtils.isEmpty(matchId)) {
            matchId = ((StatsOperateActivity) getActivity()).getMatchId();
        }
        LogUtil.defaultLog("开始查询数据库：" + System.currentTimeMillis() + "; matchId: " + matchId);
        List<MCRecordData> recordList = RecordFactory.queryRecords(matchId, teamId);
        LogUtil.defaultLog("结束查询数据库：" + System.currentTimeMillis());
        int size = recordList.size();
        if (0 != size) {
            LogUtil.defaultLog("数据库中的数据的size: " + size + ", 数据：" + recordList);
            mRecordBackUpCancelList.ensureCapacity(size);
            mRecordWaitUploadDataList.ensureCapacity(size);
            for (MCRecordData MCRecordData : recordList) {
                int code = Integer.parseInt(MCRecordData.getCode());
                switch (Integer.parseInt(MCRecordData.getState())) {
                    case 1:
                        //标记为失败状态，以便下次可以重新提交
                        MCRecordData.setState(MCRecordData.STATE_FAILED + "");
                        mRecordWaitUploadDataList.add(MCRecordData);
                        if (code != EventCode.Ctrl && code != EventCode.EnterThePitch && code != EventCode.LeaveThePitch) {
                            mRecordBackUpCancelList.add(MCRecordData);
                        }
                        break;
                    case 2:
                        if (code != EventCode.Ctrl && code != EventCode.EnterThePitch && code != EventCode.LeaveThePitch) {
                            mRecordBackUpCancelList.add(MCRecordData);
                        }
                        break;
                    case 3:
                        mRecordWaitUploadDataList.add(MCRecordData);
                        if (code != EventCode.Ctrl && code != EventCode.EnterThePitch && code != EventCode.LeaveThePitch) {
                            mRecordBackUpCancelList.add(MCRecordData);
                        }
                        break;
                    case 4:
                        if (!TextUtils.isEmpty(MCRecordData.get_id())) {
                            mDeleteEventList.add(MCRecordData.get_id());
                        }
                        break;
                }
            }
            LogUtil.defaultLog("处理数据结束：" + System.currentTimeMillis());
            if (null != mUpdateCallback && 0 != mRecordBackUpCancelList.size()) {
                mUpdateCallback.update(mRecordBackUpCancelList.get(mRecordBackUpCancelList.size() - 1));
            }
        }

        if (0 != mDeleteEventList.size()) {
            isDeleteEventFinished = false;
            LogUtil.defaultLog("撤销队列不为空，正在发送网络请求...");
//            deleteEvent(mDeleteEventList);
        }

        if (0 != mRecordWaitUploadDataList.size()) {
//            LogUtil.defaultLog("db球员动作队列不为空,正在发送网络请求...");
//            LogUtil.defaultLog("handleDbDatas upload size " + mRecordWaitUploadDataList.size());
//            isHandleRecordEventFinished = false;
//            uploadEvent(mRecordWaitUploadDataList);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((StatsOperateActivity) getActivity()).hideTipView();
        if (!((StatsOperateActivity) getActivity()).getMatchIsPlaying()) {
            return;
        }
        if (R.id.gridView_left == parent.getId()) {
            MCPlayerTextItem selectItem = (MCPlayerTextItem) mPlayerAdapter.getItem(position);
            if (null == selectItem) {
                return;
            } else if (mSelectedPosition != position) {
                if (-1 != mSelectedPosition) {
                    MCPlayerTextItem lastSelectedItem = (MCPlayerTextItem) mPlayerAdapter.getItem(mSelectedPosition);
                    if (lastSelectedItem != null) {
                        lastSelectedItem.selected = false;
                        //换点球员后的控球事件
                        mLastSelectedPlayerTime = generateEvent(EventCode.Ctrl, MCConstants.EventType.EVENT_ACTION, "", 0);
                    }
                    if (isRealTime) {
                        handleRecordEvent();
                    }
                } else {
                    mLastSelectedPlayerTime = System.currentTimeMillis();
                }
                selectItem.selected = true;
                mSelectedPosition = position;
                mSelectedPlayer = selectItem.player;
            } else { //控球事件
                generateEvent(EventCode.Ctrl, MCConstants.EventType.EVENT_ACTION, "", 0);
                if (isRealTime) {
                    handleRecordEvent();
                }
                //取消选中的球员
                selectItem.selected = false;
                mSelectedPlayer = null;
                mSelectedPosition = -1;
            }
            changePlayerBackground(((StatsOperateActivity) getActivity()).getMatchIsPlaying(), false);
            sp.play(mNumberMusic, 1, 1, 0, 0, 1);
        }
    }

    private int lastTouchPosition = -1;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ((StatsOperateActivity) getActivity()).hideTipView();
        if (v.getId() == R.id.gridView_left) {
            if (!((StatsOperateActivity) getActivity()).getMatchIsPlaying()) {
                return true;
            } else {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                }
            }
        } else if (v.getId() == R.id.gridView_right) {
            int downX = (int) event.getX();
            int downY = (int) event.getY();
            //根据按下的X,Y坐标获取所点击item的position
            int position = ((AbsListView) v).pointToPosition(downX, downY);
            int action = event.getAction();
            if (AdapterView.INVALID_POSITION != position) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        touchDown(position);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchMove(position);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        touchUp(position);
                }
            } else {
                if (action == MotionEvent.ACTION_MOVE) {
                    return true;
                }
            }

            if (MotionEvent.ACTION_CANCEL == action ||
                    MotionEvent.ACTION_UP == action) {
                lastTouchPosition = -1;
                if (AdapterView.INVALID_POSITION == position) {
                    resetState();
                } else {
                    for (int i = 0; i < mActionGridView.getChildCount(); i++) {
                        ViewGroup viewGroup = (ViewGroup) mActionGridView.getChildAt(i);
                        viewGroup.getChildAt(0).setBackgroundResource(R.drawable.gray_round_rectangle_bg);
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void touchUp(int position) {
//        Log.e(TAG, "touchUp() action:" + event.getAction() + " x: " + event.getX() + ", y:" + event.getY());
        StatsActionItem item = (StatsActionItem) mActionAdapter.getItem(position);
        int menuClass = mActionAdapter.getMenuClass();
        if (item == null) {
            resetState();
        } else if (menuClass == 1) {
            if (item.menuClass == 0) {
                if (item.immediatelyDismiss) {
                    generateEvent(item.code, MCConstants.EventType.EVENT_ACTION, "", 0);
                    resetState();
                } else if (item.isSelected) {
                    resetState();
                } else {
                    item.isSelected = true;
                }
            } else if (item.menuClass == 1) {
                if (item.nextActions == null || item.nextActions.size() == 0) {
                    generateEvent(item.code, MCConstants.EventType.EVENT_ACTION, "", 0);
                    if (item.immediatelyDismiss) {
                        resetState();
                    }
                } else {
                    mActionAdapter.setActions(item.nextActions);
                    mActionAdapter.setMenuClass(2);
                    mActionAdapter.notifyDataSetChanged();
                }
            }
        } else if (menuClass == 0 && item.nextActions == null) {//一级事件,没有二级事件,点击就直接产生事件
            generateEvent(item.code, MCConstants.EventType.EVENT_ACTION, "", 0);
        }
    }

    private void touchMove(int position) {
        ViewGroup item;
        if (position != lastTouchPosition) {
            if (-1 != lastTouchPosition) {
                item = (ViewGroup) mActionGridView.getChildAt(lastTouchPosition);
                if (null != item) {
                    item.getChildAt(0).setBackgroundResource(R.drawable.gray_round_rectangle_bg);
                }
                lastTouchPosition = position;
            }
            item = (ViewGroup) mActionGridView.getChildAt(position);
            if (null != item) {
                item.getChildAt(0).setBackgroundResource(R.drawable.white_round_rectangle_bg);
            }
        } else {
            if (-1 != lastTouchPosition) {
                item = (ViewGroup) mActionGridView.getChildAt(lastTouchPosition);
                if (null != item) {
                    item.getChildAt(0).setBackgroundResource(R.drawable.white_round_rectangle_bg);
                }
                lastTouchPosition = position;
            }
        }
    }

    private void touchDown(int position) {
//        Log.e(TAG, "touchDown() action:" + event.getAction() + " x: " + event.getX() + ", y:" + event.getY());
        lastTouchPosition = position;
        StatsActionItem item = (StatsActionItem) mActionAdapter.getItem(position);
        int menuClass = mActionAdapter.getMenuClass();
        if (null != item) {
            if (0 == menuClass && 0 == item.menuClass) {
                if (null != item.nextActions) {
                    mActionAdapter.setActions(item.nextActions);
                    mActionAdapter.setMenuClass(1);
                    mActionAdapter.notifyDataSetChanged();
                }
            }
        }
        ViewGroup view = (ViewGroup) mActionGridView.getChildAt(position);
        if (null != view) {
            view.getChildAt(0).setBackgroundResource(R.drawable.white_round_rectangle_bg);
        }


    }

    private void resetState() {
        mActionAdapter.setActions(generateActions());
        mActionAdapter.setMenuClass(0);
        mActionAdapter.notifyDataSetChanged();
    }

    /**
     * 生成比赛事件,包含比赛阶段和实时事件
     *
     * @param code      事件代码
     * @param eventType 事件类型 1-上场事件 2-下场事件 3-进程事件 4-动作事件
     * @param playedId  只有球员上下场时需要传,其他需要ID的事件可在本类中获取
     * @param eventTime 事件时间
     * @return
     */
    public long generateEvent(int code, int eventType, String playedId, long eventTime) {
        int matchProgress = ((StatsOperateActivity) getActivity()).getMatchProcess();
        //不是一个完整的事件
        if (eventType == MCConstants.EventType.EVENT_ACTION) {
            if (mSelectedPlayer == null || TextUtils.isEmpty(mSelectedPlayer.Player_Num)) {
                LogUtil.defaultLog(code + " no player,event invalid!");
                return 0;
            }
            if (code == 0) {
                LogUtil.defaultLog(code + " no action,event invalid!");
                return 0;
            }
            if ((matchProgress == EventCode.Begin || !((StatsOperateActivity) getActivity()).getMatchIsPlaying()) && code != EventCode.Ctrl) {
                LogUtil.defaultLog(code + " not begin,event invalid！");
                return 0;
            }
            if ((matchProgress == EventCode.PenaltyShootOutBegin || matchProgress == EventCode.PenaltyShootOutFinish)&& code == EventCode.Ctrl) { //点球阶段无控球事件
                LogUtil.defaultLog(code + " penalty shoot,ctrl event invalid！");
                return 0;
            }
        }

        if (code != EventCode.Ctrl && code != EventCode.EnterThePitch && code != EventCode.LeaveThePitch) {
            sp.play(mActionMusic, 1, 1, 0, 0, 1);
            mVibrator.vibrate(50);
        }

        if (eventType == MCConstants.EventType.EVENT_ACTION && ((StatsOperateActivity) getActivity()).isPenalty()) { //比赛中点球事件额外处理
            if (code == EventCode.ShotOnTarget) { //射正
                code = EventCode.penaltyShotOnTarget;
            } else if (code == EventCode.ShotOffTarget) { //射偏
                code = EventCode.penaltyShotOffTarget;
            } else if (code == EventCode.Save) { //扑救
                code = EventCode.penaltySave;
            } else if (code == EventCode.Goal) { //进球
                code = EventCode.penaltyGoal;
            }
        }

        long currentTime = System.currentTimeMillis();
        String clientEventId = UUID.randomUUID() + "";
        if (mLastSelectedPlayerTime == 0l) {
            mLastSelectedPlayerTime = currentTime;
        }
        String clientStartedAt = TimeUtil.getTimeFormatMicrosecond(mLastSelectedPlayerTime + timeDiff);
        String clientEndedAt = TimeUtil.getTimeFormatMicrosecond(currentTime + timeDiff);
        //比赛时间点事件和球员上下场事件,触发事件即为当前时间
        if (eventTime != 0) {
            clientStartedAt = TimeUtil.getTimeFormatMicrosecond(eventTime + timeDiff);
        }

        float matchTime = BigDecimalUtil.
                div(((StatsOperateActivity) getActivity()).getMatchTime(), 60000, 2);
        LogUtil.defaultLog("mSelectedPosition " + mSelectedPosition + "; mSelectedPlayer: " + mSelectedPlayer + "; playedId: " + playedId + "; eventType: " + eventType + "; code: " + code + "; clientStartedAt: " + clientStartedAt + "; matchTime: " + matchTime);
        if (eventType == MCConstants.EventType.EVENT_PROCESS) { //比赛进程事件,事件开始结束时间一样
            playedId = null;
            clientEndedAt = clientStartedAt;
        } else if (eventType == MCConstants.EventType.EVENT_ENTERTHEPITCH || eventType == MCConstants.EventType.EVENT_LEAVETHEPITCH) { //上下场事件,事件开始结束时间一样
            clientEndedAt = clientStartedAt;
        } else if (eventType == MCConstants.EventType.EVENT_ACTION) { //比赛具体动作事件
            playedId = mSelectedPlayer.Player_Num;
        }

        if (code == EventCode.ShotOffTarget || code == EventCode.ShotOnTarget) {
            ((StatsOperateActivity) getActivity()).send(mSelectedPlayer.Player_Num + "号 射门 " + matchTime, mSelectedPlayer.Player_Num, code + "", ((StatsOperateActivity) getActivity()).getShowMatchTime(), clientStartedAt);
        }

        MCRecordData MCRecordData = new MCRecordData(clientEventId, clientEventId, matchId, teamId, playedId, code + "", "", "", "", clientEventId, clientStartedAt, clientEndedAt, matchTime + "", "0");
        if (MCRecordData != null) {
            mRecordWaitUploadDataList.add(MCRecordData);
        }
        if (MCRecordData != null && code != EventCode.Ctrl && code != EventCode.EnterThePitch && code != EventCode.LeaveThePitch) {
            mRecordBackUpCancelList.add(MCRecordData);
        }

        if (MCRecordData != null && mUpdateCallback != null) {
            mUpdateCallback.update(MCRecordData);
        }

        if (isRealTime) {
            handleRecordEvent();
        }
        return currentTime;
    }

    /**
     * 撤销事件,撤销纪录中最后一条数据
     *
     * @return 事件code
     */
    public int undoLastAction() {
        //取消选中球员
        changePlayerBackground(((StatsOperateActivity) getActivity()).getMatchIsPlaying(), true);
        MCRecordData updateData = null;
        MCRecordData lastMCRecordData = null;
        int state = MCRecordData.STATE_NOT_COMMIT;
        if (isRealTime) {
            //实时操作
            synchronized (mRecordBackUpCancelList) {
                if (0 != mRecordBackUpCancelList.size()) {
                    lastMCRecordData = mRecordBackUpCancelList.get(mRecordBackUpCancelList.size() - 1);
                    state = Integer.parseInt(lastMCRecordData.getState());
                    LogUtil.defaultLog("撤销动作：" + lastMCRecordData.getEvent_detail() + ", 当前状态：" + state + "; serverId: " + lastMCRecordData.get_id());
                    if (MCRecordData.STATE_SUCCESS == state) {
                        //提交成功的数据将不会出现在mMCRecordDataList队列中
                        LogUtil.defaultLog("cacel size " + mRecordBackUpCancelList.size());
                        mRecordBackUpCancelList.remove(lastMCRecordData);
                        LogUtil.defaultLog("cacel size " + mRecordBackUpCancelList.size());
                    } else {
                        mRecordBackUpCancelList.remove(lastMCRecordData);
                        mRecordWaitUploadDataList.remove(lastMCRecordData);
                    }

                    if (mRecordBackUpCancelList.size() > 0) {
                        updateData = mRecordBackUpCancelList.get(mRecordBackUpCancelList.size() - 1);
                    }

                    if (null != mUpdateCallback) {
                        mUpdateCallback.update(updateData);
                    }
                }
            }

            //处理一些比较耗时的操作，不放在同步块中进行
            if (null != lastMCRecordData) {
                mVibrator.vibrate(50); //震动一下
                if (MCRecordData.STATE_FAILED == state) {//上传失败的操作，直接从本地数据库中删除
                    LogUtil.defaultLog("直接从本地数据库中删除：" + lastMCRecordData);
                    RecordFactory.deleteRecordByEventKey(lastMCRecordData.getEvent_detail());
                } else {
                    //将数据库中的记录状态更新为撤销状态，然后等网络请求成功后从数据库中删除
                    if (lastMCRecordData.getEvent_detail() != null && lastMCRecordData.getEvent_detail().length() < 32) {
                        mDeleteEventList.add(lastMCRecordData.getEvent_detail());
                    }
                    if (!TextUtils.isEmpty(lastMCRecordData.get_id())) {
                        mDeleteEventList.add(lastMCRecordData.get_id());
                        LogUtil.d(TAG, "undo event_key: " + lastMCRecordData.getEvent_detail() + "; undo server_id: " + lastMCRecordData.get_id());
                    }
                    LogUtil.defaultLog("current queue data:" + mDeleteEventList);
                    if (isDeleteEventFinished) {
                        isDeleteEventFinished = false;
                        LogUtil.defaultLog("send delete request");
//                        deleteEvent(mDeleteEventList);
                    } else {
                        LogUtil.defaultLog("the lasted delete quest is not return,waiting...");
                    }
                    lastMCRecordData.setState(MCRecordData.STATE_UNDO + "");
                    LogUtil.defaultLog("更新数据库中数据的状态为撤销状态:" + lastMCRecordData);
                    RecordFactory.updateRecord(lastMCRecordData);
                }
            }
        } else {
            synchronized (mRecordBackUpCancelList) {
                if (0 != mRecordBackUpCancelList.size()) {
                    lastMCRecordData = mRecordBackUpCancelList.get(mRecordBackUpCancelList.size() - 1);
                    state = Integer.parseInt(lastMCRecordData.getState());
                    if (MCRecordData.STATE_NOT_COMMIT == state) {
                        //还未提交
                        LogUtil.defaultLog("撤销未提交的动作：" + lastMCRecordData.getEvent_detail());
                        mRecordBackUpCancelList.remove(lastMCRecordData);
                        mRecordWaitUploadDataList.remove(lastMCRecordData);
                    } else if (MCRecordData.STATE_COMMITED == state) {
                        LogUtil.defaultLog("撤销提交未有结果的动作：" + lastMCRecordData.getEvent_detail());
                        mRecordBackUpCancelList.remove(lastMCRecordData);
                    }

                    if (0 != mRecordBackUpCancelList.size()) {
                        updateData = mRecordBackUpCancelList.get(mRecordBackUpCancelList.size() - 1);
                    }
                }
            }

            if (null != mUpdateCallback) {
                mUpdateCallback.update(updateData);
            }

            if (null != lastMCRecordData) {
                mVibrator.vibrate(50); //震动一下
                if (MCRecordData.STATE_COMMITED == state) {
                    //对于已经提交的操作，从数据库中删除
                    LogUtil.d(TAG, "delete db record server id: " + lastMCRecordData.get_id() + "; event key: " + lastMCRecordData.getEvent_detail());
                    RecordFactory.deleteRecordByEventKey(lastMCRecordData.getEvent_detail());
                }
            }
        }
        if (lastMCRecordData == null) {
            return -1;
        }
        return TextUtils.isEmpty(lastMCRecordData.getCode()) ? -1 : Integer.parseInt(lastMCRecordData.getCode());
    }

    /**
     * 处理RecordEvent是否已结束，当发起网络请求时置为false,等网络数据返回处理完成置为true
     */
    private boolean isHandleRecordEventFinished = true;

    /**
     * 处理事件
     *
     * @return 是否已经处理完成，当有网络请求时返回false
     */
    private boolean handleRecordEvent() {
        if (0 == mRecordWaitUploadDataList.size()) {
            return true;
        }

        ArrayList<MCRecordData> MCRecordDatas = new ArrayList<>();
        synchronized (mRecordWaitUploadDataList) {
            MCRecordDatas.addAll(mRecordWaitUploadDataList);
        }

        if (isRealTime) {
            //实时处理事件
            ArrayList<MCRecordData> needCommitList = new ArrayList<>();
            ArrayList<MCRecordData> newCommitList = new ArrayList<>();
            for (MCRecordData record : MCRecordDatas) {
                LogUtil.defaultLog("handle the event of real action , queue size: " + MCRecordDatas.size() + ", data: " + record.getCode());
                int state = Integer.parseInt(record.getState());
                if (MCRecordData.STATE_NOT_COMMIT == state) {
                    record.setState(MCRecordData.STATE_COMMITED + "");
                    needCommitList.add(record);
                    newCommitList.add(record);
                } else if (MCRecordData.STATE_FAILED == state) {
                    record.setState(MCRecordData.STATE_COMMITED + "");
                    needCommitList.add(record);
                } else if (MCRecordData.STATE_COMMITED == state) {
                    LogUtil.defaultLog("this data already exist in db: " + record);
                    needCommitList.add(record);
                }
            }

            //只将新提交的数据插入到数据库中
            if (0 != newCommitList.size()) {
                LogUtil.defaultLog("save new data to db , queue size: " + newCommitList.size() + ", data: " + newCommitList);
                RecordFactory.insertRecords(newCommitList);
            }
            LogUtil.defaultLog("isHandleRecordEventFinished: " + isHandleRecordEventFinished);
            if (needCommitList != null && needCommitList.size() > 0) {
                if (isHandleRecordEventFinished) {
                    isHandleRecordEventFinished = false;
                    LogUtil.defaultLog("send network request");
                    //发送网络请求
//                    uploadEvent(needCommitList);
                    return false;
                } else {
                    LogUtil.defaultLog("the last result not return,waiting...");
                }
            } else {
                LogUtil.defaultLog("network queue is null!");
            }
        } else {
            LogUtil.defaultLog("save data to db, queue size: " + MCRecordDatas.size() + ", data: " + MCRecordDatas);
            for (MCRecordData MCRecordData : MCRecordDatas) {
                MCRecordData.setState(MCRecordData.STATE_COMMITED + "");
            }
            //存储至数据库中
            RecordFactory.insertRecords(MCRecordDatas);
        }

        return true;
    }

    /**
     * 视比赛状态更改球员背景
     *
     * @param isPlaying
     */
    public void changePlayerBackground(boolean isPlaying, boolean isUndo) {
        if (!isPlaying) { //从暂停状态恢复比赛不选中球员

            if (mSelectedPlayer != null) { //球员灯亮的情况下改变比赛进程,生成当前球员控球事件,点球大战阶段除外
                generateEvent(EventCode.Ctrl, MCConstants.EventType.EVENT_ACTION, "", 0);
                LogUtil.defaultLog("change background generate ctrl event");
            }
            mSelectedPlayer = null;
        }
        if (mPlayerAdapter != null) {
            mPlayerAdapter.notifyDataSetChanged(isPlaying, isUndo);
        }
    }

    /**
     * 用于退出统计时调用，上传未发送的数据
     */
    public boolean uploadData() {
        isHandleRecordEventFinished = true;
        mLastSelectedPlayerTime = System.currentTimeMillis();
        return handleRecordEvent();
    }

    @Override
    public void onDestroy() {
        unRegisterBroadcast();
        super.onDestroy();
    }

    private ArrayList<StatsActionItem> generateActions() {
        //区分为base&advanced
        boolean highMode = (statsMode == 1);
        ArrayList<StatsActionItem> resultList = new ArrayList<>();
        //空
        resultList.add(null);
        //射
        StatsActionItem item_0 = new StatsActionItem(0, getString(R.string.shot), 0, true,
                new StatsActionItem[]{
                        new StatsActionItem(1, getString(R.string.shot_off_target), EventCode.ShotOffTarget),
                        new StatsActionItem(0, getString(R.string.shot), 0, true, null),
                        new StatsActionItem(1, getString(R.string.shot_on_target), EventCode.ShotOnTarget),
                });
        resultList.add(item_0);

        //空
        resultList.add(null);

        //进
        StatsActionItem item_1 = new StatsActionItem(0, getString(R.string.goal), EventCode.Goal, true, new StatsActionItem[]{
                null,
                null,
                null,
                new StatsActionItem(1, getString(R.string.goal), EventCode.Goal, true, null),
                new StatsActionItem(1, getString(R.string.goal_assist), EventCode.GoalAssist),
                null,
                new StatsActionItem(1, getString(R.string.goal_own), EventCode.OwnGoal),
                null,
        });
        resultList.add(item_1);

        //防
        StatsActionItem item_2;
        if (highMode) {
            item_2 = new StatsActionItem(0, getString(R.string.challenge), 0, true,
                    new StatsActionItem[]{
                            null,
                            null,
                            null,
                            new StatsActionItem(1, getString(R.string.tackle_lost), EventCode.TackleLost), //tackle_won
                            new StatsActionItem(0, getString(R.string.challenge), 0, true, null),
                            new StatsActionItem(1, getString(R.string.tackle_won), EventCode.TackleWon),
                            null,
                            new StatsActionItem(1, getString(R.string.interception), EventCode.Interception), //interception
                            new StatsActionItem(1, getString(R.string.clearance), EventCode.Clearance),
//                        new StatsActionItem(1, "扑救", EventCode.Save),
                    });
        } else {
            item_2 = new StatsActionItem(0, getString(R.string.tackle), 0, true,
                    new StatsActionItem[]{
                            null,
                            null,
                            null,
                            new StatsActionItem(1, getString(R.string.tackle_lost), EventCode.TackleLost),
//                            new StatsActionItem(0, "抢断", "", false, null),
                            new StatsActionItem(0, getString(R.string.tackle), 0, true, null),
                            new StatsActionItem(1, getString(R.string.tackle_won), EventCode.TackleWon),
                    });
        }
        resultList.add(item_2);

        //1v1
//        StatsActionItem item_0 = new StatsActionItem(0, "1v1", "", false,
//                new StatsActionItem[]{
//                        new StatsActionItem(0, "1v1", "", false, null),
//                        new StatsActionItem(1, "过人成功", EventCode.SuccessfulTakeOn),
//                        new StatsActionItem(1, "防守成功", EventCode.ChallengeWon),
//                        new StatsActionItem(1, "过人失败", EventCode.UnsuccessfulTakeOn),
//                        new StatsActionItem(1, "防守失败", EventCode.ChallengeLost),
//                        new StatsActionItem(1, "犯规", EventCode.FoulConceded),
//                });
//        resultList.add(item_0);
        //扑救
        StatsActionItem item_3 = new StatsActionItem(0, getString(R.string.save), EventCode.Save, true,
                new StatsActionItem[]{
                        null,
                        null,
                        null,
                        null,
                        null,
                        new StatsActionItem(0, getString(R.string.save), EventCode.Save, true, null)
                });
        resultList.add(item_3);

        //犯
        StatsActionItem item_4;
        if (highMode) {

            item_4 = new StatsActionItem(0, getString(R.string.foul_conceded), EventCode.FoulConceded, true,
                    new StatsActionItem[]{
                            null,
                            null,
                            null,
                            new StatsActionItem(1, getString(R.string.offside), EventCode.Offside),
                            new StatsActionItem(1, getString(R.string.red_card), EventCode.RedCard),
                            null,
                            new StatsActionItem(0, getString(R.string.foul_conceded), EventCode.FoulConceded, true, null),
                            new StatsActionItem(1, getString(R.string.yellow_card), EventCode.YellowCard),
                            null,
                    });
        } else {
            item_4 = new StatsActionItem(0, getString(R.string.punish_card), 0, true,
                    new StatsActionItem[]{
                            null,
                            null,
                            null,
                            null,
                            new StatsActionItem(1, getString(R.string.red_card), EventCode.RedCard),
                            null,
                            new StatsActionItem(0, getString(R.string.punish_card), 0, true, null),
                            new StatsActionItem(1, getString(R.string.yellow_card), EventCode.YellowCard),
                            null,
                    });
        }
        resultList.add(item_4);

        //传
        StatsActionItem item_5;
        if (highMode) {
            item_5 = new StatsActionItem(0, getString(R.string.pass_ball), EventCode.SuccessfulPass, true,
                    new StatsActionItem[]{
                            null,
                            null,
                            null,
                            null,
                            new StatsActionItem(1, getString(R.string.key_pass), EventCode.KeyPass),
                            null,
                            new StatsActionItem(1, getString(R.string.unsuccessful_pass), EventCode.UnsuccessfulPass),
                            new StatsActionItem(0, getString(R.string.pass_ball), EventCode.SuccessfulPass, true, null),
                            new StatsActionItem(1, getString(R.string.successful_pass), EventCode.SuccessfulPass),
                    });
        } else {
            item_5 = new StatsActionItem(0, getString(R.string.pass_ball), EventCode.SuccessfulPass, true,
                    new StatsActionItem[]{
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            new StatsActionItem(1, getString(R.string.unsuccessful_pass), EventCode.UnsuccessfulPass),
                            new StatsActionItem(0, getString(R.string.pass_ball), EventCode.SuccessfulPass, true, null),
                            new StatsActionItem(1, getString(R.string.successful_pass), EventCode.SuccessfulPass),
                    });
        }
        resultList.add(item_5);

        //定
        if (highMode) {
            StatsActionItem item_6 = new StatsActionItem(0, getString(R.string.placement), 0, true, //placement
                    new StatsActionItem[]{
                            null,
                            null,
                            null,
                            null,
                            new StatsActionItem(1, getString(R.string.free_kick), EventCode.FreeKick),
                            new StatsActionItem(1, getString(R.string.corner), EventCode.Corner),
                            new StatsActionItem(1, getString(R.string.penalty_kick), EventCode.PenaltyKick),
                            new StatsActionItem(1, getString(R.string.throw_in), EventCode.ThrowIn),
                            new StatsActionItem(0, getString(R.string.placement), 0, true, null),
                    });
            resultList.add(item_6);
        }
        mLastSelectedPlayerTime = System.currentTimeMillis();
        return resultList;
    }


}
