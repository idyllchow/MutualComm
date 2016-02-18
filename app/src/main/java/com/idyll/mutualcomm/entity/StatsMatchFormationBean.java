package com.idyll.mutualcomm.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.sponia.foundationmoudle.bean.SponiaBaseBean;


/**
 * @packageName com.sponia.soccerstats.bean
 * @description 比赛阵容Bean
 * @date 15/9/13
 * @auther shibo
 */
public class StatsMatchFormationBean extends SponiaBaseBean implements Comparable<StatsMatchFormationBean> {


    public String name;
    public int gender;
    public String created_at;
    public String updated_at;
    public String team_id;
    public String Player_Num;
    public String relation_id;
    public String nationality;
    public String id;
    public String description;
    //在列表中位置
    public int index;
    //是否在场上
    public boolean onField;
    public int textColor;
    //是否被选中
    public boolean selected = false;
    //场上位置
    public String position;

    public StatsMatchFormationBean(String id, String Player_Num) {
        this.id = id;
        this.Player_Num = Player_Num;
    }

    public StatsMatchFormationBean(String id, String Player_Num, int textColor, boolean selected) {
        this.id = id;
        this.Player_Num = Player_Num;
        this.textColor = textColor;
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.gender);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.team_id);
        dest.writeString(this.Player_Num);
        dest.writeString(this.relation_id);
        dest.writeString(this.nationality);
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeInt(this.index);
        dest.writeByte(onField ? (byte) 1 : (byte) 0);
        dest.writeInt(this.textColor);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
        dest.writeString(this.position);
    }

    protected StatsMatchFormationBean(Parcel in) {
        this.name = in.readString();
        this.gender = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.team_id = in.readString();
        this.Player_Num = in.readString();
        this.relation_id = in.readString();
        this.nationality = in.readString();
        this.id = in.readString();
        this.description = in.readString();
        this.index = in.readInt();
        this.onField = in.readByte() != 0;
        this.textColor = in.readInt();
        this.selected = in.readByte() != 0;
        this.position = in.readString();
    }

    public static final Parcelable.Creator<StatsMatchFormationBean> CREATOR = new Parcelable.Creator<StatsMatchFormationBean>() {
        public StatsMatchFormationBean createFromParcel(Parcel source) {
            return new StatsMatchFormationBean(source);
        }

        public StatsMatchFormationBean[] newArray(int size) {
            return new StatsMatchFormationBean[size];
        }
    };

    @Override
    public int compareTo(StatsMatchFormationBean statsMatchFormationBean) {
        return this.Player_Num.compareTo(statsMatchFormationBean.Player_Num);
    }
}
