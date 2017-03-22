package com.honeywell.homepanel.configcenter.databases.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by H135901 on 3/16/2017.
 */

public class AlarmHistory implements Parcelable {
    public long mId = -1;
    public String mUuid = "";
    public  long mTime = 0;//utc time,becacuse of ams need such record
    public  String mAlarmType = "";
    public  String mMessage = "";
    public  int mRead = 0;
    public AlarmHistory(){}

    public AlarmHistory(String mUuid, long mTime, String mMessage, int mRead, String mAlarmType) {
        this.mUuid = mUuid;
        this.mTime = mTime;
        this.mMessage = mMessage;
        this.mRead = mRead;
        this.mAlarmType = mAlarmType;
    }

    @Override
    public String toString() {
        return "AlarmHistory{" +
                "mId=" + mId +
                ", mUuid='" + mUuid + '\'' +
                ", mTime=" + mTime +
                ", mAlarmType='" + mAlarmType + '\'' +
                ", mMessage='" + mMessage + '\'' +
                ", mRead=" + mRead +
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
        dest.writeLong(this.mTime);
        dest.writeString(this.mAlarmType);
        dest.writeString(this.mMessage);
        dest.writeInt(this.mRead);
    }

    protected AlarmHistory(Parcel in) {
        this.mId = in.readLong();
        this.mUuid = in.readString();
        this.mTime = in.readLong();
        this.mAlarmType = in.readString();
        this.mMessage = in.readString();
        this.mRead = in.readInt();
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
