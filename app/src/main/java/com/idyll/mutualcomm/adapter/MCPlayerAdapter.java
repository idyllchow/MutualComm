package com.idyll.mutualcomm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.entity.MCPlayerTextItem;
import com.sponia.foundationmoudle.utils.LogUtil;

import java.util.ArrayList;

/**
 * @author shibo
 * @packageName com.sponia.stats.adapter
 * @description 球员Adapter
 * @date 15/9/25
 */
public class MCPlayerAdapter extends BaseAdapter {

    //上下文
    private Context mContext;
    private ArrayList<MCPlayerTextItem> players;
    private final LayoutInflater mInflater;

    private int mItemWidth;
    private int mItemHeight;
    //比赛是否进行
    private boolean isPlaying;
    //是否为撤销动作
    private boolean isUndo;


    public MCPlayerAdapter(Context context, ArrayList<MCPlayerTextItem> players) {
        this.players = players;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);

    }

    public void setItemWidthAndHeight(int width, int height) {
        mItemWidth = width;
        mItemHeight = height;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_choose_player, null, false);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.number_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MCPlayerTextItem item = players.get(position);
        if (players != null) { //背号有可能为0
            holder.tv.setText(item.player.Player_Num);
            LogUtil.defaultLog("Player_Num----->" + item.player.Player_Num);
            holder.tv.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.tv.setSelected(item.selected);
            holder.tv.setBackgroundResource(R.drawable.bg_black_ring_selector);
//            convertView.setVisibility(position == mHidePosition ? View.INVISIBLE : View.VISIBLE);
        } else {
            holder.tv.setText("");
            holder.tv.setBackgroundResource(0);
            convertView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    /**
     * 通知适配器更改
     *
     * @param isPlaying 比赛是否进行
     * @param isUndo    是否为撤销动作
     */
    public void notifyDataSetChanged(boolean isPlaying, boolean isUndo) {
        this.isPlaying = isPlaying;
        this.isUndo = isUndo;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tv;
    }

    /**
     *
     * @return
     */
    public ArrayList<MCPlayerTextItem> getSlectedPlayer() {
        ArrayList<MCPlayerTextItem> selectedPlayers = new ArrayList<>();
        return selectedPlayers;
    }
}
