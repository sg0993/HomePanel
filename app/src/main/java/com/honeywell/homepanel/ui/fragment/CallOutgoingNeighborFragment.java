package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUICall;
import com.honeywell.homepanel.ui.activities.CallActivity;
import com.honeywell.homepanel.ui.uicomponent.CallAnimationBrusher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class CallOutgoingNeighborFragment extends CallBaseFragment implements View.OnClickListener{
    private String mTitle = "";
    private static  final  String TAG = "CallOutgoing";
    private Context mContext = null;

    private TextView mUnitTv = null;
    private Button mCancelBtn = null;

    private static String UNIT_PRESTR = null;
    private CallAnimationBrusher mAnimationBtusher = new
            CallAnimationBrusher(R.mipmap.call_outgoing_bright,R.mipmap.call_outgoing_dim);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Log.d(TAG,"CallOutgoingNeighborFragment.onCreate() 11111111");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        UNIT_PRESTR = getString(R.string.dialing);
        View view = inflater.inflate(R.layout.fragment_calloutgoing_neighborl, null);
        initViews(view);
        mAnimationBtusher.init(view);
        Log.d(TAG,"CallOutgoingNeighborFragment.onCreateView() 11111111");
        return view;
    }
    @Override
    public void onResume() {
        Log.d(TAG,"CallOutgoingNeighborFragment.onResume() 11111111");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"CallOutgoingNeighborFragment.onDestroy() 11111111");
        EventBus.getDefault().unregister(this);
        mAnimationBtusher.destroy();
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
        mUnitTv  = (TextView)view.findViewById(R.id.unit_tv);
        if(getActivity() instanceof CallActivity){
            mUnitTv.setText(UNIT_PRESTR + ((CallActivity) getActivity()).mUnit);
        }
        mCancelBtn = (Button)view.findViewById(R.id.cancel_btn);
        mCancelBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.cancel_btn:
               // getActivity().finish();
                CallActivity.switchFragmentInFragment(this, CommonData.CALL_CONNECTED_AUDIO_NETGHBOR);
            default:
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISCallOutMessageRsp msg) {
        String action = msg.optString(CommonData.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonData.JSON_ACTION_VALUE_RESPONSE)) {
            String uuid = msg.optString(CommonData.JSON_UUID_KEY, "");
            String callType = msg.optString(CommonData.JSON_CALLTYPE_KEY, "");
            String aliasName = msg.optString(CommonData.JSON_ALIASNAME_KEY, "");
            CallActivity.CallBaseInfo.setCallUuid(uuid);
            CallActivity.switchFragmentInFragment(this, CommonData.CALL_CONNECTED_AUDIO_NETGHBOR);
        }
    }
}
