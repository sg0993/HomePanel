package com.honeywell.homepanel.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.NotificationUtil;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.ui.domain.TopStaus;

/**
 * Created by H135901 on 2/20/2017.
 */
public class ScenarioSelectHintActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "ScenarioSelectHint";
    private ProgressBar mProgressBar;
    private Button mCancelBtn = null;
    private int mSelect_Scenario = 4;
    private Runnable mRunable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_scenarioselecthint);
        mSelect_Scenario = getIntent().getIntExtra(CommonData.INTENT_KEY_SCENARIO,4);
        initViews();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(mProgressBar.getProgress() < 100){
                    mProgressBar.incrementProgressBy(1);
                    postDelayed(mProgressBar, this, 20L);
                }
                else{
                    TopStaus.getInstance(getApplicationContext()).setCurScenario(mSelect_Scenario);
                    finish();
                }
            }
        };
        mRunable = runnable;
        postDelayed(mProgressBar, runnable, 20L);
    }

    private void postDelayed(ProgressBar progressBar, Runnable runnable, long delay){
        progressBar.postDelayed(runnable, delay);
    }

    private void initViews() {
        mProgressBar = ((ProgressBar)findViewById(R.id.progress_bar));
        mCancelBtn = (Button)findViewById(R.id.cancel);
        mCancelBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.cancel:
                if(null != mProgressBar && mRunable != null){
                    mProgressBar.removeCallbacks(mRunable);
                }
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
