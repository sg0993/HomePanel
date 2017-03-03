package com.honeywell.homepanel.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.common.utils.CommonUtils;
import com.honeywell.homepanel.ui.fragment.CallIncomingNeighbor;
import com.honeywell.homepanel.ui.fragment.CallLobbyIncomingAndConnected;
import com.honeywell.homepanel.ui.fragment.CallNeighborAndioAndVideoConnected;
import com.honeywell.homepanel.ui.fragment.CallOutgoingNeighborFragment;
import com.honeywell.homepanel.ui.uicomponent.TopViewBrusher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by H135901 on 1/24/2017.
 */

public  class CallActivity extends FragmentActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{

    private static  final String TAG = "CallActivity";
    private TopViewBrusher mTopViewBrusher = new TopViewBrusher();
    private Map<Integer,Fragment> mFragments = new HashMap<Integer, Fragment>();

    public int mCurCallStatus = CommonData.CALL_LOBBY_INCOMMING;
    public String mUnit = "";

    private AudioManager mAudioManager = null;
    private boolean mSpeakerAdjust = false;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_call);
        getAllParameter(getIntent());
        EventBus.getDefault().register(this);
        initViewAndListener();
        mTopViewBrusher.init(this);
        fragmentAdd(mCurCallStatus);
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    }

    private void getAllParameter(Intent intent) {
        if(intent.hasExtra(CommonData.INTENT_KEY_CALL_TYPE)){
            mCurCallStatus = intent.getIntExtra(CommonData.INTENT_KEY_CALL_TYPE,mCurCallStatus);
        }
        if(intent.hasExtra(CommonData.INTENT_KEY_UNIT)){
            mUnit = intent.getStringExtra(CommonData.INTENT_KEY_UNIT);
        }
        Log.d(TAG,"getAllParameter() mCurCallStatus:"+mCurCallStatus+",mUnit:"+mUnit +",,,1111111");
    }

    protected void initViewAndListener() {

    }
 	@Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        mTopViewBrusher.destory();
    }


    public void fragmentAdd(int position)  {
        mCurCallStatus = position;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = mFragments.get(position);
        if(null == fragment){
            fragment = getNewFragMent(position);
        }
        transaction.replace(R.id.main_frameLayout, fragment);
        transaction.commit();
    }
    private Fragment getNewFragMent(int position) {
        Fragment fragment = null;
        switch (position){
            case CommonData.CALL_OUTGOING_NEIGHBOR:
                fragment = new CallOutgoingNeighborFragment("" + position);
                break;
            case CommonData.CALL_INCOMING_NEIGHBOR:
                fragment = new CallIncomingNeighbor("" + position);
                break;
            case CommonData.CALL_CONNECTED_AUDIO_NETGHBOR:
                fragment = new CallNeighborAndioAndVideoConnected("" + position);/**********/
                break;
            case CommonData.CALL_CONNECTED_VIDEO_NETGHBOR:
                fragment = new CallNeighborAndioAndVideoConnected("" + position);/**********/
                break;
            case CommonData.CALL_LOBBY_INCOMMING:
                fragment = new CallLobbyIncomingAndConnected("" + position);////
                break;
            case CommonData.CALL_LOBBY_CONNECTED:
                fragment = new CallLobbyIncomingAndConnected("" + position);////
                break;
            default:
                break;
        }
        mFragments.put(position, fragment);
        return fragment;
    }

    public static void switchFragmentInFragment(Fragment fragment,int callType) {
        if(fragment.getActivity() instanceof CallActivity){
            ((CallActivity)fragment.getActivity()).fragmentAdd(callType);
        }
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.volume_decrease:
                if(mSpeakerAdjust){
                    Toast.makeText(getApplicationContext(),"volume decrease!!!!",Toast.LENGTH_SHORT).show();
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM,AudioManager.ADJUST_LOWER,AudioManager.FLAG_PLAY_SOUND);
                    mCurSeekBar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
                }
                else{

                }
                break;
            case R.id.volume_increase:
                if(mSpeakerAdjust){
                    Toast.makeText(this,"volume increase!!!!",Toast.LENGTH_SHORT).show();
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM,AudioManager.ADJUST_RAISE,AudioManager.FLAG_PLAY_SOUND);
                    mCurSeekBar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
                }
                else{

                }
                break;
            default:
                break;
        }
    }

    private SeekBar mCurSeekBar = null;
    public void volumeSpeaker(){
        mSpeakerAdjust = true;
        mCurSeekBar = CommonUtils.showCallVolumeDialog(this,this,this, mSpeakerAdjust);
        Toast.makeText(getApplicationContext(), "top_btn", Toast.LENGTH_SHORT).show();
    }

    public void volumeMic(){
        mSpeakerAdjust = false;
        //mCurSeekBar = CommonUtils.showCallVolumeDialog(this,this,this, mSpeakerAdjust);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }

    public int getCurFragmentStatus(){
        return mCurCallStatus;
    }

    public void setCurFragmentStatus(int status ){
         mCurCallStatus = status;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d(TAG,"onStopTrackingTouch() progress:" + seekBar.getProgress());
        if(mSpeakerAdjust){
            mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,seekBar.getProgress(),AudioManager.FLAG_PLAY_SOUND);
        }
        else{

        }
    }
}
