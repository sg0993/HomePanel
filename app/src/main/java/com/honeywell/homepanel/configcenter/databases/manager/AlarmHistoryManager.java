package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.utils.CommonUtils;
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

    public synchronized long add(String uuid, String triggerid, String time,String aliasName, String alarmType,String message,int read, int uploadAms, int uploadCloud){
        String str = CommonUtils.deISO8601TimeStampForCurrTime(time);//add by xiaochao
        long ltime = CommonUtils.convertStringDateToLong(str);
        ltime += 8*60*60*1000;//add 8 hours
        int time1 = 0;
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_UUID, uuid)
                .put(ConfigConstant.COLUMN_TRIGGERID, triggerid)
                .put(ConfigConstant.COLUMN_TIME, ltime)
                .put(ConfigConstant.COLUMN_NAME, aliasName)
                .put(ConfigConstant.COLUMN_ALARMTYPE, alarmType)
                .put(ConfigConstant.COLUMN_MESSAGE, message)
                .put(ConfigConstant.COLUMN_READ,read)
                .put(ConfigConstant.COLUMN_UPLOADAMS,uploadAms)
                .put(ConfigConstant.COLUMN_UPLOADCLOUD,uploadCloud).getValues();

        return DbCommonUtil.add(mContext,dbHelper,ConfigConstant.TABLE_ALARMHISTORY, values);
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

    private ContentValues putNewReportStatus(AlarmHistory device, String type){
        ContentValues values = new ContentValues();
        if(null == device){
            return  values;
        }

        if (type.equals(CommonData.ALARMREPORT_TYPE_AMS)) {
            values.put(ConfigConstant.COLUMN_UPLOADAMS, device.mUploadAms);
        } else if (type.equals(CommonData.ALARMREPORT_TYPE_CLOUD)) {
            values.put(ConfigConstant.COLUMN_UPLOADCLOUD, device.mUploadCloud);
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

    public synchronized int updateUploadStatusByUuid(String uuid, String type, AlarmHistory device) {
        if(null == uuid){
            return -1;
        }
        return  DbCommonUtil.updateByStringFiled(mContext,
                                                dbHelper,
                                                ConfigConstant.TABLE_ALARMHISTORY,
                                                putNewReportStatus(device, type),
                                                ConfigConstant.COLUMN_UUID,
                                                uuid);
    }


    public synchronized AlarmHistory getByPrimaryId(long primaryId) {
        if(primaryId <= 0){
            return  null;
        }
        Cursor cursor = DbCommonUtil.getByPrimaryId(dbHelper,ConfigConstant.TABLE_ALARMHISTORY,primaryId);
        AlarmHistory device = null;
        while(cursor.moveToNext()) {
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized int getAlarmCountByStatus(int status) {
        Cursor cursor = DbCommonUtil.getByIntField(dbHelper,ConfigConstant.TABLE_ALARMHISTORY,ConfigConstant.COLUMN_READ,status);
        int count = 0;
        if(null == cursor){
            return count;
        }
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
        while(cursor.moveToNext()) {
            if( null == AlarmHistorys) {
                AlarmHistorys = new ArrayList<AlarmHistory>();
            }
            String uuid = cursor.getString(cursor.getColumnIndex(CommonJson.JSON_UUID_KEY));
            String triggerid = cursor.getString(cursor.getColumnIndex(CommonData.JSON_KEY_TRIGGERID));
            long time = cursor.getLong(cursor.getColumnIndex(CommonData.JSON_KEY_SETTIME));
            String aliasName = cursor.getString(cursor.getColumnIndex(CommonData.JSON_KEY_NAME));
            String alarmtype = cursor.getString(cursor.getColumnIndex(CommonData.JSON_KEY_ALARMTYPE));
            String message = cursor.getString(cursor.getColumnIndex(CommonData.JSON_KEY_MESSAGE));
            int read = cursor.getInt(cursor.getColumnIndex(CommonData.DATASTATUS_READ));
            AlarmHistory item = new AlarmHistory(uuid, triggerid, time,aliasName, message, alarmtype, read, 0, 0);
            AlarmHistorys.add(item);
        }
        cursor.close();
        return AlarmHistorys;
    }

    public synchronized List<AlarmHistory> getAlarmHistoryList(int start, int count) {
        Cursor cursor = null;
        List<AlarmHistory> AlarmHistorys = new ArrayList<AlarmHistory>();

        if (count <= 0 || start < 0) {
            cursor = DbCommonUtil.getAll(dbHelper, ConfigConstant.TABLE_ALARMHISTORY, "desc");
        } else {
            cursor = DbCommonUtil.get(dbHelper, ConfigConstant.TABLE_ALARMHISTORY, "desc", start, count);
        }

        while(cursor.moveToNext()) {
            String uuid = cursor.getString(cursor.getColumnIndex(CommonJson.JSON_UUID_KEY));
            String triggerid = cursor.getString(cursor.getColumnIndex(CommonData.JSON_KEY_TRIGGERID));
            long time = cursor.getLong(cursor.getColumnIndex(CommonData.JSON_KEY_SETTIME));
            String aliasName = cursor.getString(cursor.getColumnIndex(CommonData.JSON_KEY_NAME));
            String alarmtype = cursor.getString(cursor.getColumnIndex(CommonData.JSON_KEY_ALARMTYPE));
            String message = cursor.getString(cursor.getColumnIndex(CommonData.JSON_KEY_MESSAGE));
            int read = cursor.getInt(cursor.getColumnIndex(CommonData.DATASTATUS_READ));
            AlarmHistory item = new AlarmHistory(uuid, triggerid, time, aliasName, message, alarmtype, read, 0, 0);
            AlarmHistorys.add(item);
        }
        cursor.close();
        return AlarmHistorys;
    }

    public synchronized List<AlarmHistory> getUnreportedAlarmList(String type, int start, int count) {
        List<AlarmHistory> AlarmHistorys = new ArrayList<AlarmHistory>();
//        Cursor cursor = DbCommonUtil.getAll(dbHelper,ConfigConstant.TABLE_ALARMHISTORY);
        Cursor cursor = DbCommonUtil.getUnreportedAlarmByType(dbHelper,ConfigConstant.TABLE_ALARMHISTORY, type, start, count);
        while(cursor.moveToNext()) {
            String uuid = cursor.getString(cursor.getColumnIndex(CommonJson.JSON_UUID_KEY));
            String triggerid = cursor.getString(cursor.getColumnIndex(CommonData.JSON_KEY_TRIGGERID));
            long time = cursor.getLong(cursor.getColumnIndex(CommonData.JSON_KEY_SETTIME));
            String aliasName = cursor.getString(cursor.getColumnIndex(CommonData.JSON_KEY_NAME));
            String alarmtype = cursor.getString(cursor.getColumnIndex(CommonData.JSON_KEY_ALARMTYPE));
            String message = cursor.getString(cursor.getColumnIndex(CommonData.JSON_KEY_MESSAGE));
            int read = cursor.getInt(cursor.getColumnIndex(CommonData.DATASTATUS_READ));
            int uploadams = cursor.getInt(cursor.getColumnIndex(CommonData.JSON_KEY_UPLOADAMS));
            int uploadcloud = cursor.getInt(cursor.getColumnIndex(CommonData.JSON_KEY_UPLOADCLOUD));
            AlarmHistory item = new AlarmHistory(uuid, triggerid, time, aliasName, message, alarmtype, read, uploadams, uploadcloud);
            AlarmHistorys.add(item);
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
        device.mTriggerId = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_TRIGGERID));
        device.mTime = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_TIME));
        device.mAliasName = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_NAME));
        device.mAlarmType = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_ALARMTYPE));
        device.mMessage = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_MESSAGE));
        device.mRead = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_READ));
        device.mUploadAms = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_UPLOADAMS));
        device.mUploadCloud = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_UPLOADCLOUD));
        return device;
    }

    public void notificationAlarmGet(JSONObject jsonObject) throws JSONException{
        String dataStatus = jsonObject.optString(CommonData.JSON_KEY_DATASTATUS);
        int start = Integer.valueOf(jsonObject.optString(CommonData.JSON_KEY_START, "-1"));
        int count = Integer.valueOf(jsonObject.optString(CommonData.JSON_KEY_COUNT, "-1"));
        List<AlarmHistory>lists = getAlarmHistoryList(start, count);

        JSONArray loopMapArray = new JSONArray();
        for (int index = 0; index < lists.size(); index++) {
            AlarmHistory loop = lists.get(index);
            if(!dataStatus.equals(CommonData.DATASTATUS_ALL)
                    && !dataStatus.equals(DbCommonUtil.transferReadIntToString(loop.mRead))) {
                continue;
            }

            JSONObject loopMapObject = new JSONObject();
            loopToJson(loopMapObject, loop);
            loopMapArray.put(loopMapObject);
        }
        jsonObject.put(CommonJson.JSON_LOOPMAP_KEY,loopMapArray);
        jsonObject.put(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_OK);
    }

    public void unreportedAlarmGet(JSONObject jsonObject) throws JSONException{
        int loadCount = 0;
        Log.d(TAG, "unportedAlarmGet: jsonObject:" + jsonObject.toString());
        String unreportedType =  jsonObject.optString(CommonData.JSON_TYPE_KEY);
        int start = jsonObject.optInt(CommonData.JSON_KEY_START);
        int count = jsonObject.optInt(CommonData.JSON_KEY_COUNT);
        if ( (unreportedType == null) || (start < 0)
                || (start > CommonData.MAXALARMHISTROYCOUNT)
                || (count < 0)
                || (count > CommonData.MAXALARMHISTROYCOUNT)
                || (start + count > CommonData.MAXALARMHISTROYCOUNT)) {
            Log.e(TAG, "unportedAlarmGet: ");
            return;
        }
        List<AlarmHistory>lists = getUnreportedAlarmList(unreportedType, start, count);
        Log.d(TAG, "unportedAlarmGet: lists:" + lists.size());
        JSONArray loopMapArray = new JSONArray();

        for (int i = 0; i < lists.size(); i++) {
            AlarmHistory loop = lists.get(i);
            if (i > count) {//if get more data than request then braek
                Log.w(TAG, "unportedAlarmGet: ");
                break;
            }
            JSONObject loopMapObject = new JSONObject();
            loopToJson(loopMapObject, loop);
            loopMapArray.put(loopMapObject);
            loadCount++;
        }
        jsonObject.put(CommonData.JSON_KEY_COUNT, loadCount);
        jsonObject.put(CommonJson.JSON_LOOPMAP_KEY,loopMapArray);
        jsonObject.put(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_OK);
    }

    private void loopToJson(JSONObject loopMapObject, AlarmHistory loop) throws JSONException {
        loopMapObject.put(CommonJson.JSON_UUID_KEY,loop.mUuid);
        loopMapObject.put(CommonData.JSON_KEY_TRIGGERID,loop.mTriggerId);
        loopMapObject.put(CommonData.JSON_KEY_TIME,loop.mTime);
        loopMapObject.put(CommonData.JSON_KEY_NAME, loop.mAliasName);
        loopMapObject.put(CommonData.JSON_KEY_ALARMTYPE,loop.mAlarmType);
        loopMapObject.put(CommonData.JSON_KEY_DATASTATUS, DbCommonUtil.transferReadIntToString(loop.mRead));
        loopMapObject.put(CommonData.JSON_KEY_MESSAGE,loop.mMessage);
        loopMapObject.put(CommonData.JSON_KEY_UPLOADAMS,loop.mUploadAms);
        loopMapObject.put(CommonData.JSON_KEY_UPLOADCLOUD,loop.mUploadCloud);
    }

    public void notificationAlarmAdd(JSONObject jsonObject) throws  JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        Log.d(TAG, "notificationAlarmAdd: jsonArray.length():" + jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.getString(CommonJson.JSON_UUID_KEY);
            String triggerid = loopMapObject.getString(CommonData.JSON_KEY_TRIGGERID);
            String message = loopMapObject.optString(CommonData.JSON_KEY_MESSAGE);
            String alias = loopMapObject.optString(CommonData.JSON_KEY_NAME);
            String alarmType = loopMapObject.optString(CommonData.JSON_KEY_ALARMTYPE);
            String time = loopMapObject.optString(CommonData.JSON_KEY_TIME);
            String dataStatus  = loopMapObject.optString(CommonData.JSON_KEY_DATASTATUS);
            int uploadAms  = loopMapObject.optInt(CommonData.JSON_KEY_UPLOADAMS);
            int uploadCloud  = loopMapObject.optInt(CommonData.JSON_KEY_UPLOADCLOUD);
            long rowId = add(uuid, triggerid, time, alias, alarmType, message,
                                DbCommonUtil.transferReadStringToInt(dataStatus),
                                uploadAms,
                                uploadCloud);

            DbCommonUtil.putErrorCodeFromOperate(rowId,loopMapObject);
        }

        // delete alarm records that exceed max alarm history account number
        DbCommonUtil.deleteExceedLimiteRecords(dbHelper, ConfigConstant.TABLE_ALARMHISTORY, CommonData.MAXALARMHISTROYCOUNT);
    }

    public void notificationAlarmUpdate(JSONObject jsonObject) throws  JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonJson.JSON_UUID_KEY);
            String datastatus  = loopMapObject.optString(CommonData.JSON_KEY_DATASTATUS);
            AlarmHistory loop = getByUuid(uuid);
            if(null == loop){
                continue;
            }
            loop.mRead = DbCommonUtil.transferReadStringToInt(datastatus);
            long num = updateByUuid(uuid,loop);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
        }
    }

    public void updateAlarmReportStatus(JSONObject jsonObject) throws  JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        Log.d(TAG, "updateAlarmReportStatus: jsonArray.length():" + jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonJson.JSON_UUID_KEY);
            String reportType = loopMapObject.optString(CommonData.JSON_TYPE_KEY);
            String reportStatus  = loopMapObject.optString(CommonData.ALARMREPORTSTATUS);
            Log.d(TAG, "updateAlarmReportStatus: uuid:" + uuid);
            Log.d(TAG, "updateAlarmReportStatus: reportType:" + reportType);
            Log.d(TAG, "updateAlarmReportStatus: reportStatus:" + reportStatus);
            AlarmHistory loop = getByUuid(uuid);
            if(null == loop){
                continue;
            }
            if (reportType.equals(CommonData.ALARMREPORT_TYPE_AMS)) {
                loop.mUploadAms = DbCommonUtil.transferUploadStatusToInt(reportStatus);
            } else if (reportType.equals(CommonData.ALARMREPORT_TYPE_CLOUD)) {
                Log.d(TAG, "updateAlarmReportStatus: update cloud report status");
                loop.mUploadCloud = DbCommonUtil.transferUploadStatusToInt(reportStatus);
            } else {
                Log.e(TAG, "updateAlarmReportStatus: undefine type:" + reportType);
                return;
            }
            long num = updateUploadStatusByUuid(uuid, reportType, loop);
            if (num > 1) {
                Log.e(TAG, "updateAlarmReportStatus: more rows has been affected than expected!");
            }
            DbCommonUtil.putErrorCodeFromOperate(num, loopMapObject);
        }
    }

    public void notificationAlarmDelete(JSONObject jsonObject) throws  JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject loopMapObject = jsonArray.getJSONObject(i);
                String uuid = loopMapObject.optString(CommonJson.JSON_UUID_KEY);
                long num = deleteByUuid(uuid);
                DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
            }
        } else {

        }
    }

    public void notificationAlarmCountGet(JSONObject jsonObject) throws  JSONException {
        String statusStr = jsonObject.getString(CommonData.JSON_KEY_DATASTATUS);
        int dataStatus = DbCommonUtil.transferReadStringToInt(statusStr);
        int count = getAlarmCountByStatus(dataStatus);
        jsonObject.put(CommonData.JSON_KEY_COUNT,""+count);
        jsonObject.put(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_OK);
    }
}
