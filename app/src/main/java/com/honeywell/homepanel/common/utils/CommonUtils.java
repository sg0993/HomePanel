package com.honeywell.homepanel.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.honeywell.homepanel.common.CommonPath;
import com.honeywell.homepanel.ui.AudioVideoUtil.HVideoDecoder;
import com.honeywell.homepanel.ui.AudioVideoUtil.VideoInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static void stopAndUnbindService(Context context, String serviceAction, ServiceConnection connection) {
        if (null == serviceAction || null == connection || null == context) {
            return;
        }
        Intent intent = new Intent(serviceAction);
        intent.setPackage(context.getPackageName());
        context.stopService(intent);
        context.unbindService(connection);
    }


    public static String deleteTrim(String str) {//去掉IP字符串前后所有的空格
        while (str.startsWith(" ")) {
            str = str.substring(1, str.length()).trim();
        }
        while (str.endsWith(" ")) {
            str = str.substring(0, str.length() - 1).trim();
        }
        return str;
    }

    public static boolean isIp(String ip) {//判断是否是一个IP
        boolean b = false;
        ip = deleteTrim(ip);
        if (ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = ip.split("\\.");
            if (Integer.parseInt(s[0]) < 255)
                if (Integer.parseInt(s[1]) < 255)
                    if (Integer.parseInt(s[2]) < 255)
                        if (Integer.parseInt(s[3]) < 255)
                            b = true;
        }
        return b;
    }

    public static boolean isIpv4(String ipAddress) {
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static boolean isValidMac(String mac) {
        String pattern1="^[A-F0-9]{2}(:[A-F0-9]{2}){5}$";
        if(Pattern.compile(pattern1).matcher(mac).find()) {
            return true;
        }

        String pattern2="^[A-F0-9]{2}(-[A-F0-9]{2}){5}$";
        if(Pattern.compile(pattern2).matcher(mac).find()) {
            return true;
        }

        return false;
    }


    public static boolean isIpNetmask(String ip) {//判断是否是一个
        boolean b = false;
        ip = deleteTrim(ip);
        if (ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = ip.split("\\.");
            if (Integer.parseInt(s[0]) <= 255)
                if (Integer.parseInt(s[1]) <= 255)
                    if (Integer.parseInt(s[2]) <= 255)
                        if (Integer.parseInt(s[3]) <= 255)
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

    public static boolean ISNULL(String str) {
        boolean bRet = false;
        if (str == null || str.length() == 0) {
            bRet = true;
        }
        return bRet;
    }

    public static void rebootPM(Context ctx) {
        if (null == ctx) {
            return;
        }
        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        pm.reboot("null");
    }

    public static void recoveryPM(Context ctx) {
        if (null == ctx) {
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

    public static String saveBitmap(Bitmap bitmap, String bitName) {
        String filepath = null;
        if (null == bitmap || TextUtils.isEmpty(bitName)) {
            Log.e(TAG, TAG + "saveBitmap() null");
            return filepath;
        }
        File dirFile = new File(CommonPath.IMAGE_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(dirFile, bitName);
        if (file.exists()) {
            file.delete();
        }
        filepath = file.getAbsolutePath();
        Log.d(TAG, "saveBitmap: file name:" + filepath);
        FileOutputStream out;
        try {
            file.createNewFile();
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                out.flush();
                out.close();
            }
        }
        catch (Exception e){
            Log.e(TAG, "saveBitmap: exception11111111111");
            e.printStackTrace();
            filepath = null;
        }
        return  filepath;
    }

    public static Bitmap getBitmapFromFile(String name, int width, int height) {
        File file = new File(name);
        BitmapFactory.Options opts = null;
        if (null != file && file.exists()) {
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), opts);
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(file.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static Bitmap getBitmapFromFile(String path,String name, int width, int height) {
        File file = new File(path,name);
        BitmapFactory.Options opts = null;
        if (null != file && file.exists()) {
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), opts);
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(file.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static int computeSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
    public static String getLobbyBitMapName() {
        SimpleDateFormat sdf = new SimpleDateFormat(CommonData.TIME_STR_FORMAT);
        String time = sdf.format(new Date());
        time += ".png";
        return time;
    }

    public static String getLobbyVideoRecordName() {
        SimpleDateFormat sdf = new SimpleDateFormat(CommonData.TIME_STR_FORMAT);
        String time = sdf.format(new Date());
        time += ".avi";
        return time;
    }

    public static String getIpcBitMapName(String ipcName) {
        String name = ipcName;
        if(TextUtils.isEmpty(ipcName)){
            name = CommonUtils.generateCommonEventUuid();
        }
        name += ".png";
        return name;
    }
    public static MediaFormat getVideoFormat(VideoInfo mVideoInfo) {
        //ByteBuffer bb = ByteBuffer.wrap(mVideoInfo.mCsdInfo);
        MediaFormat media_format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC,CommonData.VIDEO_WIDTH,CommonData.VIDEO_HEIGHT);
       // media_format.setByteBuffer("csd-0", bb);
        HVideoDecoder.setSpsPps(media_format);
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
    public static String hexBytesToHexString(byte[] hexBytes) {
        StringBuilder hexValues = new StringBuilder();
        for (int i = 0; i < hexBytes.length; i++) {
            hexValues.append(String.format("%02x", hexBytes[i]));
        }

        return hexValues.toString();
    }
    public static String hexBytesToHexString(byte[] hexBytes, int length) {
        StringBuilder hexValues = new StringBuilder();
        length = length>=hexBytes.length? hexBytes.length:length;
        for (int i = 0; i < length; i++) {
            hexValues.append(String.format("%02x", hexBytes[i]));
        }

        return hexValues.toString();
    }
    public static byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

 public  static  void deleteOneFile(String fileName){
        if(!TextUtils.isEmpty(fileName)){
            File file = new File(fileName);
            if(file.exists()){
                file.delete();
            }
        }
    }
    public static String convertNativeModuleType(String nativeType) {
        if (nativeType.contains("RELAY") || nativeType.contains("relay")) {
            return CommonData.JSON_MODULE_NAME_RELAY;
        } else if (nativeType.contains("ALARM") || nativeType.contains("alarm")) {
            return CommonData.JSON_MODULE_NAME_ALARM;
        }
        return CommonData.JSON_MODULE_NAME_UNKNOW;
    }
}
