package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.configcenter.ConfigService;
import com.honeywell.homepanel.configcenter.databases.ConfigDatabaseHelper;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.domain.AlarmHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H135901 on 3/16/2017.
 */

public class AlarmHistoryManager {
    private Context mContext = null;
    private static final String TAG  = ConfigService.TAG;
    private static AlarmHistoryManager instance  = null;
    private ConfigDatabaseHelper dbHelper = null;

    public static synchronized AlarmHistoryManager getInstance(Context context) {
        if(instance == null){
            instance = new AlarmHistoryManager(context);
        }
        return instance;
    }
    private AlarmHistoryManager(Context context) {
        mContext = context;
        dbHelper= ConfigDatabaseHelper.getInstance(mContext);
    }

    public synchronized long add(String uuid,String time,String alarmType,String message,int read){
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_UUID, uuid)
                .put(ConfigConstant.COLUMN_TIME, time)
                .put(ConfigConstant.COLUMN_ALARMTYPE, alarmType)
                .put(ConfigConstant.COLUMN_MESSAGE, message)
                .put(ConfigConstant.COLUMN_READ,read).getValues();
        //TODO 加条数限制
        return DbCommonUtil.add(mContext,dbHelper,ConfigConstant.TABLE_ALARMHISTORY,values);
    }

    public synchronized int deleteByUuid(String uuid) {
        return DbCommonUtil.deleteByStringFieldType(mContext,dbHelper,ConfigConstant.TABLE_ALARMHISTORY,ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized int deleteByPrimaryId(long primaryId) {
        return DbCommonUtil.deleteByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_ALARMHISTORY,primaryId);
    }

    private ContentValues putUpDateValues(AlarmHistory device){
        ContentValues values = new ContentValues();
        if(null == device){
            return  values;
        }
       /* if(device.mTime > 0){
            values.put(ConfigConstant.COLUMN_TIME, device.mTime);
        }
        if(!TextUtils.isEmpty(device.mAlarmType)){
            values.put(ConfigConstant.COLUMN_ALARMTYPE, device.mAlarmType);
        }
        if(!TextUtils.isEmpty(device.mMessage)){
            values.put(ConfigConstant.COLUMN_MESSAGE, device.mMessage);
        }*/
        if(device.mRead >= 0){
            values.put(ConfigConstant.COLUMN_READ, device.mRead);
        }
        return values;
    }

    public synchronized int updateByPrimaryId(long primaryId, AlarmHistory device) {
        if(null == device || primaryId <= 0){
            return -1;
        }
        return DbCommonUtil.updateByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_ALARMHISTORY,putUpDateValues(device),primaryId);
    }

    public synchronized int updateByUuid(String  uuid, AlarmHistory device) {
        if(null == device){
            return -1;
        }
        return  DbCommonUtil.updateByStringFiled(mContext,dbHelper,ConfigConstant.TABLE_ALARMHISTORY,putUpDateValues(device),ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized AlarmHistory getByPrimaryId(long primaryId) {
        if(primaryId <= 0){
            return  null;
        }
        Cursor cursor = DbCommonUtil.getByPrimaryId(dbHelper,ConfigConstant.TABLE_ALARMHISTORY,primaryId);
        AlarmHistory device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized int getAlarmCountByStatus(int status) {
        Cursor cursor = DbCommonUtil.getByIntField(dbHelper,ConfigConstant.TABLE_ALARMHISTORY,ConfigConstant.COLUMN_READ,status);
        int count = 0;
        while(cursor.moveToNext()){
            count++;
        }
        cursor.close();
        return count;
    }


    public synchronized AlarmHistory getByUuid(String uuid) {
        if(TextUtils.isEmpty(uuid)){
            return null;
        }
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_ALARMHISTORY,ConfigConstant.COLUMN_UUID,uuid);
        AlarmHistory device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized List<AlarmHistory> getAlarmHistoryAllList() {
        List<AlarmHistory> AlarmHistorys = null;
        Cursor cursor = DbCommonUtil.getAll(dbHelper,ConfigConstant.TABLE_ALARMHISTORY);
        while(cursor.moveToNext()){
            if(null == AlarmHistorys){
                AlarmHistorys = new ArrayList<AlarmHistory>();
            }
            AlarmHistory device = fillDefault(cursor);
            AlarmHistorys.add(device);
        }
        cursor.close();
        return AlarmHistorys;
    }

    private AlarmHistory fillDefault(Cursor cursor) {
        if(null == cursor){
            return null;
        }
        AlarmHistory device = new AlarmHistory();
        device.mId = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
        device.mUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_UUID));
        device.mTime = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_TIME));
        device.mAlarmType = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_ALARMTYPE));
        device.mMessage = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_MESSAGE));
        device.mRead = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_READ));
        return device;
    }

    public void notificationAlarmGet(JSONObject jsonObject) throws JSONException{
        List<AlarmHistory>lists = getAlarmHistoryAllList();
        if(null == lists){
            return;
        }
        String dataStatus = jsonObject.optString(CommonData.JSON_KEY_DATASTATUS);
        int start = Integer.valueOf(jsonObject.optString(CommonData.JSON_KEY_START));
        int count = Integer.valueOf(jsonObject.optString(CommonData.JSON_KEY_COUNT));
        JSONArray loopMapArray = new JSONArray();
        int index = 0;
        for (int i = 0; i < lists.size(); i++) {
            AlarmHistory loop = lists.get(i);
            if(!dataStatus.equals(CommonData.DATASTATUS_ALL)
                    && !dataStatus.equals(DbCommonUtil.transferReadIntToString(loop.mRead))){
                continue;
            }
            if(index < start || index >= start + count){
                index++;
                continue;
            }
            index++;
            JSONObject loopMapObject = new JSONObject();
            loopToJson(loopMapObject,loop);
            loopMapArray.put(loopMapObject);
        }
        jsonObject.put(CommonData.JSON_LOOPMAP_KEY,loopMapArray);
        jsonObject.put(CommonData.JSON_ERRORCODE_KEY,CommonData.JSON_ERRORCODE_VALUE_OK);
    }


    private void loopToJson(JSONObject loopMapObject, AlarmHistory loop) throws JSONException {
        loopMapObject.put(CommonData.JSON_UUID_KEY,loop.mUuid);
        loopMapObject.put(CommonData.JSON_KEY_TIME,loop.mTime);
        loopMapObject.put(CommonData.JSON_KEY_DATASTATUS,DbCommonUtil.transferReadIntToString(loop.mRead));
        loopMapObject.put(CommonData.JSON_KEY_ALARMTYPE,loop.mTime);
        loopMapObject.put(CommonData.JSON_KEY_MESSAGE,loop.mTime);
    }

    public void notificationAlarmAdd(JSONObject jsonObject) throws  JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.getString(CommonData.JSON_UUID_KEY);
            String message = loopMapObject.optString(CommonData.JSON_KEY_MESSAGE);
            String alarmType = loopMapObject.optString(CommonData.JSON_KEY_ALARMTYPE);
            String time = loopMapObject.optString(CommonData.JSON_KEY_TIME);
            String dataStatus  = loopMapObject.optString(CommonData.JSON_KEY_DATASTATUS);
            long rowId = add(uuid,time,alarmType,message,DbCommonUtil.transferReadStringToInt(dataStatus));
            DbCommonUtil.putErrorCodeFromOperate(rowId,loopMapObject);
        }
    }

    public void notificationAlarmUpdate(JSONObject jsonObject) throws  JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonData.JSON_UUID_KEY);
            String datastatus  = loopMapObject.optString(CommonData.JSON_KEY_DATASTATUS);
            AlarmHistory loop = getByUuid(uuid);
            loop.mRead = DbCommonUtil.transferReadStringToInt(datastatus);
            long num = updateByUuid(uuid,loop);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
        }
    }

    public void notificationAlarmDelete(JSONObject jsonObject) throws  JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonData.JSON_UUID_KEY);
            long num = deleteByUuid(uuid);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
        }
    }

    public void notificationAlarmCountGet(JSONObject jsonObject) throws  JSONException {
        String statusStr = jsonObject.getString(CommonData.JSON_KEY_DATASTATUS);
        int dataStatus = DbCommonUtil.transferReadStringToInt(statusStr);
        int count = getAlarmCountByStatus(dataStatus);
        jsonObject.put(CommonData.JSON_KEY_COUNT,""+count);
        jsonObject.put(CommonData.JSON_ERRORCODE_KEY,CommonData.JSON_ERRORCODE_VALUE_OK);
    }
}
