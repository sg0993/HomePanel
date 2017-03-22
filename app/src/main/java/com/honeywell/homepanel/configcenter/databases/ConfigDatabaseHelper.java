package com.honeywell.homepanel.configcenter.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.manager.ContentValuesFactory;
import com.honeywell.homepanel.configcenter.databases.manager.DbCommonUtil;

import java.util.UUID;

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
        addDefaultHomePanelZone(db);
    }

    private void addDefaultHomePanelZone(SQLiteDatabase db) {
        if (DbCommonUtil.getCount(db, ConfigConstant.TABLE_ZONELOOP) > 0) {
            return;
        }
        db.beginTransaction();
        try {
            for (int i = 0; i < ConfigConstant.HOMEPANEL_ZONE_COUNT; i++) {
                String uuid = UUID.randomUUID().toString();
                int loop = i+1;
                String name = "homepanelZone" + loop;
                ContentValues values = new ContentValuesFactory()
                        .put(ConfigConstant.COLUMN_MODULEUUID,"")
                        .put(ConfigConstant.COLUMN_UUID, uuid)
                        .put(ConfigConstant.COLUMN_NAME,name)
                        .put(ConfigConstant.COLUMN_LOOP, loop)
                        .put(ConfigConstant.COLUMN_DELAYTIME, 0)
                        .put(ConfigConstant.COLUMN_ENABLED,1)
                        .put(ConfigConstant.COLUMN_ZONETYPE,CommonData.ZONETYPE_24H)
                        .put(ConfigConstant.COLUMN_ALARMTYPE,CommonData.ALARMTYPE_EMERGENCY).getValues();
                db.insert(ConfigConstant.TABLE_ZONELOOP, null, values);

                values = new ContentValuesFactory().put(ConfigConstant.COLUMN_UUID, uuid)
                        .put(ConfigConstant.COLUMN_NAME, name)
                        .put(ConfigConstant.COLUMN_TYPE,CommonData.COMMONDEVICE_TYPE_ZONE).getValues();
                db.insert(ConfigConstant.TABLE_COMMONDEVICE, null, values);
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
}
