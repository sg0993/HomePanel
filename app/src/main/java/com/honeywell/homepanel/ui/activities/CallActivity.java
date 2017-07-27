package com.honeywell.homepanel.ui.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import com.honeywell.homepanel.IAvRtpService;
import com.honeywell.homepanel.IConfigService;
import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.LogMgr;
import com.honeywell.homepanel.Utils.NotificationUtil;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.common.utils.CommonUtils;
import com.honeywell.homepanel.ui.domain.UIBaseCallInfo;
import com.honeywell.homepanel.ui.fragment.CalIpDcIncomingAndConnected;
import com.honeywell.homepanel.ui.fragment.CallBaseFragment;
import com.honeywell.homepanel.ui.fragment.CallGuardIncomingAndConnected;
import com.honeywell.homepanel.ui.fragment.CallIncomingNeighbor;
import com.honeywell.homepanel.ui.fragment.CallLobbyIncomingAndConnected;
import com.honeywell.homepanel.ui.fragment.CallNeighborAndioAndVideoConnected;
import com.honeywell.homepanel.ui.fragment.CallOutgoingNeighborFragment;
import com.honeywell.homepanel.ui.fragment.CallSubponeIncomingAndConnected;
import com.honeywell.homepanel.ui.uicomponent.Notification;
import com.honeywell.homepanel.ui.uicomponent.TopViewBrusher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.honeywell.homepanel.common.CommonData.CALL_GUARD_INCOMMING;
import static com.honeywell.homepanel.common.CommonData.CALL_GUART_OUTGOING;
import static com.honeywell.homepanel.common.CommonData.CALL_INCOMING_NEIGHBOR;
import static com.honeywell.homepanel.common.CommonData.CALL_IPDC_CONNECTED;
import static com.honeywell.homepanel.common.CommonData.CALL_IPDC_INCOMING;
import static com.honeywell.homepanel.common.CommonData.CALL_LOBBY_INCOMMING;
import static com.honeywell.homepanel.common.CommonData.CALL_OUTGOING_NEIGHBOR;
import static com.honeywell.homepanel.common.CommonData.CALL_SUBPHONE_CONNECTED;
import static com.honeywell.homepanel.common.CommonData.CALL_SUBPHONE_INCOMMING;
import static com.honeywell.homepanel.common.CommonData.CALL_SUBPHONE_OUTGOING;

/**
 * Created by H135901 on 1/24/2017.
 */

public class CallActivity extends FragmentActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "CallActivity";
    private TopViewBrusher mTopViewBrusher = new TopViewBrusher();
    private Map<Integer, CallBaseFragment> mFragments = new HashMap<Integer, CallBaseFragment>();

    public int mCurCallStatus = CALL_LOBBY_INCOMMING;
    public String mUnit = "100-202";
    public String mDisplayName = "";// for inner call out
    public String mFormatText = "";
    public String mCallType = "";
    private AudioManager mAudioManager = null;
    private boolean mSpeakerAdjust = false;

    public IAvRtpService mIAvRtpService = null;
    private ServiceConnection mIAvRtpServiceConnect = new ServiceConnectionImpl();

    public IConfigService mIConfigService = null;
    private ServiceConnection mIConfigServiceConnect = new ServiceConnectionImpl();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);//屏幕常亮
        setContentView(R.layout.layout_call);
        getAllParameter(getIntent());
        EventBus.getDefault().register(this);
        initViewAndListener();
        mTopViewBrusher.init(this);
        fragmentAdd(mCurCallStatus);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        CommonUtils.startAndBindService(getApplicationContext(), CommonData.ACTION_AVRTP_SERVICE, mIAvRtpServiceConnect);
        CommonUtils.startAndBindService(getApplicationContext(), CommonData.ACTION_CONFIG_SERVICE, mIConfigServiceConnect);

        //警告停止
        NotificationUtil.getNotificationUtil().stopShow();
    }

    private JSONObject getCallInitJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(CommonJson.JSON_ACTION_KEY, CommonJson.JSON_ACTION_VALUE_EVENT);
            jsonObject.put(CommonJson.JSON_SUBACTION_KEY, CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONEVENTADD);
            JSONArray jsonArray = new JSONArray();
            JSONObject mapObject = new JSONObject();
            mapObject.put(CommonJson.JSON_UUID_KEY, MainActivity.CallBaseInfo.getCallUuid());
            mapObject.put(CommonData.JSON_KEY_EVENTTYPE, CommonData.JSON_VALUE_VISITOR);
            mapObject.put(CommonData.JSON_KEY_TIME, getCalltime());
            mapObject.put(CommonData.JSON_KEY_DATASTATUS, CommonData.DATASTATUS_UNREAD);
            jsonArray.put(mapObject);
            jsonObject.put(CommonJson.JSON_LOOPMAP_KEY, jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public String getCalltime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = sdf.format(new Date());
        return time;
    }

    private void getAllParameter(Intent intent) {
        if (intent.hasExtra(CommonData.INTENT_KEY_CALL_TYPE)) {
            mCurCallStatus = intent.getIntExtra(CommonData.INTENT_KEY_CALL_TYPE, mCurCallStatus);
        }
        if (intent.hasExtra(CommonData.INTENT_KEY_UNIT)) {
            mUnit = intent.getStringExtra(CommonData.INTENT_KEY_UNIT);
        }
        if (intent.hasExtra(CommonData.INTENT_KEY_DISPLAYNAME)) {
            mDisplayName = intent.getStringExtra(CommonData.INTENT_KEY_DISPLAYNAME);
        }
        if (intent.hasExtra(CommonData.INTENT_KEY_TEXTFORMAT)) {
            mFormatText = intent.getStringExtra(CommonData.INTENT_KEY_TEXTFORMAT);
        }
        if (intent.hasExtra(CommonJson.JSON_CALLTYPE_KEY)) {
            mCallType = intent.getStringExtra(CommonJson.JSON_CALLTYPE_KEY);
        }

        Log.d(TAG, "getAllParameter() mCurCallStatus:" + mCurCallStatus + ",mUnit:" + mUnit + ",,,1111111");
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
        //警告恢复
        NotificationUtil.getNotificationUtil().afreshShow();
        if (null != mIAvRtpServiceConnect) {
            getApplicationContext().unbindService(mIAvRtpServiceConnect);
        }
        if (null != mIConfigServiceConnect) {
            getApplicationContext().unbindService(mIConfigServiceConnect);
        }
    }


    public void fragmentAdd(int position) {
        mCurCallStatus = position;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = mFragments.get(position);
        if (null == fragment) {
            fragment = getNewFragMent(position);
        }
        transaction.replace(R.id.main_frameLayout, fragment);
        transaction.commitAllowingStateLoss();
    }

    private Fragment getNewFragMent(int position) {
        LogMgr.e("position:" + position);
        CallBaseFragment fragment = null;
        switch (position) {
            case CALL_OUTGOING_NEIGHBOR:
                fragment = new CallOutgoingNeighborFragment("" + position);
                break;
            case CALL_INCOMING_NEIGHBOR:
                fragment = new CallIncomingNeighbor("" + position);
                break;
            case CommonData.CALL_CONNECTED_AUDIO_NETGHBOR:
                fragment = new CallNeighborAndioAndVideoConnected("" + position);
                fragment.setFragmentAidl(mIAvRtpService, getApplicationContext());
                setRingCallVolume();
                break;
            case CommonData.CALL_CONNECTED_VIDEO_NETGHBOR:
                fragment = new CallNeighborAndioAndVideoConnected("" + position);
                fragment.setFragmentAidl(mIAvRtpService, getApplicationContext());
                break;
            case CALL_LOBBY_INCOMMING:
                fragment = new CallLobbyIncomingAndConnected("" + position);////
                break;
            case CommonData.CALL_LOBBY_CONNECTED:
                fragment = new CallLobbyIncomingAndConnected("" + position);////
                break;
            case CALL_GUARD_INCOMMING:
            case CommonData.CALL_GUARD_CONNECTED:
            case CALL_GUART_OUTGOING:
                fragment = new CallGuardIncomingAndConnected("" + position);
                break;
            case CALL_IPDC_INCOMING:
            case CALL_IPDC_CONNECTED:
                fragment = new CalIpDcIncomingAndConnected("" + position);
                break;
            case CALL_SUBPHONE_INCOMMING:
            case CALL_SUBPHONE_CONNECTED:
            case CALL_SUBPHONE_OUTGOING:
                fragment = new CallSubponeIncomingAndConnected("" + position);
                break;
            default:
                break;
        }
        mFragments.put(position, fragment);
        return fragment;
    }

    public void setRingCallVolume() {
        int curVolume = getVolumeForCallStatus();
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, AudioManager.FLAG_PLAY_SOUND);
    }

    private int getVolumeForCallStatus() {
        String volume_type = getVolumeType();
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        try {
            currentVolume = mIConfigService.getIntMapConfig(volume_type);
            if (currentVolume < 0) {
                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getVolumeForCallStatus: currentVolume:" + currentVolume + ",,11111111");
        return currentVolume;
    }

    private String getVolumeType() {
        String volume_type = "";
        switch (mCurCallStatus) {
            case CALL_IPDC_INCOMING:
            case CALL_GUARD_INCOMMING:
            case CALL_GUART_OUTGOING:
            case CALL_LOBBY_INCOMMING:
            case CALL_INCOMING_NEIGHBOR:
            case CALL_OUTGOING_NEIGHBOR:
            case CALL_SUBPHONE_INCOMMING:
            case CALL_SUBPHONE_OUTGOING:
                volume_type = CommonData.KEY_VOLUME_RING;
                break;
            case CommonData.CALL_CONNECTED_AUDIO_NETGHBOR:
            case CommonData.CALL_CONNECTED_VIDEO_NETGHBOR:
                volume_type = CommonData.KEY_VOLUME_NEIGHBOR;
                break;
            case CommonData.CALL_LOBBY_CONNECTED:
                volume_type = CommonData.KEY_VOLUME_LOBBY;
                break;
            case CommonData.CALL_GUARD_CONNECTED:
                volume_type = CommonData.KEY_VOLUME_GUARD;
                break;
            case CommonData.CALL_IPDC_CONNECTED:
                volume_type = CommonData.KEY_VOLUME_IPDC;
                break;
            case CommonData.CALL_SUBPHONE_CONNECTED:
                volume_type = CommonData.KEY_VOLUME_INNER;
                break;
            default:
                volume_type = CommonData.KEY_VOLUME_RING;
                break;
        }
        Log.d(TAG, "getVolumeType: volume_type:" + volume_type + ",,111111111111");
        return volume_type;
    }

    public static void switchFragmentInFragment(Fragment fragment, int callType) {
        if (fragment.getActivity() instanceof CallActivity) {
            ((CallActivity) fragment.getActivity()).fragmentAdd(callType);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.volume_decrease:
                if (mSpeakerAdjust) {
                    Toast.makeText(getApplicationContext(), "volume decrease!!!!", Toast.LENGTH_SHORT).show();
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                    int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    mCurSeekBar.setProgress(volume);
                    writeVolumeToDb(volume);
                } else {

                }
                break;
            case R.id.volume_increase:
                if (mSpeakerAdjust) {
                    Toast.makeText(this, "volume increase!!!!", Toast.LENGTH_SHORT).show();
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                    int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    mCurSeekBar.setProgress(volume);
                    writeVolumeToDb(volume);
                } else {

                }
                break;
            default:
                break;
        }
    }

    private SeekBar mCurSeekBar = null;

    public void volumeSpeaker() {
        mSpeakerAdjust = true;
        mCurSeekBar = CommonUtils.showCallVolumeDialog(this, this, this, mSpeakerAdjust, getVolumeForCallStatus());
        Toast.makeText(getApplicationContext(), "top_btn", Toast.LENGTH_SHORT).show();
    }

    public void volumeMic() {
        mSpeakerAdjust = false;
        //mCurSeekBar = CommonUtils.showCallVolumeDialog(this,this,this, mSpeakerAdjust);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }

    public int getCurFragmentStatus() {
        return mCurCallStatus;
    }

    public void setCurFragmentStatus(int status) {
        mCurCallStatus = status;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStopTrackingTouch() progress:" + seekBar.getProgress());
        if (mSpeakerAdjust) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekBar.getProgress(), AudioManager.FLAG_PLAY_SOUND);
            writeVolumeToDb(seekBar.getProgress());
        } else {

        }
    }

    class ServiceConnectionImpl implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            String serviceClassName = name.getClassName();
            if (serviceClassName.equals(CommonData.ACTION_AVRTP_SERVICE)) {
                mIAvRtpService = IAvRtpService.Stub.asInterface(service);
                setFragmentAidl();
            } else if (serviceClassName.equals(CommonData.ACTION_CONFIG_SERVICE)) {
                mIConfigService = IConfigService.Stub.asInterface(service);
                if (mCurCallStatus == CALL_LOBBY_INCOMMING ||
                        mCurCallStatus == CALL_IPDC_INCOMING) {
                    saveCallHistory(getCallInitJsonObject());
                }
                int currentVolume = getVolumeForCallStatus();
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_PLAY_SOUND);
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            String serviceClassName = name.getClassName();
            if (serviceClassName.equals(CommonData.ACTION_AVRTP_SERVICE)) {
                mIAvRtpService = null;
            } else if (serviceClassName.equals(CommonData.ACTION_CONFIG_SERVICE)) {
                mIConfigService = null;
            }
        }
    }

    private void writeVolumeToDb(int volume) {
        if (mIConfigService != null) {
            String key = getVolumeType();
            Log.d(TAG, "writeVolumeToDb: key:" + key + ",,volume:" + volume + ",,,111111111");
            try {
                mIConfigService.putIntMapConfig(key, volume);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void setFragmentAidl() {
        CallBaseFragment fragment = mFragments.get(getCurFragmentStatus());
        if (null != fragment && mIAvRtpService != null) {
            fragment.setFragmentAidl(mIAvRtpService, getApplicationContext());
        }
    }


    public void saveCallHistory(JSONObject jsonObject) {
        if (null != mIConfigService) {
            try {
                mIConfigService.putToDBManager(jsonObject.toString().getBytes());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
