package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.configcenter.ConfigService;
import com.honeywell.homepanel.configcenter.databases.ConfigDatabaseHelper;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.domain.IpDoorCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by H135901 on 3/17/2017.
 */

public class IpDoorCardanager {
    private Context mContext = null;
    private static final String TAG  = ConfigService.TAG;
    private static IpDoorCardanager instance  = null;
    private ConfigDatabaseHelper dbHelper = null;

    public static synchronized IpDoorCardanager getInstance(Context context) {
        if(instance == null){
            instance = new IpDoorCardanager(context);
        }
        return instance;
    }
    private IpDoorCardanager(Context context) {
        mContext = context;
        dbHelper= ConfigDatabaseHelper.getInstance(mContext);
    }

    public synchronized long add(String uuid,String type,String name,String cardNo, String startDate,String expireDate,
                                 String startTime,String expireTime,String action){
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_UUID, uuid)
                .put(ConfigConstant.COLUMN_TYPE, type)
                .put(ConfigConstant.COLUMN_NAME, name)
                .put(ConfigConstant.COLUMN_CARDNO, cardNo)
                .put(ConfigConstant.COLUMN_STARTDATE,startTime)
                .put(ConfigConstant.COLUMN_EXPIREDATE,expireTime)
                .put(ConfigConstant.COLUMN_STARTTIME,startTime)
                .put(ConfigConstant.COLUMN_EXPIRETIME,expireTime)
                .put(ConfigConstant.COLUMN_ACTION,action).getValues();
        return DbCommonUtil.add(mContext,dbHelper,ConfigConstant.TABLE_IPDOORCARD,values);
    }

    public synchronized int deleteByUuid(String uuid) {
        return DbCommonUtil.deleteByStringFieldType(mContext,dbHelper,ConfigConstant.TABLE_IPDOORCARD,ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized int deleteByPrimaryId(long primaryId) {
        return DbCommonUtil.deleteByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_IPDOORCARD,primaryId);
    }

    private ContentValues putUpDateValues(IpDoorCard device){
        ContentValues values = new ContentValues();
        if(null == device){
            return  values;
        }
        if(!TextUtils.isEmpty(device.mType)){
            values.put(ConfigConstant.COLUMN_TYPE, device.mType);
        }
        if(!TextUtils.isEmpty(device.mName)){
            values.put(ConfigConstant.COLUMN_NAME, device.mName);
        }
        if(!TextUtils.isEmpty(device.mCardNo)){
            values.put(ConfigConstant.COLUMN_CARDNO, device.mCardNo);
        }
        if(!TextUtils.isEmpty(device.mStartDate)){
            values.put(ConfigConstant.COLUMN_STARTDATE, device.mStartDate);
        }
        if(!TextUtils.isEmpty(device.mExpireDate)){
            values.put(ConfigConstant.COLUMN_EXPIREDATE, device.mExpireDate);
        }
        if(!TextUtils.isEmpty(device.mStartTime)){
            values.put(ConfigConstant.COLUMN_STARTTIME, device.mStartTime);
        }
        if(!TextUtils.isEmpty(device.mExpireTime)){
            values.put(ConfigConstant.COLUMN_EXPIRETIME, device.mExpireTime);
        }
        if(!TextUtils.isEmpty(device.mAction)){
            values.put(ConfigConstant.COLUMN_ACTION, device.mAction);
        }
        return values;
    }

    public synchronized int updateByPrimaryId(long primaryId, IpDoorCard device) {
        if(null == device || primaryId <= 0){
            return -1;
        }
        return DbCommonUtil.updateByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_IPDOORCARD,putUpDateValues(device),primaryId);
    }

    public synchronized int updateByUuid(String  uuid, IpDoorCard device) {
        if(null == device){
            return -1;
        }
        return  DbCommonUtil.updateByStringFiled(mContext,dbHelper,ConfigConstant.TABLE_IPDOORCARD,putUpDateValues(device),ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized IpDoorCard getByPrimaryId(long primaryId) {
        if(primaryId <= 0){
            return  null;
        }
        Cursor cursor = DbCommonUtil.getByPrimaryId(dbHelper,ConfigConstant.TABLE_IPDOORCARD,primaryId);
        IpDoorCard device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized IpDoorCard getByUuid(String uuid) {
        if(TextUtils.isEmpty(uuid)){
            return null;
        }
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_IPDOORCARD,ConfigConstant.COLUMN_UUID,uuid);
        IpDoorCard device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized List<IpDoorCard> getIpDoorCardAllList() {
        List<IpDoorCard> IpDoorCards = null;
        Cursor cursor = DbCommonUtil.getAll(dbHelper,ConfigConstant.TABLE_IPDOORCARD);
        while(cursor.moveToNext()){
            if(null == IpDoorCards){
                IpDoorCards = new ArrayList<IpDoorCard>();
            }
            IpDoorCard device = fillDefault(cursor);
            IpDoorCards.add(device);
        }
        cursor.close();
        return IpDoorCards;
    }

    private IpDoorCard fillDefault(Cursor cursor) {
        if(null == cursor){
            return null;
        }
        IpDoorCard device = new IpDoorCard();
        device.mId = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
        device.mUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_UUID));
        device.mType = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_TYPE));
        device.mName = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_NAME));
        device.mCardNo = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_CARDNO));
        device.mStartDate = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_STARTDATE));
        device.mExpireDate = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_EXPIREDATE));
        device.mStartTime = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_STARTTIME));
        device.mExpireTime = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_EXPIRETIME));
        device.mAction = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_ACTION));
        return device;
    }

    public void cardGet(JSONObject jsonObject) throws JSONException {
        List<IpDoorCard>lists = getIpDoorCardAllList();
        if(null == lists){
            return;
        }
        int start = jsonObject.optInt(CommonData.JSON_KEY_START);
        int count = jsonObject.optInt(CommonData.JSON_KEY_COUNT);
        JSONArray loopMapArray = new JSONArray();
        int index = 0;
        for (int i = 0; i < lists.size(); i++) {
            IpDoorCard loop = lists.get(i);
            if(index < start && index >= start + count){
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
    private void loopToJson(JSONObject loopMapObject, IpDoorCard loop) throws JSONException {
        loopMapObject.put(CommonData.JSON_UUID_KEY,loop.mUuid);
        loopMapObject.put(CommonData.JSON_KEY_CARDTYPE,loop.mType);
        loopMapObject.put(CommonData.JSON_KEY_NAME,loop.mName);
        loopMapObject.put(CommonData.JSON_KEY_CARDID,loop.mCardNo);
        loopMapObject.put(CommonData.JSON_KEY_STARTDATE,loop.mStartDate);
        loopMapObject.put(CommonData.JSON_KEY_ENDDATE,loop.mExpireDate);
        loopMapObject.put(CommonData.JSON_KEY_STARTTIME,loop.mStartTime);
        loopMapObject.put(CommonData.JSON_KEY_ENDTIME,loop.mExpireTime);
        loopMapObject.put(CommonData.JSON_KEY_SWIPEACTION,loop.mAction);
    }

    public void cardAdd(JSONObject jsonObject)  throws JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String type = loopMapObject.optString(CommonData.JSON_KEY_CARDTYPE);
            String name = loopMapObject.optString(CommonData.JSON_ALIASNAME_KEY);
            String cardId = loopMapObject.optString(CommonData.JSON_KEY_CARDID);
            String startDate = loopMapObject.optString(CommonData.JSON_KEY_STARTDATE);
            String expireDate = loopMapObject.optString(CommonData.JSON_KEY_ENDDATE);
            String startTime = loopMapObject.optString(CommonData.JSON_KEY_STARTTIME);
            String expireTime = loopMapObject.optString(CommonData.JSON_KEY_ENDTIME);
            String action = loopMapObject.optString(CommonData.JSON_KEY_SWIPEACTION);

            long rowid = add(UUID.randomUUID().toString(),type,name,cardId,startDate,expireDate,startTime,expireTime,action);
            DbCommonUtil.putErrorCodeFromOperate(rowid, loopMapObject);
        }
    }

    public void cardUpdate(JSONObject jsonObject)  throws JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonData.JSON_UUID_KEY);
            String type = loopMapObject.optString(CommonData.JSON_KEY_CARDTYPE);
            String name = loopMapObject.optString(CommonData.JSON_KEY_NAME);
            String cardId = loopMapObject.optString(CommonData.JSON_KEY_CARDID);
            String startDate = loopMapObject.optString(CommonData.JSON_KEY_STARTDATE);
            String expireDate = loopMapObject.optString(CommonData.JSON_KEY_ENDDATE);
            String startTime = loopMapObject.optString(CommonData.JSON_KEY_STARTTIME);
            String expireTime = loopMapObject.optString(CommonData.JSON_KEY_ENDTIME);
            String action = loopMapObject.optString(CommonData.JSON_KEY_SWIPEACTION);
            IpDoorCard loop = getByUuid(uuid);
            loop.mType = type;
            loop.mName = name;
            loop.mCardNo = cardId;
            loop.mStartDate = startDate;
            loop.mExpireDate = expireDate;
            loop.mStartTime = startTime;
            loop.mExpireTime = expireTime;
            loop.mAction = action;
            long num = updateByUuid(uuid,loop);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
        }
    }

    public void cardDelete(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray(CommonData.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonData.JSON_UUID_KEY);
            long num = deleteByUuid(uuid);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
        }
    }
}
