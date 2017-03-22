package com.honeywell.homepanel.configcenter.databases.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by H135901 on 3/15/2017.
 */

public class CommonDevice implements Parcelable {
    public long mId = -1;
    public String mUuid = "";
    public  String mName = "";
    public  String mType = "";

    public CommonDevice(long mId, String mUuid, String mName, String mType) {
        this.mId = mId;
        this.mUuid = mUuid;
        this.mName = mName;
        this.mType = mType;
    }
    public CommonDevice() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mUuid);
        dest.writeString(this.mName);
        dest.writeString(this.mType);
    }

    protected CommonDevice(Parcel in) {
        this.mId = in.readLong();
        this.mUuid = in.readString();
        this.mName = in.readString();
        this.mType = in.readString();
    }

    public static final Parcelable.Creator<CommonDevice> CREATOR = new Parcelable.Creator<CommonDevice>() {
        @Override
        public CommonDevice createFromParcel(Parcel source) {
            return new CommonDevice(source);
        }

        @Override
        public CommonDevice[] newArray(int size) {
            return new CommonDevice[size];
        }
    };
}
