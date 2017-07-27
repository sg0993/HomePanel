package com.honeywell.homepanel.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.LogMgr;
import com.honeywell.homepanel.Utils.NotificationUtil;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.ui.domain.TopStaus;
import com.honeywell.homepanel.ui.widget.CycleProgressView;

import org.w3c.dom.Text;

import static android.R.attr.id;

/**
 * Created by H135901 on 2/20/2017.
 */
public class ScenarioSelectHintActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "ScenarioSelectHint";
    private CycleProgressView mProgressBar;
    private Button mCancelBtn = null;
    private int mSelect_Scenario = 4;
    private Thread mRunable = null;
    private int mCurrentProgress;
    private TextView mTextView;
    int size = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_scenarioselecthint);
        mSelect_Scenario = getIntent().getIntExtra(CommonData.INTENT_KEY_SCENARIO, 4);
        initViews();
        mRunable = new Thread() {
            @Override
            public void run() {
//                if (mProgressBar.getProgress() < 100) {
//                    mProgressBar.incrementProgressBy(1);
//                    postDelayed(mProgressBar, this, 20L);
//                } else {
//                    TopStaus.getInstance(getApplicationContext()).setCurScenario(mSelect_Scenario);
//                    finish();
//                }

                while (mCurrentProgress < mProgressBar.getMax()) {
                    if (mCurrentProgress % 100 == 0) {
//                        Message.obtain(mHandler, 0, mCurrentProgress / 100, 0).sendToTarget();
                        Message.obtain(mHandler, 0, size--, 0).sendToTarget();
                    }
                    mCurrentProgress++;
                    mProgressBar.setProgress(mCurrentProgress);
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (mCurrentProgress >= mProgressBar.getMax()) {
                    TopStaus.getInstance(getApplicationContext()).setCurScenario(mSelect_Scenario);
                    finish();
                }
            }
        };
//        postDelayed(mProgressBar, mRunable, 20L);
        mRunable.start();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTextView.setText(getString(R.string.exit_freely_1) + " " + (msg.arg1 < 10 ? "0" + msg.arg1 :
                    msg.arg1) + " " + getString(R.string.exit_freely_seconds));
        }
    };

    private void postDelayed(CycleProgressView progressBar, Runnable runnable, long delay) {
        progressBar.postDelayed(runnable, delay);
    }

    private void initViews() {
        mProgressBar = ((CycleProgressView) findViewById(R.id.progress_bar));
        mCancelBtn = (Button) findViewById(R.id.cancel);
        mCancelBtn.setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.tv_one);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.cancel:
//                if (null != mProgressBar && mRunable != null) {
//                    mProgressBar.removeCallbacks(mRunable);
//                }
                if (mRunable != null) {
                    mRunable.interrupt();
                }
                mRunable = null;
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        NotificationUtil.getNotificationUtil().afreshShow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
