package com.honeywell.homepanel.configcenter.databases.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by H135901 on 3/14/2017.
 */

public class PeripheralDevice implements Parcelable {
    public long mId = -1;
    public  String mModuleUuid = "";
    public String mName = "";
    public  String mType = "";
    public String mVersion = "";
    public  String mIpAddr = "";
    public String mMacAddr = "";
    public int mOnLine = 0;

    public PeripheralDevice(String mModuleUuid, String mName, String mType, String mVersion, String mIpAddr, String mMacAddr,int onLine) {
        this.mModuleUuid = mModuleUuid;
        this.mName = mName;
        this.mType = mType;
        this.mVersion = mVersion;
        this.mIpAddr = mIpAddr;
        this.mMacAddr = mMacAddr;
        this.mOnLine = onLine;
    }
    public PeripheralDevice(){}

    @Override
    public String toString() {
        return "PeripheralDeviceInfo{" +
                "mId=" + mId +
                ", mModuleUuid='" + mModuleUuid + '\'' +
                ", mName='" + mName + '\'' +
                ", mType='" + mType + '\'' +
                ", mVersion='" + mVersion + '\'' +
                ", mIpAddr='" + mIpAddr + '\'' +
                ", mMacAddr='" + mMacAddr + '\'' +
                ", mOnLine=" + mOnLine +
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
        dest.writeString(this.mName);
        dest.writeString(this.mType);
        dest.writeString(this.mVersion);
        dest.writeString(this.mIpAddr);
        dest.writeString(this.mMacAddr);
        dest.writeInt(this.mOnLine);
    }

    protected PeripheralDevice(Parcel in) {
        this.mId = in.readLong();
        this.mModuleUuid = in.readString();
        this.mName = in.readString();
        this.mType = in.readString();
        this.mVersion = in.readString();
        this.mIpAddr = in.readString();
        this.mMacAddr = in.readString();
        this.mOnLine = in.readInt();
    }

    public static final Creator<PeripheralDevice> CREATOR = new Creator<PeripheralDevice>() {
        @Override
        public PeripheralDevice createFromParcel(Parcel source) {
            return new PeripheralDevice(source);
        }

        @Override
        public PeripheralDevice[] newArray(int size) {
            return new PeripheralDevice[size];
        }
    };
}
