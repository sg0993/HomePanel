package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.CycleProgressViewUtil;
import com.honeywell.homepanel.Utils.LogMgr;
import com.honeywell.homepanel.Utils.RelativeTimer;
import com.honeywell.homepanel.Utils.RelativeTimerTask;
import com.honeywell.homepanel.Utils.WaveViewUtil;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUICall;
import com.honeywell.homepanel.ui.AudioVideoUtil.CameraWrapper;
import com.honeywell.homepanel.ui.AudioVideoUtil.TextureViewListener;
import com.honeywell.homepanel.ui.AudioVideoUtil.VideoDecoderThread;
import com.honeywell.homepanel.ui.activities.CallActivity;
import com.honeywell.homepanel.ui.activities.MainActivity;
import com.honeywell.homepanel.ui.domain.UIBaseCallInfo;
import com.honeywell.homepanel.ui.uicomponent.CalRightBrusher;
import com.honeywell.homepanel.ui.uicomponent.CallAnimationBrusher;
import com.honeywell.homepanel.ui.uicomponent.CallBottomBrusher;
import com.honeywell.homepanel.ui.uicomponent.CallTopBrusher;
import com.honeywell.homepanel.ui.uicomponent.UISendCallMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class CallNeighborAndioAndVideoConnected extends CallBaseFragment implements View.OnClickListener, CameraWrapper.CamOpenOverCallback {
    private String mTitle = "";
    private static final int MSG_SHOW_ELAPSE_TIME = 100;
    private static final String TAG = "CallNeighbor";
    //private Context mContext = null;


    private CallBottomBrusher mCallBottomBrusher = null;

    private CalRightBrusher mCallRightBrusher = new CalRightBrusher(
            this, R.mipmap.call_right_background, R.mipmap.call_microphone_audio,
            R.mipmap.call_right_background, R.mipmap.call_speaker_audio);

    private CallTopBrusher mCallTopBrusher = new CallTopBrusher(
            "20-1002", "02:59"
    );
    private View mCallTopView = null;

    private CallAnimationBrusher mAnimationBtusher =
            new CallAnimationBrusher(R.mipmap.call_audio_bright, R.mipmap.call_audio_dim);

//    private View mCallAnimationView = null;

    private TextView unit_tv = null;
    private TextView calltime_tv = null;

    private long lastTime = System.currentTimeMillis();

    private RelativeTimer mTimer = null;
    private RelativeTimerTask mTimerTask;
    private TextureView mCameraTexture = null;
    /******For other video***********/
    private TextureView mDecoderView;
    private TextureViewListener mDecoderTextureListener;
    private VideoDecoderThread mDecThread;
    private UIBaseCallInfo uiBaseCallInfo = new UIBaseCallInfo();

    private RelativeTimerTask mCalledTimeOutTask = new RelativeTimerTask("mCalledTimeOutTask"){
        @Override
        public void run() {
            Log.d(TAG, "mCalledTimeOutTask.run() 1111111111111");
            sendHungUpAndExitActivity();
        }
    };

    private void cancelCalled(){
        Log.d(TAG, "cancelCalled() 1111111111111111111111");
        if(null != mCalledTimeOutTask){
            mCalledTimeOutTask.cancel();
            mCalledTimeOutTask = null;
        }
    }

    private void sendHungUpAndExitActivity(){
        Log.d(TAG, "sendHungUpAndExitActivity() 11111111111111111");
        stopPlayRing();
        UISendCallMessage.requestForHungUp(MainActivity.CallBaseInfo);
        getActivity().finish();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "CallNeighborAndioAndVideoConnected.onCreate() 11111111");
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mCallBottomBrusher = new CallBottomBrusher
                (this, R.mipmap.call_blue_background, R.mipmap.call_lift_image, getString(R.string.lift),
                        R.mipmap.call_blue_background, R.mipmap.call_video_image, getString(R.string.video),
                        R.mipmap.call_red_background, R.mipmap.call_end_image, getString(R.string.end));
    }

    int status;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "CallNeighborAndioAndVideoConnected.onCreateView() 11111111");
        //mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_neighbor_video_connected, null);
        status = ((CallActivity) getActivity()).getCurFragmentStatus();
        initViews(view);
        changeViewStatus();
        if (status != 2) {
            initElapseTimer();
        }
        /*****For other audio show **************/
        startAudio(1.5);
        RelativeTimer.getDefault().schedule(mCalledTimeOutTask,CommonData.CALLED_TIMEOUT);
        return view;
    }

    private void initElapseTimer() {
        mTimer = RelativeTimer.getDefault();

        if (mTimerTask == null) {
            mTimerTask = new RelativeTimerTask("Call-Neigh") {
                @Override
                public void run() {
                    myHandler.sendEmptyMessage(MSG_SHOW_ELAPSE_TIME);
                }
            };
        }


        mTimer.schedule(mTimerTask, 1000, 1000);

    }

    private void changeViewStatus() {
        if (getActivity() instanceof CallActivity) {
            Log.d(TAG, "CallNeighborAndioAndVideoConnected.changeViewStatus() status:" + status);
            if (status == CommonData.CALL_CONNECTED_AUDIO_NETGHBOR) {
                LogMgr.e("mCycleProgressView==null:" + (mCycleProgressView == null));
                if (mCycleProgressView != null) {
                    mCycleProgressView.startCycleProgressView(calculateString());
                    mCycleProgressView.setTextColor(getResources().getColor(R.color.black));
                }
                mCallTopView.setVisibility(View.GONE);
//                mCallAnimationView.setVisibility(View.VISIBLE);
                mCallBottomBrusher.setImageRes(CallBottomBrusher.BOTTOM_POSTION_MIDDLE, R.color.transparent, R.mipmap.call_video_image);
                mCallBottomBrusher.setTextRes(CallBottomBrusher.BOTTOM_POSTION_MIDDLE, getString(R.string.video));
                mCallBottomBrusher.setVisible(CallBottomBrusher.BOTTOM_POSTION_MIDDLE, View.GONE);
                mCameraTexture.setVisibility(View.INVISIBLE);
            } else {
                mCallTopView.setVisibility(View.VISIBLE);
//                mCallAnimationView.setVisibility(View.GONE);
                mCallBottomBrusher.setImageRes(CallBottomBrusher.BOTTOM_POSTION_MIDDLE, R.mipmap.call_blue_background, R.mipmap.call_audio_image);
                mCallBottomBrusher.setTextRes(CallBottomBrusher.BOTTOM_POSTION_MIDDLE, getString(R.string.audio));
                mCameraTexture.setVisibility(View.VISIBLE);
            }
        }
    }

    private String calculateString() {
        String strMUnit = "";
        if (getActivity() != null && getActivity() instanceof CallActivity) {
            strMUnit = ((CallActivity) getActivity()).mUnit;
            LogMgr.d("strMUnit.length():" + strMUnit.length() + " strMUnit:" + strMUnit);
            try {
                if (!TextUtils.isEmpty(strMUnit) && strMUnit.length() >= 10) {
                    String dong = strMUnit.substring(0, 4);
                    String hao = strMUnit.substring(4, strMUnit.length());
                    for (int i = 0; i < dong.length(); i++) {
                        if (dong.charAt(i) != '0') {
                            dong = dong.substring(i, dong.length());
                            break;
                        }
                    }
                    for (int j = 0; j < hao.length(); j++) {
                        if (hao.charAt(j) != '0') {
                            hao = hao.substring(j, hao.length());
                            break;
                        }
                    }
                    strMUnit = dong + "-" + hao;

                }
            } catch (Exception e) {
                e.printStackTrace();
                strMUnit = ((CallActivity) getActivity()).mUnit;
            }
        }

        return strMUnit;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "CallNeighborAndioAndVideoConnected.onResume() 11111111");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //mAnimationBtusher.destroy();
        Log.d(TAG, "CallNeighborAndioAndVideoConnected.onDestroy() 11111111");
        mCycleProgressView.stopCycleProgressView();
        if (myHandler.hasMessages(MSG_SHOW_ELAPSE_TIME)) {
            myHandler.removeMessages(MSG_SHOW_ELAPSE_TIME);
        }
        myHandler.removeCallbacksAndMessages(mTimerTask);

        if (mTimerTask != null) {
            mTimerTask.cancel();
        }

        mTimerTask = null;
        mTimer = null;

        /****For other video show***********/
        mDecThread.stop();
        cancelCalled();
    }

    public CallNeighborAndioAndVideoConnected(String title) {
        super();
        this.mTitle = title;
    }

    public CallNeighborAndioAndVideoConnected() {
        super();
    }

    private WaveViewUtil mWaveView;
    private CycleProgressViewUtil mCycleProgressView;


    private void initViews(View view) {
        mWaveView = WaveViewUtil.getInstance(view, getActivity().getApplicationContext());
        if (mWaveView != null) mWaveView.stopWaveView();
        mCycleProgressView = CycleProgressViewUtil.getInstance(view, getActivity().getApplicationContext());
        mCallTopView = view.findViewById(R.id.call_top);
//        mCallAnimationView = view.findViewById(R.id.call_animation);
        mCallBottomBrusher.init(view);
        mCallRightBrusher.init(view);
        mCallTopBrusher.init(view);
//        mAnimationBtusher.init(view);
        if (status != 2) {
            unit_tv = (TextView) view.findViewById(R.id.unit_tv);
            calltime_tv = (TextView) view.findViewById(R.id.calltime_tv);
            unit_tv.setText(((CallActivity) getActivity()).mUnit);
            calltime_tv.setText("00:00");
            mCallTopBrusher.setResText(CallTopBrusher.POSITION_TOP, ((CallActivity) getActivity()).mUnit);
            mCallTopBrusher.setResText(CallTopBrusher.POSITION_BOTTOM, "00:00");
        }
        /**********For other video show ***************/
        mDecoderView = (TextureView) view.findViewById(R.id.textview_other); // mediacodec
        // multimedia
        mDecoderTextureListener = new TextureViewListener();
        mDecoderView.setSurfaceTextureListener(mDecoderTextureListener);
        mDecThread = new VideoDecoderThread(mDecoderTextureListener, mVideoPlayQueue);
        new Thread(mDecThread).start();
        /*************************/

        //for own show
        mCameraTexture = (TextureView) view.findViewById(R.id.own_textview);
        mCameraTexture.setSurfaceTextureListener(camTextureListener);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.left_btn:
                UISendCallMessage.requestForCallElevator(MainActivity.CallBaseInfo);
                UISendCallMessage.requestForElevatorAuth(MainActivity.CallBaseInfo);
                break;
            case R.id.middle_btn:
                int status = ((CallActivity) getActivity()).getCurFragmentStatus();
                if (status == CommonData.CALL_CONNECTED_AUDIO_NETGHBOR) {
                    status = CommonData.CALL_CONNECTED_VIDEO_NETGHBOR;
                    UISendCallMessage.requestForVideoAuth(MainActivity.CallBaseInfo);
//                    //start video
//                    startVideoGet();
                } else {
                    status = CommonData.CALL_CONNECTED_AUDIO_NETGHBOR;
                    //stop video
                    stopVideoGet();
                }
                ((CallActivity) getActivity()).setCurFragmentStatus(status);
                changeViewStatus();
                break;
            case R.id.right_btn:
                cancelCalled();
                sendHungUpAndExitActivity();
                break;
            case R.id.top_btn:
                //Toast.makeText(mContext, "bottom_btn", Toast.LENGTH_SHORT).show();
                UISendCallMessage.requestForMute(MainActivity.CallBaseInfo);
                break;
            case R.id.bottom_btn:
                ((CallActivity) getActivity()).volumeSpeaker();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISVideoAuthMessageRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {
            String errorCode = msg.optString(CommonJson.JSON_ERRORCODE_KEY);
            if (errorCode.equals(CommonJson.JSON_ERRORCODE_VALUE_OK)) {
                System.out.println("SUISVideoAuthMessageRsp success");
                String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
                String callType = msg.optString(CommonJson.JSON_CALLTYPE_KEY, "");
                String aliasName = msg.optString(CommonJson.JSON_ALIASNAME_KEY, "");
                //start video
                startVideoGet();
            } else {
                //Toast.makeText(getActivity(), "VideoAuth failed", Toast.LENGTH_SHORT).show();
                System.out.println("SUISVideoAuthMessageRsp failed");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISCallElevatorMessageRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {
            String errorCode = msg.optString(CommonJson.JSON_ERRORCODE_KEY);
            if (errorCode.equals(CommonJson.JSON_ERRORCODE_VALUE_OK)) {
                System.out.println("SUISCallElevatorMessageRsp success");
                String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
                String callType = msg.optString(CommonJson.JSON_CALLTYPE_KEY, "");
                String aliasName = msg.optString(CommonJson.JSON_ALIASNAME_KEY, "");
            } else {
                //Toast.makeText(getActivity(), "CallElevator failed", Toast.LENGTH_SHORT).show();
                System.out.println("SUISCallElevatorMessageRsp failed");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISHungUpMessageRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {

            String errorCode = msg.optString(CommonJson.JSON_ERRORCODE_KEY);
            if (errorCode.equals(CommonJson.JSON_ERRORCODE_VALUE_OK)) {
                System.out.println("SUISHungUpMessageRsp success");
                String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
            } else {
                System.out.println("SUISHungUpMessageRsp failed");
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISMuteMessageRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {

            String errorCode = msg.optString(CommonJson.JSON_ERRORCODE_KEY);
            if (errorCode.equals(CommonJson.JSON_ERRORCODE_VALUE_OK)) {
                System.out.println("SUISMuteMessageRsp success");
                String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
                ((CallActivity) getActivity()).volumeMic();
            } else {
                System.out.println("SUISMuteMessageRsp failed");
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISCallTerminatedMessageEve msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");
        Log.d(TAG, "SUISCallTerminatedMessageEve = " + action.equals(CommonJson.JSON_ACTION_VALUE_EVENT));
        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_EVENT)) {
            Log.d(TAG, "SUISCallTerminatedMessageEve finish Activity");
            cancelCalled();
            getActivity().finish();
        }
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SHOW_ELAPSE_TIME) {
                long nowTime = System.currentTimeMillis();
                long l = nowTime - lastTime;
                long hour = (l / (60 * 60 * 1000));
                long min = ((l / (60 * 1000)) - hour * 60);
                long s = (l / 1000 - hour * 60 * 60 - min * 60);

                String minString = String.valueOf(min);
                if (min < 10) {
                    minString = "0" + min;
                }

                String sString = String.valueOf(s);
                if (s < 10) {
                    sString = "0" + s;
                }
                String timeString = minString + ":" + sString;
                calltime_tv.setText(timeString);
                mCallTopBrusher.setResText(CallTopBrusher.POSITION_BOTTOM, timeString);
            }
        }
    };

    ///////////////////////  camera texture view listener  ///////////////////////////
    private TextureView.SurfaceTextureListener camTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            Log.d(TAG, "camera onSurfaceTextureAvailable");

            /*if(CheckPermission.shouldAskPermission()) {
                if(CheckPermission.checkPermission(MainActivity.this)) {
                    doOpenCamera();
                } else {
                    Log.i(TAG, "Get permissions first");
                }
            } else {
                doOpenCamera();
            }*/
            doOpenCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            Log.d(TAG, "camera onSurfaceTextureDestroyed");
            CameraWrapper.getInstance().doStopCamera();
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }
    };


    private void doOpenCamera() {
        Thread openThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CameraWrapper.getInstance().doOpenCamera(mIAvrtpService, CallNeighborAndioAndVideoConnected.this);
            }
        };
        openThread.start();
    }

    @Override
    public void cameraHasOpened() {

        CameraWrapper.getInstance().doStartPreview(mCameraTexture.getSurfaceTexture()/*, mPreviewRate*/);
    }
}
