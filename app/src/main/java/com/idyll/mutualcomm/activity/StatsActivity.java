package com.idyll.mutualcomm.activity;

import android.os.Bundle;
import android.view.WindowManager;

import com.sponia.foundationmoudle.BaseActivity;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.activity
 * @description
 * @date 16/1/25
 */
public class StatsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setActionBarGone();
    }
}
