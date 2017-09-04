package com.honeywell.homepanel.configcenter.databases.constant;

import android.util.Log;
import android.util.SparseArray;


/**
 * Created by H135901 on 3/13/2017.
 */

public class ConfigConstant {
    private static final String TAG = "ConfigConstant";
    public static final String TABLE_PERIPHERALDEVICE = "peripheraldevice";
    public static final String TABLE_COMMONDEVICE = "commondevice";
    public static final String TABLE_RELAYLOOP = "relayloop";
    public static final String TABLE_ZONELOOP = "zoneloop";
    public static final String TABLE_IPC = "ipc";
    public static final String TABLE_SCENARIO = "scenario";
    public static final String TABLE_EVENTHISTORY = "eventhistory";
    public static final String TABLE_VOICEMESSAGE = "voicemessage";
    public static final String TABLE_ALARMHISTORY = "alarmhistory";
    public static final String TABLE_SPEEDDIAL = "speeddial";
    public static final String TABLE_IPDOORCARD = "ipdoorcard";
    public static final String TABLE_DEVICEADAPTER = "deviceadapter";
    public static final String TABLE_LOCALDEVICE = "localdevice";
    public static final String TABLE_SYSTEMSETTINGS = "systemsettings";

    public static final int TABLE_PERIPHERALDEVICE_INT = 0;
    public static final int TABLE_COMMONDEVICE_INT = 1;
    public static final int TABLE_RELAYLOOP_INT = 2;
    public static final int TABLE_ZONELOOP_INT = 3;
    public static final int TABLE_IPC_INT = 4;
    public static final int TABLE_SCENARIO_INT = 5;
    public static final int TABLE_EVENTHISTORY_INT = 6;
    public static final int TABLE_VOICEMESSAGE_INT = 7;
    public static final int TABLE_ALARMHISTORY_INT = 8;
    public static final int TABLE_SPEEDDIAL_INT = 9;
    public static final int TABLE_IPDOORCARD_INT = 10;
    public static final int TABLE_DEVICEADAPTER_INT = 11;
    public static final int TABLE_LOCALDEVICE_INT = 12;



    //for basic column
    public final static String COLUMN_ID = "_id";//integer
    public final static String COLUMN_UUID = "uuid";//str
    public final static String COLUMN_NAME = "name";//str
    public final static String COLUMN_MODULEUUID = "moduleuuid";//str
    public final static String COLUMN_ADAPTERNAME = "adaptername";
    public final static String COLUMN_TYPE = "type";//str
    public static final String COLUMN_LOOP = "loop";
    public static final String COLUMN_DELAYTIME = "delaytime";
    public static final String COLUMN_ENABLED = "enabled";
    public static final String COLUMN_PHYSICTYPE = "physictype";
    public static final String COLUMN_CONFIGURATION = "configuration";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_VALUE = "value";

    public static final String COLUMN_ZONETYPE = "zonetype";
    public static final String COLUMN_ALARMTYPE = "alarmtype";
    public static final String COLUMN_IPADDR = "ip_addr";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_DEVICEUUID = "deviceuuid";
    public static final String COLUMN_ACTION = "action";

    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_CARDNO = "cardno";
    public static final String COLUMN_CARDEVENT = "cardevent";
    public static final String COLUMN_IMAGEPATH = "imagepath";
    public static final String COLUMN_VIDEOPATH = "videopath";
    public static final String COLUMN_READ = "read";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_DONGHO = "dongho";
    public static final String COLUMN_STARTDATE = "startdate";
    public static final String COLUMN_EXPIREDATE = "expiredate";
    public static final String COLUMN_STARTTIME = "starttime";
    public static final String COLUMN_EXPIRETIME = "expiretime";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_UPLOADAMS = "uploadams";
    public static final String COLUMN_UPLOADCLOUD = "uploadcloud";
    public static final String COLUMN_ROLEID = "roleid";
    public static final String COLUMN_TRIGGERID = "triggerid";

    //colume for peripheraldevice
    public final static String COLUMN_PERIPHERALDEVICE_TYPE = COLUMN_TYPE;//int,
    public final static String COLUMN_PERIPHERALDEVICE_ONLINE = "online";//int
    public final static String COLUMN_PERIPHERALDEVICE_IP = COLUMN_IPADDR;//str
    public final static String COLUMN_PERIPHERALDEVICE_PORT = "port";//byte
    public final static String COLUMN_PERIPHERALDEVICE_MAC = "mac_addr";//str
    public final static String COLUMN_PERIPHERALDEVICE_VERSION = "version";//byte
    public final static String COLUMN_PERIPHERALDEVICE_MODEL = "model";//byte

    //home panel config key
    public static final String KEY_DBVERSIONID = "dbversionid";
    public static final String KEY_SECURITY_PASSWORD = "security_password";
    public  static final String KEY_CITY = "city";
    public  static final String KEY_UNIT = "unit";
    public  static final String KEY_FLOOR = "floor";
    public  static final String KEY_FRONTLOCKTYPE = "frontlocktype";
    public  static final String KEY_BACKLOCKTYPE = "backlocktype";
    public  static final String KEY_REGISTERPWD = "registerpwd";
    public  static final String KEY_AMSIP = "amsip";
    public  static final String KEY_AMSPORT = "amsport";
    public  static final String KEY_ELEVATORIP = "elevatorip";
    public  static final String KEY_ELEVATORPORT = "elevatorport";
    public  static final String KEY_MAINIP = "mainip";
    public  static final String KEY_MAINPORT = "mainport";
    public  static final String KEY_SUBPHONEID = "subphoneid";
    public  static final String KEY_ENGINEERPWD = "engineerpwd";
    public  static final String KEY_HOMEPANELTYPE = "homepaneltype";
    public  static final String KEY_HOMESCREEN = "homescreen";
    public  static final String KEY_TEMPER = "temper";//home panel 防拆
    public  static final String KEY_CURSCENARIO = "curscenario";//cur scenario uuid


    public static final  String[] DEFAULT_SCENARIO_NAME = {"回家","离家","睡眠","起床"};

    // 13 IOs
    public static final int HOMEPANEL_LOCALIO_HONZONES_BUGLAR_COUNT = 6;
    public static final int HOMEPANEL_LOCALIO_HONZONES_EMER_COUNT = 1;
    public static final int HOMEPANEL_LOCALIO_HONZONES_HELP_COUNT = 1;
    public static final int HOMEPANEL_LOCALIO_HONZONES_GAS_COUNT = 1;
    public static final int HOMEPANEL_LOCALIO_HONZONES_FIRE_COUNT = 1;
    public static final int HOMEPANEL_LOCALIO_HONDOORBELL_COUNT = 1;
    public static final int HOMEPANEL_LOCALIO_HONSOS_COUNT = 1;
    public static final int HOMEPANEL_LOCALIO_HONTAMPER_COUNT = 1;
    public static final int HOMEPANEL_LOCALIO_HONLOWBATTERY_COUNT = 1;
    public static final int HOMEPANEL_LOCALIO_COUNT = HOMEPANEL_LOCALIO_HONZONES_BUGLAR_COUNT + HOMEPANEL_LOCALIO_HONZONES_EMER_COUNT
                                                    + HOMEPANEL_LOCALIO_HONZONES_HELP_COUNT + HOMEPANEL_LOCALIO_HONZONES_GAS_COUNT
                                                    + HOMEPANEL_LOCALIO_HONZONES_FIRE_COUNT + HOMEPANEL_LOCALIO_HONDOORBELL_COUNT
                                                    + HOMEPANEL_LOCALIO_HONSOS_COUNT + HOMEPANEL_LOCALIO_HONTAMPER_COUNT
                                                    + HOMEPANEL_LOCALIO_HONLOWBATTERY_COUNT;

    public static final int HOMEPANEL_IPDC_COUNT = 2;
    public static final int HOMEPANEL_LOCK_COUNT = 2;

    public static SparseArray<String> mTableNameMap;
    static {
        mTableNameMap = new SparseArray<String>();
        mTableNameMap.put(TABLE_PERIPHERALDEVICE_INT, TABLE_PERIPHERALDEVICE);
        mTableNameMap.put(TABLE_COMMONDEVICE_INT, TABLE_COMMONDEVICE);
        mTableNameMap.put(TABLE_RELAYLOOP_INT, TABLE_RELAYLOOP);
        mTableNameMap.put(TABLE_ZONELOOP_INT, TABLE_ZONELOOP);
        mTableNameMap.put(TABLE_IPC_INT, TABLE_IPC);
        mTableNameMap.put(TABLE_SCENARIO_INT, TABLE_SCENARIO);
        mTableNameMap.put(TABLE_EVENTHISTORY_INT, TABLE_EVENTHISTORY);
        mTableNameMap.put(TABLE_VOICEMESSAGE_INT, TABLE_VOICEMESSAGE);
        mTableNameMap.put(TABLE_ALARMHISTORY_INT, TABLE_ALARMHISTORY);
        mTableNameMap.put(TABLE_SPEEDDIAL_INT, TABLE_SPEEDDIAL);
        mTableNameMap.put(TABLE_IPDOORCARD_INT, TABLE_IPDOORCARD);
        mTableNameMap.put(TABLE_DEVICEADAPTER_INT, TABLE_DEVICEADAPTER);
    }

    public static String getTableNameByTableInt(int tableInt) {
        return mTableNameMap.get(tableInt);
    }

    public static String createPeripheralDeviceTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_PERIPHERALDEVICE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MODULEUUID + " nvarchar(128), "
                + COLUMN_NAME + " nvarchar(64), "
                + COLUMN_PERIPHERALDEVICE_TYPE + " nvarchar(32), "
                + COLUMN_PERIPHERALDEVICE_MODEL + " nvarchar(32), "
                + COLUMN_PERIPHERALDEVICE_IP + " nvarchar(32), "
                + COLUMN_PERIPHERALDEVICE_MAC + " nvarchar(64), "
                + COLUMN_PERIPHERALDEVICE_ONLINE + " INTEGER, "
                + COLUMN_PERIPHERALDEVICE_VERSION + " nvarchar(32)"
                + ") ";
    }

    public static String createCommonDeviceTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_COMMONDEVICE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_UUID + " nvarchar(128), "
                + COLUMN_NAME + " nvarchar(64), "
                + COLUMN_TYPE + " nvarchar(32),"//curtain,relay,light...
                + COLUMN_ENABLED + " INTEGER"
                + ")";
    }

    public static  String createRelayLoopTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_RELAYLOOP + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MODULEUUID + " nvarchar(128), "
                + COLUMN_UUID + " nvarchar(128), "
                + COLUMN_ADAPTERNAME + " nvarchar(128), "
                + COLUMN_NAME + " nvarchar(64), "
                + COLUMN_LOOP + " INTEGER, "
                + COLUMN_DELAYTIME + " INTEGER, "
                + COLUMN_ENABLED + " INTEGER"
                + ") ";
    }

    public static  String createZoneLoopTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_ZONELOOP + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MODULEUUID + " nvarchar(128), "
                + COLUMN_UUID + " nvarchar(128), "
                + COLUMN_ADAPTERNAME + " nvarchar(128), "
                + COLUMN_NAME + " nvarchar(64), "
                + COLUMN_LOOP + " INTEGER, "
                + COLUMN_DELAYTIME + " INTEGER, "
                + COLUMN_ENABLED + " INTEGER, "
                + COLUMN_ZONETYPE + " nvarchar(32), "
                + COLUMN_ALARMTYPE + " nvarchar(32)"
                + ") ";
    }
    public static  String createIpcLoopTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_IPC + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_UUID + " nvarchar(128), "
                + COLUMN_NAME + " nvarchar(64), "
                + COLUMN_IPADDR + " nvarchar(32), "
                + COLUMN_USER + " nvarchar(32), "
                + COLUMN_PASSWORD + " nvarchar(32)"
                + ") ";
    }
    public static  String createScenarioLoopTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_SCENARIO + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_UUID + " nvarchar(128), "//scenario's uuid
                + COLUMN_NAME + " nvarchar(64), "
                + COLUMN_DEVICEUUID + " nvarchar(128), "//device's uuid of scenario
                + COLUMN_ACTION + " nvarchar(1024)"//device's action of scenario
                + ") ";
    }

    public static  String createEventHistoryTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_EVENTHISTORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_UUID + " nvarchar(128), "
                + COLUMN_TIME + " nvarchar(64), "
                + COLUMN_TYPE + " nvarchar(32), "//vistor,card,door
                + COLUMN_CARDNO + " nvarchar(128), "
                + COLUMN_CARDEVENT + " nvarchar(256), "
                + COLUMN_IMAGEPATH + " nvarchar(256), "
                + COLUMN_VIDEOPATH + " nvarchar(256), "
                + COLUMN_READ + " INTEGER,"
                + COLUMN_UPLOADAMS + " INTEGER,"
                + COLUMN_UPLOADCLOUD + " INTEGER,"
                + COLUMN_ROLEID + " INTEGER"
                + ") ";
    }

    public static  String createVoiceMessageTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_VOICEMESSAGE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_UUID + " nvarchar(128), "
                + COLUMN_TIME + " nvarchar(64), "
                + COLUMN_LENGTH + " INTEGER, "
                + COLUMN_PATH + " nvarchar(256), "
                + COLUMN_READ + " INTEGER"
                + ") ";
    }

    public static  String createAlarmHistoryTable() {
        Log.d(TAG, "createAlarmHistoryTable: ");
        return "CREATE TABLE IF NOT EXISTS " + TABLE_ALARMHISTORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_UUID + " nvarchar(128), "
                + COLUMN_TRIGGERID + " nvarchar(128), "
                + COLUMN_TIME + " INTEGER, "
                + COLUMN_NAME + " nvarchar(32),"
                + COLUMN_ALARMTYPE + " nvarchar(32), "
                + COLUMN_MESSAGE + " nvarchar(512), "
                + COLUMN_READ + " INTEGER,"
                + COLUMN_UPLOADAMS + " INTEGER,"
                + COLUMN_UPLOADCLOUD + " INTEGER"
                + ") ";
    }

    public static  String createSpeedDialTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_SPEEDDIAL + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_UUID + " nvarchar(128), "
                + COLUMN_TYPE + " nvarchar(32), "
                + COLUMN_DONGHO + " nvarchar(32)"
                + ") ";
    }

    public static  String createIpDoorCardTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_IPDOORCARD + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_UUID + " nvarchar(128), "
                + COLUMN_TYPE + " nvarchar(32), "
                + COLUMN_NAME + " nvarchar(64), "
                + COLUMN_CARDNO + " nvarchar(128), "
                + COLUMN_STARTDATE + " nvarchar(64), "
                + COLUMN_EXPIREDATE + " nvarchar(64), "
                + COLUMN_STARTTIME + " nvarchar(64), "
                + COLUMN_EXPIRETIME + " nvarchar(64), "
                + COLUMN_ACTION + " INTEGER"
                + ") ";
    }

    public static  String createDeviceAdapterTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_DEVICEADAPTER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_UUID + " nvarchar(128), "
                + COLUMN_TYPE + " nvarchar(32), "
                + COLUMN_NAME + " nvarchar(64) unique, "
                + COLUMN_DESCRIPTION + " nvarchar(128), "
                + COLUMN_ENABLED + " INTEGER "
                + ") ";
    }

    public static  String createLocalDeviceTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_LOCALDEVICE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_UUID + " nvarchar(128), "
                + COLUMN_NAME + " nvarchar(64), "
                + COLUMN_TYPE + " nvarchar(32) , "
                + COLUMN_PHYSICTYPE + " nvarchar(32), "
                + COLUMN_ADAPTERNAME + " nvarchar(128), "
                + COLUMN_ENABLED + " INTEGER, "
                + COLUMN_CONFIGURATION + " nvarchar(1024) "
                + ") ";
    }

    public static  String createSystemSettingsTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_SYSTEMSETTINGS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_KEY + " nvarchar(32) unique, "
                + COLUMN_VALUE + " nvarchar(128)"
                + ") ";
    }
}
