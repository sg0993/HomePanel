package com.honeywell.homepanel.configcenter.databases.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by H135901 on 3/17/2017.
 */

public class IpcLoop implements Parcelable {
    public long mId = -1;
    public String mUuid = "";
    public  String mName = "";
    public String mIpAddr = "";
    public  String mUser = "";
    public  String mPwd = "";

    public IpcLoop() {}

    public IpcLoop(String mUuid, String mName, String mUser, String mPwd, String mIpAddr) {
        this.mUuid = mUuid;
        this.mName = mName;
        this.mUser = mUser;
        this.mPwd = mPwd;
        this.mIpAddr = mIpAddr;
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
        dest.writeString(this.mIpAddr);
        dest.writeString(this.mUser);
        dest.writeString(this.mPwd);
    }

    protected IpcLoop(Parcel in) {
        this.mId = in.readLong();
        this.mUuid = in.readString();
        this.mName = in.readString();
        this.mIpAddr = in.readString();
        this.mUser = in.readString();
        this.mPwd = in.readString();
    }

    public static final Parcelable.Creator<IpcLoop> CREATOR = new Parcelable.Creator<IpcLoop>() {
        @Override
        public IpcLoop createFromParcel(Parcel source) {
            return new IpcLoop(source);
        }

        @Override
        public IpcLoop[] newArray(int size) {
            return new IpcLoop[size];
        }
    };
}
