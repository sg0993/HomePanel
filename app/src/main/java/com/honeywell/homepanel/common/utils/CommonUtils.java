package com.honeywell.homepanel.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Environment;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.ui.AudioVideoUtil.VideoInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by H135901 on 3/3/2017.
 */

public class CommonUtils {

    private static final String TAG = "CommonUtils";

    public static SeekBar  showCallVolumeDialog(Activity activity, View.OnClickListener onClickListener,
                                                SeekBar.OnSeekBarChangeListener seekBarChangeListener, boolean bSpeaker){
        final WindowManager manager = activity.getWindowManager();
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_volume_adjust, null);
        View volume_decrease = view.findViewById(R.id.volume_decrease);
        volume_decrease.setOnClickListener(onClickListener);
        View volume_increase = view.findViewById(R.id.volume_increase);
        volume_increase.setOnClickListener(onClickListener);

        SeekBar seekBar = (SeekBar) view.findViewById(R.id.volume_seekbar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        AudioManager am =(AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
        if(bSpeaker){
            seekBar.setMax(am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
            seekBar.setProgress(am.getStreamVolume(AudioManager.STREAM_SYSTEM));
        }
        else{

        }
        AlertDialog alert = new AlertDialog.Builder(activity).create();
        alert.show();
        alert.getWindow().setLayout(width/2, height/4);
        alert.setTitle("测试");
        alert.getWindow().setContentView(view);
        return  seekBar;
    }

    public static void setWindowAlpha(Window window, float f) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = f;
        window.setAttributes(lp);
    }

    public static void startAndBindService(Context context,String serviceAction,ServiceConnection connection){
        if(null == serviceAction || null == connection){
            return;
        }
        Intent intent = new Intent(serviceAction);
        intent.setPackage(context.getPackageName());
        context.startService(intent);
        context.bindService(intent, connection, BIND_AUTO_CREATE);
    }

    public static void stopAndUnbindService(Context context,String serviceAction,ServiceConnection connection){
        if(null == serviceAction || null == connection){
            return;
        }
        Intent intent = new Intent(serviceAction);
        intent.setPackage(context.getPackageName());
        context.stopService(intent);
        context.unbindService(connection);
    }


    public static String deleteTrim(String str){//去掉IP字符串前后所有的空格
        while(str.startsWith(" ")){
            str= str.substring(1,str.length()).trim();
        }
        while(str.endsWith(" ")){
            str= str.substring(0,str.length()-1).trim();
        }
        return str;
    }
    public static boolean isIp(String ip){//判断是否是一个IP
        boolean b = false;
        ip = deleteTrim(ip);
        if(ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){
            String s[] = ip.split("\\.");
            if(Integer.parseInt(s[0])<255)
                if(Integer.parseInt(s[1])<255)
                    if(Integer.parseInt(s[2])<255)
                        if(Integer.parseInt(s[3])<255)
                            b = true;
        }
        return b;
    }

    public static String getSdcardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static boolean checkSdcard() {
        File sdcard = new File(getSdcardPath());
        return !(!sdcard.exists() || !sdcard.canWrite());
    }

    public static boolean ISNULL(String str){
        boolean bRet = false;
        if(str == null || str.length() == 0){
            bRet = true;
        }
        return bRet;
    }

    public static void rebootPM(Context ctx) {
        if(null == ctx) {
            return;
        }
        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        pm.reboot("null");
    }
    public static void recoveryPM(Context ctx) {
        if(null == ctx) {
            return;
        }
        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        pm.reboot("recovery");
    }

    public static String generateCommonEventUuid() {
        return UUID.randomUUID().toString();
    }

    public static String generateCommonEventMsgId() {
        return UUID.randomUUID().toString();
    }
	
	 public static  void saveBitmap(Bitmap bitmap, String bitName){
        if(null == bitmap || TextUtils.isEmpty(bitName)){
            Log.e(TAG,TAG + "saveBitmap() null");
            return;
        }
        File  dirFile = new File(CommonData.IMAGE_PATH);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        File file = new File(dirFile,bitName);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)){
                out.flush();
                out.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getBitMapName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = sdf.format(new Date());
        time += ".png";
        return time;
    }

    public static String getVideoRecordName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = sdf.format(new Date());
        time += ".mp4";
        return time;
    }

    public static MediaFormat getVideoFormat(VideoInfo mVideoInfo) {
        ByteBuffer bb = ByteBuffer.wrap(mVideoInfo.mCsdInfo);
        MediaFormat media_format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC,CommonData.VIDEO_WIDTH,CommonData.VIDEO_HEIGHT);
        media_format.setByteBuffer("csd-0", bb);
        media_format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, CommonData.VIDEO_WIDTH * CommonData.VIDEO_HEIGHT);
        media_format.setInteger(MediaFormat.KEY_FRAME_RATE,25);
        return  media_format;
    }
    public static  MediaCodec.BufferInfo getBufferInfo(int length){
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        info.flags = MediaCodec.BUFFER_FLAG_KEY_FRAME;
        info.offset = 0;
        info.size = length;
        return  info;
    }
    public static String convertCFormateString(byte[] asciiBytes) {
        int cZeroPosition = 0;
        int arraySize = asciiBytes.length;

        for (; cZeroPosition < arraySize && asciiBytes[cZeroPosition] != 0x00; cZeroPosition++ ) {

        }

        return new String(asciiBytes, 0, cZeroPosition);
    }

}
