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
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.adapter.MCActionDetailAdapter;
import com.idyll.mutualcomm.adapter.MCEventAdapter;
import com.idyll.mutualcomm.comm.MCConstants;
import com.idyll.mutualcomm.entity.EditActionItem;
import com.idyll.mutualcomm.event.EventCode;
import com.idyll.mutualcomm.event.MCRecordData;
import com.idyll.mutualcomm.event.RecordFactory;
import com.idyll.mutualcomm.view.MCGridView;
import com.sponia.foundationmoudle.BaseActivity;
import com.sponia.foundationmoudle.utils.CellphoneUtil;
import com.sponia.foundationmoudle.utils.DensityUtil;
import com.sponia.foundationmoudle.utils.LogUtil;
import com.sponia.foundationmoudle.utils.SponiaToastUtil;
import com.sponia.foundationmoudle.view.sweetalert.SweetAlertDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.activity
 * @description
 * @date 16/1/26
 */
public class EditEventActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private MCGridView gvDetail;
    private MCGridView gvDetailMode; //gv_detail_mode
    private MCGridView gvDetailAssist; //gv_detail_mode
    private MCActionDetailAdapter actionAdapter;
    private MCActionDetailAdapter actionModeAdapter;
    private MCActionDetailAdapter actionAssistAdapter;
    private MCEventAdapter eventAdapter;
    private ListView listView;

    private ArrayList<MCRecordData> dbList = new ArrayList<>();
    //初始事件
    private String initEvent;
    //事件时间
    private String eventTime;
    private String ip;
    private int port;
    private String matchId;
    private String teamId;

    private StringBuffer mConsoleStr = new StringBuffer();
    private Socket mSocket;
    private boolean isStartRecieveMsg;
    protected BufferedReader mReader;
    protected BufferedWriter mWriter;
    private String playerNum;
    private String eventCode;
    private String matchTime;
    private String clientStartAt;
    //导出和登pw
    private PopupWindow mSettingPopupWindow;

    private static final int MSG_RECEIVE_MSG = 0x001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMidView(R.layout.activity_edit_event);
        initUI();
        initData();
    }

    private void initUI() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setActionBarTitle2Center();
        setActionbarTitle("事件编辑");
        setTitleLeftBackground(R.drawable.bg_op_red);
        setTitleLeftText("撤销");
        setTitleRight3Image(R.mipmap.ic_data_menu);

        listView = (ListView) findViewById(R.id.list_view);
        gvDetail = (MCGridView) findViewById(R.id.gv_detail);
        gvDetailMode = (MCGridView) findViewById(R.id.gv_detail_mode);
        gvDetailAssist = (MCGridView) findViewById(R.id.gv_detail_assist);
        actionAdapter = new MCActionDetailAdapter(this, generateActions());
        actionAdapter.setItemWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, 10);
        actionModeAdapter = new MCActionDetailAdapter(this, generateModeActions());
        actionModeAdapter.setItemWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, 10);
        actionAssistAdapter = new MCActionDetailAdapter(this, generateAssistActions());
        actionAssistAdapter.setItemWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, 10);
        gvDetail.setAdapter(actionAdapter);
        gvDetail.setOnItemClickListener(this);
        gvDetailMode.setAdapter(actionModeAdapter);
        gvDetailMode.setOnItemClickListener(this);
        gvDetailAssist.setAdapter(actionAssistAdapter);
        gvDetailAssist.setOnItemClickListener(this);


        eventAdapter = new MCEventAdapter(this, dbList);
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(this);
    }

    private void initData() {
        if (getIntent() != null) {
            ip = getIntent().getStringExtra("ip");
            port = getIntent().getIntExtra("port", 7000);
            matchId = "matchIdSponia";
            teamId = "teamIdSponia";
        }

        if (!isStartRecieveMsg) {
            initSocket();
        }

        dbList.clear();
        dbList.addAll(RecordFactory.queryRecords(matchId, teamId));
        eventAdapter.notifyDataSetChanged();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_RECEIVE_MSG:
                    try {
                        JSONObject json = new JSONObject((String) msg.obj);
                        mConsoleStr.append(json.getString("from") + ":" + json.getString("msg") + "   " + "\n");
//                        mConsoleTxt.setText(mConsoleStr);
//                        SponiaToastUtil.showShortToast(mConsoleStr.toString());
                        playerNum = json.getString("playerNum");
                        eventCode = json.getString("eventCode");
                        matchTime = json.getString("matchTime");
                        clientStartAt = json.getString("clientStartAt");
//                        eventList.add(new MCRecordData(i + "", EventCode.sEventNameMap.get(Integer.parseInt(eventCode)), matchId, teamId, playerNum, eventCode, "", "", "", "", clientStartAt, "", matchTime, ""));
                        RecordFactory.insertRecord((new MCRecordData(UUID.randomUUID() + "", EventCode.sEventNameMap.get(Integer.parseInt(eventCode)), matchId, teamId, playerNum, eventCode, "", "", "", "", clientStartAt, "", matchTime, "")));
                        dbList.clear();
                        dbList.addAll(RecordFactory.queryRecords(matchId, teamId));
                        eventAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

        }
    };

    private int selectedIndex = -1;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.list_view:
                if (eventAdapter.getSelectedPosition() == position) {
                    position = -1;
                    actionAdapter.setSelectedItem(position);
                    actionModeAdapter.setSelectedItem(position);
                    actionAssistAdapter.setSelectedItem(position);
                }
                eventAdapter.setSelectedItem(position);
                selectedIndex = position;
                LogUtil.defaultLog("click item---->" + position);
                if (selectedIndex != -1 && eventAdapter.getItem(selectedIndex) != null) {
                    String location = ((MCRecordData) eventAdapter.getItem(selectedIndex)).getEvent_location();
                    String mode = ((MCRecordData) eventAdapter.getItem(selectedIndex)).getEvent_mode();
                    String assist = ((MCRecordData) eventAdapter.getItem(selectedIndex)).getHas_assist();
                    int locationPosition = -1;
                    int modePosition = -1;
                    int assistPosition = -1;
                    if (!TextUtils.isEmpty(location) && location.equals("小禁区")) {
                        locationPosition = 0;
                    } else if (!TextUtils.isEmpty(location) && location.equals("大禁区左侧")) {
                        locationPosition = 1;
                    } else if (!TextUtils.isEmpty(location) && location.equals("大禁区右侧")) {
                        locationPosition = 2;
                    } else if (!TextUtils.isEmpty(location) && location.equals("禁区外左侧")) {
                        locationPosition = 3;
                    } else if (!TextUtils.isEmpty(location) && location.equals("禁区外右侧")) {
                        locationPosition = 4;
                    } else if (!TextUtils.isEmpty(location) && location.equals("禁区外中左")) {
                        locationPosition = 5;
                    } else if (!TextUtils.isEmpty(location) && location.equals("禁区外中右")) {
                        locationPosition = 6;
                    } else if (!TextUtils.isEmpty(location) && location.equals("超过半场")) {
                        locationPosition = 7;
                    }
                    LogUtil.defaultLog("location: " + location);
                    actionAdapter.setSelectedItem(locationPosition);

                    if (!TextUtils.isEmpty(mode) && mode.equals("其他")) {
                        modePosition = 0;
                    } else if (!TextUtils.isEmpty(mode) && mode.equals("头球")) {
                        modePosition = 1;
                    } else if (!TextUtils.isEmpty(mode) && mode.equals("右脚")) {
                        modePosition = 2;
                    } else if (!TextUtils.isEmpty(mode) && mode.equals("左脚")) {
                        modePosition = 3;
                    }
                    actionModeAdapter.setSelectedItem(modePosition);

                    if (!TextUtils.isEmpty(assist) && assist.equals("蓄意助攻")) {
                        assistPosition = 0;
                    } else if (!TextUtils.isEmpty(assist) && assist.equals("无意助攻")) {
                        assistPosition = 1;
                    } else if (!TextUtils.isEmpty(assist) && assist.equals("无助攻")) {
                        assistPosition = 2;
                    }
                    actionAssistAdapter.setSelectedItem(assistPosition);

                }

                eventAdapter.notifyDataSetChanged();
                break;
            case R.id.gv_detail:
                if (selectedIndex == -1) {
                    SponiaToastUtil.showShortToast("请先选中事件");
                    return;
                }
                if (actionAdapter.getSelectedPosition() == position) {
                    position = -1;
                }
                actionAdapter.setSelectedItem(position);

                EditActionItem actionItem = (EditActionItem) actionAdapter.getItem(position);
                LogUtil.defaultLog("actionItem == " + actionItem + "; position == " + position);
                if (actionItem != null) {
                    MCRecordData recordData = RecordFactory.queryRecords(matchId, teamId).get(selectedIndex);
                    recordData.setEvent_location(actionItem.action);
                    RecordFactory.updateRecord(recordData);
                }
                break;
            case R.id.gv_detail_mode:
                if (selectedIndex == -1) {
                    SponiaToastUtil.showShortToast("请先选中事件");
                    return;
                }
                if (actionModeAdapter.getSelectedPosition() == position) {
                    position = -1;
                }
                actionModeAdapter.setSelectedItem(position);
                EditActionItem actionModeItem = (EditActionItem) actionModeAdapter.getItem(position);
                if (actionModeItem != null) {
                    MCRecordData recordData = RecordFactory.queryRecords(matchId, teamId).get(selectedIndex);
                    recordData.setEvent_mode(actionModeItem.action);
                    RecordFactory.updateRecord(recordData);
                }
                break;
            case R.id.gv_detail_assist:
                if (selectedIndex == -1) {
                    SponiaToastUtil.showShortToast("请先选中事件");
                    return;
                }
                if (actionAssistAdapter.getSelectedPosition() == position) {
                    position = -1;
                }
                actionAssistAdapter.setSelectedItem(position);
                EditActionItem actionAssistItem = (EditActionItem) actionAssistAdapter.getItem(position);
                if (actionAssistItem != null) {
                    MCRecordData recordData = RecordFactory.queryRecords(matchId, teamId).get(selectedIndex);
                    recordData.setHas_assist(actionAssistItem.action);
                    RecordFactory.updateRecord(recordData);
                }
                break;
            default:
                break;

        }
    }

    /**
     * 生成编辑事件
     *
     * @return
     */
    private ArrayList<EditActionItem> generateActions() {
        ArrayList<EditActionItem> results = new ArrayList<>();
        EditActionItem item11 = new EditActionItem(0, "小禁区", 0, MCConstants.EditEventType.LOCATION_EVENT);
        EditActionItem item12 = new EditActionItem(0, "大禁区左侧", 0, MCConstants.EditEventType.LOCATION_EVENT);
        EditActionItem item13 = new EditActionItem(0, "大禁区右侧", 0, MCConstants.EditEventType.LOCATION_EVENT);
        EditActionItem item14 = new EditActionItem(0, "禁区外左侧", 0, MCConstants.EditEventType.LOCATION_EVENT);
        EditActionItem item21 = new EditActionItem(0, "禁区外右侧", 0, MCConstants.EditEventType.LOCATION_EVENT);
        EditActionItem item22 = new EditActionItem(0, "禁区外中左", 0, MCConstants.EditEventType.LOCATION_EVENT);
        EditActionItem item23 = new EditActionItem(0, "禁区外中右", 0, MCConstants.EditEventType.LOCATION_EVENT);
        EditActionItem item24 = new EditActionItem(0, "超过半场", 0, MCConstants.EditEventType.LOCATION_EVENT);
        results.add(item11);
        results.add(item12);
        results.add(item13);
        results.add(item14);
        results.add(item21);
        results.add(item22);
        results.add(item23);
        results.add(item24);
        return results;
    }

    private ArrayList<EditActionItem> generateModeActions() {
        ArrayList<EditActionItem> results = new ArrayList<>();
        EditActionItem item31 = new EditActionItem(0, "其他", 0, MCConstants.EditEventType.MODE_EVENT);
        EditActionItem item32 = new EditActionItem(0, "头球", 0, MCConstants.EditEventType.MODE_EVENT);
        EditActionItem item33 = new EditActionItem(0, "右脚", 0, MCConstants.EditEventType.MODE_EVENT);
        EditActionItem item34 = new EditActionItem(0, "左脚", 0, MCConstants.EditEventType.MODE_EVENT);
        results.add(item31);
        results.add(item32);
        results.add(item33);
        results.add(item34);
        return results;
    }

    private ArrayList<EditActionItem> generateAssistActions() {
        ArrayList<EditActionItem> results = new ArrayList<>();
        EditActionItem item41 = new EditActionItem(0, "蓄意助攻", 0, MCConstants.EditEventType.ASSIST_EVENT);
        EditActionItem item42 = new EditActionItem(0, "无意助攻", 0, MCConstants.EditEventType.ASSIST_EVENT);
        EditActionItem item43 = new EditActionItem(0, "无助攻", 0, MCConstants.EditEventType.ASSIST_EVENT);
        results.add(item41);
        results.add(item42);
        results.add(item43);
        return results;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fly_title_left:
                int size = RecordFactory.queryRecords(matchId, teamId).size();
                if (size > 0) {
                    SponiaToastUtil.showShortToast("撤销 " + RecordFactory.queryRecords(matchId, teamId).get(size - 1).getPlayer_id() + "号 " + RecordFactory.queryRecords(matchId, teamId).get(size - 1).getEvent_detail());
                    RecordFactory.queryRecords(matchId, teamId);
                    RecordFactory.deleteRecordByEventKey(RecordFactory.queryRecords(matchId, teamId).get(size - 1).get_id());
                    dbList.clear();
                    dbList.addAll(RecordFactory.queryRecords(matchId, teamId));
                    actionAdapter.setSelectedItem(-1);
                    actionModeAdapter.setSelectedItem(-1);
                    actionAssistAdapter.setSelectedItem(-1);
                    eventAdapter.setSelectedItem(-1);
                    eventAdapter.notifyDataSetChanged();
                } else {
                    SponiaToastUtil.showShortToast("当前无事件");
                }
                break;
            case R.id.fly_title_right3:
                showSettingMenu();
                break;
            case R.id.export_data_action:
                if (null != mSettingPopupWindow && mSettingPopupWindow.isShowing()) {
                    mSettingPopupWindow.dismiss();
                }
                show2BtnDialog(SweetAlertDialog.NORMAL_TYPE, "提示", "确定需要导出数据吗？", null, exportDataListener);
                break;
            case R.id.clear_action:
                if (null != mSettingPopupWindow && mSettingPopupWindow.isShowing()) {
                    mSettingPopupWindow.dismiss();
                }
                show2BtnDialog(SweetAlertDialog.NORMAL_TYPE, "提示", "确定需要清除数据吗？", null, clearDataListener);
                break;
            case R.id.exit_action:
                if (null != mSettingPopupWindow && mSettingPopupWindow.isShowing()) {
                    mSettingPopupWindow.dismiss();
                }
                show2BtnDialog(SweetAlertDialog.NORMAL_TYPE, "提示", "确定需要退出当前统计吗？", null, exitStatsListener);
                break;
            default:
                break;
        }
    }

    private void clearData() {
        RecordFactory.clearRecords();
        dbList.clear();
        eventAdapter.notifyDataSetChanged();
        actionAdapter.setSelectedItem(-1);
        actionModeAdapter.setSelectedItem(-1);
        actionAssistAdapter.setSelectedItem(-1);
        SponiaToastUtil.showShortToast("已清除所有数据");
    }

    /**
     * 导出本地数据
     */
    private void exportData() {
        String filePath = CellphoneUtil.getSDPath() + File.separator + "OpenPlay" + File.separator + "demo" + File.separator + "csv";
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        LogUtil.defaultLog("开始导出本地数据:" + System.currentTimeMillis());
        RecordFactory.exportToCSV(this, filePath, matchId, teamId);
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
     * 导出数据监听
     */
    private SweetAlertDialog.OnSweetClickListener clearDataListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            sweetAlertDialog.dismiss();
            clearData();
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
                    EditEventActivity.this.finish();
                }
            });
            sweetAlertDialog.dismiss();
        }
    };

    private void initSocket() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    isStartRecieveMsg = true;
                    mSocket = new Socket(ip, port);
                    mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "utf-8"));
                    mWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "utf-8"));
                    while (isStartRecieveMsg) {
                        if (mReader.ready()) {
                            mHandler.obtainMessage(MSG_RECEIVE_MSG, mReader.readLine()).sendToTarget();
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

    private void send() {
        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                sendMsg();
                return null;
            }
        }.execute();
    }

    protected void sendMsg() {
        try {
            String socketID = "";
            String msg = "";
            JSONObject json = new JSONObject();
            json.put("to", socketID);
            json.put("msg", msg);
            mWriter.write(json.toString() + "\n");
            mWriter.flush();
            mConsoleStr.append("我:" + msg + "   " + "\n");
            SponiaToastUtil.showShortToast(mConsoleStr.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 右上角导出数据和登出
     */
    private void showSettingMenu() {
        View menuView = LayoutInflater.from(this).inflate(R.layout.export_data_menu_layout, null, false);
        menuView.findViewById(R.id.export_data_action).setOnClickListener(this);
        menuView.findViewById(R.id.exit_action).setOnClickListener(this);
        menuView.findViewById(R.id.clear_action).setOnClickListener(this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isStartRecieveMsg = false;
    }
}
