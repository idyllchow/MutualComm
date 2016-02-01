package com.idyll.mutualcomm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.entity.StatsMatchFormationBean;

import java.util.ArrayList;


/**
 * @packageName
 * @description 上场球员Adapter
 * @date 15/9/14
 * @auther shibo
 */
public class LeftDragAdapter extends DragGridBaseAdapter {

    public LeftDragAdapter(Context context, ArrayList<StatsMatchFormationBean> list) {
        super(context, list);
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
//            holder.tv.setLayoutParams(new FrameLayout.LayoutParams(DensityUtils.px2dip(mItemWidth), DensityUtils.px2dip(mItemHeight), Gravity.CENTER));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StatsMatchFormationBean player = list.get(position);
        if (null != player) {
            holder.tv.setText(player.Player_Num);
            holder.tv.setTextColor(0 == position % 2 ? whiteColor : redColor);
//            }
            convertView.setVisibility(position == mHidePosition ? View.INVISIBLE : View.VISIBLE);
        } else {
            holder.tv.setText("");
            convertView.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    private static class ViewHolder {
        TextView tv;
    }
}