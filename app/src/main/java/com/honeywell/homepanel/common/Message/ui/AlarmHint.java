package com.honeywell.homepanel.common.Message.ui;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by H135901 on 3/6/2017.
 */

public class AlarmHint implements Parcelable {
    public String alarmType = "Intrusion Alarm";
    public String zoneName = "11 zone door sensor alarming.";
    public String time = "Time: 0000.00.00   00:00:00";

    public AlarmHint(String alarmType, String zoneName, String time) {
        this.alarmType = alarmType;
        this.zoneName = zoneName;
        this.time = time;
    }
    public AlarmHint(int index) {
        alarmType = alarmType + "  " + index;
        zoneName = zoneName + "  " + index;
        time = time + "  "+ index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.alarmType);
        dest.writeString(this.zoneName);
        dest.writeString(this.time);
    }

    protected AlarmHint(Parcel in) {
        this.alarmType = in.readString();
        this.zoneName = in.readString();
        this.time = in.readString();
    }

    public static final Parcelable.Creator<AlarmHint> CREATOR = new Parcelable.Creator<AlarmHint>() {
        @Override
        public AlarmHint createFromParcel(Parcel source) {
            return new AlarmHint(source);
        }

        @Override
        public AlarmHint[] newArray(int size) {
            return new AlarmHint[size];
        }
    };
}
