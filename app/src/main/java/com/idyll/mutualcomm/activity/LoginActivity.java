package com.idyll.mutualcomm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.idyll.mutualcomm.R;
import com.sponia.foundationmoudle.BaseActivity;
import com.sponia.foundationmoudle.common.PreventContinuousClick;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {

    private TextView tvStats;
    private TextView tvEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMidView(R.layout.activity_loging);
        initView();
    }

    private void initView() {
        tvStats = (TextView) findViewById(R.id.tv_stats);
        tvEdit = (TextView) findViewById(R.id.tv_edit);
        tvStats.setOnClickListener(new PreventContinuousClick(this));
        tvEdit.setOnClickListener(new PreventContinuousClick(this));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()) {
            case R.id.tv_stats:
                startActivity(new Intent(this, InputPlayerActivity.class));
                break;
            case R.id.tv_edit:
                break;
            default:
                break;
        }
    }
}
