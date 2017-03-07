package com.honeywell.homepanel.ui.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.ui.domain.AlarmHint;
import com.honeywell.homepanel.ui.domain.TopStaus;
import com.honeywell.homepanel.ui.uicomponent.AlarmHintPopAdapter;
import com.honeywell.homepanel.watchdog.WatchDogService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity{
    private static final String TAG = "MainActivity";
    private View mCenterView = null;

    private PopupWindow mpopupWindow = null;

    private View content_scrolling = null;
    private  View mTopView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLeftNavifation(CommonData.LEFT_SELECT_HOME);

        // start watch dog
        startService(new Intent(this, WatchDogService.class));

        mTopView = findViewById(R.id.top_status);//for test
        mTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showpopupwindow(MainActivity.this.getLayoutInflater());
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
        setCenterViewBackground(TopStaus.getInstance(this).mCurScenario);
        Log.d(TAG,"onResume() mCurScenario:"+ TopStaus.getInstance(this).mCurScenario);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void OnMessageEvent(MessageEvent event)
    {

    }


    private void showpopupwindow(LayoutInflater inflater) {
        View popupView = inflater.inflate(R.layout.layout_alarm_hint, null);
        ListView listView = (ListView) popupView.findViewById(R.id.list_alarm);
        List<AlarmHint>lists = new ArrayList<AlarmHint>();
        for (int i = 0; i < 10; i++) {
            lists.add(new AlarmHint(i));
        }

        AlarmHintPopAdapter alarmHintPopAdapter = new AlarmHintPopAdapter(lists,this);
        listView.setAdapter(alarmHintPopAdapter);

        mpopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mpopupWindow.setBackgroundDrawable(new ColorDrawable());
        mpopupWindow.setOutsideTouchable(true);
        mpopupWindow.showAtLocation(content_scrolling, Gravity.LEFT | Gravity.TOP, 0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(mpopupWindow != null && mpopupWindow.isShowing()){
            mpopupWindow.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
