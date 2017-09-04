package com.honeywell.homepanel.ui.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.honeywell.homepanel.IConfigService;
import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.IConfigServiceManageUtil;
import com.honeywell.homepanel.Utils.LogMgr;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUICall;
import com.honeywell.homepanel.common.utils.CommonUtils;
import com.honeywell.homepanel.ui.RingFile.LogoFileCopy;
import com.honeywell.homepanel.ui.RingFile.RingFileCopy;
import com.honeywell.homepanel.ui.domain.EngineerModeBrush;
import com.honeywell.homepanel.ui.domain.NotificationStatisticInfo;
import com.honeywell.homepanel.ui.domain.TopStaus;
import com.honeywell.homepanel.ui.domain.UIBaseCallInfo;
import com.honeywell.homepanel.ui.fragment.HomeFragment;
import com.honeywell.homepanel.ui.services.WidgetInfoService;
import com.honeywell.homepanel.ui.uicomponent.UIEventBusCommonCmd;
import com.honeywell.homepanel.watchdog.WatchDogService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.honeywell.homepanel.common.CommonJson.JSON_CALLTYPE_VALUE_OFFICE;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private View mCenterView = null;
    private View content_scrolling = null;
    private View mTopView = null;
    //Add by xc
    public static UIBaseCallInfo CallBaseInfo = new UIBaseCallInfo();
    private ServiceConnection mIConfigServiceConnect = new DBOperationService();
    public IConfigService mIConfigService = null;
    public static int mHomePanelType = CommonData.HOMEPANEL_TYPE_MAIN;
    public static String mDongHao = "";
    public static String mSubPPhoneId = CommonData.CALL_PHONE_MAIN;
    public static final String mClientCert = "/data/security/ClientCert.pem";//"/sdcard/Download/ClientCert.pem";
    private static final int FTRESULTCODE = 126;
    private myBroadcastReceiver myBroadcastReceiver;
    private Intent mNotificationServiceIntent;

    public static int mMessageFragPage = CommonData.MESSAGE_SELECT_EVENT;

    private boolean checkAndStartFTApplication() {
        Log.d(TAG, "checkAndStartFTApplication: ");
        File file = new File(mClientCert);
        if (file.exists()) {
            /*String macAddr = getMacAddressFromCert(file);
            String curMac = EthernetManagerUtil
                    .getInstance()
                    .getMACAddress();
            if (macAddr != null && !curMac.equals(macAddr)) {
                Ifconfig.setEthernetMacAddress(macAddr);
            }*/
            return false;
        }

        try {
            getPackageManager().getPackageInfo("com.honeywell.finaltest", PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.setPackage("com.honeywell.finaltest");
        startActivityForResult(intent, FTRESULTCODE);
        Log.d(TAG, "checkAndStartFTApplication: Start FinalTest");
        return true;
    }

 /*   private String getMacAddressFromCert(File file) {
        /// read file
        InputStreamReader inputStream = null;
        try {
            inputStream = new InputStreamReader(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inputStream == null) {
            Log.e(TAG, "getMacAddressFromCert: There is not exist cert file.");
            return null;
        }
        /// read cert
        PEMReader pemReader = new PEMReader(inputStream);
        X509Certificate x509Certificate = null;
        try {
            x509Certificate = (X509Certificate) pemReader.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                pemReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (x509Certificate == null) {
            Log.e(TAG, "getMacAddressFromCert: read x509Certificate failed.");
            return null;
        }
        ///
        String macAddress = null;
        String[] principal = x509Certificate.getSubjectX500Principal().getName().split(",");
        for (String item : principal) {
            String[] itemTV = item.split("=");
            if (itemTV == null || itemTV.length != 2) {
                continue;
            }

            if (itemTV[0].trim().equals("CN")) {
                String value = itemTV[1].trim();
                if (value != null && value.length() > 12) {
                    macAddress = value.substring(value.length() - 12);
                }
            }
        }
        if (macAddress == null) {
            Log.e(TAG, "getMacAddressFromCert: read mac address failed.");
            return macAddress;
        }
        /// add colon in mac address;
        if (macAddress.indexOf(":") >= 0) {
            Log.d(TAG, "getMacAddressFromCert: mac address is " + macAddress);
            return macAddress;
        }
        macAddress = macAddress.substring(0, 2) + ":"
                + macAddress.substring(2, 4) + ":"
                + macAddress.substring(4, 6) + ":"
                + macAddress.substring(6, 8) + ":"
                + macAddress.substring(8, 10) + ":"
                + macAddress.substring(10, 12);
        Log.d(TAG, "getMacAddressFromCert: mac address is " + macAddress);

        return macAddress;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FTRESULTCODE) {
            startService(new Intent(this, WatchDogService.class));
            //bind database config service
            CommonUtils.startAndBindService(this, CommonData.ACTION_CONFIG_SERVICE, mIConfigServiceConnect);// using getContext()???
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // start watch dog
        if (!checkAndStartFTApplication()) {
            startService(new Intent(this, WatchDogService.class));
            //bind database config service
            IConfigServiceManageUtil.getInstance(this);
            CommonUtils.startAndBindService(this, CommonData.ACTION_CONFIG_SERVICE, mIConfigServiceConnect);// using getContext()???
        }

      /*  mTopView = findViewById(R.id.top_status);//for test
        mTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/
        mCenterView = findViewById(R.id.main_frameLayout);
        content_scrolling = findViewById(R.id.content_scrolling);

        LogMgr.e("MainActivity-->onCreate()");

        myBroadcastReceiver = new myBroadcastReceiver();
        IntentFilter ift = new IntentFilter();
        ift.addAction(Intent.ACTION_MEDIA_MOUNTED);
        ift.addDataScheme("file");
        registerReceiver(myBroadcastReceiver, ift);

        new Thread(new Runnable() {
            @Override
            public void run() {
                RingFileCopy.getInstance().CopyRingFileFromCard();
                LogoFileCopy.getInstance().CopyLogoFileFromCard();
            }
        }).start();
        DisplayManagerGlobal.getInstance().requestScreenOff(false);
    }


    @Override
    protected void onStart() {
        super.onStart();
//        if (!isServiceWork()) {
//            if (mNotificationServiceIntent == null) {
//                mNotificationServiceIntent = new Intent(this, NotificationService.class);
//                startService(mNotificationServiceIntent);
//            }
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setCenterViewBackground(int curScenario) {
//        if (curScenario == CommonData.SCENARIO_HOME
//                || curScenario == CommonData.SCENARIO_WAKEUP
//                || curScenario == CommonData.SCENARIO_SLEEP) {
//            if (mCenterView != null) {
//                mCenterView.setBackgroundColor(getResources().getColor(R.color.home_text_transparent));
//                TopStaus.getInstance(this).setArmStatus(CommonData.ARMSTATUS_DISARM);
//            }
//        } else if (curScenario == CommonData.SCENARIO_AWAY && mCenterView != null) {
//            TopStaus.getInstance(this).setArmStatus(CommonData.ARMSTATUS_ARM);
//            mCenterView.setBackgroundColor(getResources().getColor(R.color.home_text_transparent));
//        }
    }

    private class myBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RingFileCopy.getInstance().CopyRingFileFromCard();
                        LogoFileCopy.getInstance().CopyLogoFileFromCard();
                    }
                }).start();
                return;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int mCurScenario = TopStaus.getInstance(getApplicationContext()).getCurScenario();
        setCenterViewBackground(mCurScenario);
        Log.d(TAG, "onResume() mCurScenario:" + mCurScenario);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogMgr.e("MainActivity-->onDestroy()");
        unbindService(mIConfigServiceConnect);
        if (mNotificationServiceIntent != null) {
            stopService(mNotificationServiceIntent);
        }
        unregisterReceiver(myBroadcastReceiver);
        MainActivity.mMessageFragPage = CommonData.MESSAGE_SELECT_EVENT;
    }

    @Override
    protected int getContent() {
        return R.layout.layout_home;
    }

    @Override
    protected void initViewAndListener() {
        super.initViewAndListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISCallInMessageEve msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");
        System.out.println("SUISCallInMessageEve1111111");
        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_EVENT)) {
            String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
            String callType = msg.optString(CommonJson.JSON_CALLTYPE_KEY, "");
            String aliasName = msg.optString(CommonJson.JSON_ALIASNAME_KEY, "");
            String videocodectype = msg.optString(CommonJson.JSON_VIDEOCODEC_KEY, "");
            String audiocodectype = msg.optString(CommonJson.JSON_AUDIOCODEC_KEY, "");
            System.out.println("uuid,callType,aliasName," + uuid + callType + aliasName);
            CallBaseInfo.setCallUuid(uuid);
            CallBaseInfo.setmCallAliasName(aliasName);
            CallBaseInfo.setmCallType(callType);
            CallBaseInfo.setmVideoCodecType(videocodectype);
            CallBaseInfo.setmAudioCodecType(audiocodectype);
            System.out.println("SUISCallInMessageEve1111111uuid,callType,aliasName," + uuid + callType + aliasName);
            Intent intent = new Intent(this, CallActivity.class);
            intent.putExtra(CommonData.INTENT_KEY_UNIT, aliasName);
            if (callType.equals(CommonJson.JSON_CALLTYPE_VALUE_NEIGHBOUR)) {
                intent.putExtra(CommonData.INTENT_KEY_CALL_TYPE, CommonData.CALL_INCOMING_NEIGHBOR);
            } else if (callType.equals(CommonJson.JSON_CALLTYPE_VALUE_LOBBY)) {
                intent.putExtra(CommonData.INTENT_KEY_CALL_TYPE, CommonData.CALL_LOBBY_INCOMMING);
            } else if (callType.equals(CommonJson.JSON_CALLTYPE_VALUE_GUARD) || callType.equals(JSON_CALLTYPE_VALUE_OFFICE)) {
                intent.putExtra(CommonData.INTENT_KEY_CALL_TYPE, CommonData.CALL_GUARD_INCOMMING);
            } else if (callType.equals(CommonJson.JSON_CALLTYPE_VALUE_DOORCAMERA)) {
                intent.putExtra(CommonData.INTENT_KEY_CALL_TYPE, CommonData.CALL_IPDC_INCOMING);
            } else if (callType.equals(CommonJson.JSON_CALLTYPE_VALUE_INNER)) {
                intent.putExtra(CommonData.INTENT_KEY_CALL_TYPE, CommonData.CALL_SUBPHONE_INCOMMING);
            }
            intent.putExtra(CommonData.INTENT_KEY_CALLINFO, CallBaseInfo);
            startActivity(intent);
        }
    }

    public List<Map<String, Object>> getEventData(List<Map<String, Object>> list) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CommonJson.JSON_ACTION_KEY, CommonJson.JSON_ACTION_VALUE_REQUEST);
            jsonObject.put(CommonJson.JSON_SUBACTION_KEY, CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONEVENTGET);
            jsonObject.put(CommonData.JSON_KEY_START, "0");
            jsonObject.put(CommonData.JSON_KEY_COUNT, "100");
            jsonObject.put(CommonData.JSON_KEY_DATASTATUS, CommonData.DATASTATUS_UNREAD);
            boolean bUnreadVisitor = false;
            JSONArray jsonArray = CommonUtils.getJsonArrayFromDb(jsonObject, mIConfigService);
            if (null != jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String evT = obj.optString(CommonData.JSON_KEY_EVENTTYPE);
                    String dSta = obj.optString(CommonData.JSON_KEY_DATASTATUS);
                    if (!TextUtils.isEmpty(evT) && (evT.equals(CommonData.JSON_VALUE_VIDEO) || evT.equals(CommonData.JSON_VALUE_VISITOR))
                            && !TextUtils.isEmpty(dSta) && dSta.equals(CommonData.DATASTATUS_UNREAD)) {
                        bUnreadVisitor = true;
                        break;
                    }
                }
            }
            if (bUnreadVisitor) {
                list.add(getHashMap(R.mipmap.home_visitors, getString(R.string.newvisitor)));
            } else list.add(getHashMap(R.mipmap.home_visitors, getString(R.string.novisitor)));

            jsonObject.put(CommonJson.JSON_SUBACTION_KEY, CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONVOICEMSGGET);
            jsonArray = CommonUtils.getJsonArrayFromDb(jsonObject, mIConfigService);
            if (null != jsonArray && jsonArray.length() > 0) {
                list.add(getHashMap(R.mipmap.home_voice, getString(R.string.newvoicemsg)));
            } else list.add(getHashMap(R.mipmap.home_voice, getString(R.string.novoicemsg)));

//            jsonObject.put(CommonJson.JSON_SUBACTION_KEY, CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONALARMGET);
//            jsonArray = CommonUtils.getJsonArrayFromDb(jsonObject, mIConfigService);
//            if (null != jsonArray && jsonArray.length() > 0) {
//                list.add(getHashMap(R.mipmap.info, getString(R.string.newalarmmsg)));
//            } else list.add(getHashMap(R.mipmap.info, getString(R.string.noalarmmsg)));

            if (WidgetInfoService.newNotificationFlag == true) {
                if (!list.contains(getHashMap(R.mipmap.home_property, getString(R.string.newpropertymsg)))) {
                    list.add(getHashMap(R.mipmap.home_property, getString(R.string.newpropertymsg)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, Object> getHashMap(int image, String text) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(HomeFragment.MAP_KEY_IMG, image);
        map.put(HomeFragment.MAP_KEY_TEXT, text);
        return map;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        updateUserActionTime();
        return super.dispatchKeyEvent(event);
    }

    private void updateIndicator() {
        super.updateNewMessageIndicator(1);
    }

    class DBOperationService implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            String serviceClassName = name.getClassName();
            Log.d(TAG, "onServiceConnected: serviceClassName=" + serviceClassName);
            if (serviceClassName.equals(CommonData.ACTION_CONFIG_SERVICE)) {
                mIConfigService = IConfigService.Stub.asInterface(service);
                if (mIConfigService != null) {
                    clearDeviceConnectionStatusCB();
                    brushHomePanelConfig();
                    NotificationStatisticInfo temp = NotificationStatisticInfo.getInstance();
                    temp.getDataCountFromDB(mIConfigService);
                    if (temp.hasUnreadMsg()) {
                        updateIndicator();
                    }

                    HomeFragment homeFragement = (HomeFragment) mFragments.get(CommonData.LEFT_SELECT_HOME);
                    if (null != homeFragement) {
                        homeFragement.eventBrush();
                    }
                }
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            String serviceClassName = name.getClassName();
            if (serviceClassName.equals(CommonData.ACTION_CONFIG_SERVICE)) {
                mIConfigService = null;
            }
        }
    }

    private void clearDeviceConnectionStatusCB() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(CommonJson.JSON_ACTION_KEY, CommonJson.JSON_ACTION_VALUE_REQUEST);
            obj.put(CommonJson.JSON_SUBACTION_KEY, CommonJson.JSON_SUBACTION_VALUE_CLEARCONNECTIONSTATUS);
            mIConfigService.putToDBManager(obj.toString().getBytes());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void brushHomePanelConfig() {
        if (mIConfigService == null) return;
        try {
            mHomePanelType = mIConfigService.getIntMapConfig(CommonData.KEY_HOMEPANELTYPE);
            if (mHomePanelType < 0) {
                mHomePanelType = 0;
            }
            mDongHao = mIConfigService.getStringMapConfig(CommonData.KEY_UNIT);
            if (mHomePanelType == CommonData.HOMEPANEL_TYPE_MAIN) {
                mSubPPhoneId = CommonData.CALL_PHONE_MAIN;
            } else {
                mSubPPhoneId = mIConfigService.getStringMapConfig(CommonData.KEY_SUBPHONEID);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(EngineerModeBrush msg) {
        brushHomePanelConfig();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(UIEventBusCommonCmd cmd) {
        Log.d(TAG, "OnMessageEvent: ");
        if (cmd.getViewIdxSwitchTo() >= 0) {
            switchForFragement(cmd.getViewIdxSwitchTo());
        }
    }

/*    private boolean isServiceWork() {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager)
                getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(NotificationService.class.getName())) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }*/

    /**
     * jump to specified view
     * 0: home 1:scenario 2:device 3:message 4:dial 5:setting
     */
    public void jumpToSpecifiedView(int pageNumber) {
        if (pageNumber < 0 || pageNumber > 5) {
            return;
        }
//        public static final int LEFT_SELECT_HOME = 0;
//        public static final int LEFT_SELECT_SCENARIOEDIT = 1;
//        public static final int LEFT_SELECT_DEVICEEDIT = 2;
//        public static final int LEFT_SELECT_MESSAGE = 3;
//        public static final int LEFT_SELECT_DIAL = 4;
//        public static final int LEFT_SELECT_SETTING = 5;


    }


}
