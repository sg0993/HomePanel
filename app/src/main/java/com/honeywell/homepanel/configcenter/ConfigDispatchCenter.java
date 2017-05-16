package com.honeywell.homepanel.configcenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.configcenter.databases.domain.EventDataElement;
import com.honeywell.homepanel.configcenter.databases.manager.AdapterManager;
import com.honeywell.homepanel.configcenter.databases.manager.AlarmHistoryManager;
import com.honeywell.homepanel.configcenter.databases.manager.CommonlDeviceManager;
import com.honeywell.homepanel.configcenter.databases.manager.EventHistoryManager;
import com.honeywell.homepanel.configcenter.databases.manager.IpDoorCardanager;
import com.honeywell.homepanel.configcenter.databases.manager.IpcLoopManager;
import com.honeywell.homepanel.configcenter.databases.manager.LocalDeviceManager;
import com.honeywell.homepanel.configcenter.databases.manager.PeripheralDeviceManager;
import com.honeywell.homepanel.configcenter.databases.manager.RelayLoopManager;
import com.honeywell.homepanel.configcenter.databases.manager.ScenarioLoopManager;
import com.honeywell.homepanel.configcenter.databases.manager.SpeedDialManager;
import com.honeywell.homepanel.configcenter.databases.manager.VoiceMessageManager;
import com.honeywell.homepanel.configcenter.databases.manager.ZoneLoopManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by H135901 on 3/13/2017.
 */
public class ConfigDispatchCenter implements Runnable{
    private Context mContext = null;
    private volatile boolean mBRuning = true;
    public static final int mMaxRecvQueSize = 100; // 最大缓存100个配置消息(JSON)
    private static ConfigDispatchCenter mInstance = null;
    public static final String TAG = ConfigService.TAG;
    public BlockingQueue<EventDataElement> mRecvDataQue = new LinkedBlockingQueue<EventDataElement>(mMaxRecvQueSize);

    public static synchronized ConfigDispatchCenter getInstance(Context context) {
        if(mInstance == null){
            mInstance = new ConfigDispatchCenter(context);
        }
        return mInstance;
    }

    public static synchronized ConfigDispatchCenter getInstance() {
        return mInstance;
    }

    private ConfigDispatchCenter(Context context) {
        mContext = context;
    }

    @Override
    public void run() {
        while (mBRuning){
            try {
                EventDataElement dataElement = mRecvDataQue.take();
                JSONObject obj = new JSONObject(new String(dataElement.getDataValue()));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void stopThd() {
        mBRuning = false;
    }

    public byte[] getFromDbManager(JSONObject jsonObject) throws JSONException {
        jsonObject.put(CommonJson.JSON_ACTION_KEY, CommonJson.JSON_ACTION_VALUE_RESPONSE);
        String subAction = jsonObject.optString(CommonJson.JSON_SUBACTION_KEY);

        if(CommonJson.JSON_SUBACTION_VALUE_GETSCENARIOLIST.equals(subAction)){
            CommonlDeviceManager.getInstance(mContext).getScenarioList(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_GETSCENARIOCONFIG.equals(subAction)){
            ScenarioLoopManager.getInstance(mContext).getScenarioConfig(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_EVENTCOUNTGET.equals(subAction)){
            EventHistoryManager.getInstance(mContext).eventCountGet(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONEVENTGET.equals(subAction)){
            EventHistoryManager.getInstance(mContext).notificationEventGet(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_ALARMCOUNTGET.equals(subAction)){
            AlarmHistoryManager.getInstance(mContext).notificationAlarmCountGet(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONALARMGET.equals(subAction)){
            AlarmHistoryManager.getInstance(mContext).notificationAlarmGet(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_VOICEMSGCOUNTGET.equals(subAction)){
            VoiceMessageManager.getInstance(mContext).notificationVoiceMsgCountGet(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONVOICEMSGGET.equals(subAction)){
            VoiceMessageManager.getInstance(mContext).notificationMessageGet(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_SPEEDDIALGET.equals(subAction)){
            SpeedDialManager.getInstance(mContext).speeddialGet(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_CARDGET.equals(subAction)){
            IpDoorCardanager.getInstance(mContext).cardGet(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_IPCGET.equals(subAction)){
            IpcLoopManager.getInstance(mContext).ipcGet(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_LOCALZONEGET.equals(subAction)){
            //ZoneLoopManager.getInstance(mContext).localZoneGet(jsonObject);
            LocalDeviceManager.getInstance(mContext).getLocalZoneDetails(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_EXTENSIONMODULEGET.equals(subAction)){
            PeripheralDeviceManager.getInstance(mContext).getExtensionModuleList(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_EXTENSIONZONEGET.equals(subAction)){
            ZoneLoopManager.getInstance(mContext).extensionZoneGet(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_EXTENSIONRELAYGET.equals(subAction)){
            RelayLoopManager.getInstance(mContext).extensionRelayGet(jsonObject);
        }
        else if (CommonJson.JSON_SUBACTION_VALUE_GETALLDEVICEDETAILS.equals(subAction)){
            getAllDeviceDetailsInfo(jsonObject);
        }
        else if (CommonJson.JSON_SUBACTION_VALUE_GETALLCOMMONDEVICES.equals(subAction)) {
            CommonlDeviceManager.getInstance(mContext).getCommonDeviceList(jsonObject);
        }
        else if (CommonJson.JSON_SUBACTION_VALUE_GETDEVICEADAPTER.equals(subAction)) {
            AdapterManager.getInstance(mContext).getAvaliableAdapters(jsonObject);
        }

        Log.d(TAG, "getFromDbManager: json response:"+jsonObject.toString());

        return jsonObject.toString().getBytes();
    }

    public byte[] setFromDbManager(JSONObject jsonObject) throws JSONException {
        jsonObject.put(CommonJson.JSON_ACTION_KEY, CommonJson.JSON_ACTION_VALUE_RESPONSE);
        String subAction = jsonObject.optString(CommonJson.JSON_SUBACTION_KEY);
        if(CommonJson.JSON_SUBACTION_VALUE_SETSCENARIOCONFIG.equals(subAction)){
            ScenarioLoopManager.getInstance(mContext).setScenarioConfig(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONEVENTADD.equals(subAction)){
            EventHistoryManager.getInstance(mContext).notificationEventAdd(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONEVENTUPDATE.equals(subAction)){
            EventHistoryManager.getInstance(mContext).notificationEventUpdate(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONEVENTDELETE.equals(subAction)){
            EventHistoryManager.getInstance(mContext).notificationEventDelete(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONALARMADD.equals(subAction)){
            AlarmHistoryManager.getInstance(mContext).notificationAlarmAdd(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONALARMUPDATE.equals(subAction)){
            AlarmHistoryManager.getInstance(mContext).notificationAlarmUpdate(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONALARMDELETE.equals(subAction)){
            AlarmHistoryManager.getInstance(mContext).notificationAlarmDelete(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONVOICEMSGADD.equals(subAction)){
            VoiceMessageManager.getInstance(mContext).notificationMessageAdd(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONVOICEMSGUPDATE.equals(subAction)){
            VoiceMessageManager.getInstance(mContext).notificationMessageUpdate(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONVOICEMSGDELETE.equals(subAction)){
            VoiceMessageManager.getInstance(mContext).notificationMessageDelete(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_SPEEDDIALADD.equals(subAction)){
            SpeedDialManager.getInstance(mContext).speeddialAdd(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_SPEEDDIALUPDATE.equals(subAction)){
            SpeedDialManager.getInstance(mContext).speeddialUpdate(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_SPEEDDIALDELETE.equals(subAction)){
            SpeedDialManager.getInstance(mContext).speeddialDelete(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_CARDADD.equals(subAction)){
            IpDoorCardanager.getInstance(mContext).cardAdd(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_CARDUPDATE.equals(subAction)){
            IpDoorCardanager.getInstance(mContext).cardUpdate(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_CARDDELEETE.equals(subAction)){
            IpDoorCardanager.getInstance(mContext).cardDelete(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_IPCADD.equals(subAction)){
            IpcLoopManager.getInstance(mContext).ipcAdd(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_IPCUPDATE.equals(subAction)){
            IpcLoopManager.getInstance(mContext).ipcUpdate(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_IPCDELETE.equals(subAction)){
            IpcLoopManager.getInstance(mContext).ipcDelete(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_EXTENSIONZONEUPDATE.equals(subAction)){
            ZoneLoopManager.getInstance(mContext).zoneUpdate(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_LOCALZONEUPUDATE.equals(subAction)) {
            LocalDeviceManager.getInstance(mContext).updateDevice(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_EXTENSIONZONEDELETE.equals(subAction)){
            //ZoneLoopManager.getInstance(mContext).extensionZoneDelete(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_EXTENSIONRELAYUPDATE.equals(subAction)){
            RelayLoopManager.getInstance(mContext).relayUpdate(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_EXTENSIONRELAYDELETE.equals(subAction)){
            //RelayLoopManager.getInstance(mContext).relayDelete(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_EXTENSIONMODULEADD.equals(subAction)){
            PeripheralDeviceManager.getInstance(mContext).extensionModuleAdd(jsonObject);
        }
        else if(CommonJson.JSON_SUBACTION_VALUE_EXTENSIONMODULEDELETE.equals(subAction)){
            PeripheralDeviceManager.getInstance(mContext).extensionModuleDelete(jsonObject);
        }
        else if (CommonJson.JSON_SUBACTION_VALUE_UPDATEDEVICEADAPTER.equals(subAction)) {
            AdapterManager.getInstance(mContext).updateSystemAvaliableAdapters(jsonObject);
        }

        Log.d(TAG, "setFromDbManager: json response:"+ jsonObject.toString());
        return jsonObject.toString().getBytes();
    }

    private void getAllDeviceDetailsInfo(JSONObject jsonObject) throws JSONException {
        Log.d(TAG, "getAllDeviceDetailsInfo...");

        // get relay loop details info
        RelayLoopManager.getInstance(mContext).getDeviceLoopDetailsInfo(jsonObject);

        // get zone loop details info
        ZoneLoopManager.getInstance(mContext).getDeviceLoopDetailsInfo(jsonObject);

        // get local device details
        LocalDeviceManager.getInstance(mContext).getDeviceLoopDetailsInfo(jsonObject);
    }

    public void broadcastConfigurationUpdated(String category, String content) {
        Intent intent = new Intent();
        intent.setAction(CommonData.INTENT_ACTION_CONFIGINFO_CHANGED);
        intent.putExtra(CommonData.JSON_CONFIGDATA_CATEGORY_KEY, category);
        intent.putExtra(CommonData.JSON_CONFIGDATA_CONFIGNAME_KEY, content);

        mContext.sendBroadcast(intent);

        Log.d(TAG, "broadcastConfigurationUpdated, content:" + content);
    }
}
