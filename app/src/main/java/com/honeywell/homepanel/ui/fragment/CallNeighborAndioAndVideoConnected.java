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
import android.widget.Toast;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.ui.activities.CallActivity;
import com.honeywell.homepanel.ui.uicomponent.CalRightBrusher;
import com.honeywell.homepanel.ui.uicomponent.CallAnimationBrusher;
import com.honeywell.homepanel.ui.uicomponent.CallBottomBrusher;
import com.honeywell.homepanel.ui.uicomponent.CallTopBrusher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class CallNeighborAndioAndVideoConnected extends Fragment implements View.OnClickListener{
    private String mTitle = "";
    private static  final  String TAG = "CallNeighborAndioAndVideoConnected";
    private Context mContext = null;


    private CallBottomBrusher mCallBottomBrusher = new CallBottomBrusher
            (this,R.mipmap.call_blue_background,R.mipmap.call_lift_image,"Lift",
                    R.mipmap.call_blue_background,R.mipmap.call_audio_image,"Audio",
                    R.mipmap.call_red_background,R.mipmap.call_end_image,"End");

    private CalRightBrusher mCallRightBrusher = new CalRightBrusher(
            this,R.mipmap.call_right_background,R.mipmap.call_microphone,
            R.mipmap.call_right_background,R.mipmap.call_speaker);

    private CallTopBrusher mCallTopBrusher = new CallTopBrusher(
            "20-1002","02:59"
    );
    private View mCallTopView = null;

    private CallAnimationBrusher mAnimationBtusher = new
            CallAnimationBrusher(R.mipmap.call_audio_bright,R.mipmap.call_audio_dim);
    private View mCallAnimationView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"CallNeighborAndioAndVideoConnected.onCreate() 11111111");
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"CallNeighborAndioAndVideoConnected.onCreateView() 11111111");
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_neighbor_video_connected, null);
        initViews(view);
        mCallBottomBrusher.init(view);
        mCallRightBrusher.init(view);
        mCallTopBrusher.init(view);
        mAnimationBtusher.init(view);
        mCallTopView = view.findViewById(R.id.call_top);
        mCallAnimationView = view.findViewById(R.id.call_animation);
        changeViewStatus();
        return view;
    }

    private void changeViewStatus() {
        if(getActivity() instanceof CallActivity){
            int status = ((CallActivity)getActivity()).getCurFragmentStatus();
            Log.d(TAG,"CallNeighborAndioAndVideoConnected.changeViewStatus() status:"+status);
            if(status == CommonData.CALL_CONNECTED_AUDIO_NETGHBOR){
                mCallTopView.setVisibility(View.GONE);
                mCallAnimationView.setVisibility(View.VISIBLE);
                mCallBottomBrusher.setImageRes(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,R.mipmap.call_blue_background,R.mipmap.call_video_image);
                mCallBottomBrusher.setTextRes(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,"Video");
            }
            else{
                mCallTopView.setVisibility(View.VISIBLE);
                mCallAnimationView.setVisibility(View.GONE);
                mCallBottomBrusher.setImageRes(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,R.mipmap.call_blue_background,R.mipmap.call_audio_image);
                mCallBottomBrusher.setTextRes(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,"Audio");
            }
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG,"CallNeighborAndioAndVideoConnected.onResume() 11111111");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mAnimationBtusher.destroy();
        Log.d(TAG,"CallNeighborAndioAndVideoConnected.onDestroy() 11111111");
        super.onDestroy();
    }

    public CallNeighborAndioAndVideoConnected(String title) {
        super();
        this.mTitle = title;
    }
    public CallNeighborAndioAndVideoConnected() {
        super();
    }

    private void initViews(View view) {

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.left_btn:
                Toast.makeText(mContext,"call_left",Toast.LENGTH_SHORT).show();
                break;
            case R.id.middle_btn:
                int status = ((CallActivity)getActivity()).getCurFragmentStatus();
                if(status == CommonData.CALL_CONNECTED_AUDIO_NETGHBOR){
                    status = CommonData.CALL_CONNECTED_VIDEO_NETGHBOR;
                }
                else{
                    status = CommonData.CALL_CONNECTED_AUDIO_NETGHBOR;
                }
                ((CallActivity)getActivity()).setCurFragmentStatus(status);
                changeViewStatus();
                break;
            case R.id.right_btn:
                Toast.makeText(mContext,"call_right",Toast.LENGTH_SHORT).show();
                break;
            case R.id.top_btn:
                Toast.makeText(mContext,"top_btn",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bottom_btn:
                Toast.makeText(mContext,"bottom_btn",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }
}
