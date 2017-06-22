package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.IConfigServiceManageUtil;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.ui.UIMessagesControl;
import com.honeywell.homepanel.ui.activities.CamerasActivity;
import com.honeywell.homepanel.ui.activities.IndicatorBoardActivity;
import com.honeywell.homepanel.ui.activities.RelayActivity;
import com.honeywell.homepanel.ui.uicomponent.DevicesImageAdapter;
import com.honeywell.homepanel.ui.uicomponent.PageViewAdapter;
import com.honeywell.homepanel.ui.uicomponent.UISendLockMessage;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class DeviceEditFragment extends Fragment implements View.OnClickListener {
    private String TAG = "DeviceEditFragment";
    private String title = "";
    private PageViewAdapter mPageAdaper = null;
    private ImageView choose_scenarioImageView = null;
    private Context mContext = null;
    private boolean mStatus = true;
    GridView gridView;
    private static final int[] IMAGES = {R.mipmap.lift,
            R.mipmap.camera, R.mipmap.relay, R.mipmap.device_white,
            R.mipmap.device_white, R.mipmap.device_white, R.mipmap.device_white,
            R.mipmap.device_white};
    private static final int[] TEXTES = {R.string.device_elevator,
            R.string.device_camera, R.string.device_relay, R.string.device_empty,
            R.string.device_empty, R.string.device_empty, R.string.device_empty,
            R.string.device_empty};

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
                } else if (position == 1) {
                    startActivity(new Intent(mContext, CamerasActivity.class));
                } else if (position == 2) {
                    startActivity(new Intent(mContext, RelayActivity.class));
                } else if (position == 3) {
                    //UISendLockMessage.SendLockControlCommand(mStatus);
                    //mStatus = !mStatus;
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
