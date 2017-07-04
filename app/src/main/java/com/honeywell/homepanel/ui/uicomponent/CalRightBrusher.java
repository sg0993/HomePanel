package com.honeywell.homepanel.ui.uicomponent;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.honeywell.homepanel.R;

/**
 * Created by H135901 on 2/23/2017.
 */

public class CalRightBrusher {

    private int top_btn_back = 0;
    private int top_image = 0;
    /*private int middle_btn_back = 0;
    private int middle_image = 0;*/
    private int bottom_btn_back = 0;
    private int bottom_image = 0;

    private Button top_btn = null;
    private ImageView top_imageview = null;
    private Button middle_btn = null;
    private ImageView middle_imageview = null;
    private Button bottom_btn = null;
    private ImageView bottom_imageview = null;

    private View.OnClickListener mOnclickListener = null;

    public void init(View view) {
        top_btn = (Button) view.findViewById(R.id.top_btn);
        top_imageview = (ImageView) view.findViewById(R.id.top_image);
        /*middle_btn = (Button)view.findViewById(R.id.middle_btn);
        middle_imageview = (ImageView)view.findViewById(R.id.middle_image);*/
        bottom_btn = (Button) view.findViewById(R.id.bottom_btn);
        bottom_imageview = (ImageView) view.findViewById(R.id.bottom_image);

        top_btn.setOnClickListener(mOnclickListener);
        bottom_btn.setOnClickListener(mOnclickListener);
        setRes();
    }

    public CalRightBrusher(View.OnClickListener clickListener, int top_btn_back, int top_image/*, int middle_btn_back, int middle_image*/
            , int bottom_btn_back, int bottom_image) {
        mOnclickListener = clickListener;
        this.top_btn_back = top_btn_back;
        this.top_image = top_image;
       /* this.middle_btn_back = middle_btn_back;
        this.middle_image = middle_image;*/
        this.bottom_btn_back = bottom_btn_back;
        this.bottom_image = bottom_image;
    }

    private void setRes() {
        //top_btn.setBackgroundResource(top_btn_back);
         top_imageview.setBackgroundResource(top_image);
       /* middle_btn.setBackgroundResource(middle_btn_back);
        middle_imageview.setBackgroundResource(middle_image);*/
//        bottom_btn.setBackgroundResource(bottom_btn_back);
        bottom_imageview.setBackgroundResource(bottom_image);
    }
}
