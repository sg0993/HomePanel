package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.ui.activities.PasswordEnterActivity;
import com.honeywell.homepanel.ui.uicomponent.ImageAdapter;
import com.honeywell.homepanel.ui.uicomponent.PageViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.widget.AdapterView.LayoutParams;
import static android.widget.AdapterView.OnClickListener;
import static android.widget.AdapterView.OnItemLongClickListener;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class ScenarioEditFragment extends Fragment implements View.OnLongClickListener {
    private String title = "";
    private PageViewAdapter mPageAdaper = null;
    private GridView choose_armImageView = null;
    private Context mContext = null;
    GridView gridView;
    private static final int[] IMAGES = {R.mipmap.secnario_home3x,
            R.mipmap.secnario_away_blue3x, R.mipmap.secnario_sleep3x, R.mipmap.secnario_wakeup3x};
    private static final int[] TEXTES = {R.string.secnario_home, R.string.secnario_away,
            R.string.scenario_sleep, R.string.scenario_wakeup};


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_scenarioedit, null);
        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(getActivity(), IMAGES, TEXTES));
        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                showpopupwindow(inflater);
                return true;
            }
        });
        return view;
    }

    private void showpopupwindow(LayoutInflater inflater) {
        Button btEdit, btCancel;
        View popupView = inflater.inflate(R.layout.layout_senespopup, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(gridView, Gravity.BOTTOM, 0, 0);

        btCancel = (Button) popupView.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        btEdit = (Button) popupView.findViewById(R.id.btEdit);
        btEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PasswordEnterActivity.class));
            }
        });
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public ScenarioEditFragment(String title) {
        super();
        this.title = title;
    }


    @Override
    public boolean onLongClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.gridView:
                //              showSceneOperate();
                break;
            default:
                break;
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }

}
