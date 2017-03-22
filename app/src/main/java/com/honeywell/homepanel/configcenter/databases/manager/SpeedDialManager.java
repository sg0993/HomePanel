package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.configcenter.ConfigService;
import com.honeywell.homepanel.configcenter.databases.ConfigDatabaseHelper;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.domain.SpeedDial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by H135901 on 3/16/2017.
 */

public class SpeedDialManager {
    private Context mContext = null;
    private static final String TAG  = ConfigService.TAG;
    private static SpeedDialManager instance  = null;
    private ConfigDatabaseHelper dbHelper = null;

    public static synchronized SpeedDialManager getInstance(Context context) {
        if(instance == null){
            instance = new SpeedDialManager(context);
        }
        return instance;
    }
    private SpeedDialManager(Context context) {
        mContext = context;
        dbHelper= ConfigDatabaseHelper.getInstance(mContext);
    }

    public synchronized long add(String uuid,String type,String dongho){
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_UUID, uuid)
                .put(ConfigConstant.COLUMN_TYPE, type)
                .put(ConfigConstant.COLUMN_DONGHO,dongho).getValues();
        return DbCommonUtil.add(mContext,dbHelper,ConfigConstant.TABLE_SPEEDDIAL,values);
    }

    public synchronized int deleteByUuid(String uuid) {
        return DbCommonUtil.deleteByStringFieldType(mContext,dbHelper,ConfigConstant.TABLE_SPEEDDIAL,ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized int deleteByPrimaryId(long primaryId) {
        return DbCommonUtil.deleteByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_SPEEDDIAL,primaryId);
    }

    private ContentValues putUpDateValues(SpeedDial device){
        ContentValues values = new ContentValues();
        if(null == device){
            return  values;
        }
        if(!TextUtils.isEmpty(device.mType)){
            values.put(ConfigConstant.COLUMN_TYPE, device.mType);
        }
        if(!TextUtils.isEmpty(device.mDongHo)){
            values.put(ConfigConstant.COLUMN_DONGHO, device.mDongHo);
        }
        return values;
    }

    public synchronized int updateByPrimaryId(long primaryId, SpeedDial device) {
        if(null == device || primaryId <= 0){
            return -1;
        }
        return DbCommonUtil.updateByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_SPEEDDIAL,putUpDateValues(device),primaryId);
    }

    public synchronized int updateByUuid(String  uuid, SpeedDial device) {
        if(null == device){
            return -1;
        }
        return  DbCommonUtil.updateByStringFiled(mContext,dbHelper,ConfigConstant.TABLE_SPEEDDIAL,putUpDateValues(device),ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized SpeedDial getByPrimaryId(long primaryId) {
        if(primaryId <= 0){
            return  null;
        }
        Cursor cursor = DbCommonUtil.getByPrimaryId(dbHelper,ConfigConstant.TABLE_SPEEDDIAL,primaryId);
        SpeedDial device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized SpeedDial getByUuid(String uuid) {
        if(TextUtils.isEmpty(uuid)){
            return null;
        }
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_SPEEDDIAL,ConfigConstant.COLUMN_UUID,uuid);
        SpeedDial device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized List<SpeedDial> getSpeedDialAllList() {
        List<SpeedDial> SpeedDials = null;
        Cursor cursor = DbCommonUtil.getAll(dbHelper,ConfigConstant.TABLE_SPEEDDIAL);
        while(cursor.moveToNext()){
            if(null == SpeedDials){
                SpeedDials = new ArrayList<SpeedDial>();
            }
            SpeedDial device = fillDefault(cursor);
            SpeedDials.add(device);
        }
        cursor.close();
        return SpeedDials;
    }

    private SpeedDial fillDefault(Cursor cursor) {
        if(null == cursor){
            return null;
        }
        SpeedDial device = new SpeedDial();
        device.mUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_UUID));
        device.mType = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_TYPE));
        device.mId = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
        device.mDongHo = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_DONGHO));
        return device;
    }

    public void speeddialGet(JSONObject jsonObject) throws JSONException{
        List<SpeedDial>lists = getSpeedDialAllList();
        if(null == lists){
            return;
        }
        JSONArray loopMapArray = new JSONArray();
        for (int i = 0; i < lists.size(); i++) {
            SpeedDial loop = lists.get(i);
            JSONObject loopMapObject = new JSONObject();
            loopToJson(loopMapObject,loop);
            loopMapArray.put(loopMapObject);
        }
        jsonObject.put(CommonData.JSON_LOOPMAP_KEY,loopMapArray);
        jsonObject.put(CommonData.JSON_ERRORCODE_KEY,CommonData.JSON_ERRORCODE_VALUE_OK);
    }

    private void loopToJson(JSONObject loopMapObject, SpeedDial loop) throws JSONException {
        loopMapObject.put(CommonData.JSON_UUID_KEY,loop.mUuid);
        loopMapObject.put(CommonData.JSON_TYPE_KEY,loop.mType);
        loopMapObject.put(CommonData.JSON_DONGHO_KEY,loop.mDongHo);
    }

    public void speeddialAdd(JSONObject jsonObject)  throws JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String type = loopMapObject.optString(CommonData.JSON_TYPE_KEY);
            String dongHo = loopMapObject.optString(CommonData.JSON_DONGHO_KEY);
            long rowid = add(UUID.randomUUID().toString(),type,dongHo);
            DbCommonUtil.putErrorCodeFromOperate(rowid, loopMapObject);
        }
    }

    public void speeddialUpdate(JSONObject jsonObject)  throws JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonData.JSON_UUID_KEY);
            String dongHo  = loopMapObject.optString(CommonData.JSON_DONGHO_KEY);
            SpeedDial loop = getByUuid(uuid);
            loop.mDongHo = dongHo;
            long num = updateByUuid(uuid,loop);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
        }
    }

    public void speeddialDelete(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonData.JSON_UUID_KEY);
            long num = deleteByUuid(uuid);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
        }
    }
}
