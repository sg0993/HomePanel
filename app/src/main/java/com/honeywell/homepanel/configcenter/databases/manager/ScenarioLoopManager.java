package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.configcenter.ConfigService;
import com.honeywell.homepanel.configcenter.databases.ConfigDatabaseHelper;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.domain.CommonDevice;
import com.honeywell.homepanel.configcenter.databases.domain.ScenarioLoop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H135901 on 3/15/2017.
 */

public class ScenarioLoopManager {
    private Context mContext = null;
    private static final String TAG  = ConfigService.TAG;
    private static ScenarioLoopManager instance  = null;
    private ConfigDatabaseHelper dbHelper = null;

    public static synchronized ScenarioLoopManager getInstance(Context context) {
        if(instance == null){
            instance = new ScenarioLoopManager(context);
        }
        return instance;
    }
    private ScenarioLoopManager(Context context) {
        mContext = context;
        dbHelper= ConfigDatabaseHelper.getInstance(mContext);
    }

    public synchronized long add(String uuid,String name,String deviceUuid,String action){
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_UUID, uuid)
                .put(ConfigConstant.COLUMN_NAME, name)
                .put(ConfigConstant.COLUMN_DEVICEUUID,deviceUuid)
                .put(ConfigConstant.COLUMN_ACTION,action).getValues();
        return DbCommonUtil.add(mContext,dbHelper,ConfigConstant.TABLE_SCENARIO,values);
    }

    public synchronized int deleteByUuid(String uuid) {
        return DbCommonUtil.deleteByStringFieldType(mContext,dbHelper,ConfigConstant.TABLE_SCENARIO,ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized int deleteByDeviceuuid(String scenarioUuid,String deviceUuid) {
        int num = -1;
        if(TextUtils.isEmpty(scenarioUuid) || TextUtils.isEmpty(deviceUuid)){
            return num;
        }
        try {
            num =  dbHelper.getWritableDatabase().delete(ConfigConstant.TABLE_SCENARIO,
                    ConfigConstant.COLUMN_UUID + "=? and "
                            + ConfigConstant.COLUMN_DEVICEUUID + "=?",
                    new String[]{scenarioUuid,deviceUuid});
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(num > 0){
           PreferenceManager.updateVersionId(mContext);
        }
        return num;
    }

    public synchronized int deleteByPrimaryId(long primaryId) {
        return DbCommonUtil.deleteByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_SCENARIO,primaryId);
    }

    private ContentValues putUpDateValues(ScenarioLoop device){
        ContentValues values = new ContentValues();
        if(null == device){
            return  values;
        }
        if(!TextUtils.isEmpty(device.mName)){
            values.put(ConfigConstant.COLUMN_NAME, device.mName);
        }
        if(!TextUtils.isEmpty(device.mAction)){
            values.put(ConfigConstant.COLUMN_ACTION, device.mAction);
        }
        return values;
    }

    public synchronized int updateByPrimaryId(long primaryId, ScenarioLoop device) {
        if(null == device || primaryId <= 0){
            return -1;
        }
        return DbCommonUtil.updateByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_SCENARIO,putUpDateValues(device),primaryId);
    }

  /*  public synchronized ScenarioLoop getByPrimaryId(long primaryId) {
        if(primaryId <= 0){
            return  null;
        }
        Cursor cursor = DbCommonUtil.getByPrimaryId(dbHelper,ConfigConstant.TABLE_SCENARIO,primaryId);
        ScenarioLoop device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }*/

    public synchronized ScenarioLoop getByDeviceUuid(String scenarioUuid,String deviceUuid) {
        if(TextUtils.isEmpty(scenarioUuid) || TextUtils.isEmpty(deviceUuid)){
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ConfigConstant.TABLE_SCENARIO,null,
                ConfigConstant.COLUMN_UUID + "=? and "
                        + ConfigConstant.COLUMN_DEVICEUUID + "=?",
                new String[] { scenarioUuid,deviceUuid}, null, null, null, null);
        ScenarioLoop loop = null;
        while(cursor.moveToNext()){
            loop  = fillDefault(cursor);
            break;
        }
        cursor.close();
        return loop;
    }


    public synchronized List<ScenarioLoop> getByUuid(String uuid) {
        List<ScenarioLoop>lists = new ArrayList<ScenarioLoop>();
        if(TextUtils.isEmpty(uuid)){
            return lists;
        }
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_SCENARIO,ConfigConstant.COLUMN_UUID,uuid);
        while(cursor.moveToNext()){
            ScenarioLoop device = null;
            device = fillDefault(cursor);
            lists.add(device);
        }
        cursor.close();
        return lists;
    }

    public synchronized List<ScenarioLoop> getScenarioLoopAllList() {
        List<ScenarioLoop> ScenarioLoops = null;
        Cursor cursor = DbCommonUtil.getAll(dbHelper,ConfigConstant.TABLE_SCENARIO);
        while(cursor.moveToNext()){
            if(null == ScenarioLoops){
                ScenarioLoops = new ArrayList<ScenarioLoop>();
            }
            ScenarioLoop device = fillDefault(cursor);
            ScenarioLoops.add(device);
        }
        cursor.close();
        return ScenarioLoops;
    }

    public synchronized List<ScenarioLoop> getDistinct() {
        List<ScenarioLoop> ScenarioLoops = null;
        Cursor cursor = DbCommonUtil.getDistinctAll(dbHelper,ConfigConstant.TABLE_SCENARIO,ConfigConstant.COLUMN_UUID);
        while(cursor.moveToNext()){
            if(null == ScenarioLoops){
                ScenarioLoops = new ArrayList<ScenarioLoop>();
            }
            ScenarioLoop device = fillDefault(cursor);
            ScenarioLoops.add(device);
        }
        cursor.close();
        return ScenarioLoops;
    }

    private ScenarioLoop fillDefault(Cursor cursor) {
        if(null == cursor){
            return null;
        }
        ScenarioLoop device = new ScenarioLoop();
        device.mUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_UUID));
        device.mName = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_NAME));
        device.mId = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
        device.mDeviceUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_DEVICEUUID));
        device.mAction = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_ACTION));
        return device;
    }


    /*******************For Protocal*******************************************/

    public void getScenarioConfig(JSONObject jsonObject)  throws JSONException{
        List<ScenarioLoop>lists = getByUuid(jsonObject.optString(CommonData.JSON_UUID_KEY));
        if(null == lists){
            return;
        }
        JSONArray loopMapArray = new JSONArray();
        for (int i = 0; i < lists.size(); i++) {
            ScenarioLoop loop = lists.get(i);
            JSONObject loopMapObject = new JSONObject();
            DbCommonUtil.putKeyValueToJson(mContext,loop.mDeviceUuid,loopMapObject);
            loopMapArray.put(loopMapObject);
        }
        jsonObject.put(CommonData.JSON_LOOPMAP_KEY,loopMapArray);
        jsonObject.put(CommonData.JSON_ERRORCODE_KEY,CommonData.JSON_ERRORCODE_VALUE_OK);
    }


    public void setScenarioConfig(JSONObject jsonObject) throws  JSONException{
        String uuid = jsonObject.optString(CommonData.JSON_UUID_KEY);
        //TODO 暂不支持修改场景名字
        String name = jsonObject.optString(CommonData.JSON_KEY_NAME);
        JSONArray loopMapArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        CommonDevice commonDevice = CommonlDeviceManager.getInstance(mContext).getByUuid(uuid);
        if(null == commonDevice){
            return;
        }
        for (int i = 0; i < loopMapArray.length();i++) {
            long code = -1;
            JSONObject loopMapObject = loopMapArray.getJSONObject(i);
            String deviceUuid = loopMapObject.optString(CommonData.JSON_UUID_KEY);
            String operateType = loopMapObject.optString(CommonData.JSON_KEY_OPERATIONTYPE);
            if(operateType.equals(CommonData.JSON_OPERATIONTYPE_VALUE_ADD)){
                String action = loopMapObject.optString(CommonData.JSON_ACTION_KEY);
                ScenarioLoop exist = ScenarioLoopManager.getInstance(mContext).getByDeviceUuid(uuid,deviceUuid);
                if(null == exist){
                    code = ScenarioLoopManager.getInstance(mContext).add(uuid,commonDevice.mName,deviceUuid,action);
                }
                else{
                    code = Long.valueOf(CommonData.JSON_ERRORCODE_VALUE_EXIST);
                }
            }
            else if(operateType.equals(CommonData.JSON_OPERATIONTYPE_VALUE_DELETE)){
                code = ScenarioLoopManager.getInstance(mContext).deleteByDeviceuuid(uuid,deviceUuid);
            }
            else if(operateType.equals(CommonData.JSON_OPERATIONTYPE_VALUE_UPDATE)){
                String action = loopMapObject.optString(CommonData.JSON_ACTION_KEY);
                ScenarioLoop loop = ScenarioLoopManager.getInstance(mContext).getByDeviceUuid(uuid,deviceUuid);
              /*  if(jsonObject.has(CommonData.JSON_KEY_NAME)){
                    loop.mName = name;
                }*/
                if(jsonObject.has(CommonData.JSON_ACTION_KEY)){
                    loop.mAction = action;
                }
                code = ScenarioLoopManager.getInstance(mContext).updateByPrimaryId(loop.mId,loop);
            }
            DbCommonUtil.putErrorCodeFromOperate(code,loopMapObject);
        }

    }
}
