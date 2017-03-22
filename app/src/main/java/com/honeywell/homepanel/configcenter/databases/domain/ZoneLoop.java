package com.honeywell.homepanel.configcenter.databases.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by H135901 on 3/17/2017.
 */

public class ZoneLoop implements Parcelable {
    public long mId = -1;
    public String mModuleUuid = "";
    public String mUuid = "";
    public  String mName = "";
    public  int mLoop = 1;
    public  int mDelayTime = 0;
    public  int mEnabled = 0;
    public String mZoneType = "";
    public  String mAlarmType = "";

    public ZoneLoop(String mModuleUuid, String mName, String mUuid, int mDelayTime, int mLoop, int mEnabled, String mZoneType, String mAlarmType) {
        this.mModuleUuid = mModuleUuid;
        this.mName = mName;
        this.mUuid = mUuid;
        this.mDelayTime = mDelayTime;
        this.mLoop = mLoop;
        this.mEnabled = mEnabled;
        this.mZoneType = mZoneType;
        this.mAlarmType = mAlarmType;
    }

    public ZoneLoop() {}

    @Override
    public String toString() {
        return "ZoneLoop{" +
                "mId=" + mId +
                ", mModuleUuid='" + mModuleUuid + '\'' +
                ", mUuid='" + mUuid + '\'' +
                ", mName='" + mName + '\'' +
                ", mLoop=" + mLoop +
                ", mDelayTime=" + mDelayTime +
                ", mEnabled=" + mEnabled +
                ", mZoneType='" + mZoneType + '\'' +
                ", mAlarmType='" + mAlarmType + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mModuleUuid);
        dest.writeString(this.mUuid);
        dest.writeString(this.mName);
        dest.writeInt(this.mLoop);
        dest.writeInt(this.mDelayTime);
        dest.writeInt(this.mEnabled);
        dest.writeString(this.mZoneType);
        dest.writeString(this.mAlarmType);
    }

    protected ZoneLoop(Parcel in) {
        this.mId = in.readLong();
        this.mModuleUuid = in.readString();
        this.mUuid = in.readString();
        this.mName = in.readString();
        this.mLoop = in.readInt();
        this.mDelayTime = in.readInt();
        this.mEnabled = in.readInt();
        this.mZoneType = in.readString();
        this.mAlarmType = in.readString();
    }

    public static final Parcelable.Creator<ZoneLoop> CREATOR = new Parcelable.Creator<ZoneLoop>() {
        @Override
        public ZoneLoop createFromParcel(Parcel source) {
            return new ZoneLoop(source);
        }

        @Override
        public ZoneLoop[] newArray(int size) {
            return new ZoneLoop[size];
        }
    };
}
