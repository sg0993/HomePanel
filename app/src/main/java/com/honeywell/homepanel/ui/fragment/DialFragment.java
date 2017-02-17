package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.honeywell.homepanel.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class DialFragment extends Fragment implements View.OnClickListener{
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
    public DialFragment(String title) {
        super();
        this.title = title;

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dial, null);
        /*
        ButterKnife.bind(this, view);
        dialTv.setText("Butter knift Test!!!");
        */
        initViews(view);
        initEvents();

        // 進入系統默認為speedDial
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        setBackgroundColorById(R.id.speed_dial);
        speeddial.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_dark_pressed));
        ft.replace(R.id.fragment_content, new SpeedDialFragment());
        ft.commit();
        return view;
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
                btn.setBackgroundColor(Color.BLACK);
            } else {
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
                ft.replace(R.id.fragment_content, new SpeedDialFragment());
                break;
            case R.id.subphone:
                setBackgroundColorById(R.id.subphone);
                subphone.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_dark_pressed));
                speeddial.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
                keypad.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
                ft.replace(R.id.fragment_content, new SubphoneFragment());
                break;
            case R.id.keypad:
                setBackgroundColorById(R.id.keypad);
                keypad.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_dark_pressed));
                speeddial.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
                subphone.setTextColor(getResources().getColorStateList(R.color.common_google_signin_btn_text_light_pressed));
                ft.replace(R.id.fragment_content, new KeypadFragment());
                break;
            default:
                break;
        }
        ft.commit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.unbind(this);
    }
}



