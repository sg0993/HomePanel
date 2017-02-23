package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.Message.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class CallOutgoingNeighborFragment extends Fragment implements View.OnClickListener{
    private String mTitle = "";
    private static  final  String TAG = "HomeFragment";
    private Context mContext = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_calloutgoing_neighborl, null);
        initViews();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public CallOutgoingNeighborFragment(String title) {
        super();
        this.mTitle = title;
    }
    public CallOutgoingNeighborFragment() {
        super();
    }

    private void initViews() {

    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            default:
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }
}
