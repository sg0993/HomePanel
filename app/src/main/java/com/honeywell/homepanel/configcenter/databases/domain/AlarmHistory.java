package com.honeywell.homepanel.configcenter.databases.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by H135901 on 3/16/2017.
 */

public class AlarmHistory implements Parcelable {
    public long mId = -1;
    public String mUuid = "";
    public String mTriggerId = ""; //use to mark which trigger has been triggered (each alarm has a unique id eq:01212)
    public long mTime = 0;//utc time,becacuse of ams need such record
    public String mAliasName = "";
    public String mAlarmType = "";
    public String mMessage = "";
    public  int mRead = 0;
    public int mUploadAms = 0;//0: need upload to ams  1:it's already received by AMS
    public int mUploadCloud = 0;//0: need upload to cloud  1:it's already received by cloud
    public AlarmHistory(){}

    public AlarmHistory(String mUuid, String mTriggerId, long mTime, String aliasName, String mMessage, String mAlarmType,
                        int mRead, int mUploadAms, int mUploadCloud) {
        this.mUuid = mUuid;
        this.mTriggerId = mTriggerId;
        this.mTime = mTime;
        this.mAliasName = aliasName;
        this.mMessage = mMessage;
        this.mAlarmType = mAlarmType;
        this.mRead = mRead;
        this.mUploadAms = mUploadAms;
        this.mUploadCloud = mUploadCloud;
    }

    @Override
    public String toString() {
        return "AlarmHistory{" +
                "mId=" + mId +
                ", mUuid='" + mUuid + '\'' +
                ", mTriggerId='" + mTriggerId + '\'' +
                ", mTime=" + mTime +
                ", mAliasName ='" + mAliasName + '\'' +
                ", mAlarmType='" + mAlarmType + '\'' +
                ", mMessage='" + mMessage + '\'' +
                ", mRead=" + mRead +
                ", mUploadAms=" + mUploadAms +
                ", mUploadCloud=" + mUploadCloud +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mUuid);
        dest.writeString(this.mTriggerId);
        dest.writeLong(this.mTime);
        dest.writeString(this.mAliasName);
        dest.writeString(this.mAlarmType);
        dest.writeString(this.mMessage);
        dest.writeInt(this.mRead);
        dest.writeInt(this.mUploadAms);
        dest.writeInt(this.mUploadCloud);
    }

    protected AlarmHistory(Parcel in) {
        this.mId = in.readLong();
        this.mUuid = in.readString();
        this.mTriggerId = in.readString();
        this.mTime = in.readLong();
        this.mAliasName = in.readString();
        this.mAlarmType = in.readString();
        this.mMessage = in.readString();
        this.mRead = in.readInt();
        this.mUploadAms = in.readInt();
        this.mUploadCloud = in.readInt();
    }

    public static final Parcelable.Creator<AlarmHistory> CREATOR = new Parcelable.Creator<AlarmHistory>() {
        @Override
        public AlarmHistory createFromParcel(Parcel source) {
            return new AlarmHistory(source);
        }

        @Override
        public AlarmHistory[] newArray(int size) {
            return new AlarmHistory[size];
        }
    };
}
