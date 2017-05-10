package com.honeywell.homepanel.ui.uicomponent;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUIStatusBar;
import com.honeywell.homepanel.common.Message.ui.NetworkMsg;
import com.honeywell.homepanel.ui.domain.TopStaus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by H135901 on 2/23/2017.
 */

public class TopViewBrusher {
    private static final String TAG = "TopViewBrusher";
    private static final int TIME_FRESH_MS = 60 * 1000;
    public static final int WHAT_TIME_FRESH = 100;
    private Context mContext = null;
    private View mTopView = null;
    private Timer mTimer = new Timer();
    private TextView mTimeTv = null;
    private ImageView mWeatherImage = null;
    private TextView mTemperatureTv = null;
    private ImageView mHealthyImage = null;
    private TextView mHealthyTv = null;
    private TextView mArmTv = null;
    private ImageView mWifiImage = null;
    private ImageView mIPdoorcamera = null;
    private ImageView mConnectionLAN = null;
    private Activity mCurrentActivity = null;
    private static HashMap<String, Integer> sets = new HashMap<String, Integer>();

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
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
        switch (msg.what){
            case  WHAT_TIME_FRESH:
                updateTopBarStatus(mCurrentActivity);
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
        mTimeTv = (TextView)activity.findViewById(R.id.timeTv);
        mWeatherImage = (ImageView)activity. findViewById(R.id.weatherImage);
        mTemperatureTv = (TextView)activity.findViewById(R.id.temperatureTv);
        mHealthyImage = (ImageView) activity.findViewById(R.id.healthyImage);
        mHealthyTv = (TextView)activity.findViewById(R.id.healthyTv);
        mArmTv = (TextView)activity.findViewById(R.id.armTv);
        mWifiImage = (ImageView) activity.findViewById(R.id.wifiImage);
        mIPdoorcamera = (ImageView) activity.findViewById(R.id.ipdoorcamera);
        mConnectionLAN = (ImageView) activity.findViewById(R.id.connection_lan);

//        mTimeTv.setText(getCurrentTimeString());
        mTimer.schedule(new TimerTask() {
            public void run() {
                mHandler.sendEmptyMessage(WHAT_TIME_FRESH);
            }
        },TIME_FRESH_MS,TIME_FRESH_MS);

        setTop(activity);
    }


    public  void setTop(Context context) {
        updateWeather();
        TopStaus topStatus = TopStaus.getInstance(context);

        mTemperatureTv.setText(topStatus.getTemperature() + CommonData.TEMPERATURE_DUSTR);
        if(topStatus.getHealthy().equals(CommonData.UNHEALTHY)){
            mHealthyImage.setImageResource(R.mipmap.top_heathy_unheathy);
        }
        mHealthyTv.setText(topStatus.getHealthy());
        mArmTv.setText(topStatus.getArmStatus());
        if(topStatus.getWifiStatus() == CommonData.CONNECTED){
            mWifiImage.setImageResource(R.mipmap.top_wifi_connect);
            mWifiImage.setVisibility(View.VISIBLE);
        } else if(topStatus.getWifiStatus() == CommonData.DISCONNECT){
            mWifiImage.setVisibility(View.GONE);
        }

        if(topStatus.getmIPDCStatus() == CommonData.CONNECTED){
            mIPdoorcamera.setImageResource(R.mipmap.ipdoorcamera);
            mIPdoorcamera.setVisibility(View.VISIBLE);
        } else if(topStatus.getmIPDCStatus() == CommonData.DISCONNECT){
            mIPdoorcamera.setVisibility(View.GONE);
        }

        if(topStatus.getmServerStatus() == CommonData.CONNECTED){
            mConnectionLAN.setImageResource(R.mipmap.lan);
            mConnectionLAN.setVisibility(View.VISIBLE);
        } else if(topStatus.getmServerStatus() == CommonData.DISCONNECT){
            mConnectionLAN.setVisibility(View.GONE);
        }
        mTimeTv.setText(getCurrentTimeString());
        setTopViewBackground(topStatus.getCurScenario());
    }

    private void updateWeather() {
        TopStaus topStatus = TopStaus.getInstance(mCurrentActivity);

         if (sets.containsKey(topStatus.getWeather())) {
             mWeatherImage.setVisibility(View.VISIBLE);
             mWeatherImage.setImageResource(sets.get(topStatus.getWeather()));
         } else {
             mWeatherImage.setVisibility(View.INVISIBLE);
         }
    }

    private void setTopViewBackground(int curScenario){
        if(curScenario == CommonData.SCENARIO_HOME || curScenario == CommonData.SCENARIO_WAKEUP){
            mTopView.setBackgroundColor(mContext.getResources().getColor(R.color.topbackground_disarm));
        }
        else if(curScenario == CommonData.SCENARIO_AWAY || curScenario == CommonData.SCENARIO_SLEEP){
            mTopView.setBackgroundColor(mContext.getResources().getColor(R.color.topbackground_arm));
        }
    }

    private void updateTopBarStatus(Activity activity) {
        Log.d(TAG, "updateTopBarStatus: ");
        TopStaus topStatus = TopStaus.getInstance(activity);

        updateWeather();

        if(topStatus.getHealthy().equals(CommonData.UNHEALTHY)){
            mHealthyImage.setImageResource(R.mipmap.top_heathy_unheathy);
        }

        mTemperatureTv.setText(topStatus.getTemperature() + CommonData.TEMPERATURE_DUSTR);

        mHealthyTv.setText(topStatus.getHealthy());

        mArmTv.setText(topStatus.getArmStatus());

        if(topStatus.getWifiStatus() == CommonData.CONNECTED){
            mWifiImage.setImageResource(R.mipmap.top_wifi_connect);
            mWifiImage.setVisibility(View.VISIBLE);
        } else if(topStatus.getWifiStatus() == CommonData.DISCONNECT){
            mWifiImage.setVisibility(View.GONE);
        }

        if(topStatus.getmIPDCStatus() == CommonData.CONNECTED){
            mIPdoorcamera.setImageResource(R.mipmap.ipdoorcamera);
            mIPdoorcamera.setVisibility(View.VISIBLE);
        } else if(topStatus.getmIPDCStatus() == CommonData.DISCONNECT){
            mIPdoorcamera.setVisibility(View.GONE);
        }

        if(topStatus.getmServerStatus() == CommonData.CONNECTED){
            mConnectionLAN.setImageResource(R.mipmap.lan);
            mConnectionLAN.setVisibility(View.VISIBLE);
        } else if(topStatus.getmServerStatus() == CommonData.DISCONNECT){
            mConnectionLAN.setVisibility(View.GONE);
        }

        setTopViewBackground(topStatus.getCurScenario());

        mTimeTv.setText(getCurrentTimeString());
    }

    public static String getCurrentTimeString() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM/dd HH:mm");
        String time = sDateFormat.format(new Date());
        return time;
    }

    public void destory(){
        mTimer.cancel();
        mTimer = null;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIStatusBar.SUISConnectStatusMessageEve msg)
    {
        setTop(mCurrentActivity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIStatusBar.SUISDateTimeInfoUpdateMessageEve msg)
    {
        setTop(mCurrentActivity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIStatusBar.SUISWeatherInfoUpdateMessageEve msg)
    {
        setTop(mCurrentActivity);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(NetworkMsg msg)
    {
        if (msg.getWifiStatus() == 1) {
            TopStaus.getInstance(mCurrentActivity).setWifiStatus(CommonData.CONNECTED);
        } else if (msg.getWifiStatus() == 0){
            TopStaus.getInstance(mCurrentActivity).setWifiStatus(CommonData.DISCONNECT);
        }
        setTop(mCurrentActivity);
    }



}
