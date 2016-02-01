package com.idyll.mutualcomm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.utils.NetWorkUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sponia.foundationmoudle.BaseActivity;
import com.sponia.foundationmoudle.common.PreventContinuousClick;
import com.sponia.foundationmoudle.utils.SponiaToastUtil;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.activity
 * @description 建立连接
 * @date 16/1/26
 */
public class EditCommunicationActivity extends BaseActivity {
    private MaterialEditText metIp;
    private MaterialEditText metPort;
    private MaterialEditText met_socket_id;
    private MaterialEditText met_send_content;
    private Button btnSend;
    private Button btnBootSocket;
    private String ip;
    private String port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMidView(R.layout.activity_edit_communication);
        initUI();
        initData();
    }

    private void initUI() {
//        setActionBarBackground(R.color.op_orange);
        setActionbarTitle("建立连接");
        setActionBarTitle2Center();
        met_socket_id = (MaterialEditText) findViewById(R.id.met_socket_id);
        metIp = (MaterialEditText) findViewById(R.id.met_ip);
        metPort = (MaterialEditText) findViewById(R.id.met_port);
        met_send_content = (MaterialEditText) findViewById(R.id.met_send_content);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnBootSocket = (Button) findViewById(R.id.btn_start_socket);
        btnBootSocket.setOnClickListener(new PreventContinuousClick(this));
        btnSend.setOnClickListener(new PreventContinuousClick(this));
    }

    private void initData() {
        metIp.setText(NetWorkUtil.getIP());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_start_socket:

                ip = metIp.getText().toString().trim();
                port = metPort.getText().toString().trim();
                if (!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(port)) {
                    startActivity(new Intent(this, EditEventActivity.class).putExtra("ip", ip).putExtra("port", Integer.parseInt(port)));
                } else {
                    SponiaToastUtil.showShortToast("请填写正确的ip和port");
                }
                break;
            case R.id.btn_send:
                break;
            default:
                break;
        }
    }

}
