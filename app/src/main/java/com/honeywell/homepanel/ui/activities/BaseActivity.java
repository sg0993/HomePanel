package com.honeywell.homepanel.ui.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.EthernetManagerUtil;
import com.honeywell.homepanel.Utils.LogMgr;
import com.honeywell.homepanel.cloud.Protocols;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUINotification;
import com.honeywell.homepanel.ui.domain.NetworkInfo;
import com.honeywell.homepanel.ui.domain.mainNavigationStatusStruct;
import com.honeywell.homepanel.ui.fragment.DeviceEditFragment;
import com.honeywell.homepanel.ui.fragment.DialFragment;
import com.honeywell.homepanel.ui.fragment.HomeFragment;
import com.honeywell.homepanel.ui.fragment.MessageFragment;
import com.honeywell.homepanel.ui.fragment.ScenarioEditFragment;
import com.honeywell.homepanel.ui.fragment.SettingFragment;
import com.honeywell.homepanel.ui.uicomponent.TopViewBrusher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by H135901 on 1/24/2017.
 */

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "BaseActivity";
    private static final int PERMISSIONS_REQUEST_ALL = 0;
    private int mLeftCurPage = CommonData.LEFT_SELECT_HOME;
    private int mLeftPrePage = CommonData.LEFT_SELECT_HOME;
    private List<View> mLeftViews = new ArrayList<View>();
    private List<ImageView> mLeftImages = new ArrayList<ImageView>();
    public Map<Integer, Fragment> mFragments = new HashMap<Integer, Fragment>();
    private TopViewBrusher mTopViewBrusher = new TopViewBrusher();
    private ImageView mMsgIndicator = null;
    private boolean isBound;
    private BroadcastReceiver bindStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(getContent());
        initViewAndListener();
        mTopViewBrusher.init(this);
        fragmentAdd(true, mLeftCurPage);
        setLeftNavifation(mLeftCurPage);
        updateNewMessageIndicator(0);
        bindStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isBound = intent.getBooleanExtra(Protocols.EXTRACT_BIND_STATE, false);
            }
        };
        Intent intent = registerReceiver(bindStateReceiver, new IntentFilter(Protocols.INTENT_MQTT_BIND_STATE));
        if (intent != null) {
            isBound = intent.getBooleanExtra(Protocols.EXTRACT_BIND_STATE, false);
        } else {
            isBound = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(CommonData.MQTT_BIND_HOME_PANEL, false);
        }
    }

    public boolean getBindState() {
        return isBound;
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestAllPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.CAMERA
                , Manifest.permission.RECORD_AUDIO});
    }

    private void requestAllPermission(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_ALL);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ALL:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_ALL);
                            Snackbar.make(getWindow().getDecorView(), "请不要拒绝权限申请", Snackbar.LENGTH_LONG).show();
                            break;
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setLeftNavifation(mLeftCurPage);
        mTopViewBrusher.setTop(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, TAG + ".onDestroy() 111111");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        mTopViewBrusher.destory();
        unregisterReceiver(bindStateReceiver);
    }

    private void fragmentAdd(boolean bAdd, int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = mFragments.get(position);
        if (null == fragment) {
            fragment = getNewFragMent(position);
        }
        // TODO: 2017/7/21  luoxiang

//        if (bAdd) {
//            transaction.add(R.id.main_frameLayout, fragment);
//        } else {
//            transaction.replace(R.id.main_frameLayout, fragment);
//        }

        transaction.replace(R.id.main_frameLayout, fragment);
        transaction.commitAllowingStateLoss();
    }


    private Fragment getNewFragMent(int position) {
        Fragment fragment = null;
        switch (position) {
            case CommonData.LEFT_SELECT_HOME:
                fragment = new HomeFragment("" + position);
                break;
            case CommonData.LEFT_SELECT_SCENARIOEDIT:
                fragment = new ScenarioEditFragment("" + position);
                break;
            case CommonData.LEFT_SELECT_DEVICEEDIT:
                fragment = new DeviceEditFragment("" + position);
                break;
            case CommonData.LEFT_SELECT_MESSAGE:
                fragment = new MessageFragment("" + position);
                break;
            case CommonData.LEFT_SELECT_DIAL:
                fragment = new DialFragment("" + position);
                break;
            case CommonData.LEFT_SELECT_SETTING:
                fragment = new SettingFragment("" + position);
                break;
        }
        mFragments.put(position, fragment);
        return fragment;
    }

    protected void initViewAndListener() {
        initLeftViews();
    }

    private void initLeftViews() {
        mLeftViews.add(CommonData.LEFT_SELECT_HOME, findViewById(R.id.home_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_SCENARIOEDIT, findViewById(R.id.scenario_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_DEVICEEDIT, findViewById(R.id.device_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_MESSAGE, findViewById(R.id.message_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_DIAL, findViewById(R.id.dial_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_SETTING, findViewById(R.id.setting_layout));

        for (int i = 0; i <= CommonData.LEFT_SELECT_SETTING; i++) {
            mLeftViews.get(i).setOnClickListener(this);
        }
        mLeftImages.add(CommonData.LEFT_SELECT_HOME, (ImageView) findViewById(R.id.home_image));
        mLeftImages.add(CommonData.LEFT_SELECT_SCENARIOEDIT, (ImageView) findViewById(R.id.scenario_image));
        mLeftImages.add(CommonData.LEFT_SELECT_DEVICEEDIT, (ImageView) findViewById(R.id.device_image));
        mLeftImages.add(CommonData.LEFT_SELECT_MESSAGE, (ImageView) findViewById(R.id.message_image));
        mLeftImages.add(CommonData.LEFT_SELECT_DIAL, (ImageView) findViewById(R.id.dial_image));
        mLeftImages.add(CommonData.LEFT_SELECT_SETTING, (ImageView) findViewById(R.id.setting_image));
        mMsgIndicator = (ImageView) findViewById(R.id.message_indicator);
    }


    protected void setLeftNavifation(int curPage) {
        Log.d(TAG, "setLeftNavifation() mCur:" + mLeftCurPage + ",nPre:" + mLeftPrePage + ", 11111111111111");
        mLeftPrePage = mLeftCurPage;
        mLeftCurPage = curPage;
        initLeftPage();
    }

    protected abstract int getContent();

    private void initLeftPage() {
        switch (mLeftCurPage) {
            case CommonData.LEFT_SELECT_HOME:
                mLeftImages.get(mLeftCurPage).setImageResource(R.mipmap.home_select);
                break;
            case CommonData.LEFT_SELECT_SCENARIOEDIT:
                mLeftImages.get(mLeftCurPage).setImageResource(R.mipmap.scenario_select);
                break;
            case CommonData.LEFT_SELECT_DEVICEEDIT:
                mLeftImages.get(mLeftCurPage).setImageResource(R.mipmap.device_select);
                break;
            case CommonData.LEFT_SELECT_MESSAGE:
                mLeftImages.get(mLeftCurPage).setImageResource(R.mipmap.message_select);
                break;
            case CommonData.LEFT_SELECT_DIAL:
                mLeftImages.get(mLeftCurPage).setImageResource(R.mipmap.dial_select);
                break;
            case CommonData.LEFT_SELECT_SETTING:
                mLeftImages.get(mLeftCurPage).setImageResource(R.mipmap.setting_select);
                break;
        }
        if (mLeftCurPage != mLeftPrePage) {
            switch (mLeftPrePage) {
                case CommonData.LEFT_SELECT_HOME:
                    mLeftImages.get(mLeftPrePage).setImageResource(R.mipmap.home);
                    break;
                case CommonData.LEFT_SELECT_SCENARIOEDIT:
                    mLeftImages.get(mLeftPrePage).setImageResource(R.mipmap.scenario);
                    break;
                case CommonData.LEFT_SELECT_DEVICEEDIT:
                    mLeftImages.get(mLeftPrePage).setImageResource(R.mipmap.device);
                    break;
                case CommonData.LEFT_SELECT_MESSAGE:
                    mLeftImages.get(mLeftPrePage).setImageResource(R.mipmap.message);
                    break;
                case CommonData.LEFT_SELECT_DIAL:
                    mLeftImages.get(mLeftPrePage).setImageResource(R.mipmap.dial);
                    break;
                case CommonData.LEFT_SELECT_SETTING:
                    mLeftImages.get(mLeftPrePage).setImageResource(R.mipmap.setting);
                    break;
            }
        }
    }

    public void updateNewMessageIndicator(int unreadCount) {
        if (unreadCount > 0) {
            mMsgIndicator.setVisibility(View.VISIBLE);
        } else if (unreadCount == 0) {
            mMsgIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUINotification.SUISEventsUnreadCountMessageRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");
        String subaction = msg.optString(CommonJson.JSON_SUBACTION_KEY, "");
        String msgid = msg.optString(CommonJson.JSON_MSGID_KEY, "");

        if (!action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)
                || !subaction.equals(CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONEVENTCOUNTGET)) {
            return;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUINotification.SUISAlarmUnreadCountMessageRsp msg) {
//        jsonParse((Object) msg);
    }

    public void switchForFragement(int page) {
        setLeftNavifation(page);
        fragmentAdd(false, page);
    }


    @Override
    public void onClick(View view) {


        for (int i = 0; i <= CommonData.LEFT_SELECT_SETTING; i++) {
            if (mLeftViews.get(i).getId() == view.getId()) {
                if(i == mLeftCurPage){
                    break;
                }
                switchForFragement(i);
                break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(mainNavigationStatusStruct msg) {//receive bulletin msg
        Log.d(TAG, "OnMessageEvent: msg.type =" + msg.type);
        Log.d(TAG, "OnMessageEvent: msg.unreadCount=" + msg.unreadCount);
        updateNewMessageIndicator(msg.unreadCount);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void OnMessageEvent(NetworkInfo msg) {
        EthernetManagerUtil.getInstance().setEthernetStaticMode();
        if (msg != null)
            savaNetWork(msg);
    }


    private void savaNetWork(NetworkInfo msg) {
        try {
            EthernetManagerUtil ethernetManagerUtil = EthernetManagerUtil.getInstance();
            for (int i = 0; i < 5; i++) {
                Thread.sleep(1000);
                String ipAddress = ethernetManagerUtil.getIpAddress();
                String netmask = ethernetManagerUtil.getNetMask();
                String gateway = ethernetManagerUtil.getGateWay();
                LogMgr.d("当前线程:" + Thread.currentThread().getName() + "  netmask:" + netmask + " gateway:" + gateway + " ipAddress:" + ipAddress + "  msg.getNetWorkIP():" + msg.getNetWorkIP());

                if (!TextUtils.equals(ipAddress, msg.getNetWorkIP())) {
                    ethernetManagerUtil.setIpAddress(msg.getNetWorkIP());
                } else if (msg.getNetWorkGateway() != null && !TextUtils.equals(gateway, msg.getNetWorkGateway())) {
                    ethernetManagerUtil.setGateWay(msg.getNetWorkGateway());
                } else if (msg.getNetWorkMask() != null && !TextUtils.equals(netmask, msg.getNetWorkMask())) {
                    ethernetManagerUtil.setNetMask(msg.getNetWorkMask());
                } else {
                    ethernetManagerUtil.delete();
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
