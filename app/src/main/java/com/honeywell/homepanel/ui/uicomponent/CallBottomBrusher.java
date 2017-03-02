package com.honeywell.homepanel.ui.uicomponent;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.homepanel.R;

/**
 * Created by H135901 on 2/23/2017.
 */

public class CallBottomBrusher {

    public static final  int BOTTOM_POSTION_LEFT = 0;
    public static final  int BOTTOM_POSTION_MIDDLE = 1;
    public static final  int BOTTOM_POSTION_RIGHT = 2;

    private int left_btn_back = 0;
    private int left_image = 0;
    private String left_text = "";

    private int middle_btn_back = 0;
    private int middle_image = 0;
    private String middle_text = "";


    private int right_btn_back = 0;
    private int right_image = 0;
    private String right_text = "";

    private Button left_btn = null;
    private ImageView left_imageview = null;
    private TextView left_tv = null;

    private Button middle_btn = null;
    private ImageView middle_imageview = null;
    private TextView middle_tv = null;

    private Button right_btn = null;
    private ImageView right_imageview = null;
    private TextView right_tv = null;

    private View.OnClickListener mClickListener = null;
    public void init(View view) {
        left_btn = (Button)view.findViewById(R.id.left_btn);
        left_imageview = (ImageView)view.findViewById(R.id.left_imageview);
        left_tv = (TextView)view.findViewById(R.id.left_tv);

        middle_btn = (Button)view.findViewById(R.id.middle_btn);
        middle_imageview = (ImageView)view.findViewById(R.id.middle_imageview);
        middle_tv = (TextView)view.findViewById(R.id.middle_tv);

        right_btn = (Button)view.findViewById(R.id.right_btn);
        right_imageview = (ImageView)view.findViewById(R.id.right_imageview);
        right_tv = (TextView)view.findViewById(R.id.right_tv);

        left_btn.setOnClickListener(mClickListener);
        middle_btn.setOnClickListener(mClickListener);
        right_btn.setOnClickListener(mClickListener);
        setRes();
    }

    public CallBottomBrusher(View.OnClickListener clickListener,int left_btn_back, int left_image, String left_text,
                             int middle_btn_back, int middle_image, String middle_text,
                             int right_btn_back, int right_image, String right_text) {
        mClickListener = clickListener;
        this.left_btn_back = left_btn_back;
        this.left_image = left_image;
        this.left_text = left_text;
        this.middle_btn_back = middle_btn_back;
        this.middle_image = middle_image;
        this.middle_text = middle_text;
        this.right_btn_back = right_btn_back;
        this.right_image = right_image;
        this.right_text = right_text;
    }

    private void setRes() {
        left_btn.setBackgroundResource(left_btn_back);
        left_imageview.setImageResource(left_image);
        left_tv.setText(left_text);

        middle_btn.setBackgroundResource(middle_btn_back);
        middle_imageview.setImageResource(middle_image);
        middle_tv.setText(middle_text);

        right_btn.setBackgroundResource(right_btn_back);
        right_imageview.setImageResource(right_image);
        right_tv.setText(right_text);
    }
    public void setVisible(int position,int visible){
        switch (position){
            case BOTTOM_POSTION_LEFT:
                left_btn.setVisibility(visible);
                left_imageview.setVisibility(visible);
                left_tv.setVisibility(visible);
                break;
            case BOTTOM_POSTION_MIDDLE:
                middle_btn.setVisibility(visible);
                middle_imageview.setVisibility(visible);
                middle_tv.setVisibility(visible);
                break;
            case BOTTOM_POSTION_RIGHT:
                right_btn.setVisibility(visible);
                right_imageview.setVisibility(visible);
                right_tv.setVisibility(visible);
                break;
            default:
                break;
        }
    }
}
