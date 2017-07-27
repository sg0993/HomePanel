package com.honeywell.homepanel.ui.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.ui.uicomponent.AdapterCallback;
import com.honeywell.homepanel.ui.uicomponent.ImageAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.honeywell.homepanel.ui.domain.TopStaus;

/**
 * Created by H135901 on 2/16/2017.
 */

public class ScenarioSelectActivity extends Activity implements AdapterCallback {
    GridView gridView;
    TextView abnormalTextView;
    int scenario;
    private static final int[] TEXTES = {R.string.scenario_home,
            R.string.scenario_away,
            R.string.scenario_sleep,
            R.string.scenario_wakeup};
    private static final int[] IMAGES = {R.mipmap.home_blue, R.mipmap.away,
            R.mipmap.sleep, R.mipmap.wake_up};

    private static final int[] IMAGES1 = {R.mipmap.home_blue, R.mipmap.away,
            R.mipmap.sleep, R.mipmap.wake_up};
    private static final int[] IMAGES2 = {R.mipmap.home_sel, R.mipmap.away_red,
            R.mipmap.sleep, R.mipmap.wake_up};
    private static final int[] IMAGES3 = {R.mipmap.home_sel, R.mipmap.away,
            R.mipmap.sleep_red, R.mipmap.wake_up};
    private static final int[] IMAGES4 = {R.mipmap.home_sel, R.mipmap.away,
            R.mipmap.sleep, R.mipmap.wake_up_red};

    private static final int[] IMAGES_DOWN = {R.mipmap.home_blue, R.mipmap.away_red,
            R.mipmap.sleep_red, R.mipmap.wake_up_blue};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        setContentView(R.layout.layout_homescenarioselect);

        GridView gridView = (GridView) findViewById(R.id.gridView);

        abnormalTextView = (TextView) findViewById(R.id.screen_type);
        scenario = TopStaus.getInstance(this.getApplicationContext()).getCurScenario();
        switch (scenario){
            case CommonData.SCENARIO_HOME:
                abnormalTextView.setText(R.string.currently_scene_home);
                gridView.setAdapter(new ImageAdapter(getApplicationContext(),this,IMAGES1,IMAGES_DOWN, TEXTES,0));
                break;
            case CommonData.SCENARIO_AWAY:
                abnormalTextView.setText(R.string.currently_scene_away);
                gridView.setAdapter(new ImageAdapter(getApplicationContext(),this,IMAGES2,IMAGES_DOWN, TEXTES, 1));
                break;
            case CommonData.SCENARIO_SLEEP:
                abnormalTextView.setText(R.string.currently_scene_sleep);
                gridView.setAdapter(new ImageAdapter(getApplicationContext(),this,IMAGES3,IMAGES_DOWN, TEXTES ,2));
                break;
            case CommonData.SCENARIO_WAKEUP:
                abnormalTextView.setText(R.string.currently_scene_wake_up);
                gridView.setAdapter(new ImageAdapter(getApplicationContext(),this,IMAGES4,IMAGES_DOWN, TEXTES, 3));
                break;
            default:
                gridView.setAdapter(new ImageAdapter(getApplicationContext(),this,IMAGES,IMAGES_DOWN, TEXTES ,0));
                break;
        }
        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
              *//*  *//*
                Log.d(TAG,"positon:"+position);
                Toast.makeText(ScenarioSelectActivity.this,"positon:"+position,Toast.LENGTH_SHORT).show();
            }
        });*/
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ScenarioSelectActivity.this, PasswordEnterActivity.class);
                intent.putExtra(CommonData.INTENT_KEY_SCENARIO, position + 1);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }

    @Override
    public void subviewOnclick(int position, String more) {
        Intent intent = new Intent(ScenarioSelectActivity.this, PasswordEnterActivity.class);
        intent.putExtra(CommonData.INTENT_KEY_SCENARIO, position + 1);
        startActivity(intent);
        finish();
    }
}
