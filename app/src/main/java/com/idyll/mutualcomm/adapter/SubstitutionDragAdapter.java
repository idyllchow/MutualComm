package com.idyll.mutualcomm.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.entity.StatsMatchFormationBean;
import com.sponia.foundationmoudle.utils.LogUtil;

import java.util.ArrayList;


/**
 * @packageName
 * @description 换人时球员Adapter
 * @date 15/9/14
 * @auther shibo
 */
public class SubstitutionDragAdapter extends DragGridBaseAdapter {

    public SubstitutionDragAdapter(Context context, ArrayList<StatsMatchFormationBean> list, boolean isLeft) {
        super(context, list, isLeft);
    }

    /**
     * 由于复用convertView导致某些item消失了，所以这里不复用item，
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_left_player_num_grid, null, false);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.number_tv);
            LogUtil.defaultLog("adapter mItemHeight: " + mItemHeight + "; mItemWidth: " + mItemWidth);
            if (isLeft) {
//                convertView.setBackgroundColor(blackColor);
            } else {

            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StatsMatchFormationBean player = list.get(position);
        if (null != player && !TextUtils.isEmpty(player.id)) {
            holder.tv.setText(player.Player_Num);
            if (isLeft) {
                holder.tv.setTextColor(0 == position % 2 ? whiteColor : redColor);
            } else {
                holder.tv.setTextColor(grayColor);
            }
            convertView.setVisibility(position == mHidePosition ? View.INVISIBLE : View.VISIBLE);
        } else {
            holder.tv.setText("");
            holder.tv.setBackgroundResource(0);
            convertView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView tv;
    }
}