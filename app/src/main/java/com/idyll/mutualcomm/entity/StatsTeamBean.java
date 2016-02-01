package com.idyll.mutualcomm.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.sponia.foundationmoudle.bean.SponiaBaseBean;

/**
 * @packageName com.sponia.soccerstats.bean
 * @description
 * @date 15/9/13
 * @auther shibo
 */
public class StatsTeamBean extends SponiaBaseBean {
    public String name;
    public String logo_uri;
    public String id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeString(this.logo_uri);
        dest.writeString(this.id);
    }

    public StatsTeamBean() {
    }

    protected StatsTeamBean(Parcel in) {
        super(in);
        this.name = in.readString();
        this.logo_uri = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<StatsTeamBean> CREATOR = new Parcelable.Creator<StatsTeamBean>() {
        public StatsTeamBean createFromParcel(Parcel source) {
            return new StatsTeamBean(source);
        }

        public StatsTeamBean[] newArray(int size) {
            return new StatsTeamBean[size];
        }
    };
}
