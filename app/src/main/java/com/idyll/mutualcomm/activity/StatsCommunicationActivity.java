package com.idyll.mutualcomm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.entity.StatsMatchFormationBean;
import com.idyll.mutualcomm.socket.MCSocketServer;
import com.idyll.mutualcomm.utils.NetWorkUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sponia.foundationmoudle.BaseActivity;
import com.sponia.foundationmoudle.common.PreventContinuousClick;
import com.sponia.foundationmoudle.utils.CellphoneUtil;
import com.sponia.foundationmoudle.utils.LogUtil;
import com.sponia.foundationmoudle.utils.SponiaToastUtil;

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
    private int port;
    private final ArrayList<String> playerNums = new ArrayList<>(Arrays.asList("1", "4", "6", "7", "9", "10", "11", "13", "22", "24", "25", "34", "44", "99"));
    private ArrayList<StatsMatchFormationBean> totalPlayers = new ArrayList<>();
    private MCSocketServer server;
    private static final int MSG_START_ACTIVITY = 0x001;
    private CommHandler handler;

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
        handler = new CommHandler();
    }

    private void initData() {
        if (!CellphoneUtil.checkNetWorkAvailable()) {
            SponiaToastUtil.showShortToast("请先确保网络可用");
            finish();
        } else {
            metIp.setText(NetWorkUtil.getIP());
            addPlayers();
        }
        initLeancloud();
    }

    private void initLeancloud() {
        AVObject testObject = new AVObject("TestObject");
        testObject.put("words","Hello Sponia!");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    LogUtil.defaultLog("success for leancloud!");
                }
            }
        });
    }

    private void addPlayers() {
        for (String number : playerNums) {
            totalPlayers.add(new StatsMatchFormationBean(UUID.randomUUID().toString(), number));
        }
    }

    private void startSocket() {
        server = new MCSocketServer();
        String portStr = metPort.getText().toString();
        if (TextUtils.isEmpty(portStr)) {
            SponiaToastUtil.showShortToast("请设置端口号");
            return;
        }

        server.startSocket(Integer.parseInt(portStr));
    }

    private void startSocketServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startSocket();
            }
        }).start();
    }

    class CommHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_ACTIVITY:
//                    if (server != null && server.getIsStartServer()) {
//                    } else {
//                        SponiaToastUtil.showShortToast("端口已被占用,请更换端口或重启应用");
//                    }
                    Intent intent = new Intent(StatsCommunicationActivity.this, StatsOperateActivity.class);
                    intent.putExtra("playerList", totalPlayers);
                    intent.putExtra("matchId", "matchIdSponia");
                    intent.putExtra("ip", ip);
                    intent.putExtra("port", Integer.parseInt(metPort.getText().toString()));
                    intent.putExtra("teamId", "teamIdSponia");
                    intent.putExtra("matchType", 9);
                    startActivity(intent);
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
                if (server == null) {
                    startSocketServer();
                }
                handler.sendEmptyMessageDelayed(MSG_START_ACTIVITY, 300);
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
    }
}
