package com.idyll.mutualcomm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.entity.StatsMatchFormationBean;
import com.idyll.mutualcomm.view.IDrag;
import com.sponia.foundationmoudle.utils.LogUtil;

import java.util.ArrayList;

/**
 * @author shibo
 * @packageName com.sponia.soccerstats.adapter
 * @description 球员Adapter
 * @date 15/9/25
 */
public class StatsPlayerAdapter extends BaseAdapter implements IDrag  {
    private final ArrayList<StatsMatchFormationBean> list;
    private final LayoutInflater mInflater;

    //比赛是否进行
    private boolean isPlaying;
    //是否为撤销动作
    private boolean isUndo;
    //上下文
    private Context mContext;
    private int mHidePosition = -1;

//    public StatsPlayerAdapter(Context context, ArrayList<MCPlayerTextItem> list) {
    public StatsPlayerAdapter(Context context, ArrayList<StatsMatchFormationBean> list) {
        this.list = list;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);

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
//            convertView = mInflater.inflate(R.layout.item_choose_player, null, false);
            convertView = mInflater.inflate(R.layout.item_left_player_num_grid, null, false);
            holder = new PlayerViewHolder();
            holder.numberTv = (TextView) convertView.findViewById(R.id.number_tv);
            convertView.setTag(holder);
        } else {
            holder = (PlayerViewHolder) convertView.getTag();
        }
        StatsMatchFormationBean item = list.get(position);
        if (item != null && !TextUtils.isEmpty(item.id)) {
            holder.numberTv.setText(item.Player_Num);
            if (isPlaying) {
                holder.numberTv.setSelected(item.selected);
                holder.numberTv.setBackgroundResource(R.drawable.gray_ring_bg_selector);
            } else {
                //恢复比赛时不点亮之前选中的球员
                item.selected = false;
                holder.numberTv.setTextColor(Color.GRAY);
                holder.numberTv.setBackgroundResource(0);
            }
            int redColor = mContext.getResources().getColor(R.color.R);
            int color = item.selected ? redColor : Color.WHITE;
            holder.numberTv.setTextColor(color);
            LogUtil.defaultLog("player id " + item.id + "; number " + item.Player_Num);
        } else {
            holder.numberTv.setText("");
            holder.numberTv.setBackgroundResource(0);
        }

        return convertView;
    }

    @Override
    public void reorderItems(int oldPosition, int newPosition) {
        StatsMatchFormationBean oldItem = list.get(oldPosition);
        StatsMatchFormationBean newItem = list.get(newPosition);
        list.set(oldPosition, newItem);
        list.set(newPosition, oldItem);
        notifyDataSetChanged();
    }

    @Override
    public void setHideItem(int hidePosition) {
        this.mHidePosition = hidePosition;
        notifyDataSetChanged();
    }

    @Override
    public void removeItem(int removePosition) {
        list.remove(removePosition);
        notifyDataSetChanged();
    }

    public String getItemText(int position) {
        String text = "";
        try {
            if (list != null && list.get(position) != null && list.get(position).Player_Num != null) {
                text = list.get(position).Player_Num;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            LogUtil.defaultLog("ArrayIndexOutOfBoundsException");
        }
        return text;
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
