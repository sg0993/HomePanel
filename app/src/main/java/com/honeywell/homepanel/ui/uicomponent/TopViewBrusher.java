package com.honeywell.homepanel.ui.uicomponent;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.ui.domain.TopStaus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by H135901 on 2/23/2017.
 */

public class TopViewBrusher {

    private static final int TIME_FRESH = 60 * 1000;
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

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case  WHAT_TIME_FRESH:
                    setTime();
                    break;
            }
        }
    };

    public void init(Activity activity) {
        mContext = activity.getApplicationContext();
        mTopView = activity.findViewById(R.id.top_status);
        mTimeTv = (TextView)activity.findViewById(R.id.timeTv);
        mWeatherImage = (ImageView)activity. findViewById(R.id.weatherImage);
        mTemperatureTv = (TextView)activity.findViewById(R.id.temperatureTv);
        mHealthyImage = (ImageView) activity.findViewById(R.id.healthyImage);
        mHealthyTv = (TextView)activity.findViewById(R.id.healthyTv);
        mArmTv = (TextView)activity.findViewById(R.id.armTv);
        mWifiImage = (ImageView) activity.findViewById(R.id.wifiImage);;

        mTimeTv.setText(getCurrentTimeString());
        mTimer.schedule(new TimerTask() {
            public void run() {
                mHandler.sendEmptyMessage(WHAT_TIME_FRESH);
            }
        },TIME_FRESH,TIME_FRESH);

        setTop(activity);
    }


    public  void setTop(Context context){
        TopStaus topStatus = TopStaus.getInstance(context);
        if(topStatus.getWeather().equals(CommonData.WEATHER_SUNNY)){
            mWeatherImage.setImageResource(R.mipmap.top_weather_sunny);
        }
        mTemperatureTv.setText(topStatus.getTemperature() + CommonData.TEMPERATURE_DUSTR);
        if(topStatus.getHealthy().equals(CommonData.UNHEALTHY)){
            mHealthyImage.setImageResource(R.mipmap.top_heathy_unheathy);
        }
        mHealthyTv.setText(topStatus.getHealthy());
        mArmTv.setText(topStatus.getArmStatus());
        if(topStatus.getWifiStatus() == CommonData.WIFI_CONNECTED){
            mWifiImage.setImageResource(R.mipmap.top_wifi_connect);
        }
        setTopViewBackground(topStatus.mCurScenario);
    }

    private void setTopViewBackground(int curScenario){
        if(curScenario == CommonData.SCENARIO_HOME || curScenario == CommonData.SCENARIO_WAKEUP){
            mTopView.setBackgroundColor(mContext.getResources().getColor(R.color.topbackground_disarm));
        }
        else if(curScenario == CommonData.SCENARIO_AWAY || curScenario == CommonData.SCENARIO_SLEEP){
            mTopView.setBackgroundColor(mContext.getResources().getColor(R.color.topbackground_arm));
        }
    }

    private void setTime() {
        String time = getCurrentTimeString();
        mTimeTv.setText(time);
    }

    public static String getCurrentTimeString() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM/dd HH:mm");
        String time = sDateFormat.format(new Date());
        return time;
    }

    public void destory(){
        mTimer.cancel();
        mTimer = null;
    }
}
