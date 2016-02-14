//package com.idyll.mutualcomm.adapter;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.idyll.mutualcomm.R;
//import com.idyll.mutualcomm.entity.StatsMatchFormationBean;
//
//import java.util.ArrayList;
//
///**
// * @packageName com.sponia.soccerstats.view.draggridview
// * @description 场下球员视图adapter
// * @date 15/9/14
// * @auther shibo
// */
//public class RightDragAdapter extends DragGridBaseAdapter {
//
//    public RightDragAdapter(Context context, ArrayList<StatsMatchFormationBean> list) {
//        super(context, list);
//    }
//
//    /**
//     * 由于复用convertView导致某些item消失了，所以这里不复用item，按一行4个item对边框的显示做控制
//     */
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (null == convertView) {
//            convertView = mInflater.inflate(R.layout.item_right_player_num_grid, null, false);
//            holder = new ViewHolder();
//            holder.tv = (TextView) convertView.findViewById(R.id.number_tv);
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        StatsMatchFormationBean player = list.get(position);
//        if (player != null) {
//            holder.tv.setText(player.Player_Num);
//            holder.tv.setTextColor(grayColor);
//            convertView.setVisibility(position == mHidePosition ? View.INVISIBLE : View.VISIBLE);
//        } else {
//            holder.tv.setText("");
//            holder.tv.setBackgroundResource(0);
//            convertView.setVisibility(View.VISIBLE);
//        }
//        return convertView;
//    }
//
//    private static class ViewHolder {
//        TextView tv;
//    }
//
//}