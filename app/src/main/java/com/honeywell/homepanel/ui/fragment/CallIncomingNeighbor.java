package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUICall;
import com.honeywell.homepanel.common.Message.ui.AlarmHint;
import com.honeywell.homepanel.ui.activities.CallActivity;
import com.honeywell.homepanel.ui.activities.MainActivity;
import com.honeywell.homepanel.ui.uicomponent.CallAnimationBrusher;
import com.honeywell.homepanel.ui.uicomponent.CallBottomBrusher;
import com.honeywell.homepanel.ui.uicomponent.UISendCallMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class CallIncomingNeighbor extends CallBaseFragment implements View.OnClickListener{
    private String mTitle = "";
    private static  final  String TAG = "CallIncomingNeighbor";
    private Context mContext = null;

    private CallAnimationBrusher mCallAnimationBrusher = new
            CallAnimationBrusher(R.mipmap.call_audio_bright,R.mipmap.call_audio_dim);
    private CallBottomBrusher mCallBottomBrusher = null;

    private TextView unitTv = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Log.d(TAG,"CallIncomingNeighbor.onResume() 11111111");
        mCallBottomBrusher = new CallBottomBrusher
                (this,R.mipmap.call_incoming_background,R.mipmap.call_incoming_call,getString(R.string.answer),
                        R.mipmap.call_red_background,R.mipmap.call_video_image,getString(R.string.end),
                        R.mipmap.call_red_background,R.mipmap.call_end_image,getString(R.string.end));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_incomingcall_neighbor, null);
        initViews(view);
        Log.d(TAG,"CallIncomingNeighbor.onCreateView() 11111111");
        startPlayRing(CALL_RING_NEIGHPHONE);
        mCallAnimationBrusher.init(view);
        mCallBottomBrusher.init(view);
        mCallBottomBrusher.setVisible(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,View.GONE);
        return view;
    }
    @Override
    public void onResume() {
        Log.d(TAG,"CallIncomingNeighbor.onResume() 11111111");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mCallAnimationBrusher.destroy();
        Log.d(TAG,"CallIncomingNeighbor.onDestroy() 11111111");
        super.onDestroy();
    }

    public CallIncomingNeighbor(String title) {
        super();
        this.mTitle = title;
    }
    public CallIncomingNeighbor() {
        super();
    }

    private void initViews(View view) {
        unitTv = (TextView)view.findViewById(R.id.unitTv);
        unitTv.setText(getString(R.string.neighbor) + "-" + ((CallActivity)getActivity()).mUnit);
    }

    private static int mTestAlarmCount = 1;
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.left_btn:
                UISendCallMessage.requestForTakeCall(MainActivity.CallBaseInfo);
                stopPlayRing();
                CallActivity.switchFragmentInFragment(this,CommonData.CALL_CONNECTED_AUDIO_NETGHBOR);
                break;
            case R.id.right_btn:
                //getActivity().finish();
                UISendCallMessage.requestForHungUp(MainActivity.CallBaseInfo);
                stopPlayRing();
                EventBus.getDefault().post(new AlarmHint(mTestAlarmCount++));
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISCallTerminatedMessageEve msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");
        stopPlayRing();
        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_EVENT)) {
            getActivity().finish();
        }
    }
}
