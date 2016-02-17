package com.idyll.mutualcomm.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.idyll.mutualcomm.BuildConfig;
import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.entity.StatsMatchFormationBean;
import com.sponia.foundationmoudle.utils.LogUtil;
import com.sponia.foundationmoudle.utils.SponiaToastUtil;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author shibo
 * @packageName com.sponia.basketballstats.adapter
 * @description
 * @date 15/12/1
 */
public class MCInputPlayerAdapter extends BaseAdapter {
    private ArrayList<StatsMatchFormationBean> players;
    private final LayoutInflater mInflater;

    private int mItemWidth;
    private int mItemHeight;
    //比赛是否进行
    private boolean isPlaying;
    //是否为撤销动作
    private boolean isUndo;
    //上下文
    private Context mContext;

    public MCInputPlayerAdapter(Context context, ArrayList<StatsMatchFormationBean> players) {
        this.players = players;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return null == players ? 0 : players.size();
    }

    @Override
    public Object getItem(int position) {
        return players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_input_player, null, false);
            holder = new ViewHolder();
            holder.tvPlayer = (TextView) convertView.findViewById(R.id.tv_player);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StatsMatchFormationBean item = players.get(position);
        if (players != null) {
            holder.tvPlayer.setTextColor(mContext.getResources().getColor(R.color.white));
            if (position == players.size() - 1) {
                holder.tvPlayer.setHint("+");
                holder.tvPlayer.setTextSize(30);
                holder.tvPlayer.setInputType(InputType.TYPE_NULL);
                holder.tvPlayer.setHintTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                holder.tvPlayer.setInputType(InputType.TYPE_CLASS_NUMBER);
//                holder.tvPlayer.setHintTextColor(mContext.getResources().getColor(R.color.white_15));
                holder.tvPlayer.setHint(Html.fromHtml("<font size=\"16\">" + "输入" + "</font>"));
                holder.tvPlayer.setTextSize(20);
                //TODO for test
                if (BuildConfig.DEBUG) {
                    for (int i = 0; i < players.size(); i++) {
                        item.id = UUID.randomUUID().toString();
                        if (!TextUtils.isEmpty(item.Player_Num)) {
                            holder.tvPlayer.setText(item.Player_Num + "");
                        }
                    }
                }
            }
        } else {
            holder.tvPlayer.setText("");
            holder.tvPlayer.setBackgroundResource(0);
            convertView.setVisibility(View.VISIBLE);
        }

        if (position == players.size() - 1) {
            holder.tvPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StatsMatchFormationBean newPlayer = new StatsMatchFormationBean("", "");
                    players.add(newPlayer);
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.tvPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ((BaseActivity) mContext).show2BtnDialog(SweetAlertDialog.WARNING_TYPE, "选择添加球员的号码", "输入球员号码", null, null);
                    inputPlayerNumber(holder.tvPlayer, position);
                }
            });
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tvPlayer;
    }

    /**
     * 获取注册合格的球员
     *
     * @return
     */
    public ArrayList<StatsMatchFormationBean> getRegisterPlayers() {
        int size = players.size();
        ArrayList<StatsMatchFormationBean> registerPlayer = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (!TextUtils.isEmpty(players.get(i).id)) {
                registerPlayer.add(players.get(i));
            }
        }
        return registerPlayer;
    }

    /**
     * 录入球员号码
     *
     * @param view
     * @param position
     */
    private void inputPlayerNumber(final TextView view, final int position) {
        final EditText editText = new EditText(mContext);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        new AlertDialog.Builder(mContext).setTitle("选择添加球员的号码").setView(editText).setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String shirtNumber = TextUtils.isEmpty(editText.getText().toString().trim()) ? "" : editText.getText().toString().trim();
                try {
                    for (StatsMatchFormationBean item : players) {
                        if (shirtNumber.equals(item.Player_Num)) {
                            SponiaToastUtil.showShortToast("号码" + shirtNumber + "已注册,请输入其它号码");
                            return;
                        }
                    }
                    view.setText(shirtNumber);
                    players.get(position).Player_Num = shirtNumber;
                    players.get(position).id = UUID.randomUUID().toString();
                } catch (NumberFormatException e) {
                    LogUtil.defaultLog(e);
                }
            }
        }).show();
    }

}
