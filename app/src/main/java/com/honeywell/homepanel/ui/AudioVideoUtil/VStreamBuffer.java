package com.honeywell.homepanel.ui.AudioVideoUtil;

import com.honeywell.homepanel.common.utils.CommonUtils;

import java.nio.ByteBuffer;

/**
 * Created by H135901 on 4/5/2017.
 */

public class VStreamBuffer {
    public ByteBuffer StreamData = null;
    public int StreamLen;
    public long TimeSTP = 0;
    public String UUID;

    public VStreamBuffer(byte[] data) {
        StreamData = ByteBuffer.wrap(data);
        StreamLen = data.length;
        UUID = CommonUtils.generateCommonEventUuid();//TODO for test
    }
}
