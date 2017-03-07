package com.honeywell.homepanel.ui.domain;

/**
 * Created by H135901 on 3/6/2017.
 */

public class AlarmHint {
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
}
