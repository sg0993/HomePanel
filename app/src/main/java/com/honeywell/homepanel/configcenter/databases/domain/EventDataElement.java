package com.honeywell.homepanel.configcenter.databases.domain;

/**
 * Created by H135901 on 3/13/2017.
 */

public class EventDataElement {
    private String mDataKey;
    private byte[] mDataValue;
    private int mFd;
    public EventDataElement(String dataKey, byte[] dataVlaue, int fd) {
        mDataKey = dataKey;
        mDataValue = dataVlaue;
        mFd = fd;
    }

    public String getDataKey() {
        return mDataKey;
    }

    public byte[] getDataValue() {
        return mDataValue;
    }

    public int getFd() {
        return mFd;
    }

    @Override
    public String toString() {
        return "EventDataElement [mDataKey=" + mDataKey + ", mDataValue="
                + new String(mDataValue) + ", mFd=" + mFd + "]";
    }
}
