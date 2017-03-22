package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.configcenter.ConfigService;
import com.honeywell.homepanel.configcenter.databases.ConfigDatabaseHelper;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.domain.VoiceMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by H135901 on 3/16/2017.
 */

public class VoiceMessageManager {
    private Context mContext = null;
    private static final String TAG  = ConfigService.TAG;
    private static VoiceMessageManager instance  = null;
    private ConfigDatabaseHelper dbHelper = null;

    public static synchronized VoiceMessageManager getInstance(Context context) {
        if(instance == null){
            instance = new VoiceMessageManager(context);
        }
        return instance;
    }
    private VoiceMessageManager(Context context) {
        mContext = context;
        dbHelper= ConfigDatabaseHelper.getInstance(mContext);
    }

    public synchronized long add(String uuid,String time,int length,String path,int read){
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_UUID, uuid)
                .put(ConfigConstant.COLUMN_TIME, time)
                .put(ConfigConstant.COLUMN_LENGTH,length)
                .put(ConfigConstant.COLUMN_PATH,path)
                .put(ConfigConstant.COLUMN_READ,read).getValues();
        return DbCommonUtil.add(mContext,dbHelper,ConfigConstant.TABLE_VOICEMESSAGE,values);
    }

    public synchronized int deleteByUuid(String uuid) {
        return DbCommonUtil.deleteByStringFieldType(mContext,dbHelper,ConfigConstant.TABLE_VOICEMESSAGE,ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized int deleteByPrimaryId(long primaryId) {
        return DbCommonUtil.deleteByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_VOICEMESSAGE,primaryId);
    }

    private ContentValues putUpDateValues(VoiceMessage device){
        ContentValues values = new ContentValues();
        if(null == device){
            return  values;
        }
        if(!TextUtils.isEmpty(device.mTime)){
            values.put(ConfigConstant.COLUMN_TIME, device.mTime);
        }
        if(!TextUtils.isEmpty(device.mPath)){
            values.put(ConfigConstant.COLUMN_PATH, device.mPath);
        }
        if(device.mRead >= 0){
            values.put(ConfigConstant.COLUMN_READ,device.mRead);
        }
        return values;
    }

    public synchronized int updateByPrimaryId(long primaryId, VoiceMessage device) {
        if(null == device || primaryId <= 0){
            return -1;
        }
        return DbCommonUtil.updateByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_VOICEMESSAGE,putUpDateValues(device),primaryId);
    }

    public synchronized int updateByUuid(String  uuid, VoiceMessage device) {
        if(null == device){
            return -1;
        }
        return  DbCommonUtil.updateByStringFiled(mContext,dbHelper,ConfigConstant.TABLE_VOICEMESSAGE,putUpDateValues(device),ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized VoiceMessage getByPrimaryId(long primaryId) {
        if(primaryId <= 0){
            return  null;
        }
        Cursor cursor = DbCommonUtil.getByPrimaryId(dbHelper,ConfigConstant.TABLE_VOICEMESSAGE,primaryId);
        VoiceMessage device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized VoiceMessage getByUuid(String uuid) {
        if(TextUtils.isEmpty(uuid)){
            return null;
        }
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_VOICEMESSAGE,ConfigConstant.COLUMN_UUID,uuid);
        VoiceMessage device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized List<VoiceMessage> getVoiceMessageAllList() {
        List<VoiceMessage> VoiceMessages = null;
        Cursor cursor = DbCommonUtil.getAll(dbHelper,ConfigConstant.TABLE_VOICEMESSAGE);
        while(cursor.moveToNext()){
            if(null == VoiceMessages){
                VoiceMessages = new ArrayList<VoiceMessage>();
            }
            VoiceMessage device = fillDefault(cursor);
            VoiceMessages.add(device);
        }
        cursor.close();
        return VoiceMessages;
    }

    private VoiceMessage fillDefault(Cursor cursor) {
        if(null == cursor){
            return null;
        }
        VoiceMessage device = new VoiceMessage();
        device.mId = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
        device.mUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_UUID));
        device.mTime = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_TIME));
        device.mLength = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_LENGTH));
        device.mPath = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_PATH));
        device.mRead = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_READ));
        return device;
    }

    public void notificationMessageGet(JSONObject jsonObject) throws JSONException{
        List<VoiceMessage>lists = getVoiceMessageAllList();
        if(null == lists){
            return;
        }
        JSONArray loopMapArray = new JSONArray();
        for (int i = 0; i < lists.size(); i++) {
            VoiceMessage loop = lists.get(i);
            JSONObject loopMapObject = new JSONObject();
            loopToJson(loopMapObject,loop);
            loopMapArray.put(loopMapObject);
        }
        jsonObject.put(CommonData.JSON_LOOPMAP_KEY,loopMapArray);
        jsonObject.put(CommonData.JSON_ERRORCODE_KEY,CommonData.JSON_ERRORCODE_VALUE_OK);
    }

    private void loopToJson(JSONObject loopMapObject, VoiceMessage loop) throws  JSONException{
        loopMapObject.put(CommonData.JSON_UUID_KEY,loop.mUuid);
        loopMapObject.put(CommonData.JSON_KEY_FILENAME,loop.mPath);
        loopMapObject.put(CommonData.JSON_KEY_TIME,loop.mTime);
        loopMapObject.put(CommonData.JSON_KEY_DURATION,loop.mLength+"");
        loopMapObject.put(CommonData.JSON_KEY_DATASTATUS,DbCommonUtil.transferReadIntToString(loop.mRead));
    }

    public void notificationMessageAdd(JSONObject jsonObject) throws  JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String fileName = loopMapObject.optString(CommonData.JSON_KEY_FILENAME);
            String time = loopMapObject.optString(CommonData.JSON_KEY_TIME);
            String duration = loopMapObject.optString(CommonData.JSON_KEY_DURATION);
            String datastatus = loopMapObject.optString(CommonData.JSON_KEY_DATASTATUS);
            long rowid = add(UUID.randomUUID().toString(),time,Integer.valueOf(duration),fileName,DbCommonUtil.transferReadStringToInt(datastatus));
            DbCommonUtil.putErrorCodeFromOperate(rowid, loopMapObject);
        }
    }

    public void notificationMessageUpdate(JSONObject jsonObject) throws  JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonData.JSON_UUID_KEY);
            String datastatus  = loopMapObject.optString(CommonData.JSON_KEY_DATASTATUS);
            VoiceMessage loop = getByUuid(uuid);
            loop.mRead = DbCommonUtil.transferReadStringToInt(datastatus);
            long num = updateByUuid(uuid,loop);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
        }
    }

    public void notificationMessageDelete(JSONObject jsonObject) throws  JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonData.JSON_UUID_KEY);
            long num = deleteByUuid(uuid);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
        }
    }
}
