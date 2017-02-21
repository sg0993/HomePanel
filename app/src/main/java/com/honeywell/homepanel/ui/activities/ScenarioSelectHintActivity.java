package com.honeywell.homepanel.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.honeywell.homepanel.R;

/**
 * Created by H135901 on 2/20/2017.
 */
public class ScenarioSelectHintActivity extends Activity implements View.OnClickListener{

    private ProgressBar mProgressBar;
    private Button mCancelBtn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_scenarioselecthint);
        initViews();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(mProgressBar.getProgress() < 100){
                    mProgressBar.incrementProgressBy(1);
                    postDelayed(mProgressBar, this, 300L);
                }

            }
        };
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
                finish();
                break;
            default:
                break;
        }
    }
}
