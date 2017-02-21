package com.honeywell.homepanel.ui.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.ui.uicomponent.AdapterCallback;
import com.honeywell.homepanel.ui.uicomponent.PasswordAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by H135901 on 2/16/2017.
 */

public class PasswordEnterActivity extends Activity implements View.OnClickListener,AdapterCallback{
    private GridView gridView;


    private Button mCancelBtn = null;
    private StringBuffer mPasswordStr = new StringBuffer();

    private List<ImageView>mImageViews = new ArrayList<ImageView>();

    private static final int IMAGES[] = {
        R.mipmap.one,R.mipmap.two,R.mipmap.three,
            R.mipmap.four,R.mipmap.five,R.mipmap.six,
            R.mipmap.seven,R.mipmap.eight,R.mipmap.nine,
            R.mipmap.clear,R.mipmap.zero,R.mipmap.delete,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_passwordenter);

        initGridView();
        initViews();
    }
    private void initGridView() {
        GridView gridView= (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new PasswordAdapter(getApplicationContext(),this, IMAGES));
      /* gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

            }
        });*/
    }

    private void comparePassword(StringBuffer password ) {
        if(password.length() == CommonData.SRCURITY_PASSWORD_LENGTH){
            startActivity(new Intent(this,ScenarioSelectHintActivity.class));
            finish();
        }
    }

    private void setPasswordImages(int passwordLength) {
        for (int i = 0; i < CommonData.SRCURITY_PASSWORD_LENGTH; i++) {
            if(i < passwordLength){
                mImageViews.get(i).setImageResource(R.mipmap.passwordenter);
            }
            else{
                mImageViews.get(i).setImageResource(R.mipmap.nopasswordenter);
            }
        }
    }

    private void initViews() {
        mCancelBtn = (Button) findViewById(R.id.cancel);
        mCancelBtn.setOnClickListener(this);
        mImageViews.add((ImageView)findViewById(R.id.oneImage));
        mImageViews.add((ImageView)findViewById(R.id.twoImage));
        mImageViews.add((ImageView)findViewById(R.id.threeImage));
        mImageViews.add((ImageView)findViewById(R.id.fourImage));
        mImageViews.add((ImageView)findViewById(R.id.fiveImage));
        mImageViews.add((ImageView)findViewById(R.id.sixImage));
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.cancel:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void subviewOnclick(int position, String more) {
        if(position == 9){
            if(mPasswordStr.length() > 0){
                mPasswordStr.replace(0,mPasswordStr.length(),"");
            }
        }
        else if(position == 11){
            if(mPasswordStr.length() > 0){
                mPasswordStr.deleteCharAt(mPasswordStr.length()-1);
            }
        }
        else if(mPasswordStr.length() < CommonData.SRCURITY_PASSWORD_LENGTH){
            if(position == 10){
                mPasswordStr.append("0");
            }
            else{
                mPasswordStr.append(position+1);
            }
        }
        setPasswordImages(mPasswordStr.length());
        comparePassword(mPasswordStr);
    }
}