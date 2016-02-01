package com.idyll.mutualcomm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.entity.StatsMatchFormationBean;
import com.idyll.mutualcomm.socket.MCSocketServer;
import com.idyll.mutualcomm.utils.NetWorkUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sponia.foundationmoudle.BaseActivity;
import com.sponia.foundationmoudle.common.PreventContinuousClick;
import com.sponia.foundationmoudle.utils.CellphoneUtil;
import com.sponia.foundationmoudle.utils.SponiaToastUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.activity
 * @description 建立连接
 * @date 16/1/26
 */
public class StatsCommunicationActivity extends BaseActivity {
    private MaterialEditText metIp;
    private MaterialEditText metPort;
    private MaterialEditText met_send_content;
    private Button btnSend;
    private Button btnBootSocket;
    private String ip;
    private String port;
    private final ArrayList<String> playerNums = new ArrayList<>(Arrays.asList("1", "4", "6", "7", "9", "10", "11", "13", "22", "24", "25", "34", "44", "99"));
    private ArrayList<StatsMatchFormationBean> totalPlayers = new ArrayList<>();

    private StringBuffer mConsoleStr = new StringBuffer();
    private Socket mSocket;
    private boolean isStartRecieveMsg;

//    private SocketHandler mHandler;
    protected BufferedReader mReader;
    protected BufferedWriter mWriter;
    private MCSocketServer server;
    private static final int MSG_START_SOCKET = 0x001;
    private static final int MSG_START_ACTIVITY = 0x002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMidView(R.layout.activity_build_communication);
        initUI();
        initData();
    }

    private void initUI() {
        setActionbarTitle("建立连接");
        setActionBarTitle2Center();
        metIp = (MaterialEditText) findViewById(R.id.met_ip);
        metPort = (MaterialEditText) findViewById(R.id.met_port);
        met_send_content = (MaterialEditText) findViewById(R.id.met_send_content);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnBootSocket = (Button) findViewById(R.id.btn_start_socket);
        btnBootSocket.setOnClickListener(new PreventContinuousClick(this));
        btnSend.setOnClickListener(new PreventContinuousClick(this));

    }

    private void initData() {
        if (!CellphoneUtil.checkNetWorkAvailable()) {
            SponiaToastUtil.showShortToast("请先确保网络可用");
            finish();
        } else {
            metIp.setText(NetWorkUtil.getIP());
            addPlayers();
        }
    }


    private void addPlayers() {
        for (String number : playerNums) {
            totalPlayers.add(new StatsMatchFormationBean(UUID.randomUUID().toString(), number));
        }
    }

    private void startSocket() {
        server = new MCSocketServer();
        port = metPort.getText().toString();
        if (TextUtils.isEmpty(port)) {
            SponiaToastUtil.showShortToast("请设置端口号");
            return;
        }
        server.startSocket(Integer.parseInt(port));
    }

    private void startSocketServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startSocket();
            }
        }).start();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_SOCKET:
                    try {
                        JSONObject json = new JSONObject((String) msg.obj);
                        Toast.makeText(StatsCommunicationActivity.this, "收到新消息：" + msg, Toast.LENGTH_SHORT).show();
                        mConsoleStr.append(json.getString("from") + ":" + json.getString("msg") + "   " + getTime(System.currentTimeMillis()) + "\n");
                        SponiaToastUtil.showShortToast(mConsoleStr.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_START_ACTIVITY:
                    if (server != null && server.getIsStartServer()) {
                        startActivity(new Intent(StatsCommunicationActivity.this, StatsOperateActivity.class).putExtra("playerList", totalPlayers).putExtra("matchId", "matchId").putExtra("ip", ip).putExtra("port", port).putExtra("matchId", "matchIdSponia").putExtra("teamId", "teamIdSponia"));
                    } else {
                        SponiaToastUtil.showShortToast("端口已被占用,请更换端口或重启应用");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_start_socket:
                startSocketServer();
                mHandler.sendEmptyMessageDelayed(MSG_START_ACTIVITY, 300);
                break;
            case R.id.btn_send:
//                send();
                break;
            case R.id.fly_title_right3:
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isStartRecieveMsg = false;
    }

    private String getTime(long millTime) {
        Date d = new Date(millTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(d));
        return sdf.format(d);
    }
}
