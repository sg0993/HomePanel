package com.honeywell.homepanel.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.ui.fragment.DeviceEditFragment;
import com.honeywell.homepanel.ui.fragment.DialFragment;
import com.honeywell.homepanel.ui.fragment.HomeFragment;
import com.honeywell.homepanel.ui.fragment.MessageFragment;
import com.honeywell.homepanel.ui.fragment.ScenarioEditFragment;
import com.honeywell.homepanel.ui.fragment.SettingFragment;
import com.honeywell.homepanel.ui.uicomponent.TopViewBrusher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by H135901 on 1/24/2017.
 */

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener{
    private static final String TAG = "BaseActivity";
    private static int mLeftCurPage = CommonData.LEFT_SELECT_HOME;
    private static int mLeftPrePage = CommonData.LEFT_SELECT_HOME;
    private List<View>mLeftViews = new ArrayList<View>();
    private List<ImageView>mLeftImages = new ArrayList<ImageView>();
    private Map<Integer,Fragment> mFragments = new HashMap<Integer, Fragment>();
    private TopViewBrusher mTopViewBrusher = new TopViewBrusher();

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setContentView(getContent());
        initViewAndListener();
        mTopViewBrusher.init(this);
        fragmentAdd(true,mLeftCurPage);

        setLeftNavifation(mLeftCurPage);
    }
 	@Override
    protected void onResume() {
        super.onResume();
        //setLeftNavifation(mLeftCurPage);

        mTopViewBrusher.setTop(getApplicationContext());
    }
    @Override
    protected void onDestroy() {
        Log.d(TAG,TAG+".onDestroy() 111111");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        mTopViewBrusher.destory();
    }

    private void fragmentAdd(boolean bAdd, int position)  {
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
            case CommonData.LEFT_SELECT_HOME:
                fragment = new HomeFragment("" + position);
                break;
            case CommonData.LEFT_SELECT_SCENARIOEDIT:
                fragment = new ScenarioEditFragment("" + position);
                break;
            case CommonData.LEFT_SELECT_DEVICEEDIT:
                fragment = new DeviceEditFragment("" + position);
                break;
            case CommonData.LEFT_SELECT_MESSAGE:
                fragment = new MessageFragment("" + position);
                break;
            case CommonData.LEFT_SELECT_DIAL:
                fragment = new DialFragment("" + position);
                break;
            case CommonData.LEFT_SELECT_SETTING:
                fragment = new SettingFragment("" + position);
                break;
        }
        mFragments.put(position, fragment);
        return fragment;
    }

    protected void initViewAndListener() {
        initLeftViews();
    }

    private void initLeftViews() {
        mLeftViews.add(CommonData.LEFT_SELECT_HOME,findViewById(R.id.home_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_SCENARIOEDIT,findViewById(R.id.scenario_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_DEVICEEDIT,findViewById(R.id.device_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_MESSAGE,findViewById(R.id.message_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_DIAL,findViewById(R.id.dial_layout));
        mLeftViews.add(CommonData.LEFT_SELECT_SETTING,findViewById(R.id.setting_layout));

        for (int i = 0; i <= CommonData.LEFT_SELECT_SETTING  ; i++) {
            mLeftViews.get(i).setOnClickListener(this);
        }

        mLeftImages.add(CommonData.LEFT_SELECT_HOME,(ImageView) findViewById(R.id.home_image));
        mLeftImages.add(CommonData.LEFT_SELECT_SCENARIOEDIT,(ImageView) findViewById(R.id.scenario_image));
        mLeftImages.add(CommonData.LEFT_SELECT_DEVICEEDIT,(ImageView) findViewById(R.id.device_image));
        mLeftImages.add(CommonData.LEFT_SELECT_MESSAGE,(ImageView) findViewById(R.id.message_image));
        mLeftImages.add(CommonData.LEFT_SELECT_DIAL,(ImageView) findViewById(R.id.dial_image));
        mLeftImages.add(CommonData.LEFT_SELECT_SETTING,(ImageView) findViewById(R.id.setting_image));
    }


    protected  void setLeftNavifation(int curPage) {
        Log.d(TAG,"setLeftNavifation() mCur:"+mLeftCurPage+",nPre:"+mLeftPrePage+", 11111111111111");
        mLeftPrePage = mLeftCurPage;
        mLeftCurPage = curPage;
        initLeftPage();
    }

    protected abstract int getContent();

    private void initLeftPage() {
        Log.d(TAG,"initLeftPage() 11111111111111");
        switch (mLeftCurPage){
            case CommonData.LEFT_SELECT_HOME:
                mLeftImages.get(mLeftCurPage).setImageResource(R.mipmap.home_select);
                break;
            case CommonData.LEFT_SELECT_SCENARIOEDIT:
                mLeftImages.get(mLeftCurPage).setImageResource(R.mipmap.scenario_select);
                break;
            case CommonData.LEFT_SELECT_DEVICEEDIT:
                mLeftImages.get(mLeftCurPage).setImageResource(R.mipmap.device_select);
                break;
            case CommonData.LEFT_SELECT_MESSAGE:
                mLeftImages.get(mLeftCurPage).setImageResource(R.mipmap.message_select);
                break;
            case CommonData.LEFT_SELECT_DIAL:
                mLeftImages.get(mLeftCurPage).setImageResource(R.mipmap.dial_select);
                break;
            case CommonData.LEFT_SELECT_SETTING:
                mLeftImages.get(mLeftCurPage).setImageResource(R.mipmap.setting_select);
                break;
        }
        if(mLeftCurPage != mLeftPrePage){
            switch (mLeftPrePage){
                case CommonData.LEFT_SELECT_HOME:
                    mLeftImages.get(mLeftPrePage).setImageResource(R.mipmap.home);
                    break;
                case CommonData.LEFT_SELECT_SCENARIOEDIT:
                    mLeftImages.get(mLeftPrePage).setImageResource(R.mipmap.scenario);
                    break;
                case CommonData.LEFT_SELECT_DEVICEEDIT:
                    mLeftImages.get(mLeftPrePage).setImageResource(R.mipmap.device);
                    break;
                case CommonData.LEFT_SELECT_MESSAGE:
                    mLeftImages.get(mLeftPrePage).setImageResource(R.mipmap.message);
                    break;
                case CommonData.LEFT_SELECT_DIAL:
                    mLeftImages.get(mLeftPrePage).setImageResource(R.mipmap.dial);
                    break;
                case CommonData.LEFT_SELECT_SETTING:
                    mLeftImages.get(mLeftPrePage).setImageResource(R.mipmap.setting);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        for (int i = 0; i <=  CommonData.LEFT_SELECT_SETTING; i++) {
            if(mLeftViews.get(i).getId() == view.getId()) {
                setLeftNavifation(i);
                fragmentAdd(false, i);
                break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode,event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event)
    {

    }
}
