package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.ui.uicomponent.ImageAdapter;
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
    private static final int[] IMAGES = {R.mipmap.device_elevator3x, R.mipmap.device_camera3x, R.mipmap.device_air3x};
    private static final int[] TEXTES = {R.string.device_elevator, R.string.device_camera, R.string.device_air};

    public DeviceEditFragment(String title) {
        super();
        this.title = title;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_deviceedit, null);
        gridView= (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(getActivity(), IMAGES, TEXTES));

        return view;
    }

    private void initViews() {
        View deviceedit = mPageAdaper.pageViews.get(0);
        choose_scenarioImageView = (ImageView) deviceedit.findViewById(R.id.choose_scenarioImage);
        choose_scenarioImageView.setOnClickListener(this);
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
