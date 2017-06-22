package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUICall;
import com.honeywell.homepanel.common.utils.CommonUtils;
import com.honeywell.homepanel.ui.AudioVideoUtil.CallRecordReadyEvent;
import com.honeywell.homepanel.ui.AudioVideoUtil.TextureViewListener;
import com.honeywell.homepanel.ui.AudioVideoUtil.VideoDecoderThread;
import com.honeywell.homepanel.ui.RingFile.RingFileData;
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
import org.json.JSONArray;
import org.json.JSONObject;

import static com.honeywell.homepanel.common.CommonData.CALL_LOBBY_CONNECTED;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class CallLobbyIncomingAndConnected extends CallBaseFragment implements View.OnClickListener{
    private String mTitle = "";
    private static  final  String TAG = "CallLobby";

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
    private int AUTO_TAKECALL_TIME = 15;
    private final Handler mHandler = new Handler();
    private UIBaseCallInfo callInfo = new UIBaseCallInfo();
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
        //mCallBottomBrusher.setVisible(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,View.GONE);
        mCallRightBrusher.init(view);
        mCallTopBrusher.init(view);
        startPlayRing(RingFileData.CALL_RING_LOBBYPHONE);
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
        Log.d(TAG, "phoneOneFrame: 111111111111");
        mDecoderView.postDelayed(new Runnable() {
            public void run() {
                Log.d(TAG, "phoneOneFrame: 22222222222");
                Bitmap bitmap = mDecoderView.getBitmap();
                String filePath = CommonUtils.saveBitmap(bitmap,CommonUtils.getLobbyBitMapName());
                //save record image history
                if(null != getActivity()){
                    ((CallActivity)getActivity()).saveCallHistory(getUpdateImagePathJson(filePath));
                }
            }
        }, 5000);
    }

    private JSONObject getUpdateImagePathJson(String filePath) {
        JSONObject jsonObject = new JSONObject();
        if(TextUtils.isEmpty(filePath)){
            return  jsonObject;
        }
        try {
            jsonObject.put(CommonJson.JSON_ACTION_KEY,CommonJson.JSON_ACTION_VALUE_EVENT);
            jsonObject.put(CommonJson.JSON_SUBACTION_KEY,CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONEVENTUPDATE);
            JSONArray jsonArray = new JSONArray();
            JSONObject mapObject = new JSONObject();
            mapObject.put(CommonJson.JSON_UUID_KEY,MainActivity.CallBaseInfo.getCallUuid());
            mapObject.put(CommonData.JSON_KEY_IMAGENAME,filePath);
            jsonArray.put(mapObject);
            jsonObject.put(CommonJson.JSON_LOOPMAP_KEY,jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonObject;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        int status = ((CallActivity)getActivity()).getCurFragmentStatus();
        switch (viewId){
            case R.id.middle_btn:
                stopPlayRing();
                UISendCallMessage.requestForHungUp(MainActivity.CallBaseInfo);
                getActivity().finish();
                Log.d(TAG, "onClick: end!!!!!!!!!!!!");
                break;
            case R.id.left_btn:
                Toast.makeText(mContext,"call_left",Toast.LENGTH_SHORT).show();
                if(status == CommonData.CALL_LOBBY_INCOMMING){
                    status = CALL_LOBBY_CONNECTED;
                    ((CallActivity)getActivity()).setCurFragmentStatus(status);
                    UISendCallMessage.requestForTakeCall(MainActivity.CallBaseInfo);
                    ((CallActivity)getActivity()).setRingCallVolume();
//                    switchToLobbyConnected();
//                    //start audio
//                    startAudio();
                }
                else{
                    status = CommonData.CALL_LOBBY_INCOMMING;
                    UISendCallMessage.requestForHungUp(MainActivity.CallBaseInfo);
                    stopPlayRing();
                    getActivity().finish();
                }
                break;
            case R.id.right_btn:
                int CurStatus = ((CallActivity)getActivity()).getCurFragmentStatus();
                UISendCallMessage.requestForOpenDoor(MainActivity.CallBaseInfo);
                //UISendCallMessage.requestForCallElevator(MainActivity.CallBaseInfo);
                UISendCallMessage.requestForElevatorAuth(MainActivity.CallBaseInfo);

                break;
            case R.id.top_btn:
                Toast.makeText(mContext, "bottom_btn", Toast.LENGTH_SHORT).show();
                UISendCallMessage.requestForMute(MainActivity.CallBaseInfo);
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
        stopPlayRing();
        if(getActivity() instanceof CallActivity){
            //mCallBottomBrusher.setImageRes(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,R.mipmap.call_blue_background,R.mipmap.call_video_image);
            mCallBottomBrusher.setVisible(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,View.GONE);
            mCallBottomBrusher.setImageRes(CallBottomBrusher.BOTTOM_POSTION_LEFT,R.mipmap.call_red_background,R.mipmap.call_end_image);
            mCallBottomBrusher.setTextRes(CallBottomBrusher.BOTTOM_POSTION_LEFT,"End");
            mCallTopBrusher.setResText(CallTopBrusher.POSITION_TOP,"Calling");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISOpenDoorMessageRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {
            String errorCode = msg.optString(CommonJson.JSON_ERRORCODE_KEY);
            if(errorCode.equals(CommonJson.JSON_ERRORCODE_VALUE_OK)){
                Toast.makeText(getActivity(),"OpenDoor success",Toast.LENGTH_SHORT).show();
                System.out.println("SUISOpenDoorMessageRsp success");
                String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
                String callType = msg.optString(CommonJson.JSON_CALLTYPE_KEY, "");
                String aliasName = msg.optString(CommonJson.JSON_ALIASNAME_KEY, "");
            }else{
                Toast.makeText(getActivity(),"OpenDoor failed",Toast.LENGTH_SHORT).show();
                System.out.println("SUISOpenDoorMessageRsp failed");
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISTakeCallMessageRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");

        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {
            stopPlayRing();
            String errorCode = msg.optString(CommonJson.JSON_ERRORCODE_KEY);
            System.out.println("SUISTakeCallMessageRsp errorCode"+errorCode);
            if(errorCode.equals(CommonJson.JSON_ERRORCODE_VALUE_OK)){
                System.out.println("SUISTakeCallMessageRsp success");
                String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
                String callType = msg.optString(CommonJson.JSON_CALLTYPE_KEY, "");
                String aliasName = msg.optString(CommonJson.JSON_ALIASNAME_KEY, "");
                System.out.println("SUISTakeCallMessageRsp  uuid,callType,aliasName,"+uuid+callType+aliasName);
                switchToLobbyConnected();
                startAudio();
            }else{
                Toast.makeText(getActivity(),"TakeCall failed",Toast.LENGTH_SHORT).show();
                System.out.println("SUISTakeCallMessageRsp failed");
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISHungUpMessageRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");
        stopPlayRing();
        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {
            String errorCode = msg.optString(CommonJson.JSON_ERRORCODE_KEY);
            if(errorCode.equals(CommonJson.JSON_ERRORCODE_VALUE_OK)){
                System.out.println("SUISHungUpMessageRsp success");
                String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
            }else{
                Toast.makeText(getActivity(),"HungUp failed",Toast.LENGTH_SHORT).show();
                System.out.println("SUISHungUpMessageRsp failed");
            }

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISCallTerminatedMessageEve msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");
        stopPlayRing();
        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_EVENT)) {
            getActivity().finish();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISAutoTakeCallMessageReq msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");

        Log.d(TAG, "OnMessageEvent: call lobby inncoming call auto take call:" + msg.toString());
        stopPlayRing();
        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_REQUEST)) {
            stopPlayRing();
            System.out.println("SUISAutoTakeCallMessageReq success");
            String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
            String callType = msg.optString(CommonJson.JSON_CALLTYPE_KEY, "");
            String aliasName = msg.optString(CommonJson.JSON_ALIASNAME_KEY, "");
            callInfo.setCallUuid(uuid);
            callInfo.setmCallType(callType);
            callInfo.setmCallAliasName(aliasName);
            UISendCallMessage.responseForAutoTakeCallReq(callInfo, CommonJson.JSON_ERRORCODE_VALUE_OK);
            UISendCallMessage.requestForTakeCall(callInfo);
            startAutoAudioReply();
            waitAutoAudioReplyFinish();
            autoRecordVideoAndAudio();
        }
    }
    private void autoRecordVideoAndAudio(){
        startAudio();
        startRecord();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRecord();
                stopPlayRing();
                UISendCallMessage.requestForHungUp(MainActivity.CallBaseInfo);
                if(getActivity() !=  null){
                    getActivity().finish();
                }
            }
        },10*1000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISRelayCallMessageEve msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");
        Log.d(TAG, "SUISRelayCallMessageEve: 11111111111111 ");
        /*if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_EVENT)) {
            CallActivity.switchFragmentInFragment(this, CommonData.CALL_CONNECTED_VIDEO_NETGHBOR);
        }*/

        int  status = ((CallActivity)getActivity()).getCurFragmentStatus();
        if(status == CALL_LOBBY_CONNECTED){
            return;
        }
        status = CommonData.CALL_LOBBY_CONNECTED;
        ((CallActivity)getActivity()).setCurFragmentStatus(status);
        switchToLobbyConnected();
        startAudio();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUICall.SUISMuteMessageRsp msg) {
        String action = msg.optString(CommonJson.JSON_ACTION_KEY, "");
        Log.d(TAG, "SUISMuteMessageRsp: 11111111111111 ");
        if (!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)) {

            String errorCode = msg.optString(CommonJson.JSON_ERRORCODE_KEY);
            if(errorCode.equals(CommonJson.JSON_ERRORCODE_VALUE_OK)){
                System.out.println("SUISMuteMessageRsp success");
                String uuid = msg.optString(CommonJson.JSON_UUID_KEY, "");
                ((CallActivity)getActivity()).volumeMic();
            }else{
                System.out.println("SUISMuteMessageRsp failed");
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(CallRecordReadyEvent msg) {
        ((CallActivity)getActivity()).saveCallHistory(getUpdateVideoPathJson((msg.path)));
    }
    private JSONObject getUpdateVideoPathJson(String filePath) {
        JSONObject jsonObject = new JSONObject();
        if(TextUtils.isEmpty(filePath)){
            return  jsonObject;
        }
        try {
            jsonObject.put(CommonJson.JSON_ACTION_KEY,CommonJson.JSON_ACTION_VALUE_EVENT);
            jsonObject.put(CommonJson.JSON_SUBACTION_KEY,CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONEVENTUPDATE);
            JSONArray jsonArray = new JSONArray();
            JSONObject mapObject = new JSONObject();
            mapObject.put(CommonJson.JSON_UUID_KEY,MainActivity.CallBaseInfo.getCallUuid());
            mapObject.put(CommonData.JSON_KEY_VIDEONAME,filePath);
            mapObject.put(CommonData.JSON_KEY_EVENTTYPE,CommonData.JSON_VALUE_VIDEO);
            jsonArray.put(mapObject);
            jsonObject.put(CommonJson.JSON_LOOPMAP_KEY,jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonObject;
    }

}
