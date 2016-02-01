package com.idyll.mutualcomm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.entity.MCPlayerTextItem;

import java.util.ArrayList;

/**
 * @author shibo
 * @packageName com.sponia.soccerstats.adapter
 * @description 球员Adapter
 * @date 15/9/25
 */
public class StatsPlayerAdapter extends BaseAdapter {
    private final ArrayList<MCPlayerTextItem> list;
    private final LayoutInflater mInflater;

    private int mItemWidth;
    private int mItemHeight;
    //比赛是否进行
    private boolean isPlaying;
    //是否为撤销动作
    private boolean isUndo;
    //上下文
    private Context mContext;

    public StatsPlayerAdapter(Context context, ArrayList<MCPlayerTextItem> list) {
        this.list = list;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);

    }

    public void setItemWidthAndHeight(int width, int height) {
        mItemWidth = width;
        mItemHeight = height;
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlayerViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_choose_player, null, false);
            holder = new PlayerViewHolder();
            holder.numberTv = (TextView) convertView.findViewById(R.id.number_tv);
//            holder.numberTv.setLayoutParams(new FrameLayout.LayoutParams((mItemWidth), (mItemHeight), Gravity.CENTER));
            convertView.setTag(holder);
        } else {
            holder = (PlayerViewHolder) convertView.getTag();
        }
        MCPlayerTextItem item = list.get(position);
        if (null != item && null != item.player) {
            holder.numberTv.setText(item.player.Player_Num);
            int redColor = mContext.getResources().getColor(R.color.R);
            int color = (0 == (position / 3 + position % 3) % 2) ? Color.WHITE : redColor;
            if (isPlaying) {
                if (isUndo) {
//                    holder.numberTv.setSelected(false);
                    holder.numberTv.setSelected(item.selected);
                } else {
                    holder.numberTv.setSelected(item.selected);
                }
                holder.numberTv.setTextColor(color);
                holder.numberTv.setBackgroundResource(R.drawable.gray_ring_bg_selector);
            } else {
                //恢复比赛时不点亮之前选中的球员
                item.selected = false;
                holder.numberTv.setTextColor(Color.GRAY);
                holder.numberTv.setBackgroundResource(0);
            }
        } else {
            holder.numberTv.setText("");
            holder.numberTv.setBackgroundResource(0);
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

    static class PlayerViewHolder {
        TextView numberTv;
    }
}
