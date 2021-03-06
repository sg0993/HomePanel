package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.RelativeTimer;
import com.honeywell.homepanel.Utils.RelativeTimerTask;
import com.honeywell.homepanel.Utils.WaveViewUtil;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUICall;
import com.honeywell.homepanel.ui.RingFile.RingFileData;
import com.honeywell.homepanel.ui.activities.CallActivity;
import com.honeywell.homepanel.ui.activities.CallFailedActivity;
import com.honeywell.homepanel.ui.activities.MainActivity;
import com.honeywell.homepanel.ui.domain.UIBaseCallInfo;
import com.honeywell.homepanel.ui.uicomponent.UISendCallMessage;
import com.honeywell.homepanel.ui.widget.WaveView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class CallOutgoingNeighborFragment extends CallBaseFragment implements View.OnClickListener {
    private String mTitle = "";
    private static final String TAG = "CallOutgoing";
    //private Context mContext = null;

//    private TextView mUnitTv = null;
    private Button mCancelBtn = null;
    private WaveView mWaveView;
    private static String UNIT_PRESTR = null;

    private WaveViewUtil mWaveViewUtil = null;

//    private CallAnimationBrusher mAnimationBtusher = new
//            CallAnimationBrusher(R.mipmap.call_outgoing_bright, R.mipmap.call_outgoing_dim);
    private UIBaseCallInfo uiBaseCallInfo = new UIBaseCallInfo();

    private RelativeTimerTask mCallingTimeOutTask = new RelativeTimerTask("CallingTimeOutTask"){
        @Override
        public void run() {
            Log.d(TAG, "CallingTimeOutTask.run() 1111111111111");
            sendHungUpAndExitActivity();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Log.d(TAG, "CallOutgoingNeighborFragment.onCreate() 11111111");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //mContext = getActivity();
        UNIT_PRESTR = getString(R.string.dialing);
        View view = inflater.inflate(R.layout.fragment_calloutgoing_neighborl, null);
        initViews(view);
        //mAnimationBtusher.init(view);
        Log.d(TAG, "CallOutgoingNeighborFragment.onCreateView() 11111111");
        startPlayRing(RingFileData.CALL_RING_OUT);
        uiBaseCallInfo.setmCallType(((CallActivity) getActivity()).mCallType);
        uiBaseCallInfo.setmCallAliasName(((CallActivity) getActivity()).mUnit);
        UISendCallMessage.requestForCallOut(uiBaseCallInfo);
        RelativeTimer.getDefault().schedule(mCallingTimeOutTask,CommonData.CALLING_TIMEOUT);
        return view;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "CallOutgoingNeighborFragment.onResume() 11111111");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "CallOutgoingNeighborFragment.onDestroy() 11111111");
        EventBus.getDefault().unregister(this);
        //mAnimationBtusher.destroy();
        cancelCalling();
        super.onDestroy();
    }

    public CallOutgoingNeighborFragment(String title) {
        super();
        this.mTitle = title;
    }

    public CallOutgoingNeighborFragment() {
        super();
    }


    private void initViews(View view) {

        mWaveViewUtil = WaveViewUtil.getInstance(view, getActivity().getApplicationContext());
        mWaveViewUtil.startWavaView(true, WaveView.CallType.OUT);

//        mUnitTv = (TextView) view.findViewById(R.id.wave_view_tv_textview);
        if (getActivity() instanceof CallActivity) {
//            mUnitTv.setText(UNIT_PRESTR + ((CallActivity) getActivity()).mUnit);
            mWaveViewUtil.setText(getString(R.string.Dialing_) +" "+  ((CallActivity) getActivity()).mUnit);
        }
        mCancelBtn = (Button) view.findViewById(R.id.cancel_btn);
        mCancelBtn.setOnClickListener(this);
    }

    private void sendHungUpAndExitActivity(){
        Log.d(TAG, "sendHungUpAndExitActivity() 11111111111111111");
        UISendCallMessage.requestForHungUp(uiBaseCallInfo);
        stopPlayRing();
        if (mWaveViewUtil != null) mWaveViewUtil.stopWaveView();
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.cancel_btn:
                //startActivity(new Intent(getActivity(),MainActivity.class));
                /*UISendCallMessage.requestForHungUp(uiBaseCallInfo);
                stopPlayRing();
                if (mWaveViewUtil != null) mWaveViewUtil.stopWaveView();
                getActivity().finish();*/
                sendHungUpAndExitActivity();
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISCallOutMessageRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");
        System.out.println("SUISCallOutMessageRsp entering");
        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {
            String errorCode = msg.optString(CommonJson.JSON_ERRORCODE_KEY);
            String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
            String callType = msg.optString(CommonJson.JSON_CALLTYPE_KEY, "");
            String aliasName = msg.optString(CommonJson.JSON_ALIASNAME_KEY, "");
            uiBaseCallInfo.setmCallType(callType);
            uiBaseCallInfo.setmCallAliasName(aliasName);
            uiBaseCallInfo.setCallUuid(uuid);

            System.out.println("SUISCallOutMessageRsp errorCode" + errorCode);
            if (errorCode.equals(CommonJson.JSON_ERRORCODE_VALUE_OK)) {
                System.out.println("SUISCallOutMessageRsp success");
            } else {
                stopPlayRing();
                Toast.makeText(getActivity().getApplicationContext(), "callout failed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CallFailedActivity.class);
                intent.putExtra(CommonData.INTENT_KEY_UNIT, aliasName);
                startActivity(intent);
                System.out.println("SUISCallOutMessageRsp failed");
                if (mWaveViewUtil != null) mWaveViewUtil.stopWaveView();
                getActivity().finish();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISCallActivedMessageEve msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");
        stopPlayRing();
        Log.d(TAG, "SUISCallActivedMessageEve() msg:"+msg.toString()+",,111111111111");
        if (mWaveViewUtil != null) mWaveViewUtil.stopWaveView();
        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_EVENT)) {
            String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
            String callType = msg.optString(CommonJson.JSON_CALLTYPE_KEY, "");
            String aliasName = msg.optString(CommonJson.JSON_ALIASNAME_KEY, "");
            MainActivity.CallBaseInfo.setCallUuid(uuid);
            MainActivity.CallBaseInfo.setmCallAliasName(aliasName);
            MainActivity.CallBaseInfo.setmCallType(callType);
            CallActivity.switchFragmentInFragment(this, CommonData.CALL_CONNECTED_AUDIO_NETGHBOR);
            cancelCalling();
        }
    }

    private void cancelCalling(){
        Log.d(TAG, "cancelCalling() 1111111111111111111111");
        if(null != mCallingTimeOutTask){
            mCallingTimeOutTask.cancel();
            mCallingTimeOutTask = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISCallTerminatedMessageEve msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");
        stopPlayRing();
        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_EVENT)) {
            String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
            String callType = msg.optString(CommonJson.JSON_CALLTYPE_KEY, "");
            String aliasName = msg.optString(CommonJson.JSON_ALIASNAME_KEY, "");
            if (mWaveViewUtil != null) mWaveViewUtil.stopWaveView();
            getActivity().finish();
        }
    }
}
