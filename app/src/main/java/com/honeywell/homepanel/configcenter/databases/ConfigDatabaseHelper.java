package com.honeywell.homepanel.configcenter.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.manager.ContentValuesFactory;
import com.honeywell.homepanel.configcenter.databases.manager.DbCommonUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by H135901 on 3/13/2017.
 */

public class ConfigDatabaseHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = CommonData.APPDATABASEFILE;
    public final static int DB_VERSION = 1;
    public Context mContext = null;
    private static ConfigDatabaseHelper instance = null;

    public static synchronized ConfigDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ConfigDatabaseHelper(context);
        }
        return instance;
    }

    private ConfigDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addAllTables(db);
        addDefaultConfig(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void checkDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

    private void addAllTables(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            String sql = ConfigConstant.createPeripheralDeviceTable();
            db.execSQL(sql);

            sql = ConfigConstant.createCommonDeviceTable();
            db.execSQL(sql);

            sql = ConfigConstant.createRelayLoopTable();
            db.execSQL(sql);

            sql = ConfigConstant.createZoneLoopTable();
            db.execSQL(sql);

            sql = ConfigConstant.createIpcLoopTable();
            db.execSQL(sql);

            sql = ConfigConstant.createScenarioLoopTable();
            db.execSQL(sql);

            sql = ConfigConstant.createEventHistoryTable();
            db.execSQL(sql);

            sql = ConfigConstant.createVoiceMessageTable();
            db.execSQL(sql);

            sql = ConfigConstant.createAlarmHistoryTable();
            db.execSQL(sql);

            sql = ConfigConstant.createSpeedDialTable();
            db.execSQL(sql);

            sql = ConfigConstant.createIpDoorCardTable();
            db.execSQL(sql);

            sql = ConfigConstant.createDeviceAdapterTable();
            db.execSQL(sql);

            sql = ConfigConstant.createLocalDeviceTable();
            db.execSQL(sql);

            sql = ConfigConstant.createSystemSettingsTable();
            db.execSQL(sql);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void addDefaultConfig(SQLiteDatabase db) {
        addDefaultSystemSettings(db);
        //addDefaultScenarioForCommonDevice(db);
        //addDefaultHomePanelZone(db);

        addDefaultScenarioForCommonDevice(db);

        addDefaultLocalDevices(db);
        addDefaultIPDoorCamera(db);
    }

    private void addDefaultIPDoorCamera(SQLiteDatabase db) {
        if (DbCommonUtil.getCount(db, ConfigConstant.TABLE_PERIPHERALDEVICE) > 0) {
            return;
        }
        db.beginTransaction();
        try {
            for (int index = 0; index < ConfigConstant.HOMEPANEL_IPDC_COUNT; index++) {
                String moduleUuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_PERIPHERALDEVICE_INT, DbCommonUtil.getSequenct(db, ConfigConstant.TABLE_PERIPHERALDEVICE));
                String ipdcType = CommonData.COMMONDEVICE_TYPE_IPDC + String.valueOf(CommonData.JSON_IPDCTYPE_VALUEINT_BASE + index);
                String name = "ipdoorcamera" + index;

                ContentValues values = new ContentValuesFactory()
                        .put(ConfigConstant.COLUMN_MODULEUUID, moduleUuid)
                        .put(ConfigConstant.COLUMN_NAME, name)
                        .put(ConfigConstant.COLUMN_TYPE, ipdcType)
                        .put(ConfigConstant.COLUMN_IPADDR, "")
                        .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_MODEL, "HDC-2000")
                        .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_MAC, "")
                        .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_VERSION, "")
                        .put(ConfigConstant.COLUMN_PERIPHERALDEVICE_ONLINE, "0")
                        .getValues();

                long rowId = db.insert(ConfigConstant.TABLE_PERIPHERALDEVICE, null, values);
                if (rowId > 0) {
                    values = new ContentValuesFactory()
                            .put(ConfigConstant.COLUMN_UUID, moduleUuid)
                            .put(ConfigConstant.COLUMN_NAME, name)
                            .put(ConfigConstant.COLUMN_TYPE, CommonData.COMMONDEVICE_TYPE_IPDC)
                            .put(ConfigConstant.COLUMN_ENABLED, 1)
                            .getValues();

                    db.insert(ConfigConstant.TABLE_COMMONDEVICE, null, values);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void addDefaultScenarioForCommonDevice(SQLiteDatabase db) {
        if (DbCommonUtil.getCount(db, ConfigConstant.TABLE_COMMONDEVICE) > 0) {
            return;
        }
        db.beginTransaction();
        try {
            for (int i = 0; i < ConfigConstant.DEFAULT_SCENARIO_NAME.length; i++) {
                ContentValues values = new ContentValuesFactory()
                        .put(ConfigConstant.COLUMN_TYPE, CommonData.COMMONDEVICE_TYPE_SCENARIO)
                        .put(ConfigConstant.COLUMN_UUID, i + 1 + "")
                        .put(ConfigConstant.COLUMN_NAME, ConfigConstant.DEFAULT_SCENARIO_NAME[i])
                        .put(ConfigConstant.COLUMN_ENABLED, 1)
                        .getValues();
                db.insert(ConfigConstant.TABLE_COMMONDEVICE, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void addDefaultLocalDevices(SQLiteDatabase db) {
        if (DbCommonUtil.getCount(db, ConfigConstant.TABLE_LOCALDEVICE) > 0) {
            return;
        }

        db.beginTransaction();

        try {
            addDefaultLocalIODevice(db);
            addDefaultLocalUartDevice(db);
            addDefaultLocal485tDevice(db);
            addDefaultElevatorDevice(db);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void addDefaultLocalUartDevice(SQLiteDatabase db) throws JSONException {
        for (int index = 0; index < ConfigConstant.HOMEPANEL_LOCK_COUNT; index++) {
            String doorId = String.valueOf(index + 1);
            String defaultUartName = "MomasLock" + doorId;
            String uartUuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_LOCALDEVICE_INT, DbCommonUtil.getSequenct(db, ConfigConstant.TABLE_LOCALDEVICE));

            JSONObject config = new JSONObject();
            config.put(CommonJson.JSON_DOORID_KEY, doorId);
            config.put(ConfigConstant.COLUMN_DELAYTIME, 0 + "");
            config.put(ConfigConstant.COLUMN_ZONETYPE, CommonData.ZONETYPE_24H);
            config.put(ConfigConstant.COLUMN_ALARMTYPE, CommonData.ALARMTYPE_EMERGENCY);

            ContentValues uartValues = new ContentValuesFactory()
                    .put(ConfigConstant.COLUMN_UUID, uartUuid)
                    .put(ConfigConstant.COLUMN_NAME, defaultUartName)
                    .put(ConfigConstant.COLUMN_TYPE, CommonData.COMMONDEVICE_TYPE_LOCK)
                    .put(ConfigConstant.COLUMN_PHYSICTYPE, CommonJson.JSON_LOCALDEV_PHYSICTYPE_VALUE_UART)
                    .put(ConfigConstant.COLUMN_ADAPTERNAME, CommonData.DEVADAPTER_LOCK_MOMAS)
                    .put(ConfigConstant.COLUMN_CONFIGURATION, config.toString())
                    .put(ConfigConstant.COLUMN_ENABLED, 1)
                    .getValues();

            long rowId = db.insert(ConfigConstant.TABLE_LOCALDEVICE, null, uartValues);
            if (rowId > 0) {
                uartValues = new ContentValuesFactory()
                        .put(ConfigConstant.COLUMN_UUID, uartUuid)
                        .put(ConfigConstant.COLUMN_NAME, defaultUartName)
                        .put(ConfigConstant.COLUMN_TYPE, CommonData.COMMONDEVICE_TYPE_LOCK)
                        .put(ConfigConstant.COLUMN_ENABLED, 1)
                        .getValues();

                db.insert(ConfigConstant.TABLE_COMMONDEVICE, null, uartValues);
            }
        }
    }

    private void addDefaultElevatorDevice(SQLiteDatabase db) {
        String defaultElevatorName = "elevator";
        String elevatorUUID = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_LOCALDEVICE_INT,
                DbCommonUtil.getSequenct(db, ConfigConstant.TABLE_LOCALDEVICE));
        ContentValues elevatorValues = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_UUID, elevatorUUID)
                .put(ConfigConstant.COLUMN_NAME, defaultElevatorName)
                .put(ConfigConstant.COLUMN_TYPE, CommonData.COMMONDEVICE_TYPE_ELEVATOR)
                .put(ConfigConstant.COLUMN_PHYSICTYPE, CommonJson.JSON_LOCALDEV_PHYSICTYPE_VALUE_ELEVATOR)
                .put(ConfigConstant.COLUMN_ADAPTERNAME, CommonData.DEVADAPTER_ELEVATOR_IP_HON)
                .put(ConfigConstant.COLUMN_CONFIGURATION, "{}")
                .put(ConfigConstant.COLUMN_ENABLED, 1)
                .getValues();

        long rowId = db.insert(ConfigConstant.TABLE_LOCALDEVICE, null, elevatorValues);
        if (rowId > 0) {
            elevatorValues = new ContentValuesFactory()
                    .put(ConfigConstant.COLUMN_UUID, elevatorUUID)
                    .put(ConfigConstant.COLUMN_NAME, defaultElevatorName)
                    .put(ConfigConstant.COLUMN_TYPE, CommonData.COMMONDEVICE_TYPE_ELEVATOR)
                    .put(ConfigConstant.COLUMN_ENABLED, 1)
                    .getValues();

            db.insert(ConfigConstant.TABLE_COMMONDEVICE, null, elevatorValues);
        }
    }

    private void addDefaultLocalIODevice(SQLiteDatabase db) {
        try {
            // add 6 buglar zones
            JSONObject zoneConfigs = new JSONObject();
            zoneConfigs.put(ConfigConstant.COLUMN_DELAYTIME, 0 + "");
            zoneConfigs.put(ConfigConstant.COLUMN_ZONETYPE, CommonData.ZONETYPE_INSTANT);
            zoneConfigs.put(ConfigConstant.COLUMN_ALARMTYPE, CommonData.ALARMTYPE_INTRUSION);
            addDefaultLocalIODevice(db, "homePanelIntrusion", CommonData.COMMONDEVICE_TYPE_ZONE, ConfigConstant.HOMEPANEL_LOCALIO_HONZONES_BUGLAR_COUNT,
                    1, CommonData.DEVADAPTER_ZONE_LOCAL_HON, zoneConfigs);

            // add 1 emergency  24hours zones
            zoneConfigs.put(ConfigConstant.COLUMN_DELAYTIME, 0 + "");
            zoneConfigs.put(ConfigConstant.COLUMN_ZONETYPE, CommonData.ZONETYPE_24H);
            zoneConfigs.put(ConfigConstant.COLUMN_ALARMTYPE, CommonData.ALARMTYPE_EMERGENCY);
            addDefaultLocalIODevice(db, "homePanelEmergency", CommonData.COMMONDEVICE_TYPE_ZONE, ConfigConstant.HOMEPANEL_LOCALIO_HONZONES_EMER_COUNT,
                    7, CommonData.DEVADAPTER_ZONE_LOCAL_HON, zoneConfigs);

            // add 1 help  24hours zones
            zoneConfigs.put(ConfigConstant.COLUMN_DELAYTIME, 0 + "");
            zoneConfigs.put(ConfigConstant.COLUMN_ZONETYPE, CommonData.ZONETYPE_24H);
            zoneConfigs.put(ConfigConstant.COLUMN_ALARMTYPE, CommonData.ALARMTYPE_EMERGENCY_SILENCE);
            addDefaultLocalIODevice(db, "homePanelHelp", CommonData.COMMONDEVICE_TYPE_ZONE, ConfigConstant.HOMEPANEL_LOCALIO_HONZONES_HELP_COUNT,
                    8, CommonData.DEVADAPTER_ZONE_LOCAL_HON, zoneConfigs);

            // add 1 gas  24hours zones
            zoneConfigs.put(ConfigConstant.COLUMN_DELAYTIME, 0 + "");
            zoneConfigs.put(ConfigConstant.COLUMN_ZONETYPE, CommonData.ZONETYPE_24H);
            zoneConfigs.put(ConfigConstant.COLUMN_ALARMTYPE, CommonData.ALARMTYPE_GAS);
            addDefaultLocalIODevice(db, "homePanelGas", CommonData.COMMONDEVICE_TYPE_ZONE, ConfigConstant.HOMEPANEL_LOCALIO_HONZONES_GAS_COUNT,
                    9, CommonData.DEVADAPTER_ZONE_LOCAL_HON, zoneConfigs);

            // add 1 fire  24hours zones
            zoneConfigs.put(ConfigConstant.COLUMN_DELAYTIME, 0 + "");
            zoneConfigs.put(ConfigConstant.COLUMN_ZONETYPE, CommonData.ZONETYPE_24H);
            zoneConfigs.put(ConfigConstant.COLUMN_ALARMTYPE, CommonData.ALARMTYPE_FIRE);
            addDefaultLocalIODevice(db, "homePanelFire", CommonData.COMMONDEVICE_TYPE_ZONE, ConfigConstant.HOMEPANEL_LOCALIO_HONZONES_FIRE_COUNT,
                    10, CommonData.DEVADAPTER_ZONE_LOCAL_HON, zoneConfigs);

            // add one door bell
            zoneConfigs = new JSONObject();
            addDefaultLocalIODevice(db, "homePanelDoorBell", CommonData.COMMONDEVICE_TYPE_DOORBELL, ConfigConstant.HOMEPANEL_LOCALIO_HONDOORBELL_COUNT,
                    11, CommonData.DEVADAPTER_DOORBELL_LOCAL_HON, zoneConfigs, 1);

            // add one sos zone
            zoneConfigs = new JSONObject();
            zoneConfigs.put(ConfigConstant.COLUMN_DELAYTIME, 0 + "");
            zoneConfigs.put(ConfigConstant.COLUMN_ZONETYPE, CommonData.ZONETYPE_24H);
            zoneConfigs.put(ConfigConstant.COLUMN_ALARMTYPE, CommonData.ALARMTYPE_EMERGENCY);
            addDefaultLocalIODevice(db, "homePanelSOS", CommonData.COMMONDEVICE_TYPE_ZONE, ConfigConstant.HOMEPANEL_LOCALIO_HONSOS_COUNT,
                    12, CommonData.DEVADAPTER_ZONE_LOCAL_HON, zoneConfigs);

            //add one tamper zone
            zoneConfigs = new JSONObject();
            zoneConfigs.put(ConfigConstant.COLUMN_DELAYTIME, 0 + "");
            zoneConfigs.put(ConfigConstant.COLUMN_ZONETYPE, CommonData.ZONETYPE_24H);
            zoneConfigs.put(ConfigConstant.COLUMN_ALARMTYPE, CommonData.ALARMTYPE_TAMPER);
            addDefaultLocalIODevice(db, "homePanelTamper", CommonData.COMMONDEVICE_TYPE_ZONE, ConfigConstant.HOMEPANEL_LOCALIO_HONTAMPER_COUNT,
                    13, CommonData.DEVADAPTER_ZONE_LOCAL_HON, zoneConfigs);

            // add one LOW BATTERY zone
            zoneConfigs = new JSONObject();
            zoneConfigs.put(ConfigConstant.COLUMN_DELAYTIME, 0 + "");
            zoneConfigs.put(ConfigConstant.COLUMN_ZONETYPE, CommonData.ZONETYPE_24H);
            zoneConfigs.put(ConfigConstant.COLUMN_ALARMTYPE, CommonData.ALARMTYPE_EMERGENCY);
            addDefaultLocalIODevice(db, "homePanelLowBattery", CommonData.COMMONDEVICE_TYPE_ZONE, ConfigConstant.HOMEPANEL_LOCALIO_HONLOWBATTERY_COUNT,
                    14, CommonData.DEVADAPTER_ZONE_LOCAL_HON, zoneConfigs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDefaultLocalIODevice(SQLiteDatabase db, String baseName, String type, int count, int iobase, String adapter, JSONObject additionConfigs) {
        addDefaultLocalIODevice(db, baseName, type, count, iobase, adapter, additionConfigs, 0);
    }

    private void addDefaultLocalIODevice(SQLiteDatabase db, String baseName, String type, int count, int iobase, String adapter, JSONObject additionConfigs, int enable) {
        try {
            for (int i = 0; i < count; i++) {
                int loop = i + 1;
                String name = baseName + loop;
                long sequence = DbCommonUtil.getSequenct(db, ConfigConstant.TABLE_LOCALDEVICE);
                String deviceUuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_LOCALDEVICE_INT, sequence);

                // add loop id to addition args
                additionConfigs.put(CommonJson.JSON_LOOPID_KEY, String.valueOf(iobase + i));

                ContentValues values = new ContentValuesFactory()
                        .put(ConfigConstant.COLUMN_UUID, deviceUuid)
                        .put(ConfigConstant.COLUMN_NAME, name)
                        .put(ConfigConstant.COLUMN_TYPE, type)
                        .put(ConfigConstant.COLUMN_ENABLED, enable) // default disable all zone
                        .put(ConfigConstant.COLUMN_ADAPTERNAME, adapter)
                        .put(ConfigConstant.COLUMN_CONFIGURATION, additionConfigs.toString())
                        .put(ConfigConstant.COLUMN_PHYSICTYPE, CommonJson.JSON_LOCALDEV_PHYSICTYPE_VALUE_IOBASE + (iobase + i))
                        .getValues();

                long rowId = db.insert(ConfigConstant.TABLE_LOCALDEVICE, null, values);
                if (rowId > 0) {
                    values = new ContentValuesFactory()
                            .put(ConfigConstant.COLUMN_UUID, deviceUuid)
                            .put(ConfigConstant.COLUMN_NAME, name)
                            .put(ConfigConstant.COLUMN_TYPE, type)
                            .put(ConfigConstant.COLUMN_ENABLED, 0)
                            .getValues();

                    db.insert(ConfigConstant.TABLE_COMMONDEVICE, null, values);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDefaultLocal485tDevice(SQLiteDatabase db) {
        // todo: add 485 cofigs here
        //addDefaultLocal485tDevice(db, ...)
    }

    private void addDefaultLocal485tDevice(SQLiteDatabase db, String physicType, String deviceType, String adapter, String addtionConfigs) {
        String nameBase = "485device";

        for (int index = 0; index < 2; index++) {
            String uuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_LOCALDEVICE_INT,
                    DbCommonUtil.getSequenct(db, ConfigConstant.TABLE_LOCALDEVICE));
            ContentValues configValues = new ContentValuesFactory()
                    .put(ConfigConstant.COLUMN_UUID, uuid)
                    .put(ConfigConstant.COLUMN_NAME, nameBase)
                    .put(ConfigConstant.COLUMN_TYPE, deviceType)
                    .put(ConfigConstant.COLUMN_PHYSICTYPE, physicType)
                    .put(ConfigConstant.COLUMN_ADAPTERNAME, adapter)
                    .put(ConfigConstant.COLUMN_CONFIGURATION, addtionConfigs)
                    .put(ConfigConstant.COLUMN_ENABLED, 1)
                    .getValues();

            long rowId = db.insert(ConfigConstant.TABLE_LOCALDEVICE, null, configValues);
            if (rowId > 0) {
                configValues = new ContentValuesFactory()
                        .put(ConfigConstant.COLUMN_UUID, uuid)
                        .put(ConfigConstant.COLUMN_NAME, nameBase)
                        .put(ConfigConstant.COLUMN_TYPE, deviceType)
                        .put(ConfigConstant.COLUMN_ENABLED, 1)
                        .getValues();

                db.insert(ConfigConstant.TABLE_COMMONDEVICE, null, configValues);
            }
        }
    }

    private synchronized long addDefaultSystemSetting(SQLiteDatabase db, String key, String value) {
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_KEY, key)
                .put(ConfigConstant.COLUMN_VALUE, value)
                .getValues();

        return db.insert(ConfigConstant.TABLE_SYSTEMSETTINGS, null, values);
    }

    private void addDefaultSystemSettings(SQLiteDatabase db) {
        if (DbCommonUtil.getCount(db, ConfigConstant.TABLE_SYSTEMSETTINGS) > 0) {
            return;
        }

        db.beginTransaction();

        try {
            addDefaultSystemSetting(db, CommonData.JSON_KEY_VERSION, "");
            addDefaultSystemSetting(db, CommonData.JSON_KEY_CITY, "");
            addDefaultSystemSetting(db, CommonData.JSON_KEY_AUTOTIME, 0 + "");
            addDefaultSystemSetting(db, CommonData.JSON_KEY_24HOUR, 0 + "");
            addDefaultSystemSetting(db, CommonData.JSON_KEY_SETDATE, "");
            addDefaultSystemSetting(db, CommonData.JSON_KEY_SETTIME, "");

            // scenario settings
            addDefaultSystemSetting(db, CommonData.JSON_KEY_ALARM_PWD, "123456");
            addDefaultSystemSetting(db, CommonData.KEY_CURRENT_SCENARIO_ID, CommonData.SCENARIO_ID_HOME);
            addDefaultSystemSetting(db, CommonData.KEY_ENGINEERPWD, "085213");

            addDefaultSystemSetting(db, CommonData.KEY_AMSIP, "192.168.10.172");
            addDefaultSystemSetting(db, CommonData.KEY_AMSPORT, "085213");
            addDefaultSystemSetting(db, CommonData.KEY_SUBPHONEID, "");
            addDefaultSystemSetting(db, CommonData.KEY_REGISTERPWD, "0000");
            addDefaultSystemSetting(db, CommonData.JSON_UNIT_KEY, "0602000601");
            addDefaultSystemSetting(db, CommonData.JSON_MAINIP_KEY, "");
            addDefaultSystemSetting(db, CommonData.JSON_SUBPHONENAME_KEY, "");
            addDefaultSystemSetting(db, CommonData.KEY_HOMEPANELTYPE, CommonData.HOMEPANEL_TYPE_MAIN + "");

            // elevator default info
            addDefaultSystemSetting(db, CommonJson.JSON_ELEVATORIP_KEY, "");
            addDefaultSystemSetting(db, CommonJson.JSON_ELEVATORPORT_KEY, "");
            addDefaultSystemSetting(db, CommonJson.JSON_FLOORNO_KEY, "");

            // ipdc default info
            addDefaultSystemSetting(db, CommonData.KEY_FRONTLOCKTYPE, CommonData.KEY_TYPE_MOMAS);
            addDefaultSystemSetting(db, CommonData.KEY_BACKLOCKTYPE, CommonData.KEY_TYPE_MOMAS);
            addDefaultSystemSetting(db, CommonData.KEY_CARD_ACTION, CommonData.CARD_ACTION_DOOROPEN);
            addDefaultSystemSetting(db, CommonData.KEY_IPDC_FRONT_VERSION, "");
            addDefaultSystemSetting(db, CommonData.KEY_IPDC_BACK_VERSION, "");

            // ring
            addDefaultSystemSetting(db, CommonData.KEY_VOLUME_RING, CommonData.VOLUME_VALUE_DEFAULT + "");
            addDefaultSystemSetting(db, CommonData.KEY_VOLUME_NEIGHBOR, CommonData.VOLUME_VALUE_DEFAULT + "");
            addDefaultSystemSetting(db, CommonData.KEY_VOLUME_LOBBY, CommonData.VOLUME_VALUE_DEFAULT + "");
            addDefaultSystemSetting(db, CommonData.KEY_VOLUME_GUARD, CommonData.VOLUME_VALUE_DEFAULT + "");
            addDefaultSystemSetting(db, CommonData.KEY_VOLUME_INNER, CommonData.VOLUME_VALUE_DEFAULT + "");
            addDefaultSystemSetting(db, CommonData.KEY_VOLUME_IPDC, CommonData.VOLUME_VALUE_DEFAULT + "");

            addDefaultSystemSetting(db, CommonData.ETHERNET_IP, "");
            addDefaultSystemSetting(db, CommonData.ETHERNET_NETMASK, "");
            addDefaultSystemSetting(db, CommonData.ETHERNET_GATEWAY, "");

            addDefaultSystemSetting(db, CommonData.NETMASK_IP_ADDRESS, "");
            addDefaultSystemSetting(db, CommonData.NETMASK_IP_NETMASK, "");
            addDefaultSystemSetting(db, CommonData.NETMASK_IP_GATEWAY, "");


            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
