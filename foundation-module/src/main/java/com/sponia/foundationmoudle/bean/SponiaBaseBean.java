package com.sponia.foundationmoudle.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @packageName com.sponia.foundationmoudle.bean
 * @description 基础bean
 * @date 15/9/7
 * @auther shibo
 */
public class SponiaBaseBean implements Parcelable {
    public String message;
    public int error_code;


    public SponiaBaseBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeInt(this.error_code);
    }

    protected SponiaBaseBean(Parcel in) {
        this.message = in.readString();
        this.error_code = in.readInt();
    }

    public static final Creator<SponiaBaseBean> CREATOR = new Creator<SponiaBaseBean>() {
        public SponiaBaseBean createFromParcel(Parcel source) {
            return new SponiaBaseBean(source);
        }

        public SponiaBaseBean[] newArray(int size) {
            return new SponiaBaseBean[size];
        }
    };
}