package com.honeywell.homepanel.configcenter.databases.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by H135901 on 3/17/2017.
 */

public class RelayLoop implements Parcelable {
    public long mId = -1;
    public String mModuleUuid = "";
    public String mUuid = "";
    public  String mName = "";
    public  int mLoop = 1;
    public  int mDelayTime = 0;
    public  int mEnabled = 0;

    public RelayLoop(String mModuleUuid, String mUuid, String mName, int mLoop, int mDelayTime, int mEnabled) {
        this.mModuleUuid = mModuleUuid;
        this.mUuid = mUuid;
        this.mName = mName;
        this.mLoop = mLoop;
        this.mDelayTime = mDelayTime;
        this.mEnabled = mEnabled;
    }

    public RelayLoop() {}

    @Override
    public String toString() {
        return "RelayLoop{" +
                "mId=" + mId +
                ", mModuleUuid='" + mModuleUuid + '\'' +
                ", mUuid='" + mUuid + '\'' +
                ", mName='" + mName + '\'' +
                ", mLoop=" + mLoop +
                ", mDelayTime=" + mDelayTime +
                ", mEnabled=" + mEnabled +
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
    }

    protected RelayLoop(Parcel in) {
        this.mId = in.readLong();
        this.mModuleUuid = in.readString();
        this.mUuid = in.readString();
        this.mName = in.readString();
        this.mLoop = in.readInt();
        this.mDelayTime = in.readInt();
        this.mEnabled = in.readInt();
    }

    public static final Parcelable.Creator<RelayLoop> CREATOR = new Parcelable.Creator<RelayLoop>() {
        @Override
        public RelayLoop createFromParcel(Parcel source) {
            return new RelayLoop(source);
        }

        @Override
        public RelayLoop[] newArray(int size) {
            return new RelayLoop[size];
        }
    };
}
