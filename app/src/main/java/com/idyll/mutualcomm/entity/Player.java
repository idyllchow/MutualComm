package com.idyll.mutualcomm.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author shibo
 * @packageName bean
 * @description 球员
 * @date 15/11/30
 */
public class Player implements Parcelable, Comparable<Player> {

    //球员姓名
    public String name = "";
    //球员背号
    public String playerNum;
    //球员id
    public String id;
    //球队名称
    public String teamName;
    //排序
    public int position;
    //位置
    public String place;
    //是否在场
    public boolean onField;
    //描述
    public String description;


    public Player(String name, String id, String playerNum) {
        this.name = name;
        this.id = id;
        this.playerNum = playerNum;
    }

    public Player(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.playerNum);
        dest.writeString(this.id);
        dest.writeString(this.teamName);
    }

    protected Player(Parcel in) {
        this.name = in.readString();
        this.playerNum = in.readString();
        this.id = in.readString();
        this.teamName = in.readString();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        public Player createFromParcel(Parcel source) {
            return new Player(source);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public int compareTo(Player player) {
        return this.playerNum.compareTo(player.playerNum);
    }
}
