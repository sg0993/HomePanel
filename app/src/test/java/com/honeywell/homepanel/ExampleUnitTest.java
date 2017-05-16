package com.honeywell.homepanel;

import android.util.Log;

import com.google.gson.internal.bind.util.ISO8601Utils;

import org.apache.commons.net.ntp.TimeStamp;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        String url = "http://115.159.152.188:8080/packages/0x00210002/1.0.2/4.5.5.1/rubygems-update-2.6.4.gem";
        String[] splits = url.split("/");
        assertTrue(splits[splits.length - 1].equals("rubygems-update-2.6.4.gem"));
    }

    /**
     * time format is ("yyyy-MM-dd'T'HH:mm:ss'Z'");
     */
    @Test
    public void test_ISO_8601_UTC() throws Exception {
        String date = "1970-01-01T00:01:30Z";
        Date time = ISO8601Utils.parse(date, new ParsePosition(0));
        assertEquals("parse ISO 8601 UTC error", 30 * 1000, time.getTime());
    }

    @Test
    public void test_create_iso_8601_utc() throws Exception {
        Date time = new Date();
        time.setTime(60 * 1000L);
        String iso = ISO8601Utils.format(time);
        assertEquals("", "1970-01-01T00:00:30Z", iso);
    }
}