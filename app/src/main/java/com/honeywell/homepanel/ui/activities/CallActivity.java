package com.honeywell.homepanel.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.ui.fragment.CallOutgoingNeighborFragment;
import com.honeywell.homepanel.ui.uicomponent.TopViewBrusher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by H135901 on 1/24/2017.
 */

public  class CallActivity extends FragmentActivity implements View.OnClickListener{

    private TopViewBrusher mTopViewBrusher = new TopViewBrusher();
    private Map<Integer,Fragment> mFragments = new HashMap<Integer, Fragment>();

    private int mCurCallStatus = CommonData.CALL_OUTGOING_NEIGHBOR;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setContentView(R.layout.layout_call);
        initViewAndListener();
        mTopViewBrusher.initTop(this);
        /*mCurCallStatus = // from intent*/
        fragmentAdd(true,mCurCallStatus);
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
    }


    public void fragmentAdd(boolean bAdd, int position)  {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = mFragments.get(position);
        if(null == fragment){
            fragment = getNewFragMent(position);
        }
        if(bAdd){
            transaction.add(R.id.main_frameLayout, fragment);
        }
        else {
            transaction.replace(R.id.main_frameLayout, fragment);
        }
        transaction.commit();
    }
    private Fragment getNewFragMent(int position) {
        Fragment fragment = null;
        switch (position){
            case CommonData.CALL_OUTGOING_NEIGHBOR:
                fragment = new CallOutgoingNeighborFragment("" + position);
                break;
            case CommonData.call_CONNECTED_AUDIO_NETGHBOR:
                break;
            case CommonData.call_CONNECTED_VIDEO_NETGHBOR:
                break;
            case CommonData.CALL_LOBBY_INCOMMING:
                break;
            case CommonData.CALL_LOBBY_CONNECTED:
                break;
            default:
                break;
        }
        mFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void onClick(View view) {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }
}
