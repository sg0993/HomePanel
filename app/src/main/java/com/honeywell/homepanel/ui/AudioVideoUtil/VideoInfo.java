package com.honeywell.homepanel.ui.AudioVideoUtil;

/**
 * Created by H135901 on 4/5/2017.
 */

public class VideoInfo {
    public byte [] mCsdInfo;
    public int mWidth;
    public int mHeight;
    public VideoInfo() {
        mCsdInfo = null;
        mWidth = 0;
        mHeight = 0;
    }
    public void reset() {
        mCsdInfo = null;
        mWidth = 0;
        mHeight = 0;
    }
}
