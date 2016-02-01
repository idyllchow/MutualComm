package com.idyll.mutualcomm.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author shibo
 * @packageName bean
 * @description 球员
 * @date 15/11/30
 */
public class MCPlayer implements Parcelable {

    //球员姓名
    public String name = "";
    //球员背号
    public String Player_Num;
    //球员id
    public String id;
    //球员状态,首发（ Starting ）和后备（ Bench ）
    public String lineup;
    //球队名称
    public String Team;

    public String gender;
    public String created_at;
    public String updated_at;
    public String team_id;
    public String relation_id;
    public String nationality;
    public String description;
    public int position;
    public boolean onField;


    public MCPlayer(String name, String id, String Player_Num) {
        this.name = name;
        this.id = id;
        this.Player_Num = Player_Num;
    }

    public MCPlayer(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.Player_Num);
        dest.writeString(this.id);
        dest.writeString(this.lineup);
        dest.writeString(this.Team);
    }

    protected MCPlayer(Parcel in) {
        this.name = in.readString();
        this.Player_Num = in.readString();
        this.id = in.readString();
        this.lineup = in.readString();
        this.Team = in.readString();
    }

    public static final Creator<MCPlayer> CREATOR = new Creator<MCPlayer>() {
        public MCPlayer createFromParcel(Parcel source) {
            return new MCPlayer(source);
        }

        public MCPlayer[] newArray(int size) {
            return new MCPlayer[size];
        }
    };
}
