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
public class DialFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "DialFragment";
    /*
        @Bind(R.id.dialTv)
        TextView dialTv;
        */
    private String title = "";
    private Button speeddial = null;
    private Button subphone = null;
    private Button keypad = null;
    private List<Button> btnList = new ArrayList<Button>();
    private FragmentManager fm;
    private FragmentTransaction ft;
    private SpeedDialFragment speedDialFragment;
    private SubphoneFragment subphoneFragment;
    private KeypadFragment keyPadFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dial, null);
        /*
        ButterKnife.bind(this, view);
        dialTv.setText("Butter knift Test!!!");
        */
        initViews(view);
        initEvents();
        Log.d(TAG,"DialFragment.onCreateView() 11111111111111");
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        setBackgroundColorById(R.id.speed_dial);
        speeddial.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_dark_pressed));
        ft.replace(R.id.fragment_content, new SpeedDialFragment());
        ft.commit();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public DialFragment(String title) {
        super();
        this.title = title;

    }
    public DialFragment() {
        super();
    }
    private void initViews(View view) {
        speeddial = (Button) view.findViewById(R.id.speed_dial);
        subphone = (Button) view.findViewById(R.id.subphone);
        keypad = (Button) view.findViewById(R.id.keypad);
    }
    private void initEvents(){
        speeddial.setOnClickListener(this);
        subphone.setOnClickListener(this);
        keypad.setOnClickListener(this);
        btnList.add(speeddial);
        btnList.add(subphone);
        btnList.add(keypad);
    }
    private void setBackgroundColorById(int btnId) {
        for (Button btn : btnList) {
            if (btn.getId() == btnId) {
                btn.setBackgroundResource(R.drawable.button_pressed);
                btn.setTextColor(getResources().getColor(R.color.white));
            } else {
                btn.setTextColor(getResources().getColor(R.color.black));
                btn.setBackgroundColor(Color.WHITE);
            }
        }
    }
    public void onClick(View v){
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        switch (v.getId()){
            case R.id.speed_dial:
                setBackgroundColorById(R.id.speed_dial);
                speeddial.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_dark_pressed));
                subphone.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
                keypad.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
                if(speedDialFragment == null)
                    speedDialFragment = new SpeedDialFragment();
                ft.replace(R.id.fragment_content, speedDialFragment);
                break;
            case R.id.subphone:
                setBackgroundColorById(R.id.subphone);
                subphone.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_dark_pressed));
                speeddial.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
                keypad.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
                if(subphoneFragment == null)
                    subphoneFragment = new SubphoneFragment();
                ft.replace(R.id.fragment_content, subphoneFragment);
                break;
            case R.id.keypad:
                setBackgroundColorById(R.id.keypad);
                keypad.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_dark_pressed));
                speeddial.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
                subphone.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
                if(keyPadFragment == null)
                    keyPadFragment = new KeypadFragment();
                ft.replace(R.id.fragment_content, keyPadFragment);
                break;
            default:
                break;
        }
        ft.commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event)
    {

    }
}



