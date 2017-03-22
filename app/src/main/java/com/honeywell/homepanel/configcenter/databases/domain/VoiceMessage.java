package com.honeywell.homepanel.configcenter.databases.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by H135901 on 3/16/2017.
 */

public class VoiceMessage implements Parcelable {
    public long mId = -1;
    public String mUuid = "";
    public  String mTime = "";
    public  int mLength = 0;
    public  String mPath = "";
    public  int mRead = 0;
    public VoiceMessage(){}

    public VoiceMessage(String mTime, String mUuid, int mLength, String mPath, int mRead) {
        this.mTime = mTime;
        this.mUuid = mUuid;
        this.mLength = mLength;
        this.mPath = mPath;
        this.mRead = mRead;
    }

    @Override
    public String toString() {
        return "VoiceMessage{" +
                "mId=" + mId +
                ", mUuid='" + mUuid + '\'' +
                ", mTime='" + mTime + '\'' +
                ", mLength=" + mLength +
                ", mPath='" + mPath + '\'' +
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
        dest.writeString(this.mTime);
        dest.writeInt(this.mLength);
        dest.writeString(this.mPath);
        dest.writeInt(this.mRead);
    }

    protected VoiceMessage(Parcel in) {
        this.mId = in.readLong();
        this.mUuid = in.readString();
        this.mTime = in.readString();
        this.mLength = in.readInt();
        this.mPath = in.readString();
        this.mRead = in.readInt();
    }

    public static final Parcelable.Creator<VoiceMessage> CREATOR = new Parcelable.Creator<VoiceMessage>() {
        @Override
        public VoiceMessage createFromParcel(Parcel source) {
            return new VoiceMessage(source);
        }

        @Override
        public VoiceMessage[] newArray(int size) {
            return new VoiceMessage[size];
        }
    };
}
