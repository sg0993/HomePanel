package com.honeywell.homepanel.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.honeywell.homepanel.IConfigService;
import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUICall;
import com.honeywell.homepanel.common.Message.ui.AlarmHint;
import com.honeywell.homepanel.common.utils.CommonUtils;
import com.honeywell.homepanel.ui.domain.NotificationStatisticInfo;
import com.honeywell.homepanel.ui.domain.TopStaus;
import com.honeywell.homepanel.ui.domain.UIBaseCallInfo;
import com.honeywell.homepanel.ui.uicomponent.AdapterCallback;
import com.honeywell.homepanel.ui.uicomponent.AlarmHintPopAdapter;
import com.honeywell.homepanel.watchdog.WatchDogService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.honeywell.homepanel.common.CommonJson.JSON_CALLTYPE_VALUE_OFFICE;

public class MainActivity extends BaseActivity implements AdapterCallback,PopupWindow.OnDismissListener{
    private static final String TAG = "MainActivity";
    private View mCenterView = null;

    private PopupWindow mpopupWindow = null;

    private View content_scrolling = null;
    private  View mTopView = null;
    //Add by xc
    private boolean screenSaverIsRun = false;
    private float screenSaverStartTime = 60;
    private Date lastInputEventTime;
    private Handler mHandler01 = new Handler();
    private Handler mHandler02 = new Handler();
    private final int MILLISECOND_COUNT = 1000;
    private long timePeriod;
    private boolean screenSaverEnabled = false;
    private Handler mScreenSaverTimerHandler = new Handler();
    public static UIBaseCallInfo CallBaseInfo = new UIBaseCallInfo();
    private ServiceConnection mIConfigServiceConnect = new DBOperationService();
    public IConfigService mIConfigService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* setLeftNavifation(CommonData.LEFT_SELECT_HOME);*/

        // start watch dog
        startService(new Intent(this, WatchDogService.class));
        //bind database config service
        CommonUtils.startAndBindService(this, CommonData.ACTION_CONFIG_SERVICE, mIConfigServiceConnect);// using getContext()???

        mTopView = findViewById(R.id.top_status);//for test
        mTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           /* showpopupwindow(getLayoutInflater(),new AlarmHint(100));
            SUISMessagesUIStatusBar.SUISConnectStatusMessageEve msg = new SUISMessagesUIStatusBar.SUISConnectStatusMessageEve();
            try {
                msg.put("action", "event111");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(msg);*/
            }
        });
        mCenterView = findViewById(R.id.main_frameLayout);
        content_scrolling = findViewById(R.id.content_scrolling);
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    private void setCenterViewBackground(int curScenario){
        if(curScenario == CommonData.SCENARIO_HOME
                || curScenario == CommonData.SCENARIO_WAKEUP
                || curScenario == CommonData.SCENARIO_SLEEP ){
            mCenterView.setBackgroundColor(getResources().getColor(R.color.centerbackground_disarm));
        }
        else if(curScenario == CommonData.SCENARIO_AWAY ){
            mCenterView.setBackgroundColor(getResources().getColor(R.color.centerbackground_arm));
        }
    }

    private void screenSaverReset() {
    if (screenSaverEnabled == true) {
        Log.d(TAG, "screenSaverReset: ");
        screenSaverIsRun = false;
        lastInputEventTime = new Date(System.currentTimeMillis());
        mScreenSaverTimerHandler.postAtTime(mScreeSaverTimerUpdateTask, MILLISECOND_COUNT);
        }
    }
	
    @Override
    protected void onResume() {
        super.onResume();
        int mCurScenario  = TopStaus.getInstance(getApplicationContext()).getCurScenario();
        setCenterViewBackground(mCurScenario);
        Log.d(TAG,"onResume() mCurScenario:"+ mCurScenario);
            screenSaverReset();
    }
    @Override
    protected void onDestroy() {
        Log.d(TAG,TAG+".onDestroy() 111111");
        super.onDestroy();
        unbindService(mIConfigServiceConnect);
        if (mpopupWindow != null && mpopupWindow.isShowing()) {
            mpopupWindow.dismiss();
        }
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
    public void OnMessageEvent(AlarmHint alarmHint) {
        showpopupwindow(getLayoutInflater(),alarmHint);
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
			System.out.println("uuid,callType,aliasName,"+uuid+callType+aliasName);
            CallBaseInfo.setCallUuid(uuid);
            CallBaseInfo.setmCallAliasName(aliasName);
            CallBaseInfo.setmCallType(callType);
            CallBaseInfo.setmVideoCodecType(videocodectype);
            CallBaseInfo.setmAudioCodecType(audiocodectype);
            System.out.println("SUISCallInMessageEve1111111uuid,callType,aliasName,"+uuid+callType+aliasName);
            Intent intent = new Intent(this, CallActivity.class);
            intent.putExtra(CommonData.INTENT_KEY_UNIT,aliasName);
            if(callType.equals(CommonJson.JSON_CALLTYPE_VALUE_NEIGHBOUR)){
                intent.putExtra(CommonData.INTENT_KEY_CALL_TYPE,CommonData.CALL_INCOMING_NEIGHBOR);
            }
            else if(callType.equals(CommonJson.JSON_CALLTYPE_VALUE_LOBBY)) {
                intent.putExtra(CommonData.INTENT_KEY_CALL_TYPE,CommonData.CALL_LOBBY_INCOMMING);
            }
            else  if(callType.equals(CommonJson.JSON_CALLTYPE_VALUE_GUARD)||callType.equals(JSON_CALLTYPE_VALUE_OFFICE)){
                intent.putExtra(CommonData.INTENT_KEY_CALL_TYPE,CommonData.CALL_GUARD_INCOMMING);
            }
            else if(callType.equals(CommonJson.JSON_CALLTYPE_VALUE_DOORCAMERA)){
                intent.putExtra(CommonData.INTENT_KEY_CALL_TYPE,CommonData.CALL_IPDC_INCOMING);
            }
            intent.putExtra(CommonData.INTENT_KEY_CALLINFO,CallBaseInfo);
            startActivity(intent);
        }
    }
    private ListView mListView = null;
    private List<AlarmHint>mLists = new ArrayList<AlarmHint>();
    private  AlarmHintPopAdapter mAlarmHintPopAdapter = null;
    private View mPopWndView = null;

    private void showpopupwindow(LayoutInflater inflater,AlarmHint alarmHint) {
        if(null == mListView){
            mPopWndView = inflater.inflate(R.layout.layout_alarm_hint, null);
            mListView = (ListView) mPopWndView.findViewById(R.id.list_alarm);
        }
        mLists.add(alarmHint);
        if(mAlarmHintPopAdapter == null){
            mAlarmHintPopAdapter = new AlarmHintPopAdapter(mLists,this,this);
            mListView.setAdapter(mAlarmHintPopAdapter);
        }
        else{
            mAlarmHintPopAdapter.notifyDataSetChanged();
        }
        if(mpopupWindow == null){
            mpopupWindow = new PopupWindow(mPopWndView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mpopupWindow.setBackgroundDrawable(new ColorDrawable());
            mpopupWindow.setOutsideTouchable(true);
            mpopupWindow.setOnDismissListener(this);
        }
        if(!mpopupWindow.isShowing()){
            mpopupWindow.showAtLocation(content_scrolling, Gravity.LEFT | Gravity.TOP, 0, 0);
        }
        CommonUtils.setWindowAlpha(getWindow(),0.3f);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(mpopupWindow != null && mpopupWindow.isShowing()){
            mpopupWindow.dismiss();
            return true;
        }
       /* if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }*/
        return  super.onKeyDown(keyCode,event);
    }

    @Override
    public void subviewOnclick(int position, String more) {
        mpopupWindow.dismiss();
    }

    @Override
    public void onDismiss() {
        CommonUtils.setWindowAlpha(getWindow(),1.0f);
    }

    private Runnable mScreeSaverTimerUpdateTask = new Runnable() {
        @Override
        public void run() {
            Date timeNow = new Date(System.currentTimeMillis());
            timePeriod = (long) timeNow.getTime() - (long) lastInputEventTime.getTime();
            float timePeriodSecond = ((float) timePeriod / MILLISECOND_COUNT);
            Log.d(TAG, "run: timePeriodSecond = " + timePeriodSecond);
            if(timePeriodSecond > screenSaverStartTime) {
                if(screenSaverIsRun == false) {
                    screenSaverIsRun = true;
                    showScreenSaver();
                }
            }

            mScreenSaverTimerHandler.postDelayed(mScreeSaverTimerUpdateTask, MILLISECOND_COUNT);// 1 sec update cycle
        }
    };

    private void showScreenSaver() {
//        Intent intent = new Intent(this, screensaveractivity.class);
//        Intent intent = new Intent(this, timescreensaveractivity.class);
        Random random = new Random();
        int t = random.nextInt(100);
        if (t % 2 == 0) {
            Intent intent = new Intent(this, ScreensaverTextClockActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, Screensaverwallpaper.class);
            startActivity(intent);
        }
    }

    public void updateUserActionTime() {
        Date timeNow = new Date(System.currentTimeMillis());
        if(null != lastInputEventTime){
            timePeriod = timeNow.getTime() - lastInputEventTime.getTime();
            lastInputEventTime.setTime(timeNow.getTime());
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
//        mHandler01.removeCallbacks(mTask01);
//        mHandler02.removeCallbacks(mTask02);
        super.onPause();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        updateUserActionTime();
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
            if(serviceClassName.equals(CommonData.ACTION_CONFIG_SERVICE)) {
                mIConfigService = IConfigService.Stub.asInterface(service);
                NotificationStatisticInfo temp = NotificationStatisticInfo.getInstance();
                temp.getDataCountFromDB(mIConfigService);
                if (temp.hasUnreadMsg()) {
                    updateIndicator();
                }
            }
        }
        public void onServiceDisconnected(ComponentName name) {
            String serviceClassName = name.getClassName();
            if(serviceClassName.equals(CommonData.ACTION_CONFIG_SERVICE)){
                mIConfigService = null;
            }
        }
    }

}
