package com.honeywell.homepanel.ui.uicomponent;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.RelativeTimer;
import com.honeywell.homepanel.Utils.RelativeTimerTask;

/**
 * Created by H135901 on 2/23/2017.
 */

public class CallAnimationBrusher {
    private static final int TIME_FRESH = 1 * 1000;
    public static final int WHAT_TIME_FRESH = 100;


    private int mCallBrightRes = 0;
    private int mCallDimRes = 0;

    private View mTopView = null;
    private RelativeTimerTask mElapseTask;

    private ImageView call_left = null;
    private ImageView call_middle = null;
    private ImageView call_right = null;

    private int mCount = 0;
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case  WHAT_TIME_FRESH:
                    setImageViewSrc();
                    mCount++;
                    break;
            }
        }
    };

    public CallAnimationBrusher(int mCallBrightRes, int mCallDimRes) {
        this.mCallBrightRes = mCallBrightRes;
        this.mCallDimRes = mCallDimRes;
    }

    public void init(View view) {
        mTopView = view.findViewById(R.id.call_animation);
        call_left = (ImageView)mTopView. findViewById(R.id.call_left);
        call_middle = (ImageView) mTopView.findViewById(R.id.call_middle);
        call_right = (ImageView) mTopView.findViewById(R.id.call_right);;

        mElapseTask = new RelativeTimerTask("") {
            public void run() {
                mHandler.sendEmptyMessage(WHAT_TIME_FRESH);
            }
        };

        RelativeTimer.getDefault().schedule(mElapseTask,TIME_FRESH,TIME_FRESH);
    }

    private void setImageViewSrc() {
        int status = mCount % 3;
        switch (status){
            case 0:
                call_left.setImageResource(mCallDimRes);
                call_middle.setImageResource(mCallBrightRes);
                call_right.setImageResource(mCallDimRes);
                break;
            case 1:
                call_left.setImageResource(mCallDimRes);
                call_middle.setImageResource(mCallDimRes);
                call_right.setImageResource(mCallBrightRes);
                break;
            case 2:
                call_left.setImageResource(mCallBrightRes);
                call_middle.setImageResource(mCallDimRes);
                call_right.setImageResource(mCallDimRes);
                break;
            default:
                break;
        }
    }

    public void destroy(){
        mCount = 0;

        if (mElapseTask != null) {
            mElapseTask.cancel();
            mElapseTask = null;
        }
    }
}
