package com.honeywell.homepanel.configcenter.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;

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
            // createPeripheralDeviceTable
            String sql = ConfigConstant.createPeripheralDeviceTable();
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

    }

}
