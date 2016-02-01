package com.idyll.mutualcomm.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.entity.StatsActionItem;

import java.util.ArrayList;

/**
 * @author shibo
 * @packageName com.sponia.soccerstats.adapter
 * @description 统计事件行为
 * @date 15/9/25
 */
public class StatsActionAdapter extends BaseAdapter {
    private ArrayList<StatsActionItem> mActions;
    private int menuClass = 0;
    private LayoutInflater mInflater;
    private int mItemWidth;
    private int mItemHeight;
    private int mMatchType;

    public StatsActionAdapter(Context context, ArrayList<StatsActionItem> StatsActionItems, int matchType) {
        mInflater = LayoutInflater.from(context);
        this.mActions = StatsActionItems;
        this.mMatchType = matchType;
    }

    public void setActions(ArrayList<StatsActionItem> mActions) {
        this.mActions = mActions;
    }

    public int getMenuClass() {
        return menuClass;
    }

    public void setMenuClass(int menuClass) {
        this.menuClass = menuClass;
    }

    public void setItemWidthAndHeight(int width, int height) {
        mItemWidth = width;
        mItemHeight = height;
    }

    @Override
    public int getCount() {
        return mMatchType;
    }

    @Override
    public Object getItem(int position) {
        if (null != mActions && position < mActions.size()) {
            return mActions.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ActionViewHolder holder;
        if (null == convertView) {
            holder = new ActionViewHolder();
            convertView = mInflater.inflate(R.layout.action_grid_item, null, false);
            holder.itemLayout = convertView.findViewById(R.id.item_layout);
            holder.itemLayout.setLayoutParams(new FrameLayout.LayoutParams(mItemWidth, mItemHeight, Gravity.CENTER));
            holder.actionIcon = (ImageView) convertView.findViewById(R.id.action_icon);
            holder.actionTv = (TextView) convertView.findViewById(R.id.action_tv);
            convertView.setTag(holder);
        } else {
            holder = (ActionViewHolder) convertView.getTag();
        }
        StatsActionItem item = (StatsActionItem) getItem(position);
        if (null != item) {
            holder.itemLayout.setVisibility(View.VISIBLE);
            holder.actionTv.setText(item.action);
//            holder.actionIcon.setImageResource(R.mipmap.ic_arrow_back_red);
            holder.itemLayout.setBackgroundResource(R.drawable.gray_round_rectangle_bg);
//            if (0 != item.drawableId) {
//                holder.actionIcon.setVisibility(View.VISIBLE);
//            } else {
//                holder.actionIcon.setVisibility(View.INVISIBLE);
//            }
        } else {
            holder.itemLayout.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    static class ActionViewHolder {
        TextView actionTv;
        ImageView actionIcon;
        View itemLayout;
    }
}
