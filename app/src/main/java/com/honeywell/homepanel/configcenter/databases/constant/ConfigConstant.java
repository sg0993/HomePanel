package com.honeywell.homepanel.configcenter.databases.constant;

/**
 * Created by H135901 on 3/13/2017.
 */

public class ConfigConstant {

    public static final String TABLE_PERIPHERALDEVICE = "peripheraldevice";


    //for basic column
    public final static String COLUMN_ID = "_id";//integer
    public final static String COLUMN_UUID = "uuid";//str
    public final static String COLUMN_NAME = "name";//str
    public final static String COLUMN_MODULEUUID = "moduleuuid";//str

    //colume for peripheraldevice
    public final static String COLUMN_PERIPHERALDEVICE_TYPE = "type";//int,
    public final static String COLUMN_PERIPHERALDEVICE_ONLINE = "online";//int
    public final static String COLUMN_PERIPHERALDEVICE_IP = "ip_addr";//str
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

}
