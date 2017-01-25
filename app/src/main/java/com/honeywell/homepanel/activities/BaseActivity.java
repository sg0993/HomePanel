package com.honeywell.homepanel.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.domain.TopStaus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by H135901 on 1/24/2017.
 */

public abstract class BaseActivity extends Activity implements View.OnClickListener{

    private static final int TIME_FRESH = 60 * 1000;
    public static final int WHAT_TIME_FRESH = 100;
    private static int mLeftCurPage = CommonData.LEFT_SELECT_HOME;
    private List<View>mLeftViews = new ArrayList<View>();
    private List<ImageView>mLeftImages = new ArrayList<ImageView>();
    private List<Class> mActivities = new ArrayList<Class>();

    private TextView mTimeTv = null;
    private ImageView mWeatherImage = null;
    private TextView mTemperatureTv = null;
    private ImageView mHealthyImage = null;
    private TextView mHealthyTv = null;
    private TextView mArmTv = null;
    private ImageView mWifiImage = null;

    private Timer mTimer = new Timer();
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case  WHAT_TIME_FRESH:
                    setTime();
                    break;
            }
        }
    };

    private void setTime() {
        String time = getCurrentTimeString();
        mTimeTv.setText(time);
    }

    public String getCurrentTimeString() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM/dd HH:mm");
        String time = sDateFormat.format(new Date());
        return time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initIntentValue();
        setContentView(getContent());
        initViewAndListener();
        setTop();

        initClasses();
    }

    private void initClasses() {
        mActivities.add(MainActivity.class);
        mActivities.add(ScenarioEditActivity.class);
        mActivities.add(DeviceEditActivity.class);
        mActivities.add(MessageActivity.class);
        mActivities.add(DialActivity.class);
        mActivities.add(SettinglActivity.class);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLeftNavifation(mLeftCurPage);
    }

    //for sub class init self control and listener
    protected void initViewAndListener() {
        initLeftViews();
        initTopViews();
    }

    private void initTopViews() {
        mTimeTv = (TextView)findViewById(R.id.timeTv);
        mWeatherImage = (ImageView) findViewById(R.id.weatherImage);
        mTemperatureTv = (TextView)findViewById(R.id.temperatureTv);
        mHealthyImage = (ImageView) findViewById(R.id.healthyImage);
        mHealthyTv = (TextView)findViewById(R.id.healthyTv);
        mArmTv = (TextView)findViewById(R.id.armTv);
        mWifiImage = (ImageView) findViewById(R.id.wifiImage);;

        mTimeTv.setText(getCurrentTimeString());
        mTimer.schedule(new TimerTask() {
            public void run() {
                mHandler.sendEmptyMessage(WHAT_TIME_FRESH);
            }
        },TIME_FRESH,TIME_FRESH);
    }

    private void initLeftViews() {
        mLeftViews.add(CommonData.LEFT_SELECT_HOME,findViewById(R.id.home_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_SCENARIOEDIT,findViewById(R.id.scenario_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_DEVICEEDIT,findViewById(R.id.device_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_MESSAGE,findViewById(R.id.message_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_DIAL,findViewById(R.id.dial_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_SETTING,findViewById(R.id.setting_layout));

        for (int i = 0; i <= CommonData.LEFT_SELECT_SETTING  ; i++) {
            mLeftViews.get(i).setOnClickListener(this);
        }

        mLeftImages.add(CommonData.LEFT_SELECT_HOME,(ImageView) findViewById(R.id.home_image));
        mLeftImages.add(CommonData.LEFT_SELECT_SCENARIOEDIT,(ImageView) findViewById(R.id.scenario_image));
        mLeftImages.add(CommonData.LEFT_SELECT_DEVICEEDIT,(ImageView) findViewById(R.id.device_image));
        mLeftImages.add(CommonData.LEFT_SELECT_MESSAGE,(ImageView) findViewById(R.id.message_image));
        mLeftImages.add(CommonData.LEFT_SELECT_DIAL,(ImageView) findViewById(R.id.dial_image));
        mLeftImages.add(CommonData.LEFT_SELECT_SETTING,(ImageView) findViewById(R.id.setting_image));
    }


    protected  void setLeftNavifation(int curPage) {
        mLeftCurPage = curPage;
        initLeftPage();
    }

    protected  void setTop(){
        TopStaus topStatus = TopStaus.getInstance(getApplicationContext());
        //mTimeTv.setText("");
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
    }

    //protected void initIntentValue() {}


    protected abstract int getContent();

    private void initLeftPage() {
        int selectColor = getResources().getColor(R.color.blue);
        int unselectColor = getResources().getColor(R.color.mainpage_noselect_bkg);
        for (int i = 0; i <= CommonData.LEFT_SELECT_SETTING; i++) {
            if(i == mLeftCurPage){
                mLeftViews.get(i).setBackgroundColor(selectColor);
            }
            else {
                mLeftViews.get(i).setBackgroundColor(unselectColor);
            }
        }
    }

    @Override
    public void onClick(View view) {
        for (int i = 0; i <=  CommonData.LEFT_SELECT_SETTING; i++) {
            if(mLeftViews.get(i).getId() == view.getId()){
                if(mLeftCurPage != CommonData.LEFT_SELECT_HOME){
                    finish();
                }
                setLeftNavifation(i);
                launchActivity(i);
                break;
            }
        }
    }

    private void launchActivity(int i) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),mActivities.get(i));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setLeftNavifation(CommonData.LEFT_SELECT_HOME);
            finish();
            return  true;
        }
        return super.onKeyDown(keyCode,event);
    }
}