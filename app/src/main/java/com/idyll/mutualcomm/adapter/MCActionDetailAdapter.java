package com.idyll.mutualcomm.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.entity.EditActionItem;

import java.util.ArrayList;


/**
 * @author shibo
 * @packageName com.sponia.stats.adapter
 * @description 统计事件行为
 * @date 15/9/25
 */
public class MCActionDetailAdapter extends BaseAdapter {
    private ArrayList<EditActionItem> mActions;
    private int menuClass = 0;
    private LayoutInflater mInflater;
    private int mItemWidth;
    private int mItemHeight;

    private int selectedItem = -1;

    public MCActionDetailAdapter(Context context, ArrayList<EditActionItem> StatsActionItems) {
        mInflater = LayoutInflater.from(context);
        this.mActions = StatsActionItems;
    }

    public void setActions(ArrayList<EditActionItem> mActions) {
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
        return mActions == null ? 0 : mActions.size();
    }

    @Override
    public Object getItem(int position) {
        if (null != mActions && position < mActions.size() && position != -1) {
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
            convertView = mInflater.inflate(R.layout.item_action_grid, null, false);
            holder.itemLayout = convertView.findViewById(R.id.rly_action_item);
            holder.actionIcon = (ImageView) convertView.findViewById(R.id.action_icon);
            holder.actionTv = (TextView) convertView.findViewById(R.id.action_tv);

            convertView.setTag(holder);
        } else {
            holder = (ActionViewHolder) convertView.getTag();
        }
//        holder.itemLayout.setLayoutParams(new FrameLayout.LayoutParams(mItemWidth, mItemHeight, Gravity.CENTER));
        EditActionItem item = (EditActionItem) getItem(position);
        if (null != item && !TextUtils.isEmpty(item.action)) {
            holder.itemLayout.setVisibility(View.VISIBLE);
            holder.actionTv.setText(item.action);
        } else {
            holder.itemLayout.setVisibility(View.INVISIBLE);
        }

//        if (item.actionType == ((EditActionItem) getItem(oldPosition)).actionType) { //
//
//        }

        if (position == selectedItem) {
            holder.itemLayout.setBackgroundResource(R.drawable.bg_green_rectangle);
        } else {
            holder.itemLayout.setBackgroundResource(R.drawable.bg_gray_rectangle);
        }

        return convertView;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedItem;
    }

    static class ActionViewHolder {
        TextView actionTv;
        ImageView actionIcon;
        View itemLayout;
    }

}
