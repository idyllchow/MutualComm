package com.idyll.mutualcomm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.event.MCRecordData;
import com.sponia.foundationmoudle.utils.LogUtil;

import java.util.ArrayList;

/**
 * @author shibo
 * @packageName com.sponia.soccerstats.adapter
 * @description 球员Adapter
 * @date 15/9/25
 */
public class MCEventAdapter extends BaseAdapter {
    private final ArrayList<MCRecordData> list;
    private final LayoutInflater mInflater;
    private Context mContext;
    private int selectedItem = -1;

    public MCEventAdapter(Context context, ArrayList<MCRecordData> list) {
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
        ViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_event, null, false);
            holder = new ViewHolder();
            holder.tvEvent = (TextView) convertView.findViewById(R.id.tv_event);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvEvent.setText(list.get(position).getPlayer_id() + "号" + list.get(position).getEvent_detail());
        holder.tvTime.setText(list.get(position).getClient_match_clock());
        LogUtil.defaultLog("event ------->" + list.get(position).getEvent_detail());

        if (position == selectedItem) {
            convertView.setBackgroundResource(R.drawable.bg_green_rectangle);
        } else {
            convertView.setBackgroundResource(R.drawable.bg_gray_rectangle);
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

    static class ViewHolder {
        TextView tvEvent;
        TextView tvTime;
    }
}
