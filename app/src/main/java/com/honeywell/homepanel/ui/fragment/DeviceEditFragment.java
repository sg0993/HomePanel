package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.ui.activities.CamerasActivity;
import com.honeywell.homepanel.ui.activities.IndicatorBoardActivity;
import com.honeywell.homepanel.ui.uicomponent.DevicesImageAdapter;
import com.honeywell.homepanel.ui.uicomponent.PageViewAdapter;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class DeviceEditFragment extends Fragment implements View.OnClickListener {
    private String title = "";
    private PageViewAdapter mPageAdaper = null;
    private ImageView choose_scenarioImageView = null;
    private Context mContext = null;
    GridView gridView;
    private static final int[] IMAGES = {R.mipmap.device_elevator3x,
            R.mipmap.device_camera3x, R.mipmap.device_air3x};
    private static final int[] TEXTES = {R.string.device_elevator,
            R.string.device_camera, R.string.device_air};

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_devices, null);
        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(new DevicesImageAdapter(getActivity(), IMAGES, TEXTES));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                if (position == 0) {
                    startActivity(new Intent(mContext, IndicatorBoardActivity.class));
                }
                if (position == 1) {
                    startActivity(new Intent(mContext, CamerasActivity.class));
                }
            }
        });
        return view;
    }

    public DeviceEditFragment(String title) {
        super();
        this.title = title;
    }
    public DeviceEditFragment() {
        super();
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            default:
                break;
        }
    }
}
