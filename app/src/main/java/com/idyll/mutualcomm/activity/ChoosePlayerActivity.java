package com.idyll.mutualcomm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.adapter.MCPlayerAdapter;
import com.idyll.mutualcomm.global.MCConstants;
import com.idyll.mutualcomm.entity.StatsMatchFormationBean;
import com.idyll.mutualcomm.view.MCGridView;
import com.sponia.foundationmoudle.BaseActivity;
import com.sponia.foundationmoudle.utils.LogUtil;
import com.sponia.foundationmoudle.utils.SponiaToastUtil;
import com.sponia.foundationmoudle.view.sweetalert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.activity
 * @description 选择球员界面
 * @date 16/1/25
 */
public class ChoosePlayerActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    //球员GridView
    private MCGridView gvPlayer;
    private MCPlayerAdapter adapter;
    private ArrayList<StatsMatchFormationBean> totalPlayers = new ArrayList<>();
    //所选中首发球员
    private ArrayList<StatsMatchFormationBean> fieldPlayers = new ArrayList<>();
    //所选中球员position
//    private int mSelectedPosition = -1;
    //matchId
    private String matchId;
    //teamId
    private String teamId;
    //球员号码list
    //1、4、7、44、6、9、10、11、13、22、24、25、34、99
    private final ArrayList<String> playerNums = new ArrayList<>(Arrays.asList("1", "4", "6", "7", "9", "10", "11", "13", "22", "24", "25", "34", "44", "99"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMidView(R.layout.activity_choose_player);
        initUI();
        initData();
    }

    private void initUI() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setActionbarTitle(Html.fromHtml("<font color=\"#E3FFFFFF\">" + "选择上场球员(" + "</font>" + "<font color=\"#17C4C4\">" + 0 + "</font>" + "<font color=\"#E3FFFFFF\">" + "/14)" + "</font>"));
        setTitleLeftImg(R.mipmap.icon_back_green);
        setTitleRight3Background(R.drawable.bg_green);
        setTitleRight3Text(getString(R.string.complete));
        gvPlayer = (MCGridView) findViewById(R.id.gv_player);
        gvPlayer.setOnItemClickListener(this);
    }

    private void initData() {
        if (getIntent() != null) {
            for (String number : playerNums) {
                totalPlayers.add(new StatsMatchFormationBean("", number));
                LogUtil.defaultLog("playerNums---->" + number);
            }
//            totalPlayers = getIntent().getParcelableArrayListExtra(MCConstants.TOTAL_PLAYERS);
//            matchId = getIntent().getStringExtra(MCConstants.MATCH_ID);
//            teamId = getIntent().getStringExtra(MCConstants.TEAM_ID);
        }
        adapter = new MCPlayerAdapter(this, totalPlayers);
        gvPlayer.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StatsMatchFormationBean MCPlayerTextItem = (StatsMatchFormationBean) adapter.getItem(position);
        if (MCPlayerTextItem == null) {
            return;
        }
        if (fieldPlayers.size() >= 11 && !MCPlayerTextItem.selected) {
            SponiaToastUtil.showShortToast(getResources().getString(R.string.at_most_eleven));
            return;
        }
        if (!MCPlayerTextItem.selected) {
            MCPlayerTextItem.selected = true;
        } else {
            MCPlayerTextItem.selected = false;
        }
        if (MCPlayerTextItem.selected) {
            fieldPlayers.add(MCPlayerTextItem);
        } else {
            fieldPlayers.remove(MCPlayerTextItem);
        }
        setActionbarTitle(Html.fromHtml("<font color=\"#E3FFFFFF\">" + "选择上场球员(" + "</font>" + "<font color=\"#17C4C4\">" + fieldPlayers.size() + "</font>" + "<font color=\"#E3FFFFFF\">" + "/14)" + "</font>"));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fly_title_left:
                break;
            case R.id.fly_title_right2:
                break;
            case R.id.fly_title_right3:
                if (fieldPlayers.size() > 0) {
                    skip2StatsPage();
                } else {
                    show1BtnDialog(SweetAlertDialog.NORMAL_TYPE, getString(R.string.prompt), getString(R.string.choose_player), null);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 跳转至统计页面
     */
    private void skip2StatsPage() {
        Intent intent = new Intent(this, StatsActivity.class);
        for (StatsMatchFormationBean MCPlayerTextItem : fieldPlayers) {
            MCPlayerTextItem.selected = false;
        }
        Collections.sort(fieldPlayers);
        intent.putExtra(MCConstants.TeamData.FIELD_PLAYERS, fieldPlayers);
        intent.putExtra(MCConstants.TeamData.TOTAL_PLAYERS, totalPlayers);
        intent.putExtra(MCConstants.TeamData.MATCH_ID, matchId);
        intent.putExtra(MCConstants.TeamData.TEAM_ID, teamId);
        startActivity(intent);
        this.finish();
    }

}
