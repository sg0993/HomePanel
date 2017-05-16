package com.honeywell.homepanel.ui.activities;


import android.app.Activity;
import android.app.usage.UsageEvents;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.EventBusWrapper;
import com.honeywell.homepanel.Utils.IConfigServiceManageUtil;
import com.honeywell.homepanel.Utils.RelativeTimer;
import com.honeywell.homepanel.Utils.RelativeTimerTask;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUICall;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUIControl;
import com.honeywell.homepanel.deviceadapter.adapters.elevator.honelevator.elevatorLibThread;
import com.honeywell.homepanel.ui.domain.UIBaseCallInfo;
import com.honeywell.homepanel.ui.uicomponent.UISendCallElevatorMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by H135901 on 2/16/2017.
 */

public class IndicatorBoardActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "IndicatorBoardActivity";
    private static  final  int [] TEXTES = {R.string.indicator_board_cancel,
            R.string.indicator_board_tap};
    private Button callBtn = null;
    private TextView floorView = null;
    private RelativeTimer mTimer;
    private RelativeTimerTask mTimerTask;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_deviceelevator);
        callBtn = (Button)findViewById(R.id.call_elevator);
        floorView = (TextView)findViewById(R.id.floor);

        mTimer = new RelativeTimer();
        EventBusWrapper.register(this);


        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UISendCallElevatorMessage.SendCallElevatorCommand();
            }
        });
    }

    public void onClickCancel(View view)
    {
        finish();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

        if (mTimerTask != null) {
            mTimerTask.cancel();
        }

        if (mTimer != null) {
            mTimer.cancel();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIControl.SUISCallElevatorRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {
            String errorCode = msg.optString(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_FAIL);
            final String elevatorId = msg.optString(CommonJson.JSON_ELEVATORID_KEY, "");

            // success call elevator
            if (CommonJson.JSON_ERRORCODE_VALUE_OK.equals(errorCode) && !TextUtils.isEmpty(elevatorId)) {
                mTimerTask = new RelativeTimerTask("QueryElevatorTask") {
                    @Override
                    public void run() {
                        UISendCallElevatorMessage.SendQueryElevatorCommand(elevatorId);
                    }
                };

                mTimer.schedule(mTimerTask, 100, 500);
            } else {
                Log.e(TAG, "Failed to call elevator!!!!!!");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIControl.SUISQueryElevatorRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {
            String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
            String floor = msg.optString(CommonJson.JSON_FLOORNO_KEY, "");

            if (!TextUtils.isEmpty(floor)) {
                floorView.setText(floor);
            }
        }
    }
}
