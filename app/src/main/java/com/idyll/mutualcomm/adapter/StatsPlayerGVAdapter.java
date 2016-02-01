package com.idyll.mutualcomm.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.idyll.mutualcomm.R;

/**
 * @author shibo
 * @packageName com.sponia.soccerstats.view.draggridview
 * @description 场下球员背景边框
 * @date 15/10/20
 */
public class StatsPlayerGVAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private int mItemWidth;
    private int mItemHeight;
    /**
     * 每页球员数
     */
    private static final int PLAYER_NUM = 16;

    public StatsPlayerGVAdapter(Context context) {
        this.mContext = context;
        mInflater = ((Activity) mContext).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return PLAYER_NUM;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_player_bg_grid, null, false);
            holder = new ViewHolder();
            holder.subLeft = convertView.findViewById(R.id.sub_left);
            holder.subTop = convertView.findViewById(R.id.sub_top);
            holder.subRight = convertView.findViewById(R.id.sub_right);
            holder.subBottom = convertView.findViewById(R.id.sub_bottom);
            convertView.setLayoutParams(new GridView.LayoutParams(mItemWidth, mItemHeight));

            if ((position + 1) % 4 == 0) { //每一行最后一列右边框可见,其余隐藏
                holder.subRight.setVisibility(View.VISIBLE);
            } else {
                holder.subRight.setVisibility(View.GONE);
            }
            if (position >= 12) {
                holder.subBottom.setVisibility(View.VISIBLE);
            } else {
                holder.subBottom.setVisibility(View.GONE);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private static class ViewHolder {
        View subLeft;
        View subTop;
        View subRight;
        View subBottom;
    }

    public void setItemWidthAndHeight(int width, int height) {
        mItemWidth = width;
        mItemHeight = height;
    }
}
