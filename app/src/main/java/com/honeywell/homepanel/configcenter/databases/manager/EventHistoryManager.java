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
import com.honeywell.homepanel.configcenter.databases.domain.EventHistory;
import com.honeywell.homepanel.configcenter.databases.domain.IpDoorCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H135901 on 3/16/2017.
 */

public class EventHistoryManager {
    private Context mContext = null;
    private static final String TAG  = ConfigService.TAG;
    private static EventHistoryManager instance  = null;
    private ConfigDatabaseHelper dbHelper = null;

    public static synchronized EventHistoryManager getInstance(Context context) {
        if(instance == null){
            instance = new EventHistoryManager(context);
        }
        return instance;
    }
    private EventHistoryManager(Context context) {
        mContext = context;
        dbHelper= ConfigDatabaseHelper.getInstance(mContext);
    }

    public synchronized long add(String uuid,String time,String type,String cardNo,String cardEvent,
                                 String imagePath,String videoPath,int read, int uploadams, int uploadcloud,
                                 int roleid){
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_UUID, uuid)
                .put(ConfigConstant.COLUMN_TIME, time)
                .put(ConfigConstant.COLUMN_TYPE, type)
                .put(ConfigConstant.COLUMN_CARDNO, cardNo)
                .put(ConfigConstant.COLUMN_CARDEVENT,cardEvent)
                .put(ConfigConstant.COLUMN_IMAGEPATH,imagePath)
                .put(ConfigConstant.COLUMN_VIDEOPATH,videoPath)
                .put(ConfigConstant.COLUMN_READ,read)
                .put(ConfigConstant.COLUMN_UPLOADAMS,uploadams)
                .put(ConfigConstant.COLUMN_UPLOADCLOUD,uploadcloud)
                .put(ConfigConstant.COLUMN_ROLEID,roleid).getValues();

       if(DbCommonUtil.getCount(dbHelper.getWritableDatabase(),
               ConfigConstant.TABLE_EVENTHISTORY) >= CommonData.CALL_HISTORY_MAX_COUNT){
           long _id = DbCommonUtil.getFirstRecord(dbHelper,ConfigConstant.TABLE_EVENTHISTORY);
           Log.d(TAG, "add: _id:"+_id);
           if(_id > 0){
                deleteFirstHistoryImage(_id);
                DbCommonUtil.deleteByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_EVENTHISTORY,_id);
           }
       }
        return DbCommonUtil.add(mContext,dbHelper,ConfigConstant.TABLE_EVENTHISTORY,values);
    }

    private void deleteFirstHistoryImage(long id) {
        EventHistory history = getByPrimaryId(id);
        if(null != history){
            CommonUtils.deleteOneFile(history.mImagePath);
            CommonUtils.deleteOneFile(history.mVideoPath);
        }
    }

    public synchronized int deleteByUuid(String uuid) {
        return DbCommonUtil.deleteByStringFieldType(mContext,dbHelper,ConfigConstant.TABLE_EVENTHISTORY,ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized int deleteByPrimaryId(long primaryId) {
        return DbCommonUtil.deleteByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_EVENTHISTORY,primaryId);
    }

    private ContentValues putUpDateValues(EventHistory device){
        ContentValues values = new ContentValues();
        if(null == device){
            return  values;
        }
       /* if(!TextUtils.isEmpty(device.mTime)){
            values.put(ConfigConstant.COLUMN_TIME, device.mTime);
        }
        if(!TextUtils.isEmpty(device.mCardEvent)){
            values.put(ConfigConstant.COLUMN_CARDEVENT, device.mCardEvent);
        }*/
        if(!TextUtils.isEmpty(device.mImagePath)){
            values.put(ConfigConstant.COLUMN_IMAGEPATH, device.mImagePath);
        }
        if(!TextUtils.isEmpty(device.mVideoPath)){
            values.put(ConfigConstant.COLUMN_VIDEOPATH, device.mVideoPath);
        }
        if(!TextUtils.isEmpty(device.mType)){
            values.put(ConfigConstant.COLUMN_TYPE, device.mType);
        }
        if(device.mRead >= 0){
            values.put(ConfigConstant.COLUMN_READ, device.mRead);
        }
        return values;
    }

    public synchronized int updateByPrimaryId(long primaryId, EventHistory device) {
        if(null == device || primaryId <= 0){
            return -1;
        }
        return DbCommonUtil.updateByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_EVENTHISTORY,putUpDateValues(device),primaryId);
    }

    public synchronized int updateByUuid(String  uuid, EventHistory device) {
        if(null == device){
            return -1;
        }
        return  DbCommonUtil.updateByStringFiled(mContext,dbHelper,ConfigConstant.TABLE_EVENTHISTORY,putUpDateValues(device),ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized EventHistory getByPrimaryId(long primaryId) {
        if(primaryId <= 0){
            return  null;
        }
        Cursor cursor = DbCommonUtil.getByPrimaryId(dbHelper,ConfigConstant.TABLE_EVENTHISTORY,primaryId);
        EventHistory device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized int getEventCountByStatus(int status) {
        Cursor cursor = DbCommonUtil.getByIntField(dbHelper,ConfigConstant.TABLE_EVENTHISTORY,ConfigConstant.COLUMN_READ,status);
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

    public synchronized EventHistory getByUuid(String uuid) {
        if(TextUtils.isEmpty(uuid)){
            return null;
        }
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_EVENTHISTORY,ConfigConstant.COLUMN_UUID,uuid);
        EventHistory device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized List<EventHistory> getEventHistoryAllList() {
        List<EventHistory> EventHistorys = null;
        Cursor cursor = DbCommonUtil.getAll(dbHelper,ConfigConstant.TABLE_EVENTHISTORY, "desc");
        while(cursor.moveToNext()){
            if(null == EventHistorys){
                EventHistorys = new ArrayList<EventHistory>();
            }
            EventHistory device = fillDefault(cursor);
            EventHistorys.add(device);
        }
        cursor.close();
        return EventHistorys;
    }

    private EventHistory fillDefault(Cursor cursor) {
        if(null == cursor){
            return null;
        }
        EventHistory device = new EventHistory();
        device.mId = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
        device.mUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_UUID));
        device.mTime = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_TIME));
        device.mType = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_TYPE));
        device.mCardNo = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_CARDNO));
        device.mCardEvent = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_CARDEVENT));
        device.mImagePath = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_IMAGEPATH));
        device.mVideoPath = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_VIDEOPATH));
        device.mRead = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_READ));
        device.mUploadAms = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_UPLOADAMS));
        device.mUploadCloud = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_UPLOADCLOUD));
        device.mRoleID = cursor.getInt(cursor.getColumnIndex(ConfigConstant.COLUMN_ROLEID));
        return device;
    }

    public void notificationEventGet(JSONObject jsonObject)throws JSONException{
        List<EventHistory>lists = getEventHistoryAllList();
        if(null == lists){
            return;
        }
        String dataStatus = jsonObject.optString(CommonData.JSON_KEY_DATASTATUS);
        int start = Integer.valueOf(jsonObject.optString(CommonData.JSON_KEY_START));
        int count = Integer.valueOf(jsonObject.optString(CommonData.JSON_KEY_COUNT));
        JSONArray loopMapArray = new JSONArray();
        int index = 0;
        for (int i = 0; i < lists.size(); i++) {
            EventHistory loop = lists.get(i);
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
        jsonObject.put(CommonJson.JSON_LOOPMAP_KEY,loopMapArray);
        jsonObject.put(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_OK);
    }

    private void loopToJson(JSONObject loopMapObject, EventHistory loop) throws JSONException {
        loopMapObject.put(CommonJson.JSON_UUID_KEY,loop.mUuid);
        loopMapObject.put(CommonData.JSON_KEY_EVENTTYPE,loop.mType);
        loopMapObject.put(CommonData.JSON_KEY_TIME,loop.mTime);
        loopMapObject.put(CommonData.JSON_KEY_IMAGENAME,loop.mImagePath);
        loopMapObject.put(CommonData.JSON_KEY_VIDEONAME,loop.mVideoPath);
        loopMapObject.put(CommonData.JSON_KEY_DATASTATUS,DbCommonUtil.transferReadIntToString(loop.mRead));
        loopMapObject.put(CommonData.JSON_KEY_CARDID,loop.mCardNo);
        loopMapObject.put(CommonData.JSON_KEY_SWIPEACTION,loop.mCardEvent);
        loopMapObject.put(CommonData.JSON_KEY_ROLEID, loop.mRoleID);
        if(!TextUtils.isEmpty(loop.mCardNo)){
           IpDoorCard card = IpDoorCardanager.getInstance(mContext).getByCardNo(loop.mCardNo);
            if(null != card && !TextUtils.isEmpty(card.mType)){
                loopMapObject.put(CommonData.JSON_KEY_CARDTYPE,card.mType);
            }
            else{
                loopMapObject.put(CommonData.JSON_KEY_CARDTYPE,CommonData.CARD_PERMANENT);
            }

            if(null != card && !TextUtils.isEmpty(card.mName)){
                loopMapObject.put(CommonData.JSON_KEY_NAME,card.mName);
            }
            else{
                loopMapObject.put(CommonData.JSON_KEY_NAME,"unknown(deleted)");
            }
        }
        loopMapObject.put(CommonJson.JSON_ERRORCODE_KEY,CommonJson.JSON_ERRORCODE_VALUE_OK);
    }

    public void notificationEventAdd(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonJson.JSON_UUID_KEY);
            String eventType = loopMapObject.optString(CommonData.JSON_KEY_EVENTTYPE);
            String time = loopMapObject.optString(CommonData.JSON_KEY_TIME);
            String imgname = loopMapObject.optString(CommonData.JSON_KEY_IMAGENAME);
            String videoname = loopMapObject.optString(CommonData.JSON_KEY_VIDEONAME);
            String datastatus  = loopMapObject.optString(CommonData.JSON_KEY_DATASTATUS);
            String cardid = loopMapObject.optString(CommonData.JSON_KEY_CARDID);
            String swipeaction = loopMapObject.optString(CommonData.JSON_KEY_SWIPEACTION);
            int uploadams = loopMapObject.optInt(CommonData.JSON_KEY_UPLOADAMS);
            int uploadcloud = loopMapObject.optInt(CommonData.JSON_KEY_UPLOADCLOUD);
            int roleid = loopMapObject.optInt(CommonData.JSON_KEY_ROLEID);
            long rowId = add(uuid,time,eventType, cardid, swipeaction, imgname, videoname,
                    DbCommonUtil.transferReadStringToInt(datastatus), uploadams, uploadcloud, roleid);
            DbCommonUtil.putErrorCodeFromOperate(rowId,loopMapObject);
        }
    }

    public void notificationEventUpdate(JSONObject jsonObject) throws  JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonJson.JSON_UUID_KEY);
            String datastatus  = loopMapObject.optString(CommonData.JSON_KEY_DATASTATUS);
            EventHistory loop = getByUuid(uuid);
            if(null == loop){
                continue;
            }
            if(loopMapObject.has(CommonData.JSON_KEY_DATASTATUS)){
                loop.mRead = DbCommonUtil.transferReadStringToInt(datastatus);
            }
            if(loopMapObject.has(CommonData.JSON_KEY_IMAGENAME)){
                loop.mImagePath = loopMapObject.getString(CommonData.JSON_KEY_IMAGENAME);
            }
            if(loopMapObject.has(CommonData.JSON_KEY_VIDEONAME)){
                loop.mVideoPath = loopMapObject.getString(CommonData.JSON_KEY_VIDEONAME);
            }
            if(loopMapObject.has(CommonData.JSON_KEY_EVENTTYPE)){
                loop.mType = loopMapObject.getString(CommonData.JSON_KEY_EVENTTYPE);
            }
            long num = updateByUuid(uuid,loop);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
        }
    }

    public void notificationEventDelete(JSONObject jsonObject) throws  JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonJson.JSON_UUID_KEY);
            long num = deleteByUuid(uuid);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
        }
    }

    public void eventCountGet(JSONObject jsonObject) throws  JSONException {
        String statusStr = jsonObject.getString(CommonData.JSON_KEY_DATASTATUS);
        int dataStatus = DbCommonUtil.transferReadStringToInt(statusStr);
        int count = getEventCountByStatus(dataStatus);
        jsonObject.put(CommonData.JSON_KEY_COUNT,""+count);
        jsonObject.put(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_OK);
    }
}
