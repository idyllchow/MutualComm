package com.idyll.mutualcomm.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author shibo
 * @packageName bean
 * @description 球员号码文本项
 * @date 15/11/30
 */
public class MCPlayerTextItem implements Parcelable, Comparable<MCPlayerTextItem>{

    public StatsMatchFormationBean player;
    public int textColor;
    public boolean selected = false;

    public MCPlayerTextItem(StatsMatchFormationBean player) {
        this.player = player;
    }

    public MCPlayerTextItem(StatsMatchFormationBean player, int textColor, boolean selected) {
        this.player = player;
        this.textColor = textColor;
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.player, 0);
        dest.writeInt(this.textColor);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
    }

    protected MCPlayerTextItem(Parcel in) {
        this.player = in.readParcelable(Player.class.getClassLoader());
        this.textColor = in.readInt();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<MCPlayerTextItem> CREATOR = new Creator<MCPlayerTextItem>() {
        public MCPlayerTextItem createFromParcel(Parcel source) {
            return new MCPlayerTextItem(source);
        }

        public MCPlayerTextItem[] newArray(int size) {
            return new MCPlayerTextItem[size];
        }
    };

    @Override
    public int compareTo(MCPlayerTextItem MCPlayerTextItem) {
        return Integer.parseInt(this.player.Player_Num) - Integer.parseInt(MCPlayerTextItem.player.Player_Num);
    }
}
