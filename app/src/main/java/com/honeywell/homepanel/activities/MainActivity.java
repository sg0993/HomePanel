package com.honeywell.homepanel.activities;

import android.os.Bundle;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;

public class MainActivity extends BaseActivity{

    //private PageViewAdapter mPageAdaper = null;

    @Override
    protected int getContent() {
        return R.layout.layout_home;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLeftNavifation(CommonData.LEFT_SELECT_HOME);
    }



    @Override
    protected void initViewAndListener() {
        super.initViewAndListener();
    }
}
