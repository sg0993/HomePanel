package com.honeywell.homepanel.configcenter.databases.constant;

/**
 * Created by H135901 on 3/13/2017.
 */

public class ConfigConstant {

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



    //for basic column
    public final static String COLUMN_ID = "_id";//integer
    public final static String COLUMN_UUID = "uuid";//str
    public final static String COLUMN_NAME = "name";//str
    public final static String COLUMN_MODULEUUID = "moduleuuid";//str
    public final static String COLUMN_TYPE = "type";//str
    public static final String COLUMN_LOOP = "loop";
    public static final String COLUMN_DELAYTIME = "delaytime";
    public static final String COLUMN_ENABLED = "enabled";

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

    //colume for peripheraldevice
    public final static String COLUMN_PERIPHERALDEVICE_TYPE = COLUMN_TYPE;//int,
    public final static String COLUMN_PERIPHERALDEVICE_ONLINE = "online";//int
    public final static String COLUMN_PERIPHERALDEVICE_IP = COLUMN_IPADDR;//str
    public final static String COLUMN_PERIPHERALDEVICE_MAC = "mac_addr";//str
    public final static String COLUMN_PERIPHERALDEVICE_VERSION = "version";//byte

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

    public  static final int HOMEPANEL_ZONE_COUNT = 8;

    public static String createPeripheralDeviceTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_PERIPHERALDEVICE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MODULEUUID + " nvarchar(128), "
                + COLUMN_NAME + " nvarchar(64), "
                + COLUMN_PERIPHERALDEVICE_TYPE + " nvarchar(32), "
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
                + COLUMN_TYPE + " nvarchar(32)"//curtain,relay,light...
                + ")";
    }

    public static  String createRelayLoopTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_RELAYLOOP + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MODULEUUID + " nvarchar(128), "
                + COLUMN_UUID + " nvarchar(128), "
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
                + COLUMN_READ + " INTEGER"
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
        return "CREATE TABLE IF NOT EXISTS " + TABLE_ALARMHISTORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_UUID + " nvarchar(128), "
                + COLUMN_TIME + " INTEGER, "
                + COLUMN_ALARMTYPE + " nvarchar(32), "
                + COLUMN_MESSAGE + " nvarchar(512), "
                + COLUMN_READ + " INTEGER"
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
}