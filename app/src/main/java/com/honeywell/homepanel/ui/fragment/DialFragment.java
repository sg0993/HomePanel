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
import com.honeywell.homepanel.Utils.LogMgr;
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
public class DialFragment extends Fragment implements View.OnClickListener {
    /*
    @Bind(R.id.dialTv)
    TextView dialTv;
    */
    private String title = "";
    private Button speeddial = null;
    private Button subphone = null;
    private Button keypad = null;

    private View speeddialUnderline = null;
    private View subphoneUnderline = null;
    private View keypadUnderline = null;

    private final List<Button> btnList = new ArrayList<Button>();
    private FragmentManager fm;
    private FragmentTransaction ft;
    private SpeedDialFragment speedDialFragment;
    private SubphoneFragment subphoneFragment;
    private KeypadFragment keyPadFragment;
    private final String BLACK_GREY = "#4A4A4A";
    private final String NORMAL_GREY = "#ABABAB";

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

        fm = getChildFragmentManager();
        ft = fm.beginTransaction();
        setBackgroundColorById(0);
//        speeddial.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_dark_pressed));
        speeddial.setTextColor(Color.parseColor(BLACK_GREY));

        // TODO: 2017/7/26  luoxiang
        if (R.id.fragment_content != 0) {
            ft.replace(R.id.fragment_content, new SpeedDialFragment());
            ft.commitAllowingStateLoss();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        btnList.clear();
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

        speeddialUnderline = view.findViewById(R.id.dial_underline);
        subphoneUnderline = view.findViewById(R.id.subphone_underline);
        keypadUnderline = view.findViewById(R.id.keypad_underline);
    }

    private void initEvents() {
        speeddial.setOnClickListener(this);
        subphone.setOnClickListener(this);
        keypad.setOnClickListener(this);
        btnList.add(speeddial);
        btnList.add(subphone);
        btnList.add(keypad);
    }

    private void setBackgroundColorById(int btnId) {
        switch (btnId) {
            case 0:
                speeddial.setTextColor(Color.parseColor(BLACK_GREY));
                subphone.setTextColor(Color.parseColor(NORMAL_GREY));
                keypad.setTextColor(Color.parseColor(NORMAL_GREY));

                speeddialUnderline.setVisibility(View.VISIBLE);
                subphoneUnderline.setVisibility(View.INVISIBLE);
                keypadUnderline.setVisibility(View.INVISIBLE);
                break;
            case 1:
                speeddial.setTextColor(Color.parseColor(NORMAL_GREY));
                subphone.setTextColor(Color.parseColor(BLACK_GREY));
                keypad.setTextColor(Color.parseColor(NORMAL_GREY));

                speeddialUnderline.setVisibility(View.INVISIBLE);
                subphoneUnderline.setVisibility(View.VISIBLE);
                keypadUnderline.setVisibility(View.INVISIBLE);
                break;
            case 2:
                speeddial.setTextColor(Color.parseColor(NORMAL_GREY));
                subphone.setTextColor(Color.parseColor(NORMAL_GREY));
                keypad.setTextColor(Color.parseColor(BLACK_GREY));

                speeddialUnderline.setVisibility(View.INVISIBLE);
                subphoneUnderline.setVisibility(View.INVISIBLE);
                keypadUnderline.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void onClick(View v) {
        fm = getChildFragmentManager();
        ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.speed_dial:
                setBackgroundColorById(0);
//                speeddial.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_dark_pressed));
//                subphone.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
//                keypad.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
                if (speedDialFragment == null)
                    speedDialFragment = new SpeedDialFragment();
                ft.replace(R.id.fragment_content, speedDialFragment);
                break;
            case R.id.subphone:
                setBackgroundColorById(1);
//                subphone.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_dark_pressed));
//                speeddial.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
//                keypad.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
                if (subphoneFragment == null)
                    subphoneFragment = new SubphoneFragment();
                ft.replace(R.id.fragment_content, subphoneFragment);
                break;
            case R.id.keypad:
                LogMgr.d("keypad");
                Keypad123Fragment.isCall = true;
                setBackgroundColorById(2);
//                keypad.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_dark_pressed));
//                speeddial.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
//                subphone.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
                if (keyPadFragment == null)
                    keyPadFragment = new KeypadFragment();
                ft.replace(R.id.fragment_content, keyPadFragment);
                break;
            default:
                break;
        }
        ft.commitAllowingStateLoss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }
}



