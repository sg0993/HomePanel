package com.honeywell.homepanel.configcenter.databases.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by H135901 on 3/17/2017.
 */

public class ScenarioLoop implements Parcelable {
    public long mId = -1;
    public String mUuid = "";//场景的uuid
    public  String mName = "";//场景的名字
    public  String mDeviceUuid = "";
    public  String mAction = "";

    public ScenarioLoop(String mUuid, String mName, String mDeviceUuid, String mAction) {
        this.mUuid = mUuid;
        this.mName = mName;
        this.mDeviceUuid = mDeviceUuid;
        this.mAction = mAction;
    }

    public ScenarioLoop() {}

    @Override
    public String toString() {
        return "ScenarioLoop{" +
                "mId=" + mId +
                ", mUuid='" + mUuid + '\'' +
                ", mName='" + mName + '\'' +
                ", mDeviceUuid='" + mDeviceUuid + '\'' +
                ", mAction='" + mAction + '\'' +
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
        dest.writeString(this.mName);
        dest.writeString(this.mDeviceUuid);
        dest.writeString(this.mAction);
    }

    protected ScenarioLoop(Parcel in) {
        this.mId = in.readLong();
        this.mUuid = in.readString();
        this.mName = in.readString();
        this.mDeviceUuid = in.readString();
        this.mAction = in.readString();
    }

    public static final Parcelable.Creator<ScenarioLoop> CREATOR = new Parcelable.Creator<ScenarioLoop>() {
        @Override
        public ScenarioLoop createFromParcel(Parcel source) {
            return new ScenarioLoop(source);
        }

        @Override
        public ScenarioLoop[] newArray(int size) {
            return new ScenarioLoop[size];
        }
    };
}
