package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.EventBusWrapper;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUINotification;
import com.honeywell.homepanel.common.Message.ui.UIMessagesNotification;

import com.honeywell.homepanel.ui.domain.NotifyVoiceEventBusClass;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class MessageFragment extends Fragment implements View.OnClickListener, NotificationBridge{
    private Button mNaviEvent;
    private Button mNaviAlarm;
    private Button mNaviNotification;
    private Button mNaviVoice;

    private View mEventUnderline;
    private View mAlarmUnderline;
    private View mNotificationUnderline;
    private View mVoiceUnderline;

    private List<Button> tabBtnList = new ArrayList<Button>();
    private FragmentEvent mTabFragEvent;
    private FragmentAlarm mTabFragAlarm;
    private FragmentNofity mTabFragNotify;
    private FragmentVoice mTabFragVoice;
    private static final String TAG = "MessageFragment";

    private String title = "";
    private final String NORMAL_GREY = "#ABABAB";

    public static Map<String, Integer> unreadCountMap;

    public static Integer getUnreadCount(String type) {
        return unreadCountMap.get(type);
    }

    public static void setUnreadCount(String type, Integer count) {
        unreadCountMap.put(type, count);
    }

    UUID uuid = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        initData();
        initView(view);
        initEvents();
        setSelect(true,0);
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initData() {
        getEventCount();
        getAlarmCount();
        getNotificationCount();
        getVoiceMsgCount();
        UIMessagesNotification.UIGetVoiceMsgListMessageReq dataReq = new UIMessagesNotification.UIGetVoiceMsgListMessageReq();

    }

    private void getEventCount() {
        UIMessagesNotification.UIGetEventsListMessageReq dataReq = new UIMessagesNotification.UIGetEventsListMessageReq();
        UUID msgid = UUID.randomUUID();
        try {
            dataReq.put(CommonData.JSON_ACTION_KEY, CommonData.JSON_ACTION_VALUE_REQUEST);
            dataReq.put(CommonData.JSON_SUBACTION_KEY, CommonData.JSON_SUBACTION_VALUE_EVENTCOUNTGET);
            dataReq.put(CommonData.JSON_MSGID_KEY, msgid);
            dataReq.put(CommonData.JSON_KEY_DATASTATUS, CommonData.DATASTATUS_UNREAD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        EventBusWrapper.emitMessageToEventBus(dataReq);
    }

    private void getAlarmCount() {
        UIMessagesNotification.UIGetAlarmListMessageReq dataReq = new UIMessagesNotification.UIGetAlarmListMessageReq();
        UUID msgid = UUID.randomUUID();
        try {
            dataReq.put(CommonData.JSON_ACTION_KEY, CommonData.JSON_ACTION_VALUE_REQUEST);
            dataReq.put(CommonData.JSON_SUBACTION_KEY, CommonData.JSON_SUBACTION_VALUE_ALARMCOUNTGET);
            dataReq.put(CommonData.JSON_MSGID_KEY, msgid);
            dataReq.put(CommonData.JSON_KEY_DATASTATUS, CommonData.DATASTATUS_UNREAD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        EventBusWrapper.emitMessageToEventBus(dataReq);
    }

    private void getNotificationCount() {
        UIMessagesNotification.UIGetBulletinListMessageReq dataReq = new UIMessagesNotification.UIGetBulletinListMessageReq();
        UUID msgid = UUID.randomUUID();
        try {
            dataReq.put(CommonData.JSON_ACTION_KEY, CommonData.JSON_ACTION_VALUE_REQUEST);
            dataReq.put(CommonData.JSON_SUBACTION_KEY, CommonData.JSON_SUBACTION_VALUE_BULLETINCOUNTGET);
            dataReq.put(CommonData.JSON_MSGID_KEY, msgid);
            dataReq.put(CommonData.JSON_KEY_DATASTATUS, CommonData.DATASTATUS_UNREAD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        EventBusWrapper.emitMessageToEventBus(dataReq);
    }

    private void getVoiceMsgCount() {
        UIMessagesNotification.UIGetVoiceMsgListMessageReq dataReq = new UIMessagesNotification.UIGetVoiceMsgListMessageReq();
        UUID msgid = UUID.randomUUID();
        try {
            dataReq.put(CommonData.JSON_ACTION_KEY, CommonData.JSON_ACTION_VALUE_REQUEST);
            dataReq.put(CommonData.JSON_SUBACTION_KEY, CommonData.JSON_SUBACTION_VALUE_VOICEMSGCOUNTGET);
            dataReq.put(CommonData.JSON_MSGID_KEY, msgid);
            dataReq.put(CommonData.JSON_KEY_DATASTATUS, CommonData.DATASTATUS_UNREAD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        EventBusWrapper.emitMessageToEventBus(dataReq);
    }


    public MessageFragment(String title) {
        super();
        this.title = title;
    }

    public MessageFragment() {
        super();
    }
    private void setSelect(boolean bFirst,int i) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        //hideFragment(transaction);
        setStyleById(i);
        //set img to bright color
        switch (i)
        {
            case 0:
                if (mTabFragEvent == null || bFirst) {
                    mTabFragEvent = new FragmentEvent();
                    mTabFragEvent.setUpdateTitleCB(this);
                    transaction.replace(R.id.id_msg_body, mTabFragEvent);
                } else {
                    transaction.replace(R.id.id_msg_body,mTabFragEvent);
                }
                break;
            case 1:
                if (mTabFragAlarm == null|| bFirst) {
                    mTabFragAlarm = new FragmentAlarm();
                    mTabFragAlarm.setUpdateTitleCB(this);
                    transaction.replace(R.id.id_msg_body, mTabFragAlarm);
                } else {
                    transaction.replace(R.id.id_msg_body,mTabFragAlarm);
                }
                break;
            case 2:
                if (mTabFragNotify == null|| bFirst) {
                    mTabFragNotify = new FragmentNofity();
                    mTabFragNotify.setUpdateTitleCB(this);
                    transaction.replace(R.id.id_msg_body, mTabFragNotify);
                } else {
                    transaction.replace(R.id.id_msg_body,mTabFragNotify);
                }
                break;
            case 3:
                if (mTabFragVoice == null|| bFirst) {
                    mTabFragVoice = new FragmentVoice();
                    mTabFragVoice.setUpdateTitleCB(this);
                    transaction.replace(R.id.id_msg_body, mTabFragVoice);
                } else {
                    transaction.replace(R.id.id_msg_body,mTabFragVoice);
                }
                break;
        }
        transaction.commit();
    }

    private void initEvents() {
        mNaviEvent.setOnClickListener(this);
        mNaviAlarm.setOnClickListener(this);
        mNaviNotification.setOnClickListener(this);
        mNaviVoice.setOnClickListener(this);
    }

    private void initView(View view) {
        mNaviEvent = (Button)view.findViewById(R.id.msg_navi_event);
        mNaviAlarm = (Button)view.findViewById(R.id.msg_navi_alarm);
        mNaviNotification = (Button)view.findViewById(R.id.msg_navi_notify);
        mNaviVoice = (Button)view.findViewById(R.id.msg_navi_voice);

        mEventUnderline = (View)view.findViewById(R.id.event_underline);
        mAlarmUnderline = (View)view.findViewById(R.id.alarm_underline);
        mNotificationUnderline = (View)view.findViewById(R.id.notify_underline);
        mVoiceUnderline = (View)view.findViewById(R.id.voice_underline);

        mEventUnderline.setVisibility(View.VISIBLE);
        mAlarmUnderline.setVisibility(View.INVISIBLE);
        mNotificationUnderline.setVisibility(View.INVISIBLE);
        mVoiceUnderline.setVisibility(View.INVISIBLE);

        tabBtnList.add(mNaviEvent);
        tabBtnList.add(mNaviAlarm);
        tabBtnList.add(mNaviNotification);
        tabBtnList.add(mNaviVoice);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.msg_navi_event:
                setSelect(false,0);
                break;
            case R.id.msg_navi_alarm:
                setSelect(false,1);
                break;
            case R.id.msg_navi_notify:
                setSelect(false,2);
                break;
            case R.id.msg_navi_voice:
                setSelect(false,3);
                break;
        }
    }

    private void setStyleById(int id) {
        switch (id)
        {
            case 0:
                mNaviEvent.setTextColor(Color.parseColor(CommonData.COLOR_DARKGREY));
                mNaviAlarm.setTextColor(Color.parseColor(NORMAL_GREY));
                mNaviNotification.setTextColor(Color.parseColor(NORMAL_GREY));
                mNaviVoice.setTextColor(Color.parseColor(NORMAL_GREY));

                mEventUnderline.setVisibility(View.VISIBLE);
                mAlarmUnderline.setVisibility(View.INVISIBLE);
                mNotificationUnderline.setVisibility(View.INVISIBLE);
                mVoiceUnderline.setVisibility(View.INVISIBLE);
                break;
            case 1:
                mNaviEvent.setTextColor(Color.parseColor(NORMAL_GREY));
                mNaviAlarm.setTextColor(Color.parseColor(CommonData.COLOR_DARKGREY));
                mNaviNotification.setTextColor(Color.parseColor(NORMAL_GREY));
                mNaviVoice.setTextColor(Color.parseColor(NORMAL_GREY));

                mEventUnderline.setVisibility(View.INVISIBLE);
                mAlarmUnderline.setVisibility(View.VISIBLE);
                mNotificationUnderline.setVisibility(View.INVISIBLE);
                mVoiceUnderline.setVisibility(View.INVISIBLE);

                break;
            case 2:
                mNaviEvent.setTextColor(Color.parseColor(NORMAL_GREY));
                mNaviAlarm.setTextColor(Color.parseColor(NORMAL_GREY));
                mNaviNotification.setTextColor(Color.parseColor(CommonData.COLOR_DARKGREY));
                mNaviVoice.setTextColor(Color.parseColor(NORMAL_GREY));

                mEventUnderline.setVisibility(View.INVISIBLE);
                mAlarmUnderline.setVisibility(View.INVISIBLE);
                mNotificationUnderline.setVisibility(View.VISIBLE);
                mVoiceUnderline.setVisibility(View.INVISIBLE);
                break;
            case 3:
                mNaviEvent.setTextColor(Color.parseColor(NORMAL_GREY));
                mNaviAlarm.setTextColor(Color.parseColor(NORMAL_GREY));
                mNaviNotification.setTextColor(Color.parseColor(NORMAL_GREY));
                mNaviVoice.setTextColor(Color.parseColor(CommonData.COLOR_DARKGREY));

                mEventUnderline.setVisibility(View.INVISIBLE);
                mAlarmUnderline.setVisibility(View.INVISIBLE);
                mNotificationUnderline.setVisibility(View.INVISIBLE);
                mVoiceUnderline.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event)
    {
        Log.d(TAG, "OnMessageEvent: ");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(NotifyVoiceEventBusClass event)
    {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUINotification.SUISGetEventsListMessageRsp msg) {
        String action = msg.optString(CommonData.JSON_ACTION_KEY, "");
        String subaction = msg.optString(CommonData.JSON_SUBACTION_KEY, "");
        String msgid = msg.optString(CommonData.JSON_MSGID_KEY, "");

        if (msgid == "" || action == "" || subaction == "") {
            return;
        }

        if ( (action.equals(CommonData.JSON_ACTION_VALUE_RESPONSE)
                && subaction.equals(CommonData.JSON_SUBACTION_VALUE_EVENTCOUNTGET))) {
            String errorcode = msg.optString(CommonData.JSON_ERRORCODE_KEY, "");
            String datastatus = msg.optString(CommonData.DATASTATUS_UNREAD, "");
            if ( (errorcode == CommonData.JSON_ERRORCODE_VALUE_OK) && (datastatus == CommonData.DATASTATUS_UNREAD) ) {
                String count = msg.optString(CommonData.JSON_KEY_COUNT, "");
                setUnreadCount(CommonData.FRAGMENT_EVENT, Integer.valueOf(count));
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(UIMessagesNotification.UIGetAlarmListMessageReq msg) {
        String action = msg.optString(CommonData.JSON_ACTION_KEY, "");
        String subaction = msg.optString(CommonData.JSON_SUBACTION_KEY, "");
        String msgid = msg.optString(CommonData.JSON_MSGID_KEY, "");

        if (msgid == "" || action == "" || subaction == "") {
            return;
        }

        if ( (action.equals(CommonData.JSON_ACTION_VALUE_RESPONSE)
                && subaction.equals(CommonData.JSON_SUBACTION_VALUE_ALARMCOUNTGET))) {
            String errorcode = msg.optString(CommonData.JSON_ERRORCODE_KEY, "");
            String datastatus = msg.optString(CommonData.DATASTATUS_UNREAD, "");
            if ( (errorcode == CommonData.JSON_ERRORCODE_VALUE_OK) && (datastatus == CommonData.DATASTATUS_UNREAD) ) {
                String count = msg.optString(CommonData.JSON_KEY_COUNT, "");
                setUnreadCount(CommonData.FRAGMENT_ALARM, Integer.valueOf(count));
            }
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(UIMessagesNotification.UIGetBulletinListMessageReq msg) {
        String action = msg.optString(CommonData.JSON_ACTION_KEY, "");
        String subaction = msg.optString(CommonData.JSON_SUBACTION_KEY, "");
        String msgid = msg.optString(CommonData.JSON_MSGID_KEY, "");

        if (msgid == "" || action == "" || subaction == "") {
            return;
        }

        if ( (action.equals(CommonData.JSON_ACTION_VALUE_RESPONSE)
                && subaction.equals(CommonData.JSON_SUBACTION_VALUE_NOTIFICATIONCOUNTGET))) {
            String errorcode = msg.optString(CommonData.JSON_ERRORCODE_KEY, "");
            String datastatus = msg.optString(CommonData.DATASTATUS_UNREAD, "");
            if ( (errorcode == CommonData.JSON_ERRORCODE_VALUE_OK) && (datastatus == CommonData.DATASTATUS_UNREAD) ) {
                String count = msg.optString(CommonData.JSON_KEY_COUNT, "");
                setUnreadCount(CommonData.FRAGMENT_NOTIFICATION, Integer.valueOf(count));
            }
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(UIMessagesNotification.UIGetVoiceMsgListMessageReq msg) {
        String action = msg.optString(CommonData.JSON_ACTION_KEY, "");
        String subaction = msg.optString(CommonData.JSON_SUBACTION_KEY, "");
        String msgid = msg.optString(CommonData.JSON_MSGID_KEY, "");

        if (msgid == "" || action == "" || subaction == "") {
            return;
        }

        if ( (action.equals(CommonData.JSON_ACTION_VALUE_RESPONSE)
                && subaction.equals(CommonData.JSON_SUBACTION_VALUE_VOICEMSGCOUNTGET))) {
            String errorcode = msg.optString(CommonData.JSON_ERRORCODE_KEY, "");
            String datastatus = msg.optString(CommonData.DATASTATUS_UNREAD, "");
            if ( (errorcode == CommonData.JSON_ERRORCODE_VALUE_OK) && (datastatus == CommonData.DATASTATUS_UNREAD) ) {
                String count = msg.optString(CommonData.JSON_KEY_COUNT, "");
                setUnreadCount(CommonData.FRAGMENT_VOICEMSG, Integer.valueOf(count));
            }
        }

    }


    @Override
    public void updateTitle(String type, String count) {
        int cnt = Integer.valueOf(count);
        if (type == CommonData.FRAGMENT_EVENT) {
            if (cnt == 0) {
                mNaviEvent.setText(CommonData.FRAGMENT_EVENT);
            } else {
                mNaviEvent.setText(CommonData.FRAGMENT_EVENT + "(" + cnt + ")");
            }
        } else if (type == CommonData.FRAGMENT_ALARM) {
            if (cnt == 0) {
                mNaviAlarm.setText(CommonData.FRAGMENT_ALARM);
            } else {
                mNaviAlarm.setText(CommonData.FRAGMENT_ALARM + "(" + cnt + ")");
            }
        } else if (type == CommonData.FRAGMENT_NOTIFICATION) {
            if (cnt == 0) {
                mNaviNotification.setText(CommonData.FRAGMENT_NOTIFICATION);
            } else {
                mNaviNotification.setText(CommonData.FRAGMENT_NOTIFICATION + "(" + cnt + ")");
            }
        } else if (type == CommonData.FRAGMENT_VOICEMSG) {
            if (cnt == 0) {
                mNaviVoice.setText(CommonData.FRAGMENT_VOICEMSG);
            } else {
                mNaviVoice.setText(CommonData.FRAGMENT_VOICEMSG + "(" + cnt + ")");
            }
        }
    }
}
