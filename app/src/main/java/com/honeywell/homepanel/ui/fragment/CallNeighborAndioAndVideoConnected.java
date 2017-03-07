package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class CallNeighborAndioAndVideoConnected extends Fragment implements View.OnClickListener{
    private String mTitle = "";
    private static final int MSG_SHOW_ELAPSE_TIME = 100;
    private static  final  String TAG = "CallNeighborAndioAndVideoConnected";
    private Context mContext = null;


    private CallBottomBrusher mCallBottomBrusher = null;

    private CalRightBrusher mCallRightBrusher = new CalRightBrusher(
            this,R.mipmap.call_right_background,R.mipmap.call_microphone,
            R.mipmap.call_right_background,R.mipmap.call_speaker);

    private CallTopBrusher mCallTopBrusher = new CallTopBrusher(
            "20-1002","02:59"
    );
    private View mCallTopView = null;

    private CallAnimationBrusher mAnimationBtusher = new CallAnimationBrusher(R.mipmap.call_audio_bright,R.mipmap.call_audio_dim);
    private View mCallAnimationView = null;

    private TextView unit_tv = null;
    private TextView calltime_tv = null;

    private long lastTime = System.currentTimeMillis();
    private Timer mTimer = null;
    private ImageView lefttop_image = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"CallNeighborAndioAndVideoConnected.onCreate() 11111111");
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mCallBottomBrusher = new CallBottomBrusher
                (this,R.mipmap.call_blue_background,R.mipmap.call_lift_image,getString(R.string.lift),
                        R.mipmap.call_blue_background,R.mipmap.call_audio_image,getString(R.string.video),
                        R.mipmap.call_red_background,R.mipmap.call_end_image,getString(R.string.end));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"CallNeighborAndioAndVideoConnected.onCreateView() 11111111");
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_neighbor_video_connected, null);
        initViews(view);
        changeViewStatus();
        initElapseTimer();
        return view;
    }

    private void initElapseTimer() {
        if(mTimer == null){
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            public void run() {
                myHandler.sendEmptyMessage(MSG_SHOW_ELAPSE_TIME);
            }
        },1000,1000);
    }

    private void changeViewStatus() {
        if(getActivity() instanceof CallActivity){
            int status = ((CallActivity)getActivity()).getCurFragmentStatus();
            Log.d(TAG,"CallNeighborAndioAndVideoConnected.changeViewStatus() status:"+status);
            if(status == CommonData.CALL_CONNECTED_AUDIO_NETGHBOR){
                mCallTopView.setVisibility(View.GONE);
                mCallAnimationView.setVisibility(View.VISIBLE);
                mCallBottomBrusher.setImageRes(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,R.mipmap.call_blue_background,R.mipmap.call_video_image);
                mCallBottomBrusher.setTextRes(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,getString(R.string.video));
                lefttop_image.setVisibility(View.INVISIBLE);
            }
            else{
                mCallTopView.setVisibility(View.VISIBLE);
                mCallAnimationView.setVisibility(View.GONE);
                mCallBottomBrusher.setImageRes(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,R.mipmap.call_blue_background,R.mipmap.call_audio_image);
                mCallBottomBrusher.setTextRes(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,getString(R.string.audio));
                lefttop_image.setVisibility(View.VISIBLE);
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
        mTimer.cancel();
        mTimer = null;
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
        mCallTopView = view.findViewById(R.id.call_top);
        mCallAnimationView = view.findViewById(R.id.call_animation);
        mCallBottomBrusher.init(view);
        mCallRightBrusher.init(view);
        mCallTopBrusher.init(view);
        mAnimationBtusher.init(view);
        unit_tv = (TextView) view.findViewById(R.id.unit_tv);
        calltime_tv = (TextView) view.findViewById(R.id.calltime_tv);
        unit_tv.setText(((CallActivity) getActivity()).mUnit);
        calltime_tv.setText("00:00");
        mCallTopBrusher.setResText(CallTopBrusher.POSITION_TOP,((CallActivity) getActivity()).mUnit);
        mCallTopBrusher.setResText(CallTopBrusher.POSITION_BOTTOM,"00:00");
        lefttop_image = (ImageView)view.findViewById(R.id.lefttop_image);
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
                getActivity().finish();
                break;
            case R.id.top_btn:
                Toast.makeText(mContext, "bottom_btn", Toast.LENGTH_SHORT).show();
                ((CallActivity)getActivity()).volumeMic();
                break;
            case R.id.bottom_btn:
                ((CallActivity)getActivity()).volumeSpeaker();
                break;
            default:
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if(msg.what == MSG_SHOW_ELAPSE_TIME){
                long nowTime = System.currentTimeMillis();
                long l = nowTime - lastTime;
                long hour=(l/(60 * 60 * 1000));
                long min=((l/(60 * 1000))- hour*60);
                long s=(l/1000 - hour*60*60 - min*60);

                String minString = String.valueOf(min);
                if(min < 10){
                    minString = "0" + min;
                }

                String sString = String.valueOf(s);
                if(s<10){
                    sString = "0" + s;
                }
                String timeString = minString + ":" + sString;
               calltime_tv.setText(timeString);
               mCallTopBrusher.setResText(CallTopBrusher.POSITION_BOTTOM,timeString);
            }
        }
    };
}
