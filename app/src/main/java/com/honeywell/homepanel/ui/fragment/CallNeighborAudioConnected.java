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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class CallNeighborAudioConnected extends Fragment implements View.OnClickListener{
    private String mTitle = "";
    private static  final  String TAG = "CallNeighborAudioConnected";
    private Context mContext = null;

    private CallAnimationBrusher mCallAnimationBrusher = new
            CallAnimationBrusher(R.mipmap.call_audio_bright,R.mipmap.call_audio_dim);
    private CallBottomBrusher mCallBottomBrusher = new CallBottomBrusher
            (this,R.mipmap.call_blue_background,R.mipmap.call_lift_image,"Lift",
                    R.mipmap.call_blue_background,R.mipmap.call_video_image,"Video",
                    R.mipmap.call_red_background,R.mipmap.call_end_image,"End");

    private CalRightBrusher mCallRightBrusher = new CalRightBrusher(
            this,R.mipmap.call_right_background,R.mipmap.call_microphone,
           /* R.mipmap.call_right_background,R.mipmap.call_volume_increase,*/
            R.mipmap.call_right_background,R.mipmap.call_speaker);
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Log.d(TAG,"CallNeighborAudioConnected.onCreate() 11111111");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        Log.d(TAG,"CallNeighborAudioConnected.onCreateView() 11111111");
        View view = inflater.inflate(R.layout.fragment_neighbor_audio_connected, null);
        initViews(view);
        mCallAnimationBrusher.init(view);
        mCallBottomBrusher.init(view);
        mCallRightBrusher.init(view);
        return view;
    }
    @Override
    public void onResume() {
        Log.d(TAG,"CallNeighborAudioConnected.onResume() 11111111");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"CallNeighborAudioConnected.onDestroy() 11111111");
        EventBus.getDefault().unregister(this);
        mCallAnimationBrusher.destroy();
        super.onDestroy();
    }

    public CallNeighborAudioConnected(String title) {
        super();
        this.mTitle = title;
    }
    public CallNeighborAudioConnected() {
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
                Toast.makeText(mContext,"call_middle",Toast.LENGTH_SHORT).show();
                CallActivity.switchFragmentInFragment(this,CommonData.CALL_CONNECTED_VIDEO_NETGHBOR);
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
