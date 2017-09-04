package com.honeywell.homepanel.configcenter.databases.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.utils.CommonUtils;
import com.honeywell.homepanel.configcenter.ConfigService;
import com.honeywell.homepanel.configcenter.databases.ConfigDatabaseHelper;
import com.honeywell.homepanel.configcenter.databases.constant.ConfigConstant;
import com.honeywell.homepanel.configcenter.databases.domain.IpcLoop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H135901 on 3/15/2017.
 */

public class IpcLoopManager {
    private Context mContext = null;
    private static final String TAG  = ConfigService.TAG;
    private static IpcLoopManager instance  = null;
    private ConfigDatabaseHelper dbHelper = null;

    public static synchronized IpcLoopManager getInstance(Context context) {
        if(instance == null){
            instance = new IpcLoopManager(context);
        }
        return instance;
    }
    private IpcLoopManager(Context context) {
        mContext = context;
        dbHelper= ConfigDatabaseHelper.getInstance(mContext);
    }

    public synchronized long add(String uuid,String name,String ipAddr,String user,String password){
        ContentValues values = new ContentValuesFactory()
                .put(ConfigConstant.COLUMN_UUID, uuid)
                .put(ConfigConstant.COLUMN_NAME, name)
                .put(ConfigConstant.COLUMN_IPADDR, ipAddr)
                .put(ConfigConstant.COLUMN_USER, user)
                .put(ConfigConstant.COLUMN_PASSWORD,password).getValues();
        return DbCommonUtil.add(mContext,dbHelper,ConfigConstant.TABLE_IPC,values);
    }

    public synchronized int deleteByUuid(String uuid) {
        return DbCommonUtil.deleteByStringFieldType(mContext,dbHelper,ConfigConstant.TABLE_IPC,ConfigConstant.COLUMN_UUID, uuid);
    }

    public synchronized int deleteByPrimaryId(long primaryId) {
        return DbCommonUtil.deleteByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_IPC,primaryId);
    }

    private ContentValues putUpDateValues(IpcLoop device){
        ContentValues values = new ContentValues();
        if(null == device){
            return  values;
        }
        if(!TextUtils.isEmpty(device.mName)){
            values.put(ConfigConstant.COLUMN_NAME, device.mName);
        }
        if(!TextUtils.isEmpty(device.mUser)){
            values.put(ConfigConstant.COLUMN_USER, device.mUser);
        }
        if(!TextUtils.isEmpty(device.mPwd)){
            values.put(ConfigConstant.COLUMN_PASSWORD, device.mPwd);
        }
        if(!TextUtils.isEmpty(device.mIpAddr)){
            values.put(ConfigConstant.COLUMN_IPADDR, device.mIpAddr);
        }
        return values;
    }
    public synchronized int updateByPrimaryId(long primaryId, IpcLoop device) {
        if(null == device || primaryId <= 0){
            return -1;
        }
        return DbCommonUtil.updateByPrimaryId(mContext,dbHelper,ConfigConstant.TABLE_IPC,putUpDateValues(device),primaryId);
    }
    public synchronized int updateByUuid(String  uuid, IpcLoop device) {
        if(null == device){
            return -1;
        }
        return  DbCommonUtil.updateByStringFiled(mContext,dbHelper,ConfigConstant.TABLE_IPC,putUpDateValues(device),ConfigConstant.COLUMN_UUID,uuid);
    }

    public synchronized IpcLoop getByPrimaryId(long primaryId) {
        if(primaryId <= 0){
            return  null;
        }
        Cursor cursor = DbCommonUtil.getByPrimaryId(dbHelper,ConfigConstant.TABLE_IPC,primaryId);
        IpcLoop device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized IpcLoop getByUuid(String uuid) {
        if(TextUtils.isEmpty(uuid)){
            return null;
        }
        Cursor cursor = DbCommonUtil.getByStringField(dbHelper,ConfigConstant.TABLE_IPC,ConfigConstant.COLUMN_UUID,uuid);
        IpcLoop device = null;
        while(cursor.moveToNext()){
            device = fillDefault(cursor);
        }
        cursor.close();
        return device;
    }

    public synchronized List<IpcLoop> getIpcLoopAllList() {
        List<IpcLoop> IpcLoops = null;
        Cursor cursor = DbCommonUtil.getAll(dbHelper,ConfigConstant.TABLE_IPC);
        while(cursor.moveToNext()){
            if(null == IpcLoops){
                IpcLoops = new ArrayList<IpcLoop>();
            }
            IpcLoop device = fillDefault(cursor);
            IpcLoops.add(device);
        }
        cursor.close();
        return IpcLoops;
    }

    private IpcLoop fillDefault(Cursor cursor) {
        if(null == cursor){
            return null;
        }
        IpcLoop device = new IpcLoop();
        device.mUuid = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_UUID));
        device.mName = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_NAME));
        device.mId = cursor.getLong(cursor.getColumnIndex(ConfigConstant.COLUMN_ID));
        device.mIpAddr = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_IPADDR));
        device.mUser = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_USER));
        device.mPwd = cursor.getString(cursor.getColumnIndex(ConfigConstant.COLUMN_PASSWORD));
        return device;
    }

    public void ipcGet(JSONObject jsonObject) throws JSONException {
        List<IpcLoop>lists = getIpcLoopAllList();
        if(null == lists){
            return;
        }
        JSONArray loopMapArray = new JSONArray();
        for (int i = 0; i < lists.size(); i++) {
            IpcLoop loop = lists.get(i);
            JSONObject loopMapObject = new JSONObject();
            loopToJson(loopMapObject,loop);
            loopMapArray.put(loopMapObject);
        }
        jsonObject.put(CommonJson.JSON_LOOPMAP_KEY,loopMapArray);
        jsonObject.put(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_OK);
    }

    private void loopToJson(JSONObject loopMapObject, IpcLoop loop) throws JSONException {
        loopMapObject.put(CommonJson.JSON_UUID_KEY,loop.mUuid);
        loopMapObject.put(CommonData.JSON_IP_KEY,loop.mIpAddr);
        loopMapObject.put(CommonData.JSON_USERNAME_KEY,loop.mUser);
        loopMapObject.put(CommonJson.JSON_PASSWORD_KEY,loop.mPwd);
        loopMapObject.put(CommonData.JSON_KEY_NAME,loop.mName);
        loopMapObject.put(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_OK);
    }

    public void ipcAdd(JSONObject jsonObject)  throws JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String ip = loopMapObject.optString(CommonData.JSON_IP_KEY);
            String userName = loopMapObject.optString(CommonData.JSON_USERNAME_KEY);
            String pwd = loopMapObject.optString(CommonJson.JSON_PASSWORD_KEY);
            String name = loopMapObject.optString(CommonJson.JSON_ALIASNAME_KEY);
            String uuid = CommonUtils.generateCommonEventUuid();
            long rowid = add(uuid,name,ip,userName,pwd);
            if(rowid > 0){
                CommonDeviceManager.getInstance(mContext).add(uuid,name,CommonData.COMMONDEVICE_TYPE_IPC, 1);
            }
            DbCommonUtil.putErrorCodeFromOperate(rowid, loopMapObject);
        }
    }

    public void ipcUpdate(JSONObject jsonObject)  throws JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonJson.JSON_UUID_KEY);
            String name  = loopMapObject.optString(CommonJson.JSON_ALIASNAME_KEY);
            String user  = loopMapObject.optString(CommonData.JSON_USERNAME_KEY);
            String pwd  = loopMapObject.optString(CommonJson.JSON_PASSWORD_KEY);
            IpcLoop loop = getByUuid(uuid);
            if(null == loop){
                continue;
            }
            if(loopMapObject.has(CommonJson.JSON_ALIASNAME_KEY)){
                loop.mName = name;
                DbCommonUtil.updateCommonName(mContext,uuid,name, 1);
            }
            if(loopMapObject.has(CommonData.JSON_USERNAME_KEY)){
                loop.mUser = user;
            }
            if(loopMapObject.has(CommonJson.JSON_PASSWORD_KEY)){
                loop.mPwd = pwd;
            }
            if(loopMapObject.has(CommonData.JSON_IP_KEY)){
                loop.mIpAddr = loopMapObject.getString(CommonData.JSON_IP_KEY);
            }
            long num = updateByUuid(uuid,loop);

            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
        }
    }

    public void ipcDelete(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray(CommonJson.JSON_LOOPMAP_KEY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject loopMapObject = jsonArray.getJSONObject(i);
            String uuid = loopMapObject.optString(CommonJson.JSON_UUID_KEY);
            long num = deleteByUuid(uuid);
            DbCommonUtil.putErrorCodeFromOperate(num,loopMapObject);
            if(num > 0) {
                CommonDeviceManager.getInstance(mContext).deleteByUuid(uuid);
            }
        }
    }
}
