package com.honeywell.homepanel.configcenter.databases.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by H135901 on 3/16/2017.
 */

public class EventHistory implements Parcelable {
    public long mId = -1;
    public String mUuid = "";
    public  String mTime = "";
    public  String mType = "";
    public  String mCardNo = "";
    public  String mCardEvent = "";
    public  String mImagePath = "";
    public  String mVideoPath = "";
    public int mRead = 0;
    public int mUploadAms = 0;//0: not upload to ams yet
    public int mUploadCloud = 0;//0: not upload to ams yet
    public int mRoleID = 0;
    public EventHistory(){}

    public EventHistory(long mId, String mUuid, String mTime, String mType, String mCardNo,
                        String mCardEvent, String mImagePath, String mVideoPath, int mRead, int
                                mUploadAms, int mUploadCloud, int mRoleID) {
        this.mId = mId;
        this.mUuid = mUuid;
        this.mTime = mTime;
        this.mType = mType;
        this.mCardNo = mCardNo;
        this.mCardEvent = mCardEvent;
        this.mImagePath = mImagePath;
        this.mVideoPath = mVideoPath;
        this.mRead = mRead;
        this.mUploadAms = mUploadAms;
        this.mUploadCloud = mUploadCloud;
        this.mRoleID = mRoleID;
    }

    @Override
    public String toString() {
        return "EventHistory{" +
                "mId=" + mId +
                ", mUuid='" + mUuid + '\'' +
                ", mTime='" + mTime + '\'' +
                ", mType='" + mType + '\'' +
                ", mCardNo='" + mCardNo + '\'' +
                ", mCardEvent='" + mCardEvent + '\'' +
                ", mImagePath='" + mImagePath + '\'' +
                ", mVideoPath='" + mVideoPath + '\'' +
                ", mRead=" + mRead +
                ", mUploadAms=" + mUploadAms +
                ", mUploadCloud=" + mUploadCloud +
                ", mRoleID=" + mRoleID +
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
        dest.writeString(this.mTime);
        dest.writeString(this.mType);
        dest.writeString(this.mCardNo);
        dest.writeString(this.mCardEvent);
        dest.writeString(this.mImagePath);
        dest.writeString(this.mVideoPath);
        dest.writeInt(this.mRead);
        dest.writeInt(this.mUploadAms);
        dest.writeInt(this.mUploadCloud);
        dest.writeInt(this.mRoleID);
    }

    protected EventHistory(Parcel in) {
        this.mId = in.readLong();
        this.mUuid = in.readString();
        this.mTime = in.readString();
        this.mType = in.readString();
        this.mCardNo = in.readString();
        this.mCardEvent = in.readString();
        this.mImagePath = in.readString();
        this.mVideoPath = in.readString();
        this.mRead = in.readInt();
        this.mUploadAms = in.readInt();
        this.mUploadCloud = in.readInt();
        this.mRoleID = in.readInt();
    }

    public static final Parcelable.Creator<EventHistory> CREATOR = new Parcelable.Creator<EventHistory>() {
        @Override
        public EventHistory createFromParcel(Parcel source) {
            return new EventHistory(source);
        }

        @Override
        public EventHistory[] newArray(int size) {
            return new EventHistory[size];
        }
    };
}
