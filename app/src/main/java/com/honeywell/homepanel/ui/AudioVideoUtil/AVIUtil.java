package com.honeywell.homepanel.ui.AudioVideoUtil;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by H135901 on 4/19/2017.
 */

public class AVIUtil {
    static {
        System.loadLibrary("avilib");
    }
    //Function list of JNI C
    static public native int init(String path,int width,int height,int fps);
    static public native int writeVideo(byte[] video,int len,long videoTime);
    static public native int writeAudio(byte[] audio,int len);
    static public native int close();
}
