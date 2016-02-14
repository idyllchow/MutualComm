package com.idyll.mutualcomm.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.entity
 * @description
 * @date 16/2/3
 */
public class Team implements Parcelable {

    /**
     * 球队名
     */
    private String name;
    /**
     * 球队logo
     */
    private String logo;
    /**
     * 球队描述
     */
    private String description;
    /**
     * 链接
     */
    private String link;
    /**
     * 球员
     */
    private Player player;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.logo);
        dest.writeString(this.description);
        dest.writeString(this.link);
        dest.writeParcelable(this.player, 0);
    }

    public Team() {
    }

    protected Team(Parcel in) {
        this.name = in.readString();
        this.logo = in.readString();
        this.description = in.readString();
        this.link = in.readString();
        this.player = in.readParcelable(Player.class.getClassLoader());
    }

    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        public Team createFromParcel(Parcel source) {
            return new Team(source);
        }

        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

}
