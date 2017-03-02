package com.honeywell.homepanel.ui.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.ui.uicomponent.ImageAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by H135901 on 2/16/2017.
 */

public class ScenarioSelectActivity extends Activity {
    GridView gridView;
    private static final int[] TEXTES = {R.string.scenario_home,
            R.string.scenario_away,
            R.string.scenario_sleep,
            R.string.scenario_wakeup};
    private static final int[] IMAGES = {R.mipmap.home_sel, R.mipmap.away, R.mipmap.sleep, R.mipmap.wakeup};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        setContentView(R.layout.layout_homescenarioselect);

        GridView gridView = (GridView) findViewById(R.id.gridView);

        gridView.setAdapter(new ImageAdapter(this, IMAGES, TEXTES));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
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
}
