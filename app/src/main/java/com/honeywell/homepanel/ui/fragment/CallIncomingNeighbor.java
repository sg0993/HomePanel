package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.ui.activities.CallActivity;
import com.honeywell.homepanel.ui.uicomponent.CallAnimationBrusher;
import com.honeywell.homepanel.ui.uicomponent.CallBottomBrusher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class CallIncomingNeighbor extends Fragment implements View.OnClickListener{
    private String mTitle = "";
    private static  final  String TAG = "CallIncomingNeighbor";
    private Context mContext = null;

    private CallAnimationBrusher mCallAnimationBrusher = new
            CallAnimationBrusher(R.mipmap.call_audio_bright,R.mipmap.call_audio_dim);
    private CallBottomBrusher mCallBottomBrusher = new CallBottomBrusher
            (this,R.mipmap.call_incoming_background,R.mipmap.call_incoming_call,"Answer",
                    R.mipmap.call_red_background,R.mipmap.call_video_image,"End",
                    R.mipmap.call_red_background,R.mipmap.call_end_image,"End");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Log.d(TAG,"CallIncomingNeighbor.onResume() 11111111");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_incomingcall_neighbor, null);
        initViews(view);
        Log.d(TAG,"CallIncomingNeighbor.onCreateView() 11111111");
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

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.left_btn:
                CallActivity.switchFragmentInFragment(this,CommonData.CALL_CONNECTED_AUDIO_NETGHBOR);
                break;
            case R.id.right_btn:
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }
}