package com.honeywell.homepanel.configcenter.databases.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by H135901 on 3/17/2017.
 */

public class SpeedDial implements Parcelable {
    public long mId = -1;
    public String mUuid = "";
    public  String mType = "";
    public String mDongHo = "";

    public SpeedDial() {}

    public SpeedDial(String mUuid, String mType, String mDongHo) {
        this.mUuid = mUuid;
        this.mType = mType;
        this.mDongHo = mDongHo;
    }

    @Override
    public String toString() {
        return "SpeedDial{" +
                "mId=" + mId +
                ", mUuid='" + mUuid + '\'' +
                ", mType='" + mType + '\'' +
                ", mDongHo='" + mDongHo + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mUuid);
        dest.writeString(this.mType);
        dest.writeString(this.mDongHo);
    }

    protected SpeedDial(Parcel in) {
        this.mId = in.readLong();
        this.mUuid = in.readString();
        this.mType = in.readString();
        this.mDongHo = in.readString();
    }

    public static final Parcelable.Creator<SpeedDial> CREATOR = new Parcelable.Creator<SpeedDial>() {
        @Override
        public SpeedDial createFromParcel(Parcel source) {
            return new SpeedDial(source);
        }

        @Override
        public SpeedDial[] newArray(int size) {
            return new SpeedDial[size];
        }
    };
}
