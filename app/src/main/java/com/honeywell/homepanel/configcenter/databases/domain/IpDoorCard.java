package com.honeywell.homepanel.configcenter.databases.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by H135901 on 3/16/2017.
 */

public class IpDoorCard implements Parcelable {
    public long mId = -1;
    public String mUuid = "";
    public  String mType = "";
    public  String mName = "";
    public  String mCardNo = "";
    public  String mStartDate = "";
    public  String mExpireDate = "";
    public  String mStartTime = "";
    public  String mExpireTime = "";
    public  String mAction = "";
    public IpDoorCard(){}

    public IpDoorCard(String mUuid, String mType, String mName, String mCardNo, String mStartDate, String mExpireDate, String mStartTime, String mExpireTime, String mAction) {
        this.mUuid = mUuid;
        this.mType = mType;
        this.mName = mName;
        this.mCardNo = mCardNo;
        this.mStartDate = mStartDate;
        this.mExpireDate = mExpireDate;
        this.mStartTime = mStartTime;
        this.mExpireTime = mExpireTime;
        this.mAction = mAction;
    }

    @Override
    public String toString() {
        return "IpDoorCard{" +
                "mId=" + mId +
                ", mUuid='" + mUuid + '\'' +
                ", mType='" + mType + '\'' +
                ", mName='" + mName + '\'' +
                ", mCardNo='" + mCardNo + '\'' +
                ", mStartDate='" + mStartDate + '\'' +
                ", mExpireDate='" + mExpireDate + '\'' +
                ", mStartTime='" + mStartTime + '\'' +
                ", mExpireTime='" + mExpireTime + '\'' +
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
        dest.writeString(this.mType);
        dest.writeString(this.mName);
        dest.writeString(this.mCardNo);
        dest.writeString(this.mStartDate);
        dest.writeString(this.mExpireDate);
        dest.writeString(this.mStartTime);
        dest.writeString(this.mExpireTime);
        dest.writeString(this.mAction);
    }

    protected IpDoorCard(Parcel in) {
        this.mId = in.readLong();
        this.mUuid = in.readString();
        this.mType = in.readString();
        this.mName = in.readString();
        this.mCardNo = in.readString();
        this.mStartDate = in.readString();
        this.mExpireDate = in.readString();
        this.mStartTime = in.readString();
        this.mExpireTime = in.readString();
        this.mAction = in.readString();
    }

    public static final Parcelable.Creator<IpDoorCard> CREATOR = new Parcelable.Creator<IpDoorCard>() {
        @Override
        public IpDoorCard createFromParcel(Parcel source) {
            return new IpDoorCard(source);
        }

        @Override
        public IpDoorCard[] newArray(int size) {
            return new IpDoorCard[size];
        }
    };
}
