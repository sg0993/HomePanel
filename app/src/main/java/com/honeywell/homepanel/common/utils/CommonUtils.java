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
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.honeywell.homepanel.IConfigService;
import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.BCrypt;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.CommonPath;
import com.honeywell.homepanel.nativeapi.NativeEncry;
import com.honeywell.homepanel.sensingservice.security.AlarmCode;
import com.honeywell.homepanel.ui.AudioVideoUtil.HVideoDecoder;
import com.honeywell.homepanel.ui.AudioVideoUtil.VideoInfo;
import com.honeywell.homepanel.ui.uicomponent.UIEventBusActivityFinishCmd;
import com.honeywell.homepanel.ui.uicomponent.UIEventBusCommonCmd;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by H135901 on 3/3/2017.
 */

public class CommonUtils {
    private static final String TAG = "CommonUtils";
    public static final String FIXSTRING = "HON_HS_CUBE";
    public static final int ENCRYPTION_TYPE_NONE = 0;
    public static final int ENCRYPTION_TYPE_CHACHA = 1;
    public static final int ENCRYPTION_TYPE_ECC = 0x10;

    public static SeekBar showCallVolumeDialog(Activity activity, View.OnClickListener onClickListener,
                                               SeekBar.OnSeekBarChangeListener seekBarChangeListener, boolean bSpeaker, int volume) {
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
        AudioManager am = (AudioManager) activity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if (bSpeaker) {
            seekBar.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seekBar.setProgress(volume);
        } else {

        }
        AlertDialog alert = new AlertDialog.Builder(activity).create();
        alert.show();
        alert.getWindow().setLayout(width / 2, height / 4);
        alert.setTitle("测试");
        alert.getWindow().setContentView(view);
        return seekBar;
    }

    public static void setWindowAlpha(Window window, float f) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = f;
        window.setAttributes(lp);
    }

    public static void startAndBindService(Context context, String serviceAction, ServiceConnection connection) {
        if (null == serviceAction || null == connection) {
            return;
        }
        Intent intent = new Intent(serviceAction);
        intent.setPackage(context.getPackageName());
        context.startService(intent);
        context.bindService(intent, connection, 0);
    }

    public static void stopAndUnbindService(Context context, String serviceAction, ServiceConnection connection) {
        if (null == serviceAction || null == connection || null == context) {
            return;
        }
//        Intent intent = new Intent(serviceAction);
//        intent.setPackage(context.getPackageName());
//        context.stopService(intent);
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

    public static int getModuleLoopCount(String moduleType) {
        if (moduleType.equals(CommonData.JSON_MODULE_NAME_RELAY)) {
            return 4;
        } else if (moduleType.equals(CommonData.JSON_KEY_DEVICETYPE_WIREDZONE)) {
            return 8;//TBD
        }
        return -1;
    }

    public static boolean isValidMac(String mac) {
        String pattern1 = "^[A-F0-9]{2}(:[A-F0-9]{2}){5}$";
        if (Pattern.compile(pattern1).matcher(mac).find()) {
            return true;
        }

        String pattern2 = "^[A-F0-9]{2}(-[A-F0-9]{2}){5}$";
        if (Pattern.compile(pattern2).matcher(mac).find()) {
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
        PowerManager pm = (PowerManager) ctx.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        pm.reboot("null");
    }

    public static void recoveryPM(Context ctx) {
        if (null == ctx) {
            return;
        }
        PowerManager pm = (PowerManager) ctx.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        pm.reboot("recovery");
    }

    public static String generateCommonEventUuid() {
        return UUID.randomUUID().toString();
    }

    public static String generateCommonEventMsgId() {
        return UUID.randomUUID().toString();
    }

    public static String generateCommonEventCloudEncryption() {
        int len = 32;
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }

        String sbString = sb.toString();
        sb.delete(0, sb.length());
        return sbString;
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
        FileOutputStream out = null;
        try {
            file.createNewFile();
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                out.flush();
            }
        } catch (Exception e) {
            Log.e(TAG, "saveBitmap: exception11111111111");
            e.printStackTrace();
            filepath = null;
        }
        finally {
            if(null != out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filepath;
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
                opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
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

    public static Bitmap getBitmapFromFile(String path, String name, int width, int height) {
        File file = new File(path, name);
        BitmapFactory.Options opts = null;
        if (null != file && file.exists()) {
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), opts);
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
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

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
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

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
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
        if (TextUtils.isEmpty(ipcName)) {
            name = CommonUtils.generateCommonEventUuid();
        }
        name += ".png";
        return name;
    }

    public static MediaFormat getVideoFormat(VideoInfo mVideoInfo) {
        //ByteBuffer bb = ByteBuffer.wrap(mVideoInfo.mCsdInfo);
        MediaFormat media_format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, CommonData.VIDEO_WIDTH, CommonData.VIDEO_HEIGHT);
        // media_format.setByteBuffer("csd-0", bb);
        HVideoDecoder.setSpsPps(media_format);
        media_format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, CommonData.VIDEO_WIDTH * CommonData.VIDEO_HEIGHT);
        media_format.setInteger(MediaFormat.KEY_FRAME_RATE, 25);
        return media_format;
    }

    public static MediaCodec.BufferInfo getBufferInfo(int length) {
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        info.flags = MediaCodec.BUFFER_FLAG_KEY_FRAME;
        info.offset = 0;
        info.size = length;
        return info;
    }

    public static String convertCFormateString(byte[] asciiBytes) {
        int cZeroPosition = 0;
        int arraySize = asciiBytes.length;

        for (; cZeroPosition < arraySize && asciiBytes[cZeroPosition] != 0x00; cZeroPosition++) {

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
        length = length >= hexBytes.length ? hexBytes.length : length;
        for (int i = 0; i < length; i++) {
            hexValues.append(String.format("%02x", hexBytes[i]));
        }

        return hexValues.toString();
    }

    public static boolean isValidUUID(String uuid) {
        if ((uuid == null) || (uuid.length() == 1)) {
            return false;
        }
        return true;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static void deleteOneFile(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static String convertNativeModuleType(String nativeType) {
        if (nativeType.contains("RELAY") || nativeType.contains("relay")) {
            return CommonData.JSON_MODULE_NAME_RELAY;
        } else if (nativeType.contains("ALARM") || nativeType.contains("alarm")) {
            return CommonData.JSON_KEY_DEVICETYPE_WIREDZONE;
        }
        return CommonData.JSON_MODULE_NAME_UNKNOW;
    }

    public static int convertScenarioNameToIndex(String scenarioName) {
        if (!TextUtils.isEmpty(scenarioName)) {
            if (scenarioName.equals(CommonData.JSON_SCENARIO_HOME)) {
                return CommonData.SCENARIO_HOME;
            } else if (scenarioName.equals(CommonData.JSON_SCENARIO_AWAY)) {
                return CommonData.SCENARIO_AWAY;
            } else if (scenarioName.equals(CommonData.JSON_SCENARIO_SLEEP)) {
                return CommonData.SCENARIO_SLEEP;
            } else if (scenarioName.equals(CommonData.JSON_SCENARIO_WAKEUP)) {
                return CommonData.SCENARIO_WAKEUP;
            }
        }
        return -1;
    }

    public static String genISO8601TimeStampForCurrTime() {
        String DATEFORMAT_ISO8601_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat TIMEFORMATE = new SimpleDateFormat(DATEFORMAT_ISO8601_UTC);
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();
        Date date = new Date(cal.getTimeInMillis() - timeZone.getRawOffset());

        String timeStamp = TIMEFORMATE.format(date);

        return timeStamp;
    }

    public static String deISO8601TimeStampForCurrTime(String ISO8601TimeStamp) {
        //timeStamp:2010-01-01T00:03:24Z
        //remove 'Z'
        String str = ISO8601TimeStamp.substring(0, ISO8601TimeStamp.length() - 1).replace("T", " ");
        if (str.length() != 19) {
            Log.w(TAG, "deISO8601TimeStampForCurrTime: error");
        }
        return str;
    }


    /**
     * @param strDate must be align with SimpleDateFormat() prarmeter
     * @return a Long type of date
     */
    public static long convertStringDateToLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate.getTime();
    }

    /**
     * Input integer year, month, day  return a "yyyy/MM/dd" format date string
     * @return
     */
    public static String convertLongDateToSlashFormat(int year, int month, int day) {
        GregorianCalendar date1 = new GregorianCalendar(year, month, day);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        formatter.setCalendar(date1);
        return formatter.format(date1.getTime());
    }

    /**
     * Input integer year, month, day  return a "yyyy/MM/dd" format date string
     * @return
     */
    public static String convertDateToyyyyMMdd(int year, int month, int day) {
        GregorianCalendar date1 = new GregorianCalendar(year, month, day);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        formatter.setCalendar(date1);
        return formatter.format(date1.getTime());
    }

    /**
     *
     * @param dateString 20170810
     * @return 2017/08/10
     */
    public static String convertyyyyMMddToSlashFormat(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = formatter.parse(dateString);
            return convertMillisToSlashFormat(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static long convertyyyyMMddToMillis(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = formatter.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long convertyyyyMMddToMillis_zh(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = formatter.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static long convertyyyyMMddToMillis_en(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM, dd, yyyy");
        try {
            Date date = formatter.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param millis
     * @return
     */
    public static String convertMillisToSlashFormat(long millis) {
        Date date = new Date(millis );
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(date);
    }

    public static String convertMillisToEnglishFormat(long millis){
        Date date = new Date(millis);
        SimpleDateFormat formatter = new SimpleDateFormat("MM, dd, yyyy");
        return formatter.format(date);
    }

    /**
     * @param year 2017
     * @param month 03
     * @param day 10
     * @return 10, April, 2017
     */
    public static String convertToEnglisthDate(int year, int month, int day) {
        GregorianCalendar date1 = new GregorianCalendar(year, month, day);
        SimpleDateFormat formatter = new SimpleDateFormat("MM, dd, yyyy");
        formatter.setCalendar(date1);
        String dateString = formatter.format(date1.getTime());
        return dateString;
    }


    /**
     * @param millis millis of date
     * @return
     */
    public static String convertMillisToDate(long millis) {
        Date date = new Date(millis );
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = formatter.format(date);
        return str;
    }

    public static String getconvertMillisToDate(String data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        try {
            Date date = format.parse(data);
            return convertMillisToDate(date.getTime() + (8 * 60 * 60000));//workaround
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param millis millis of date
     * @return
     */
    public static String convertMillisToDateForAms(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = formatter.format(date);
        return str;
    }

//    public static String convertDateForNotification(String str) {
//        SimpleDateFormat formatterOri = new SimpleDateFormat("yyyyMMddHHMMSS");
//        ParsePosition pos = new ParsePosition(0);
//        Date dest = formatterOri.parse(str, pos);
//        SimpleDateFormat formatterDest = new SimpleDateFormat("yyyy.MM.dd HH:MM");
//        String destFormatDate = formatterDest.format(dest);
//        Log.d(TAG, "convertDateForNotification: " + destFormatDate);
//        return destFormatDate;
//    }

    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(strDate, pos);
        return date;
    }

    public static String formatterDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static String formatterDate2(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }
    /**
     * This is only necessary when upload data to cloud with 'datatime'( currently cloud only support UTC+0 TimeZone)
     * @param localMillis localMillis of date
     * @return
     */
    public static String convertMillisToISO8601Date(long localMillis) {
        Date date = new Date(localMillis);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC+0"));
        String str = formatter.format(date);
        return str;
    }


    /**
     * @param ISO8601Date millis of date
     * @return
     */
    public static String convertISO8601DateToAMSProtocolDate(String ISO8601Date) {
        if (TextUtils.isEmpty(ISO8601Date)) {
            return null;
        }

        String normalDate = deISO8601TimeStampForCurrTime(ISO8601Date);

        long millis = convertStringDateToLong(normalDate);

        Date date = new Date(millis);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHMMSS");
        String str = formatter.format(date);
        return str;
    }


    public static boolean judgeZoneType(String zoneType) {
        boolean bRet = false;
        if (null == zoneType) {
            return bRet;
        }
        if (zoneType.equals(CommonData.ZONETYPE_24H) || zoneType.equals(CommonData.ZONETYPE_TRIGGER)
                || zoneType.equals(CommonData.ZONETYPE_DELAY) || zoneType.equals(CommonData.ZONETYPE_ENVIRONMENT)
                || zoneType.equals(CommonData.ZONETYPE_INSTANT)) {
            bRet = true;
        }
        return bRet;
    }

    public static boolean isHejModule(String type) {
        if (CommonData.JSON_MODULE_NAME_RELAY.equals(type) || CommonData.JSON_KEY_DEVICETYPE_WIREDZONE.equals(type)) {
            return true;
        }

        return false;
    }

    /**
     * @param doorType eg:front door  back door
     * @return
     */
    public static String ConvertDoorTypeToID(String doorType) {
        if (doorType.contains("front")) {
            return "1";
        } else if (doorType.contains("back")) {
            return "2";
        }
        return "1";
    }

    /**
     * @param role eg:front door  back door
     * @return
     */
    public static String ConvertRoleTypeToID(String role) {
        if (role.contains(CommonData.JSON_VALUE_ROLE_HOST)) {
            return "0";
        } else if (role.contains(CommonData.JSON_VALUE_ROLE_HOSTESS)) {
            return "1";
        } else if (role.contains(CommonData.JSON_VALUE_ROLE_CHILD)) {
            return "2";
        } else if (role.contains(CommonData.JSON_VALUE_ROLE_HOUSEKEEPER)) {
            return "3";
        } else if (role.contains(CommonData.JSON_VALUE_ROLE_FRIEND)) {
            return "4";
        } else if (role.contains(CommonData.JSON_VALUE_ROLE_RELATIVE)) {
            return "5";
        } else if (role.contains(CommonData.JSON_VALUE_ROLE_ELDERLY)) {
            return "6";
        } else {
            return "7";
        }
    }

    public static JSONArray getJsonArrayFromDb(JSONObject jsonObject, IConfigService mIConfigService) throws Exception {
        if (null != mIConfigService && null != jsonObject) {
            byte[] resp = mIConfigService.getFromDbManager(jsonObject.toString().getBytes());
            if (null != resp) {
                String jStr = new String(resp);
                Log.d(TAG, "getJsonArrayFromDb: json:" + jStr);
                JSONObject respObj = new JSONObject(jStr);
                JSONArray jsonArray = respObj.optJSONArray(CommonJson.JSON_LOOPMAP_KEY);
                return jsonArray;
            }
        }
        return null;
    }

    public static int convertAlarmMsgIdToResId(String alarmMsgId) {
        int alarmCode = Integer.parseInt(alarmMsgId);
        int resourceId = R.string.alarmcontent_emergency;

        switch (alarmCode) {
            case AlarmCode.ALARM_CODE_DOOROPEN:
                resourceId = R.string.alarmcontent_dooropen;
                break;
            case AlarmCode.ALARM_CODE_DURESS:
            case AlarmCode.ALARM_CODE_DURESS_ACCESS:
            case AlarmCode.ALARM_CODE_DURESS_EGRESS:
                resourceId = R.string.alarmcontent_duress;
                break;
            case AlarmCode.ALARM_CODE_FIRE:
                resourceId = R.string.alarmcontent_fire;
                break;
            case AlarmCode.ALARM_CODE_GAS:
                resourceId = R.string.alarmcontent_gas;
                break;
            case AlarmCode.ALARM_CODE_INTRUSION:
                resourceId = R.string.alarmcontent_intrusion;
                break;
            case AlarmCode.ALARM_CODE_LOWBATTERY:
                resourceId = R.string.alarmcontent_lowbattery;
                break;
            case AlarmCode.ALARM_CODE_MEDICALAID:
                resourceId = R.string.alarmcontent_medicaid;
                break;
            case AlarmCode.ALARM_CODE_TAMPER:
                resourceId = R.string.alarmcontent_tamper;
                break;
            case AlarmCode.ALARM_CODE_PWDWRONG:
                resourceId = R.string.alarmcontent_wrongpwd;
                break;
            case AlarmCode.ALARM_CODE_PANIC:
            case AlarmCode.ALARM_CODE_ZONETROUBLE:
            case AlarmCode.ALARM_CODE_EMERGENCY:
            default:
                resourceId = R.string.alarmcontent_emergency;
        }

        return resourceId;
    }

    public static int convertAlarmTypeToResId(String alarmType) {
        int resourceId = R.string.alarmcontent_emergency;

        if (alarmType.equals(CommonData.ALARMTYPE_EMERGENCY) || alarmType.equals(CommonData.ALARMTYPE_EMERGENCY_NODISTURB)
                || alarmType.equals(CommonData.ALARMTYPE_EMERGENCY_SILENCE)) {
            resourceId = R.string.alarmtype_emergency;
        } else if (alarmType.equals(CommonData.ALARMTYPE_FIRE)) {
            resourceId = R.string.alarmcontent_fire;
        } else if (alarmType.equals(CommonData.ALARMTYPE_GAS)) {
            resourceId = R.string.alarmtype_gas;
        } else if (alarmType.equals(CommonData.ALARMTYPE_INTRUSION)) {
            resourceId = R.string.alarmtype_intrusion;
        } else if (alarmType.equals(CommonData.ALARMTYPE_TAMPER)) {
            resourceId = R.string.alarmtype_tamper;
        }

        return resourceId;
    }

    /**
     * byte array to hex string
     *
     * @param bytes
     * @return
     */
    public static String byteArrayToHexString(byte[] bytes) {
        if (null == bytes) {
            return "";
        }
        char[] hexArray =
                {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] hexChars = new char[bytes.length * 2];
        int v;

        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static byte[] appendSendingBytes(byte[] bytes, int size, int encType,
                                            byte[] encKey) {
        byte[] data;
        if (bytes == null || size == 0) {
            return null;
        }
        if (size >= bytes.length) {
            data = bytes;
        } else {
            data = Arrays.copyOfRange(bytes, 0, size);
        }

        data = wrapArroudData(data, encType, encKey);
        return data;
    }

    private static byte[] wrapArroudData(byte[] bytes, int encType, byte[] encKey) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        int size;
        byte[] header = FIXSTRING.getBytes();
        byte[] data = null;

        // write miracle string
        byteArray.write(header, 0, header.length);

        // write encrypt type
        byteArray.write(encType);

        data = encryptData(bytes, encType, encKey);
        if (null == data) {
            size = 0;
        } else {
            size = data.length;
        }
//        Log.d(TAG, "Util.java wrapArroudData body length="+size+",,,,\r\n");

        // write length
        byteArray.write((size & 0xff000000) >> 24);
        byteArray.write((size & 0x00ff0000) >> 16);
        byteArray.write((size & 0x0000ff00) >> 8);
        byteArray.write(size & 0x000000ff);

        // write body
        byteArray.write(data, 0, size);

        return byteArray.toByteArray();
    }

    private static byte[] encryptData(byte[] bytes, int encType, byte[] encKey) {
        byte ret[] = null;
        switch (encType) {
            case ENCRYPTION_TYPE_NONE:
                ret = bytes;
                break;
            case ENCRYPTION_TYPE_CHACHA:
                ret = bytes;
                break;
            case ENCRYPTION_TYPE_ECC:
                if (null != encKey) {
                    ret = NativeEncry.encWithKey(bytes,
                            bytes.length, encKey);
                } else {
                    ret = bytes;
                }
                break;
        }

        return ret;
    }

    /**
     * hex string to byte array
     *
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        if (len % 2 != 0) {
            len = len - 1; //maybe should return null.
        }
        if (len <= 0) {
            return null;
        }
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    public static String getFileSHA256Str(File file) {
        if (!file.isFile()) {
            return null;
        }

        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;

        try {
            digest = MessageDigest.getInstance("SHA256");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if(null != in){
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        BigInteger bigInt = new BigInteger(1, digest.digest());
        // Alwin: if start with '0'，it will be removed, so checkit.
        String retStr = bigInt.toString(16);//16->hex string
        while (retStr.length() < 64) {
            retStr = "0" + retStr;
        }

        return retStr;//bigInt.toString(16);
    }

    public static boolean isHomePanelByDeviceType(String deviceType) {
        if (CommonJson.JSON_UPGRADE_DEVTYPE_VDP7KA.equals(deviceType)
                || CommonJson.JSON_UPGRADE_DEVTYPE_VDP7PRO.equals(deviceType)) {
            return true;
        }

        return false;
    }

    public static boolean isHejModuleByDeviceType(String deviceType) {
        if (CommonJson.JSON_UPGRADE_DEVTYPE_HEJRELAY4.equals(deviceType)
                || CommonJson.JSON_UPGRADE_DEVTYPE_HEJALARM8.equals(deviceType)) {
            return true;
        }

        return false;
    }

    public static String covertHejModuleDeviceType(String deviceTypeName) {
        if (CommonJson.JSON_UPGRADE_DEVTYPE_HEJRELAY4.equals(deviceTypeName)) {
            return CommonData.JSON_MODULE_NAME_RELAY;
        } else if (CommonJson.JSON_UPGRADE_DEVTYPE_HEJALARM8.equals(deviceTypeName)) {
            return CommonData.JSON_MODULE_NAME_ALARM;
        }

        return CommonData.JSON_MODULE_NAME_RELAY;
    }


    //added by ellen
    public static ExecutorService getCachedThreadPool(int count) {
        ExecutorService cachedThreadPool = null;
        //cached thread for not device number thread
        if (count <= 0) {
            cachedThreadPool = Executors.newCachedThreadPool();
        }
        //single thread
        else if (count == 1) {
            cachedThreadPool = Executors.newSingleThreadExecutor();
        }
        //inflate definite number cached thread
        else {
            //Runtime.getRuntime().availableProcessors()
            cachedThreadPool = Executors.newFixedThreadPool(count);
        }
        return cachedThreadPool;
    }

    //execute thread,added by ellen
    public static void executeThreadPool(ExecutorService service, Runnable runnable) {
        if (null == service || null == runnable) {
            return;
        }
        service.execute(runnable);
    }

    // er fen cha zhao,added by ellen
    public static int find_position_by_id(int id, int[] data) {
        int count = data.length;
        int middle = (count - 1) / 2;
        int left = 0;
        int right = (count - 1);
        int position = -1;
        while (left <= right) {
            middle = (left + right) / 2;
            if (data[middle] > id) {
                //left
                right = middle - 1;
            } else if (data[middle] < id) {
                //right
                left = middle + 1;
            } else {
                position = middle;
                break;
            }
        }
        return position;
    }

    public static String getHomePanelDeviceId() {
        String deviceId = NetWorkInfoUtils.getCommunityNetworkMacAddr();
        if(TextUtils.isEmpty(deviceId)){
            return  "unknown";
        }
        if(null != deviceId){
            deviceId = deviceId.toLowerCase().replace(":", "");
        }
        return deviceId;
    }

    public static String file_() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName();
    }

    public static int line_() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getLineNumber();
    }

    public static String fileLine() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ste.getLineNumber();
    }

    public static String getStringSHA256Str(String  str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        byte[] buffer = str.getBytes();
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("SHA256");
            digest.update(buffer, 0, buffer.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        BigInteger bigInt = new BigInteger(1, digest.digest());
        String retStr = bigInt.toString(16);//16->hex string
        while(retStr.length() < 64) {
            retStr = "0" + retStr;
        }
        return retStr;
    }


    public  static  final String getBcryptStr(String pwd){
        if(TextUtils.isEmpty(pwd)){
            return pwd;
        }
        String pwd_sha256 = CommonUtils.getStringSHA256Str(pwd);
        Log.d(TAG, "getBcryptStr() pwd_sha256:"+pwd_sha256+",,1111111111");
        String hashed = BCrypt.hashpw(pwd_sha256,BCrypt.gensalt());
        Log.d(TAG, "getBcryptStr() hashed:"+hashed+",,22222222");
        return  hashed;
    }


    public  static  final boolean checkBcryptStr(String pwd,String dbPwd){
        Log.d(TAG, "checkBcryptStr() pwd:"+pwd+",dbPwd:"+dbPwd+",,11111111111");
        if(TextUtils.isEmpty(pwd) || TextUtils.isEmpty(dbPwd)){
            return  false;
        }
        try {
            if (pwd.equals(dbPwd)) {
                return true;
            }
            String pwd_sha256 = CommonUtils.getStringSHA256Str(pwd);
            if (pwd_sha256.equals(dbPwd)) {
                return true;
            }
            if (BCrypt.checkpw(pwd, dbPwd)) {
                return true;
            }
            Log.d(TAG, "checkBcryptStr() pwd_sha256:" + pwd_sha256);
            boolean bRet = BCrypt.checkpw(pwd_sha256, dbPwd);
            Log.d(TAG, "checkBcryptStr() bRet:" + bRet);
            return bRet;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "checkBcryptStr: faile ");
            return false;
        }
    }
    public static boolean checkBcryptStr_sha256(String cloudPwd, String dbPwd) {
        if(TextUtils.isEmpty(cloudPwd) || TextUtils.isEmpty(dbPwd)){
            return  false;
        }
        if(cloudPwd.equals(dbPwd)){
            return  true;
        }
        Log.d(TAG, "checkBcryptStr() pwd_sha256:"+cloudPwd);
        boolean bRet = BCrypt.checkpw(cloudPwd,dbPwd);
        Log.d(TAG, "checkBcryptStr() bRet:"+bRet);
        return bRet;
    }

    /**
     * send common command in ui thread
     * @param cmd
     */
    public static void sendUIEventBusCommonCmd(int cmd) {
        UIEventBusCommonCmd tmp = new UIEventBusCommonCmd();
        if (tmp != null) {
            tmp.setScreenSaverCtrlCmd(cmd);
            EventBus.getDefault().post(tmp);
        }
    }

    public static void sendUIEventBusFinishCmd(int cmd) {
        UIEventBusActivityFinishCmd tmp = new UIEventBusActivityFinishCmd();
        if (tmp != null) {
            Log.d(TAG, "sendUIEventBusFinishCmd: " + cmd);
            tmp.setCmd(cmd);
            EventBus.getDefault().post(tmp);
        }
    }

    public  static  String getFileName(String fileName){
        String name = "";
        if(TextUtils.isEmpty(fileName)){
            return name;
        }
        int index = fileName.indexOf(".");
        if(index >= 0){
            String pre = fileName.substring(0, index);
            String[] aa = pre.split("/");
            if(null != aa){
                name = aa[aa.length -1];
            }
        }
        return name;
    }

    /**
     * convert dongho 4-4 to 4-6
     * dongho must be 06020306
     */
    public static String convertDongHao44To46(String donghao) {
        String strDong = null;
        String strHao = null;

        if (!TextUtils.isEmpty(donghao)) {
            int hao = Integer.valueOf(donghao.substring(4));
            strDong = donghao.substring(0, 4);
            strHao =  String.format("%06d", hao);
        }
        return strDong+strHao;
    }

    /**
     *
     * @return system eplased time
     */
    public static long getSystemElapsedTime(){
        return SystemClock.elapsedRealtime();
    }
}
