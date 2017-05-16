package com.honeywell.homepanel.configcenter.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.manager.ContentValuesFactory;
import com.honeywell.homepanel.configcenter.databases.manager.DbCommonUtil;;import org.json.JSONObject;

/**
 * Created by H135901 on 3/13/2017.
 */

public class ConfigDatabaseHelper extends SQLiteOpenHelper{

    private final static String DB_NAME = CommonData.APPDATABASEFILE;
    public final static int DB_VERSION = 1;
    public  Context mContext = null;
    private static ConfigDatabaseHelper instance = null;
    public static synchronized ConfigDatabaseHelper getInstance(Context context) {
        if (instance == null){
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

    private void addAllTables(SQLiteDatabase db){
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

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            db.endTransaction();
        }
    }
    private void addDefaultConfig(SQLiteDatabase db){
        //add 4 commondevice scenario
        addDefaultScenarioForCommonDevice(db);
        addDefaultLocalDevices(db);
    }

    private void addDefaultHomePanelZone(SQLiteDatabase db) {
        if (DbCommonUtil.getCount(db, ConfigConstant.TABLE_ZONELOOP) > 0) {
            return;
        }
        db.beginTransaction();
        try {
            for (int i = 0; i < ConfigConstant.HOMEPANEL_LOCALIO_COUNT; i++) {
                String deviceUuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_ZONELOOP_INT,DbCommonUtil.getSequenct(db,ConfigConstant.TABLE_ZONELOOP));
                int loop = i+1;
                String name = "homepanelZone" + loop;
                ContentValues values = new ContentValuesFactory()
                        .put(ConfigConstant.COLUMN_MODULEUUID,"")
                        .put(ConfigConstant.COLUMN_UUID,deviceUuid)
                        .put(ConfigConstant.COLUMN_NAME,name)
                        .put(ConfigConstant.COLUMN_LOOP, loop)
                        .put(ConfigConstant.COLUMN_DELAYTIME, 0)
                        .put(ConfigConstant.COLUMN_ENABLED,1)
                        .put(ConfigConstant.COLUMN_ZONETYPE,CommonData.ZONETYPE_24H)
                        .put(ConfigConstant.COLUMN_ALARMTYPE,CommonData.ALARMTYPE_EMERGENCY).getValues();
                long rowId = db.insert(ConfigConstant.TABLE_ZONELOOP, null, values);
                if(rowId > 0) {
                    values = new ContentValuesFactory().put(ConfigConstant.COLUMN_UUID,deviceUuid)
                            .put(ConfigConstant.COLUMN_NAME, name)
                            .put(ConfigConstant.COLUMN_TYPE, CommonData.COMMONDEVICE_TYPE_ZONE).getValues();
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
                        /*.put(ConfigConstant.COLUMN_UUID, UUID.randomUUID().toString())*/
                        .put(ConfigConstant.COLUMN_UUID,i+1+"")
                        .put(ConfigConstant.COLUMN_NAME, ConfigConstant.DEFAULT_SCENARIO_NAME[i]).getValues();
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
            addDefaultLocalUartDevice(db);
            addDefaultElevatorDevice(db);
            addDefaultLocalIODevice(db);
            addDefaultLocal485tDevice(db);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void addDefaultLocalUartDevice(SQLiteDatabase db) {
        String defaultUartName = "Momas Lock";
        String uartUuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_LOCALDEVICE_INT,
                                DbCommonUtil.getSequenct(db,ConfigConstant.TABLE_LOCALDEVICE));
        ContentValues uartValues = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_UUID, uartUuid)
                .put(ConfigConstant.COLUMN_NAME, defaultUartName)
                .put(ConfigConstant.COLUMN_TYPE, CommonData.COMMONDEVICE_TYPE_LOCK)
                .put(ConfigConstant.COLUMN_PHYSICTYPE, CommonJson.JSON_LOCALDEV_PHYSICTYPE_VALUE_UART)
                .put(ConfigConstant.COLUMN_ADAPTERNAME, CommonData.DEVADAPTER_LOCK_MOMAS)
                .put(ConfigConstant.COLUMN_CONFIGURATION, "{}")
                .put(ConfigConstant.COLUMN_ENABLED, 1)
                .getValues();

        long rowId = db.insert(ConfigConstant.TABLE_LOCALDEVICE, null, uartValues);
        if(rowId > 0) {
            uartValues = new ContentValuesFactory()
                    .put(ConfigConstant.COLUMN_UUID,uartUuid)
                    .put(ConfigConstant.COLUMN_NAME, defaultUartName)
                    .put(ConfigConstant.COLUMN_TYPE, CommonData.COMMONDEVICE_TYPE_LOCK).getValues();

            db.insert(ConfigConstant.TABLE_COMMONDEVICE, null, uartValues);
        }
    }

    private void addDefaultElevatorDevice(SQLiteDatabase db) {
        String defaultElevatorName = "elevator";
        String elevatorUUID = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_LOCALDEVICE_INT,
                DbCommonUtil.getSequenct(db,ConfigConstant.TABLE_LOCALDEVICE));
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
        if(rowId > 0) {
            elevatorValues = new ContentValuesFactory()
                    .put(ConfigConstant.COLUMN_UUID,elevatorUUID)
                    .put(ConfigConstant.COLUMN_NAME, defaultElevatorName)
                    .put(ConfigConstant.COLUMN_TYPE, CommonData.COMMONDEVICE_TYPE_ELEVATOR).getValues();

            db.insert(ConfigConstant.TABLE_COMMONDEVICE, null, elevatorValues);
        }
    }

    private void addDefaultLocalIODevice(SQLiteDatabase db) {
        try {
            // add 10 zones
            JSONObject zoneConfigs = new JSONObject();
            zoneConfigs.put(ConfigConstant.COLUMN_DELAYTIME, 0 + "");
            zoneConfigs.put(ConfigConstant.COLUMN_ZONETYPE, CommonData.ZONETYPE_24H);
            zoneConfigs.put(ConfigConstant.COLUMN_ALARMTYPE, CommonData.ALARMTYPE_INTRUSION);
            addDefaultLocalIODevice(db, "homePanelZone", CommonData.COMMONDEVICE_TYPE_ZONE, ConfigConstant.HOMEPANEL_LOCALIO_HONZONES_COUNT,
                    0, CommonData.DEVADAPTER_ZONE_LOCAL_HON, zoneConfigs);

            //add one tamper zone
            zoneConfigs = new JSONObject();
            zoneConfigs.put(ConfigConstant.COLUMN_DELAYTIME, 0 + "");
            zoneConfigs.put(ConfigConstant.COLUMN_ZONETYPE, CommonData.ZONETYPE_24H);
            zoneConfigs.put(ConfigConstant.COLUMN_ALARMTYPE, CommonData.ALARMTYPE_EMERGENCY);
            addDefaultLocalIODevice(db, "homePanelTamper", CommonData.COMMONDEVICE_TYPE_ZONE, ConfigConstant.HOMEPANEL_LOCALIO_HONTAMPER_COUNT,
                    10, CommonData.DEVADAPTER_ZONE_LOCAL_HON, zoneConfigs);

            // add one door bell
            zoneConfigs = new JSONObject();
            addDefaultLocalIODevice(db, "homePanelDoorBell", CommonData.COMMONDEVICE_TYPE_DOORBELL, ConfigConstant.HOMEPANEL_LOCALIO_HONDOORBELL_COUNT,
                    11, CommonData.DEVADAPTER_DOORBELL_LOCAL_HON, zoneConfigs);

            // add one emergency zone
            zoneConfigs = new JSONObject();
            zoneConfigs.put(ConfigConstant.COLUMN_DELAYTIME, 0 + "");
            zoneConfigs.put(ConfigConstant.COLUMN_ZONETYPE, CommonData.ZONETYPE_24H);
            zoneConfigs.put(ConfigConstant.COLUMN_ALARMTYPE, CommonData.ALARMTYPE_EMERGENCY);
            addDefaultLocalIODevice(db, "homePanelEmergency", CommonData.COMMONDEVICE_TYPE_ZONE,  ConfigConstant.HOMEPANEL_LOCALIO_HONEMERGENCY_COUNT,
                    12, CommonData.DEVADAPTER_ZONE_LOCAL_HON, zoneConfigs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDefaultLocalIODevice(SQLiteDatabase db, String baseName, String type, int count, int iobase, String adapter, JSONObject additionConfigs) {
        try {
            for (int i = 0; i < count; i++) {
                int loop = i + 1;
                String name = baseName + loop;
                long sequence = DbCommonUtil.getSequenct(db, ConfigConstant.TABLE_LOCALDEVICE);
                String deviceUuid = DbCommonUtil.generateDeviceUuid(ConfigConstant.TABLE_LOCALDEVICE_INT, sequence);

                // add loop id to addition args
                additionConfigs.put(CommonJson.JSON_LOOPID_KEY, iobase + 1);

                ContentValues values = new ContentValuesFactory()
                        .put(ConfigConstant.COLUMN_UUID, deviceUuid)
                        .put(ConfigConstant.COLUMN_NAME, name)
                        .put(ConfigConstant.COLUMN_TYPE, type)
                        .put(ConfigConstant.COLUMN_ENABLED, 1)
                        .put(ConfigConstant.COLUMN_ADAPTERNAME, adapter)
                        .put(ConfigConstant.COLUMN_CONFIGURATION, additionConfigs.toString())
                        .put(ConfigConstant.COLUMN_PHYSICTYPE, CommonJson.JSON_LOCALDEV_PHYSICTYPE_VALUE_IOBASE  + (iobase + i))
                        .getValues();

                long rowId = db.insert(ConfigConstant.TABLE_LOCALDEVICE, null, values);
                if (rowId > 0) {
                    values = new ContentValuesFactory()
                            .put(ConfigConstant.COLUMN_UUID, deviceUuid)
                            .put(ConfigConstant.COLUMN_NAME, name)
                            .put(ConfigConstant.COLUMN_TYPE, type)
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
                    DbCommonUtil.getSequenct(db,ConfigConstant.TABLE_LOCALDEVICE));
            ContentValues configValues = new ContentValuesFactory()
                    .put(ConfigConstant.COLUMN_UUID, uuid)
                    .put(ConfigConstant.COLUMN_NAME, nameBase)
                    .put(ConfigConstant.COLUMN_TYPE,deviceType)
                    .put(ConfigConstant.COLUMN_PHYSICTYPE, physicType)
                    .put(ConfigConstant.COLUMN_ADAPTERNAME, adapter)
                    .put(ConfigConstant.COLUMN_CONFIGURATION, addtionConfigs)
                    .put(ConfigConstant.COLUMN_ENABLED, 1)
                    .getValues();

            long rowId = db.insert(ConfigConstant.TABLE_LOCALDEVICE, null, configValues);
            if(rowId > 0) {
                configValues = new ContentValuesFactory()
                        .put(ConfigConstant.COLUMN_UUID,uuid)
                        .put(ConfigConstant.COLUMN_NAME, nameBase)
                        .put(ConfigConstant.COLUMN_TYPE, deviceType).getValues();

                db.insert(ConfigConstant.TABLE_COMMONDEVICE, null, configValues);
            }
        }
    }
}
