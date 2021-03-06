package com.honeywell.homepanel.ui.activities;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.EventBusWrapper;
import com.honeywell.homepanel.Utils.LogMgr;
import com.honeywell.homepanel.Utils.RelativeTimer;
import com.honeywell.homepanel.Utils.RelativeTimerTask;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUIControl;
import com.honeywell.homepanel.common.utils.CommonUtils;
import com.honeywell.homepanel.ui.uicomponent.UISendCallElevatorMessage;
import com.honeywell.homepanel.ui.widget.RoundProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;

import static com.honeywell.homepanel.common.CommonJson.JSON_ERRORCODE_VALUE_OK;
import static java.lang.Integer.MAX_VALUE;

/**
 * Created by H135901 on 2/16/2017.
 */

public class IndicatorBoardActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "IndicatorBoardActivity";
    private static final int[] TEXTES = {R.string.indicator_board_cancel,
            R.string.indicator_board_tap};
    private Button callBtn = null;
    private TextView floorView = null;
    private TextView stateView = null;
    private RelativeTimerTask mTimerTask;
    private RoundProgressBar mRoundProgressBar;
    private float jingdu;
    private boolean isBtn;
    private String mCurrentElevator = "";
    private String TestCurrentEle = "26";
    private Toast mToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_deviceelevator);
//        IConfigServiceManageUtil.getInstance(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mRoundProgressBar = (RoundProgressBar) findViewById(R.id.roundprogressbar_view);
        mRoundProgressBar.setmRadius(250);
        callBtn = (Button) findViewById(R.id.call_elevator);
        floorView = (TextView) findViewById(R.id.floor);
        stateView = (TextView) findViewById(R.id.text_view);
        EventBusWrapper.register(this);

        findViewById(R.id.fragment_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBtn) return;
//                mCurrentElevator = IConfigServiceManageUtil.getStringMapConfigM(CommonJson.JSON_FLOORNO_KEY);
//                if (TextUtils.isEmpty(mCurrentElevator) ||
//                        getString(R.string.unknown).equalsIgnoreCase(mCurrentElevator)) {
//                    Toast.makeText(IndicatorBoardActivity.this, "请先配置电梯!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                // TODO: 2017/7/20  正式代码
                UISendCallElevatorMessage.SendCallElevatorCommand();

                // TODO: 2017/7/20  测试代码
//                setTest();
                isBtn = true;
                CommonUtils.sendUIEventBusCommonCmd(CommonData.SCREEN_DISABLE);
            }
        });

    }

    public void onClickCancel(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mRoundProgressBar.setProgressBar(0);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
//        if (IConfigServiceManageUtil.getIConfigServiceManage() != null)
//            IConfigServiceManageUtil.getIConfigServiceManage().delete();
        stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogMgr.d("IndicatorBoardActivity->onDestroy()");
        EventBus.getDefault().unregister(this);
        CommonUtils.sendUIEventBusCommonCmd(CommonData.SCREEN_WEAKUP_AND_ENABLE);
    }


    private void stop() {
        isBtn = false;
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }
        mTimerTask = null;

        if (mToast != null) {
            mToast.cancel();
        }
        mToast = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIControl.SUISCallElevatorRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {
            String errorCode = msg.optString(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_FAIL);
            final String elevatorId = msg.optString(CommonJson.JSON_ELEVATORID_KEY, "");

            // success call elevator
            if (JSON_ERRORCODE_VALUE_OK.equals(errorCode) && !TextUtils.isEmpty(elevatorId)) {
                if (mTimerTask == null) {
                    mTimerTask = new RelativeTimerTask("QueryElevatorTask") {
                        @Override
                        public void run() {
                            UISendCallElevatorMessage.SendQueryElevatorCommand(elevatorId);
                        }
                    };
                    RelativeTimer.getDefault().schedule(mTimerTask, 100, 500);
                }
            } else {
                Log.e(TAG, "Failed to call elevator!!!!!!");

                Toast.makeText(IndicatorBoardActivity.this, "请先配置电梯!", Toast.LENGTH_SHORT).show();
                stop();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIControl.SUISAuthElevatorRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {
            String errorCode = msg.optString(CommonJson.JSON_ERRORCODE_KEY, CommonJson.JSON_ERRORCODE_VALUE_FAIL);
            final String elevatorId = msg.optString(CommonJson.JSON_ELEVATORID_KEY, "");

            // success auth elevator
            if (JSON_ERRORCODE_VALUE_OK.equals(errorCode) && !TextUtils.isEmpty(elevatorId)) {
                if (mTimerTask == null) {
                    mTimerTask = new RelativeTimerTask("QueryElevatorTask") {
                        @Override
                        public void run() {
                            UISendCallElevatorMessage.SendQueryElevatorCommand(elevatorId);
                        }
                    };
                }
                RelativeTimer.getDefault().schedule(mTimerTask, 100, 500);
            } else {
                Log.e(TAG, "Failed to auth elevator!!!!!!");
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIControl.SUISQueryElevatorRsp msg) {
        if (msg == null) return;

        LogMgr.d("msg:" + msg.toString());

        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");

        String errorcode = msg.optString(CommonJson.JSON_ERRORCODE_KEY, "");

        if (!JSON_ERRORCODE_VALUE_OK.equals(errorcode)) {
            mToast = Toast.makeText(IndicatorBoardActivity.this,
                    "errorcode:" + errorcode, Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {

            String floor = msg.optString(CommonJson.JSON_FLOORNO_KEY, "");
            String mFloor = msg.optString(CommonJson.JSON_MAIN_FLOORNO_KEY, "");
            LogMgr.e("mCurrentElevator:" + mCurrentElevator + "  floor:" + floor);
            LogMgr.e("mainfloor:" + mFloor);

            if (TextUtils.isEmpty(floor) || TextUtils.isEmpty(mFloor)) {
                return;
            }

            if (getString(R.string.unknown).equalsIgnoreCase(mFloor)) {
                Toast.makeText(IndicatorBoardActivity.this, "请先配置电梯!", Toast.LENGTH_SHORT).show();
                stop();
                return;
            }


            floorView.setText(floor);

            if (getDistance(mFloor, floor) == -1) return;


            mRoundProgressBar.setProgressBar(result);


            if (TextUtils.equals(mFloor, floor)) {
                stop();
                handler.sendEmptyMessageDelayed(0, 1000);
                mRoundProgressBar.setProgressBar(1);
                stateView.setText(getString(R.string.demofloor3));
            } else {
                stateView.setText(getString(R.string.demofloor2));
            }

        }
    }


    private int getDistance(String floor1, String floor2) {
        if (a == 0) {
            a = StrToInt(floor1);
            b = StrToInt(floor2);
            if (b == MAX_VALUE) {
                stop();
            }
            if (b < 0) {
                juli = a - b;
            } else {
                juli = Math.abs(a - b);
            }

            LogMgr.e("juli:" + juli);

            if (juli == 0) {
                handler.sendEmptyMessageDelayed(0, 1000);
                mRoundProgressBar.setProgressBar(1);
                stateView.setText(getString(R.string.demofloor3));
                stop();
                return -1;
            }

            LogMgr.e("juli:" + juli + "  result:" + result);

            result = div(1, juli, 5);
        }
        return 1;
    }


    private int a;
    private int b;
    private int juli = 0;
    private float result = 0f;

   /* private void setTest() {
        LogMgr.e("mCurrentElevator:" + mCurrentElevator);
        mTimerTask = new RelativeTimerTask("TestElevtor") {
            @Override
            public void run() {

                if (a == 0) {
                    a = StrToInt(mCurrentElevator);
                    b = StrToInt(TestCurrentEle);
                    if (b == MAX_VALUE) return;
                    if (b < 0) {
                        juli = a - b;
                    } else {
                        juli = Math.abs(a - b);
                    }

                    if (juli == 0) {
                        floorView.post(new Runnable() {
                            @Override
                            public void run() {
                                floorView.setText(mCurrentElevator);
                            }
                        });

                        stop();
                        mRoundProgressBar.setProgressBar(1);
                        stateView.post(new Runnable() {
                            @Override
                            public void run() {
                                stateView.setText(getString(R.string.demofloor3));
                            }
                        });
                        handler.sendEmptyMessageDelayed(0, 1000);
                        return;
                    }
                }


                if (TextUtils.equals(mCurrentElevator, b + "")) {
                    stop();
                    mRoundProgressBar.setProgressBar(1);
                    stateView.post(new Runnable() {
                        @Override
                        public void run() {
                            stateView.setText(getString(R.string.demofloor3));
                        }
                    });
                    handler.sendEmptyMessageDelayed(0, 1000);
                    return;
                } else {
                    if (result == 0f) {
//                        if (jieguo >= 360) {
//                            jieguo = jieguo + 360;
//                        } else {
//                        }
                        result = (float) div(1, juli, 5);
//                        result = Math.floor(r)
                    }
                    mRoundProgressBar.setProgressBar(result);
                    LogMgr.e("juli:" + juli + "  result:" + result);
                }

                if (b < a) {
                    b++;
                } else {
                    b--;
                }

                floorView.post(new Runnable() {
                    @Override
                    public void run() {
                        floorView.setText(b + "");
                        stateView.setText(getString(R.string.demofloor2));
                    }
                });
            }
        };

        RelativeTimer.getDefault().schedule(mTimerTask, 10, 500);
    }*/

    private float div(int v1, int v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Integer.toString(v1));
        BigDecimal b2 = new BigDecimal(Integer.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    private int StrToInt(String string) {
        if (TextUtils.isEmpty(string)) return MAX_VALUE;
        int current = MAX_VALUE;
        try {
            current = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return current;
    }
}
