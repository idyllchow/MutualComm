package com.idyll.mutualcomm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.global.SpCode;
import com.sponia.foundationmoudle.BaseActivity;
import com.sponia.foundationmoudle.common.PreventContinuousClick;
import com.sponia.foundationmoudle.utils.SponiaSpUtil;
import com.sponia.foundationmoudle.utils.SponiaToastUtil;

import java.util.HashSet;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.activity
 * @description 登录界面
 * @date 16/1/25
 */
public class LoginActivity extends BaseActivity {

    private TextView tvStats;
    private TextView tvEdit;
    //退出应用时间
    private long exitTime = 0;
    //退出时间间隔
    private static final int EXIT_INTERVAL = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMidView(R.layout.activity_loging);
        initView();
        initData();
    }

    private void initView() {
        setActionbarTitle("选择功能");
        setActionBarTitle2Center();
        tvStats = (TextView) findViewById(R.id.tv_stats);
        tvEdit = (TextView) findViewById(R.id.tv_edit);
        tvStats.setOnClickListener(new PreventContinuousClick(this));
        tvEdit.setOnClickListener(new PreventContinuousClick(this));
    }

    private void initData() {
        SponiaSpUtil.setDefaultSpValue(SpCode.SOCKET_ID, new HashSet<>());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()) {
            case R.id.tv_stats:
//                startSocketServer();
                startActivity(new Intent(this, StatsCommunicationActivity.class));
                break;
            case R.id.tv_edit:
                startActivity(new Intent(this, EditCommunicationActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exitAPP();
    }

    /**
     * 退出应用
     */
    private void exitAPP() {
        if ((System.currentTimeMillis() - exitTime) > EXIT_INTERVAL) {
            SponiaToastUtil.showShortToast(getString(R.string.exit_prompt));
            exitTime = System.currentTimeMillis();
        } else {
//            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }
}
