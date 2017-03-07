package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.Message.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class SettingFragment extends Fragment {
    private String title = "";
    private GridView gridView = null;
    private List<Map<String,Object>> data_list;
    private SimpleAdapter simpleAdapter;
    private int[] icon = {R.mipmap.setting_wifi,R.mipmap.setting_location,
            R.mipmap.setting_volume, R.mipmap.setting_date,R.mipmap.setting_account,
            R.mipmap.setting_advanced, R.mipmap.setting_cleaning,R.mipmap.setting_upgrade};
    private String[] iconName = {"WIFI","Location","Brightness","Date&Time",
            "Account Settings","Advanced Settings","Cleaning","Upgrade"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        gridView = (GridView) view.findViewById(R.id.gridview_setting);
        data_list = new ArrayList<Map<String, Object>>();
        getData();
        simpleAdapter = new SimpleAdapter(getActivity().getApplicationContext(),data_list,R.layout.item_setting,
                new String[] {"image","text"},new int[] {R.id.image, R.id.text});
        gridView.setAdapter(simpleAdapter);
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public SettingFragment(String title) {
        super();
        this.title = title;

    }

    public SettingFragment() {
        super();
    }
    public List<Map<String, Object>> getData(){
        for(int i=0;i<icon.length;i++){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("image",icon[i]);
            map.put("text",iconName[i]);
            data_list.add(map);
        }
        return data_list;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event)
    {

    }
}
