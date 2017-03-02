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
import com.honeywell.homepanel.common.Message.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class MessageFragment extends Fragment implements View.OnClickListener{
    private Button mNaviEvent;
    private Button mNaviAlarm;
    private Button mNaviNotification;
    private Button mNaviVoice;
    private List<Button> tabBtnList = new ArrayList<Button>();
    private Fragment mTabFragEvent;
    private Fragment mTabFragAlarm;
    private Fragment mTabFragNotify;
    private Fragment mTabFragVoice;

    private String title = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        Log.d("MessageFragment", "#####  onCreateView ");
        initView(view);
        initEvents();
        setSelect(true,0, mNaviEvent.getId());

        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public MessageFragment(String title) {
        super();
        this.title = title;
    }

    private void setSelect(boolean bFirst,int i, int id) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        //hideFragment(transaction);
        setStyleById(id);
        //set img to bright color
        switch (i)
        {
            case 0:
                Log.d("MessageFragment", "setSelect 0");
                if (mTabFragEvent == null || bFirst) {
                    Log.d("MessageFragment", "mTabFragEvent is null");
                    mTabFragEvent = new FragmentEvent();
                    transaction.replace(R.id.id_msg_body, mTabFragEvent);
                } else {
                    Log.d("MessageFragment", "mTabFragEvent not null");
                    transaction.replace(R.id.id_msg_body,mTabFragEvent);
                }
                break;
            case 1:
                Log.d("MessageFragment", "setSelect 1 ");
                if (mTabFragAlarm == null|| bFirst) {
                    Log.d("MessageFragment", "alarm is null");
                    mTabFragAlarm = new FragmentAlarm();
                    transaction.replace(R.id.id_msg_body, mTabFragAlarm);
                } else {
                    Log.d("MessageFragment", "alarm not null");
                    transaction.replace(R.id.id_msg_body,mTabFragAlarm);
                }
                break;
            case 2:
                Log.d("MessageFragment", "setSelect 2 ");
                if (mTabFragNotify == null|| bFirst) {
                    Log.d("MessageFragment", "notify is null");
                    mTabFragNotify = new FragmentNofity();
                    transaction.replace(R.id.id_msg_body, mTabFragNotify);
                } else {
                    Log.d("MessageFragment", "notify not null");
                    transaction.replace(R.id.id_msg_body,mTabFragNotify);
                }
                break;
            case 3:
                Log.d("MessageFragment", "setSelect 3 ");
                if (mTabFragVoice == null|| bFirst) {
                    Log.d("MessageFragment", "voice is null");
                    mTabFragVoice = new FragmentVoice();
                    transaction.replace(R.id.id_msg_body, mTabFragVoice);
                } else {
                    Log.d("MessageFragment", "voice not null");
                    transaction.replace(R.id.id_msg_body,mTabFragVoice);
                }
                break;
        }
        transaction.commit();
    }

    /*private void hideFragment(FragmentTransaction transaction) {
        if (mTabFragEvent != null) {
            transaction.hide(mTabFragEvent);
        }
        if (mTabFragAlarm != null) {
            transaction.hide(mTabFragAlarm);
        }
        if (mTabFragNotify != null) {
            transaction.hide(mTabFragNotify);
        }
        if (mTabFragVoice != null) {
            transaction.hide(mTabFragVoice);
        }
    }*/

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
                setSelect(false,0, view.getId());
                break;
            case R.id.msg_navi_alarm:
                setSelect(false,1, view.getId());
                break;
            case R.id.msg_navi_notify:
                setSelect(false,2, view.getId());
                break;
            case R.id.msg_navi_voice:
                setSelect(false,3, view.getId());
                break;
        }
    }

    private void setStyleById(int id) {
        for (Button btn : tabBtnList) {
            if (btn.getId() == id) {
                btn.setBackgroundResource(R.drawable.button_pressed);
                btn.setTextColor(getResources().getColor(R.color.white));
            } else {
                btn.setTextColor(getResources().getColor(R.color.black));
                btn.setBackgroundColor(Color.WHITE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event)
    {

    }
}
