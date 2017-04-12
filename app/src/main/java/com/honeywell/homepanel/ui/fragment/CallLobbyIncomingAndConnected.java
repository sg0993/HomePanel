package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUICall;
import com.honeywell.homepanel.common.utils.CommonUtils;
import com.honeywell.homepanel.ui.AudioVideoUtil.TextureViewListener;
import com.honeywell.homepanel.ui.AudioVideoUtil.VideoDecoderThread;
import com.honeywell.homepanel.ui.activities.CallActivity;
import com.honeywell.homepanel.ui.activities.MainActivity;
import com.honeywell.homepanel.ui.domain.UIBaseCallInfo;
import com.honeywell.homepanel.ui.uicomponent.CalRightBrusher;
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
public class CallLobbyIncomingAndConnected extends CallBaseFragment implements View.OnClickListener{
    private String mTitle = "";
    private static  final  String TAG = "CallNeighbor";

    private Context mContext = null;
    private CallBottomBrusher mCallBottomBrusher = new CallBottomBrusher
            (this,R.mipmap.call_incoming_background,R.mipmap.call_incoming_call,"Answer",
                    R.mipmap.call_red_background,R.mipmap.call_end_image,"End",
                    R.mipmap.call_blue_background,R.mipmap.call_dooropen,"Open");
    private CalRightBrusher mCallRightBrusher = new CalRightBrusher(
            this,R.mipmap.call_right_background,R.mipmap.call_microphone,
            R.mipmap.call_right_background,R.mipmap.call_speaker);

    private CallTopBrusher mCallTopBrusher = new CallTopBrusher(
            "Incomming call.","Lobby"
    );

    //for video

    private TextureView mDecoderView;
    private TextureViewListener mDecoderTextureListener;
    private VideoDecoderThread mDecThread;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"CallLobbyIncomingAndConnected.onCreate() 11111111");
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"CallLobbyIncomingAndConnected.onCreateView() 11111111");
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_neighbor_lobby_incommingconnected, null);
        initViews(view);
        mCallBottomBrusher.init(view);
        mCallBottomBrusher.setVisible(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,View.GONE);
        mCallRightBrusher.init(view);
        mCallTopBrusher.init(view);

        startVideoGet();
        //for photo one bitmap to sdcard
        phoneOneFrame();
        return view;
    }

    @Override
    public void onResume() {
        Log.d(TAG,"CallLobbyIncomingAndConnected.onResume() 11111111");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        Log.d(TAG,"CallLobbyIncomingAndConnected.onDestroy() 11111111");
        mDecThread.stop();
        super.onDestroy();
    }

    public CallLobbyIncomingAndConnected(String title) {
        super();
        this.mTitle = title;
    }
    public CallLobbyIncomingAndConnected() {
        super();
    }

    private void initViews(View view) {
        mDecoderView = (TextureView) view.findViewById(R.id.texture_view); // mediacodec
        // multimedia
        mDecoderTextureListener = new TextureViewListener();
        mDecoderView.setSurfaceTextureListener(mDecoderTextureListener);
        mDecThread = new VideoDecoderThread(mDecoderTextureListener,mVideoPlayQueue);
        new Thread(mDecThread).start();
    }

    private void phoneOneFrame() {
        mDecoderView.postDelayed(new Runnable() {
            public void run() {
                Bitmap bitmap = mDecoderView.getBitmap();
                CommonUtils.saveBitmap(bitmap,CommonUtils.getBitMapName());
            }
        }, 5000);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.left_btn:
                Toast.makeText(mContext,"call_left",Toast.LENGTH_SHORT).show();
                int status = ((CallActivity)getActivity()).getCurFragmentStatus();
                if(status == CommonData.CALL_LOBBY_INCOMMING){
                    status = CommonData.CALL_LOBBY_CONNECTED;
                    ((CallActivity)getActivity()).setCurFragmentStatus(status);
                    UISendCallMessage.requestForTakeCall(MainActivity.CallBaseInfo);
                    switchToLobbyConnected();
                    //start audio
                    startAudio();
                }
                else{
                    status = CommonData.CALL_LOBBY_INCOMMING;
                    UISendCallMessage.requestForHungUp(MainActivity.CallBaseInfo);
                    getActivity().finish();
                }
                break;
            case R.id.right_btn:
                int CurStatus = ((CallActivity)getActivity()).getCurFragmentStatus();
                if(CurStatus == CommonData.CALL_LOBBY_INCOMMING){
                    CurStatus = CommonData.CALL_LOBBY_CONNECTED;
                    ((CallActivity)getActivity()).setCurFragmentStatus(CurStatus);
                    UISendCallMessage.requestForHungUp(MainActivity.CallBaseInfo);
                    switchToLobbyConnected();
                    startAudio();
                }
                else{
                    status = CommonData.CALL_LOBBY_INCOMMING;
                    UISendCallMessage.requestForOpenDoor(MainActivity.CallBaseInfo);
                    getActivity().finish();
                }

                Toast.makeText(mContext,"call_right",Toast.LENGTH_SHORT).show();
                break;
            case R.id.top_btn:
                Toast.makeText(mContext, "bottom_btn", Toast.LENGTH_SHORT).show();
                ((CallActivity)getActivity()).volumeMic();
              /*  mSpeakerAdjust = false;
                Toast.makeText(getApplicationContext(), "bottom_btn", Toast.LENGTH_SHORT).show();*/
                break;
            case R.id.bottom_btn:
                ((CallActivity)getActivity()).volumeSpeaker();
                break;
            default:
                break;
        }
    }

    private void switchToLobbyConnected() {
        if(getActivity() instanceof CallActivity){
            mCallBottomBrusher.setImageRes(CallBottomBrusher.BOTTOM_POSTION_LEFT,R.mipmap.call_red_background,R.mipmap.call_end_image);
            mCallBottomBrusher.setTextRes(CallBottomBrusher.BOTTOM_POSTION_LEFT,"End");
            mCallTopBrusher.setResText(CallTopBrusher.POSITION_TOP,"Calling");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISOpenDoorMessageRsp msg) {
        String action = msg.optString(CommonData.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonData.JSON_ACTION_VALUE_RESPONSE)) {
            String errorCode = msg.optString(CommonData.JSON_ERRORCODE_KEY);
            if(Integer.parseInt(errorCode) == 0){
                System.out.println("SUISOpenDoorMessageRsp success");
                String uuid = msg.optString(CommonData.JSON_UUID_KEY, "");
                String callType = msg.optString(CommonData.JSON_CALLTYPE_KEY, "");
                String aliasName = msg.optString(CommonData.JSON_ALIASNAME_KEY, "");
            }else{
                System.out.println("SUISOpenDoorMessageRsp failed");
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISTakeCallMessageRsp msg) {
        String action = msg.optString(CommonData.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonData.JSON_ACTION_VALUE_RESPONSE)) {
            String errorCode = msg.optString(CommonData.JSON_ERRORCODE_KEY);
            if(Integer.parseInt(errorCode) == 0){
                System.out.println("SUISTakeCallMessageRsp success");
                String uuid = msg.optString(CommonData.JSON_UUID_KEY, "");
                String callType = msg.optString(CommonData.JSON_CALLTYPE_KEY, "");
                String aliasName = msg.optString(CommonData.JSON_ALIASNAME_KEY, "");
            }else{
                System.out.println("SUISTakeCallMessageRsp failed");
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISHungUpMessageRsp msg) {
        String action = msg.optString(CommonData.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonData.JSON_ACTION_VALUE_RESPONSE)) {
            String errorCode = msg.optString(CommonData.JSON_ERRORCODE_KEY);
            if(Integer.parseInt(errorCode) == 0){
                System.out.println("SUISHungUpMessageRsp success");
                String uuid = msg.optString(CommonData.JSON_UUID_KEY, "");
            }else{
                System.out.println("SUISHungUpMessageRsp failed");
            }

        }
    }

}
