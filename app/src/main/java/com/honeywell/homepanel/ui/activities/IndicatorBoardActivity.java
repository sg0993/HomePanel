package com.honeywell.homepanel.ui.activities;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.honeywell.homepanel.R;

/**
 * Created by H135901 on 2/16/2017.
 */

public class IndicatorBoardActivity extends Activity {
    private static  final  int [] TEXTES = {R.string.indicator_board_cancel,
            R.string.indicator_board_tap};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_deviceelevator);
    }

    public void onClickCancel(View view)
    {
        finish();
    }
}
