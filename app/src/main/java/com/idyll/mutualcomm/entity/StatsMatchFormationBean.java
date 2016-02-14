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


    /**
     * name : 李浩锋
     * gender : 1
     * created_at : 2015-05-07 15:40:00
     * updated_at : 2015-09-08 08:58:47
     * team_id : 5538b7a3af146b532be033f9
     * playerNum : 9
     * relation_id : 55e3a4629e319a5d6b802f9a
     * nationality : 中国
     * id : 554b8750af146b2288c9f3cd
     * description :
     */

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

    public int position;
    public boolean onField;

    public StatsMatchFormationBean() {
    }

    public StatsMatchFormationBean(String id, String Player_Num) {
        this.id = id;
        this.Player_Num = Player_Num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
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
        dest.writeInt(this.position);
        dest.writeByte(onField ? (byte) 1 : (byte) 0);
    }

    protected StatsMatchFormationBean(Parcel in) {
        super(in);
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
        this.position = in.readInt();
        this.onField = in.readByte() != 0;
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
