package com.honeywell.homepanel.ui.uicomponent;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.RelativeTimer;
import com.honeywell.homepanel.Utils.RelativeTimerTask;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUIStatusBar;
import com.honeywell.homepanel.common.Message.ui.NetworkMsg;
import com.honeywell.homepanel.common.Message.ui.TopViewBrushEvent;
import com.honeywell.homepanel.ui.activities.MainActivity;
import com.honeywell.homepanel.ui.domain.TopStaus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by H135901 on 2/23/2017.
 */

public class TopViewBrusher {
    private static final String TAG = "TopViewBrusher";
    private static final int TIME_FRESH_MS = 60 * 1000;
    public static final int WHAT_TIME_FRESH = 100;
    private Context mContext = null;
    private View mTopView = null;
    //    private Timer mTimer = new Timer();
    private RelativeTimer mTimer;
    private TextView mTimeTv = null;
    private ImageView mWeatherImage = null;
    private TextView mTemperatureTv = null;
    private ImageView mHealthyImage = null;
    private TextView mHealthyTv = null;
    private TextView mArmTv = null;
    private ImageView mWifiImage = null;
    private ImageView mBackIPdoorcamera = null;
    private ImageView mFrontIPdoorcamera = null;
    private ImageView mConnectionLAN = null;
    private ImageView mBoundCloud = null;
    private Activity mCurrentActivity = null;
    private static HashMap<String, Integer> sets = new HashMap<String, Integer>();
    private RelativeTimerTask mTimerTask;
    private boolean isFirst;

    static {
        sets.put(CommonData.WEATHER_SUNNY, R.mipmap.icon_sunny);
        sets.put(CommonData.WEATHER_CLOUDY, R.mipmap.icon_cloudy);
        sets.put(CommonData.WEATHER_FLURRIES, R.mipmap.icon_flurries);
        sets.put(CommonData.WEATHER_FOG, R.mipmap.icon_fog);
        sets.put(CommonData.WEATHER_MOSTLYCLEAR, R.mipmap.icon_mostly_clear);
        sets.put(CommonData.WEATHER_MOSTLYCLOUDYTHUNDERSTORMS, R.mipmap.icon_mostly_cloudy_thunder_storms);
        sets.put(CommonData.WEATHER_MOSTLYSUNNYFLURRIES, R.mipmap.icon_mostly_sunny_flurries);
        sets.put(CommonData.WEATHER_MOSTLYSUNNYSHOWER, R.mipmap.icon_mostly_sunny_shower);
        sets.put(CommonData.WEATHER_MOSTLYSUNNYSNOW, R.mipmap.icon_mostly_sunny_snow);
        sets.put(CommonData.WEATHER_MOSTLYSUNNYTHUNDERSTORMS, R.mipmap.icon_mostly_sunny_thunder_storms);
        sets.put(CommonData.WEATHER_MOSTLYSUNNY, R.mipmap.icon_mostly_sunny);
        sets.put(CommonData.WEATHER_PARTLYSUNNY, R.mipmap.icon_partly_sunny);
        sets.put(CommonData.WEATHER_RAINSNOWMIXED, R.mipmap.icon_rain_snow_mixed);
        sets.put(CommonData.WEATHER_RAIN, R.mipmap.icon_rain);
        sets.put(CommonData.WEATHER_SLEET, R.mipmap.icon_sleet);
        sets.put(CommonData.WEATHER_SNOW, R.mipmap.icon_snow);
        sets.put(CommonData.WEATHER_THUNDERSTORMS, R.mipmap.icon_thunderstorms);
        sets.put(CommonData.WEATHER_WINDY, R.mipmap.icon_windy);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_TIME_FRESH:
                    updateTopStatusBar(mCurrentActivity);
                    break;
            }
        }
    };

    public void init(Activity activity) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mCurrentActivity = activity;
        mContext = activity.getApplicationContext();
        mTopView = activity.findViewById(R.id.top_status);
        mTimeTv = (TextView) activity.findViewById(R.id.timeTv);
        mWeatherImage = (ImageView) activity.findViewById(R.id.weatherImage);
        mTemperatureTv = (TextView) activity.findViewById(R.id.temperatureTv);
        mHealthyImage = (ImageView) activity.findViewById(R.id.healthyImage);
        mHealthyTv = (TextView) activity.findViewById(R.id.healthyTv);
        mArmTv = (TextView) activity.findViewById(R.id.armTv);
        mWifiImage = (ImageView) activity.findViewById(R.id.wifiImage);
        mFrontIPdoorcamera = (ImageView) activity.findViewById(R.id.front_ipdoorcamera);
        mBackIPdoorcamera = (ImageView) activity.findViewById(R.id.back_ipdoorcamera);
        mConnectionLAN = (ImageView) activity.findViewById(R.id.connection_lann);
        mBoundCloud = (ImageView) activity.findViewById(R.id.cloud_bound_icon);

        startTimer();
        getCloudBoundStatus();
        setTop(activity);
    }


    private void startTimer() {
        mTimer = RelativeTimer.getDefault();

        String second = DateFormat.format("ss", System.currentTimeMillis()).toString();


        int startsecond = 0;
        if (!isFirst) {
            try {
                startsecond = (60 - Integer.valueOf(second)) * 1000;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (mTimerTask == null) {
            mTimerTask = new RelativeTimerTask("updateTimeTask") {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(WHAT_TIME_FRESH);
                }
            };
        }

        mTimer.schedule(mTimerTask, isFirst ? TIME_FRESH_MS : startsecond, TIME_FRESH_MS);
        isFirst = true;
    }


    private void stopTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }

        mTimer = null;
        mTimerTask = null;
    }

    /**
     * This methd used to update homepanel top status bar immediately
     *
     * @param context
     */
    public void setTop(Context context) {
        TopStaus topStatus = TopStaus.getInstance(context);

        updateWeather();
        updateConnectionStatus();
        updateArmStatus(topStatus);

        mTimeTv.setText(getCurrentTimeString());
//        setTopViewBackground(topStatus.getCurScenario());
    }

    private void updateWeather() {
        TopStaus topStatus = TopStaus.getInstance(mCurrentActivity);

        if (sets.containsKey(topStatus.getWeather())) {
            mWeatherImage.setVisibility(View.VISIBLE);
            mWeatherImage.setImageResource(sets.get(topStatus.getWeather()));
        } else {
            mWeatherImage.setVisibility(View.INVISIBLE);
        }

        if (TextUtils.isEmpty(topStatus.getHealthy())) {
            mHealthyImage.setVisibility(View.GONE);
        } else {
            mHealthyImage.setVisibility(View.VISIBLE);
            mHealthyImage.setImageResource(R.mipmap.top_heathy_unheathy);
        }

        if ((topStatus.getTemperature() > -40) && (topStatus.getTemperature() < 60)) {
            mTemperatureTv.setText(topStatus.getTemperature() + CommonData.TEMPERATURE_DUSTR);
            mHealthyTv.setText(topStatus.getHealthy());
            mTemperatureTv.setVisibility(View.VISIBLE);
            mHealthyTv.setVisibility(View.VISIBLE);
        } else {
            mTemperatureTv.setVisibility(View.GONE);
            mHealthyTv.setVisibility(View.GONE);
        }

    }

    private void setTopViewBackground(int curScenario) {
        if (curScenario == CommonData.SCENARIO_HOME || curScenario == CommonData.SCENARIO_WAKEUP) {
            mTopView.setBackgroundColor(mContext.getResources().getColor(R.color.topbackground_disarm));
        } else if (curScenario == CommonData.SCENARIO_AWAY || curScenario == CommonData.SCENARIO_SLEEP) {
            mTopView.setBackgroundColor(mContext.getResources().getColor(R.color.topbackground_arm));
        }
    }

    private void updateTopStatusBar(Activity activity) {
        Log.d(TAG, "updateTopStatusBar: ");
        TopStaus topStatus = TopStaus.getInstance(activity);

        updateWeather();
        updateConnectionStatus();
        updateArmStatus(topStatus);

        mTimeTv.setText(getCurrentTimeString());

    }

    private void updateArmStatus(TopStaus ts) {
        if (ts == null) return;


//        if (ts.getArmStatus().equals(CommonData.ARMSTATUS_DISARM)) {
//            mTopView.setBackgroundColor(mContext.getResources().getColor(R.color.topbackground_disarm));
//        } else if (ts.getArmStatus().equals(CommonData.ARMSTATUS_ARM)) {
//            mTopView.setBackgroundColor(mContext.getResources().getColor(R.color.topbackground_arm));
//        }
//        Log.d(TAG, "ts.getArmStatus():" + ts.getArmStatus());
        if (mContext == null) return;
        if (CommonData.ARMSTATUS_DISARM.equalsIgnoreCase(ts.getArmStatus())) {
            mArmTv.setText(mContext.getString(R.string.System_disarmed));
            mTopView.setBackgroundColor(mContext.getResources().getColor(R.color.topbackground_disarm));
        } else if (CommonData.ARMSTATUS_ARM.equalsIgnoreCase(ts.getArmStatus())) {
            mArmTv.setText(mContext.getString(R.string.System_armed));
            mTopView.setBackgroundColor(mContext.getResources().getColor(R.color.topbackground_arm));
        }
    }

    private void updateConnectionStatus() {
        TopStaus topStatus = TopStaus.getInstance(mCurrentActivity);

        if (topStatus.getWifiStatus() == CommonData.CONNECTED) {
            mWifiImage.setImageResource(R.mipmap.top_wifi_connect);
            mWifiImage.setVisibility(View.VISIBLE);
        } else if (topStatus.getWifiStatus() == CommonData.DISCONNECT) {
            mWifiImage.setVisibility(View.GONE);
        }

        if (topStatus.getmFrontIPDCStatus() == CommonData.CONNECTED) {
            mFrontIPdoorcamera.setImageResource(R.mipmap.ipdoorcamera);
            mFrontIPdoorcamera.setVisibility(View.VISIBLE);
        } else if (topStatus.getmFrontIPDCStatus() == CommonData.DISCONNECT) {
            mFrontIPdoorcamera.setVisibility(View.GONE);
        }

        if (topStatus.getmBackIPDCStatus() == CommonData.CONNECTED) {
            mBackIPdoorcamera.setImageResource(R.mipmap.ipdoorcamera);
            mBackIPdoorcamera.setVisibility(View.VISIBLE);
        } else if (topStatus.getmBackIPDCStatus() == CommonData.DISCONNECT) {
            mBackIPdoorcamera.setVisibility(View.GONE);
        }

        if (topStatus.getmServerStatus() == CommonData.CONNECTED) {
            mConnectionLAN.setImageResource(R.mipmap.lan);
            mConnectionLAN.setVisibility(View.VISIBLE);
        } else if (topStatus.getmServerStatus() == CommonData.DISCONNECT) {
            mConnectionLAN.setVisibility(View.GONE);
        }

//        Log.d(TAG, "isCloudBoundStatus: " + topStatus.isCloudBoundStatus());
//        Log.d(TAG, "isCloudConnStatus: " + topStatus.isCloudConnStatus());
        if ((topStatus.isCloudBoundStatus() == true) && (topStatus.isCloudConnStatus() == true)) {
            if (MainActivity.mHomePanelType == CommonData.HOMEPANEL_TYPE_MAIN) {//only mainphone need show bound status}
                mBoundCloud.setImageResource(R.mipmap.top_cloud_bound);
                mBoundCloud.setVisibility(View.VISIBLE);
            }
        } else if ((topStatus.isCloudBoundStatus() == false) || (topStatus.isCloudConnStatus() == false)) {
            mBoundCloud.setVisibility(View.GONE);
        }
    }

    public static String getCurrentTimeString() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM/dd HH:mm");
        String time = sDateFormat.format(new Date());
        return time;
    }

    public void destory() {
        isFirst = false;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        stopTimer();
    }

    public void getCloudBoundStatus() {
        boolean cloudBoundStatus = PreferenceManager.getDefaultSharedPreferences(mContext).
                getBoolean(CommonData.MQTT_BIND_HOME_PANEL, false);
        TopStaus.getInstance(mContext).setCloudBoundStatus(cloudBoundStatus);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIStatusBar.SUISConnectStatusMessageEve msg) {
        Log.d(TAG, TAG + ",OnMessageEvent: SUISConnectStatusMessageEve:" + msg.toString() + ",,,11111111");
        setTop(mCurrentActivity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIStatusBar.SUISubPhoneConnectStatusMessageEve msg) {
        Log.d(TAG, TAG + ",OnMessageEvent: SUISubPhoneConnectStatusMessageEve:" + msg.toString() + ",,,11111111");
        setTop(mCurrentActivity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIStatusBar.SUISDateTimeInfoUpdateMessageEve msg) {
        Log.d(TAG, TAG + ",OnMessageEvent: SUISDateTimeInfoUpdateMessageEve:" + msg.toString() + ",,,11111111");
        setTop(mCurrentActivity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIStatusBar.SUISWeatherInfoUpdateMessageEve msg) {
        Log.d(TAG, TAG + ",OnMessageEvent: SUISWeatherInfoUpdateMessageEve:" + msg.toString() + ",,,11111111");
        setTop(mCurrentActivity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(NetworkMsg msg) {
        Log.d(TAG, "NetworkMsg:" + msg.toString());
        if (msg.isWifiUpdated()) {
            if (msg.getWifiStatus() == 1) {
                TopStaus.getInstance(mCurrentActivity).setWifiStatus(CommonData.CONNECTED);
            } else if (msg.getWifiStatus() == 0) {
                TopStaus.getInstance(mCurrentActivity).setWifiStatus(CommonData.DISCONNECT);
            }
        }

        if (msg.isCloudBoundUpdated()) {
            TopStaus.getInstance(mCurrentActivity).setCloudBoundStatus(msg.isCloudBoundStatus());
        }

        if (msg.isCloudConnUpdated()) {
            TopStaus.getInstance(mCurrentActivity).setCloudConnStatus(msg.isCloudConnStatus());
        }

        if (msg.isEthUpdated()) {
            if (msg.getEthernetStatus() == 0) {//ethernet offline event
                TopStaus.getInstance(mCurrentActivity).updateEthernetStatus(CommonData.DISCONNECT);
            }
        }

        setTop(mCurrentActivity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(TopViewBrushEvent eve) {
        setTop(mCurrentActivity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(TopViewBrushEventTime eve) {
        stopTimer();
        mTimeTv.setText(getCurrentTimeString());
        startTimer();
    }

    public class TopViewBrushEventTime {
    }


}
