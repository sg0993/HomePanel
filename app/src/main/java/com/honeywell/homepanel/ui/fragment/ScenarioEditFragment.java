package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.honeywell.homepanel.R;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.ui.activities.AlarmZoneActivity;
import com.honeywell.homepanel.ui.activities.BaseActivity;
import com.honeywell.homepanel.ui.activities.IndicatorBoardActivity;
import com.honeywell.homepanel.ui.activities.MainActivity;
import com.honeywell.homepanel.ui.activities.PasswordEnterActivity;
import com.honeywell.homepanel.ui.activities.ScenarioSelectActivity;

import com.honeywell.homepanel.common.Message.MessageEvent;

import com.honeywell.homepanel.ui.activities.ScenarioSelectHintActivity;
import com.honeywell.homepanel.ui.uicomponent.ImageAdapter;
import com.honeywell.homepanel.ui.uicomponent.PageViewAdapter;
import com.honeywell.homepanel.ui.uicomponent.SencesImageAdapter;


import butterknife.OnLongClick;

import static android.R.attr.bottom;
import static android.R.attr.onClick;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.widget.AdapterView.*;
import static com.honeywell.homepanel.common.CommonData.SENCES_EDIT;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class ScenarioEditFragment extends Fragment implements OnLongClickListener {
    private FrameLayout mframement = null;
    private LinearLayout mLinearLayout = null;
    private Context mContext = null;
    GridView gridView;
    private static final int[] IMAGES = {R.mipmap.secnario_home3x,
            R.mipmap.secnario_away_blue3x, R.mipmap.secnario_sleep3x, R.mipmap.secnario_wakeup3x};
    private static final int[] TEXTES = {R.string.secnario_home, R.string.secnario_away,
            R.string.scenario_sleep, R.string.scenario_wakeup};
    private int mSelect_Scenario = 1;
    private PopupWindow mpopupWindow = null;
    private Intent mIntent;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_scenarioedit, null);

        mframement = (FrameLayout) view.findViewById(R.id.scenes_frameLayout);
        mframement.getForeground().setAlpha(0);

        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(new SencesImageAdapter(getActivity(), IMAGES, TEXTES));
        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                mframement.getForeground().setAlpha(150);
                showpopupwindow(inflater);
                return true;
            }
        });
        return view;
    }


    private void showpopupwindow(LayoutInflater inflater) {
        Button btEdit, btCancel;

        View popupView = inflater.inflate(R.layout.layout_senespopup, null);
        mpopupWindow = new PopupWindow(
                popupView,
                LayoutParams.MATCH_PARENT,
                400);
        mpopupWindow.setBackgroundDrawable(new ColorDrawable());
        mpopupWindow.setOutsideTouchable(true);
        mpopupWindow.showAtLocation(gridView, Gravity.BOTTOM, 0, 0);
        btCancel = (Button) popupView.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mpopupWindow.dismiss();
                mframement.getForeground().setAlpha(0);
            }
        });

        btEdit = (Button) popupView.findViewById(R.id.btEdit);
        btEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PasswordEnterActivity.class);
                intent.putExtra(CommonData.INTENT_KEY_SCENARIO, SENCES_EDIT);
                if (mpopupWindow != null && mpopupWindow.isShowing()) {
                    mpopupWindow.dismiss();
                }
                mframement.getForeground().setAlpha(0);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (mpopupWindow != null && mpopupWindow.isShowing()) {
            mpopupWindow.dismiss();
        }
        super.onDestroy();
    }

    public ScenarioEditFragment(String title) {
        super();
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
