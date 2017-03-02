package com.honeywell.homepanel.ui.uicomponent;

import android.view.View;
import android.widget.TextView;

import com.honeywell.homepanel.R;

/**
 * Created by H135901 on 2/23/2017.
 */

public class CallTopBrusher {

    public static  final  int POSITION_TOP = 0;
    public static  final  int POSITION_BOTTOM = 1;


    private TextView top_tv = null;
    private TextView bottom_tv = null;

    private String topStr = null;
    private String bottomStr = null;


    public void init(View view) {
        top_tv = (TextView) view.findViewById(R.id.top_tv);
        bottom_tv = (TextView) view.findViewById(R.id.bottom_tv);
        setRes();
    }

    public CallTopBrusher(String topStr,String bottomStr) {
        this.topStr = topStr;
        this.bottomStr = bottomStr;
    }

    private void setRes() {
        top_tv.setText(topStr);
        bottom_tv.setText(bottomStr);
    }

    public void setResText(int postion,String text){
        switch (postion){
            case POSITION_TOP:
                top_tv.setText(text);
                break;
            case POSITION_BOTTOM:
                bottom_tv.setText(text);
                break;
            default:
                break;
        }
    }
}
