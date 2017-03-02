package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.ui.uicomponent.CalRightBrusher;
import com.honeywell.homepanel.ui.uicomponent.CallBottomBrusher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class CallLobbyIncomingAndConnected extends Fragment implements View.OnClickListener{
    private String mTitle = "";
    private static  final  String TAG = "CallNeighborVideoConnected";
    private Context mContext = null;



    private CallBottomBrusher mCallBottomBrusher = new CallBottomBrusher
            (this,R.mipmap.call_incoming_background,R.mipmap.call_incoming_call,"Answer",
                    R.mipmap.call_red_background,R.mipmap.call_end_image,"End",
                    R.mipmap.call_blue_background,R.mipmap.call_dooropen,"Open");

    private CalRightBrusher mCallRightBrusher = new CalRightBrusher(
            this,R.mipmap.call_right_background,R.mipmap.call_microphone,
            R.mipmap.call_right_background,R.mipmap.call_speaker);
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
        mCallRightBrusher.init(view);
        mCallBottomBrusher.setVisible(CallBottomBrusher.BOTTOM_POSTION_MIDDLE,View.GONE);
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

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.left_btn:
                Toast.makeText(mContext,"call_left",Toast.LENGTH_SHORT).show();
                break;
            case R.id.right_btn:
                Toast.makeText(mContext,"call_right",Toast.LENGTH_SHORT).show();
                break;
            case R.id.top_btn:
                Toast.makeText(mContext,"top_btn",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bottom_btn:
                Toast.makeText(mContext,"bottom_btn",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }
}
