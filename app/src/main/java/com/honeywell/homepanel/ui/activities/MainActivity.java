package com.honeywell.homepanel.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.ui.domain.TopStaus;
import com.honeywell.homepanel.watchdog.WatchDogService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity{
    private static final String TAG = "MainActivity";

    //private PageViewAdapter mPageAdaper = null;

    private View mTopView = null;
    private View mCenterView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLeftNavifation(CommonData.LEFT_SELECT_HOME);

        // start watch dog
        startService(new Intent(this, WatchDogService.class));

        mTopView = findViewById(R.id.top_status);
        mCenterView = findViewById(R.id.main_frameLayout);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setTopViewBackground(int curScenario){
        if(curScenario == CommonData.SCENARIO_HOME || curScenario == CommonData.SCENARIO_WAKEUP){
            mTopView.setBackgroundColor(getResources().getColor(R.color.topbackground_disarm));
        }
        else if(curScenario == CommonData.SCENARIO_AWAY || curScenario == CommonData.SCENARIO_SLEEP){
            mTopView.setBackgroundColor(getResources().getColor(R.color.topbackground_arm));
        }
    }

    private void setCenterViewBackground(int curScenario){
        if(curScenario == CommonData.SCENARIO_HOME || curScenario == CommonData.SCENARIO_WAKEUP){
            mCenterView.setBackgroundColor(getResources().getColor(R.color.centerbackground_disarm));
        }
        else if(curScenario == CommonData.SCENARIO_AWAY || curScenario == CommonData.SCENARIO_SLEEP){
            mCenterView.setBackgroundColor(getResources().getColor(R.color.centerbackground_arm));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackgroundByScenario(TopStaus.getInstance(this).mCurScenario);
        Log.d(TAG,"onResume() mCurScenario:"+ TopStaus.getInstance(this).mCurScenario);
    }

    private void setBackgroundByScenario(int scenario) {
        setTopViewBackground(scenario);
        setCenterViewBackground(scenario);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void OnMessageEvent(MessageEvent event)
    {

    }
}
