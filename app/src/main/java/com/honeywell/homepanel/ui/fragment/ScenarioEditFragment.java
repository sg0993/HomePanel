package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.honeywell.homepanel.IConfigService;
import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.IConfigServiceManageUtil;
import com.honeywell.homepanel.Utils.LogMgr;
import com.honeywell.homepanel.Utils.SceneDBUtil;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.pbx.PBXService;
import com.honeywell.homepanel.ui.activities.PasswordEnterActivity;
import com.honeywell.homepanel.ui.activities.ScenarioSelectActivity;
import com.honeywell.homepanel.ui.domain.TopStaus;
import com.honeywell.homepanel.ui.uicomponent.SencesImageAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.AdapterView.LayoutParams;
import static android.widget.AdapterView.OnClickListener;
import static android.widget.AdapterView.OnItemClickListener;
import static android.widget.AdapterView.OnItemLongClickListener;
import static android.widget.AdapterView.OnLongClickListener;
import static com.honeywell.homepanel.Utils.IConfigServiceManageUtil.getIntMapConfigM;
import static com.honeywell.homepanel.common.CommonData.SENCES_EDIT;


@SuppressLint("ValidFragment")
public class ScenarioEditFragment extends Fragment implements OnLongClickListener {
    private String TAG = "ScenarioEditFragment";
    private FrameLayout mframement = null;
    private LinearLayout mLinearLayout = null;
    private Context mContext = null;
    GridView gridView;

    private static final int[] IMAGES1 = {R.mipmap.home_blue,
            R.mipmap.away, R.mipmap.sleep, R.mipmap.wake_up};
    private static final int[] IMAGES2 = {R.mipmap.home3x,
            R.mipmap.away_red, R.mipmap.sleep, R.mipmap.wake_up};
    private static final int[] IMAGES3 = {R.mipmap.home3x,
            R.mipmap.away, R.mipmap.sleep_red, R.mipmap.wake_up};
    private static final int[] IMAGES4 = {R.mipmap.home3x,
            R.mipmap.away, R.mipmap.sleep, R.mipmap.wake_up_red};

    private static final int[] TEXTES = {R.string.secnario_home, R.string.secnario_away,
            R.string.scenario_sleep, R.string.scenario_wakeup};
    private int mSelect_Scenario = 1;
    private PopupWindow mpopupWindow = null;
    private Intent mIntent;
    private TextView sencestitle;
    int mSenceNum = 0;
    int mZoneNum = 0;
    String mSenarioName[] = new String[10];
    String mSenarioUUID[] = new String[10];


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SceneDBUtil.getInstance(getActivity());
        GetScenarioList();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        GetScenarioList();

    }

    @Override
    public void onResume() {
        super.onResume();
        int scenario = TopStaus.getInstance(this.getContext()).getCurScenario();
        switch (scenario){
            case CommonData.SCENARIO_HOME:
                gridView.setAdapter(new SencesImageAdapter(getActivity(), IMAGES1, TEXTES,0));
                break;
            case CommonData.SCENARIO_AWAY:
                gridView.setAdapter(new SencesImageAdapter(getActivity(), IMAGES2, TEXTES,1));
                break;
            case CommonData.SCENARIO_SLEEP:
                gridView.setAdapter(new SencesImageAdapter(getActivity(), IMAGES3, TEXTES,2));
                break;
            case CommonData.SCENARIO_WAKEUP:
                gridView.setAdapter(new SencesImageAdapter(getActivity(), IMAGES4, TEXTES,3));
                break;
            default:
                gridView.setAdapter(new SencesImageAdapter(getActivity(), IMAGES1, TEXTES,0));
                break;
        }
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_scenarioedit, null);


        mframement = (FrameLayout) view.findViewById(R.id.scenes_frameLayout);
        mframement.getForeground().setAlpha(0);

        gridView = (GridView) view.findViewById(R.id.gridView);

        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                int mode = IConfigServiceManageUtil.getIntMapConfigM(CommonData.JSON_HOMEPANELTYPE_KEY);
                Log.w(TAG, "onServiceConnected: working mode=" + mode);
                if (0 == mode) {
                    mframement.getForeground().setAlpha(150);
                    GetScenarioList();
                    showpopupwindow(inflater, position);
                }
                return true;
            }
        });
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mframement.getForeground().setAlpha(0);
                Intent intent = new Intent(getActivity().getApplicationContext(), PasswordEnterActivity.class);
                intent.putExtra(CommonData.INTENT_KEY_SCENARIO, position + 1);
                startActivity(intent);
            }
        });
        return view;
    }

    private void showpopupwindow(LayoutInflater inflater, final int position) {
        Button btEdit, btCancel;

        View popupView = inflater.inflate(R.layout.layout_senespopup, null);
        mpopupWindow = new PopupWindow(
                popupView,
                LayoutParams.MATCH_PARENT,
                200);
        mpopupWindow.setBackgroundDrawable(new ColorDrawable());
        mpopupWindow.setOutsideTouchable(true);
        mpopupWindow.showAtLocation(gridView, Gravity.BOTTOM, 0, 0);
        mpopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mframement.getForeground().setAlpha(1);
            }
        });
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
                intent.putExtra(CommonData.INTENT_KEY_SCENARIO_UUID, mSenarioUUID[position]);
                intent.putExtra(CommonData.INTENT_KEY_SCENARIO_NAME, mSenarioName[position]);
                Log.d(TAG, "Senario UUID:" + mSenarioUUID[position] + " postion:" + position);
                Log.d(TAG, "Senario name:" + mSenarioName[position] + " postion:" + position);
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
        if (SceneDBUtil.getSceneDBUtil() != null) {
            SceneDBUtil.getSceneDBUtil().delete();
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

    private void GetScenarioList() {
        Log.d(TAG, "getscenariolist");
        JSONObject resObject = SceneDBUtil.getSceneList();
        if (resObject == null) {
            Log.d(TAG, "GetScenarioList resObject is null");
            return;
        } else {
            Log.d(TAG, "GetScenarioList response:" + resObject.toString());
        }
        String action = resObject.optString(CommonJson.JSON_ACTION_KEY, "");
        String subAction = resObject.optString(CommonJson.JSON_SUBACTION_KEY, "");
        String errorCode = resObject.optString(CommonJson.JSON_ERRORCODE_KEY, "");
        if (errorCode.equals("") || errorCode == null) {
            Log.e(TAG, "errorCode is null");
            return;
        }
        if (Integer.parseInt(errorCode) != 0) {
            Log.e(TAG, "GetScenarioList failed");
            return;
        }
        if ((!action.isEmpty() && action.equals(CommonJson.JSON_ACTION_VALUE_RESPONSE)
                && subAction.equals(CommonJson.JSON_SUBACTION_VALUE_GETSCENARIOLIST))) {
            JSONArray loopMap = resObject.optJSONArray(CommonJson.JSON_LOOPMAP_KEY);
            if (loopMap != null) {
                mSenceNum = loopMap.length();
                for (int index = 0; index < mSenceNum; index++) {
                    JSONObject loop = null;
                    try {
                        loop = loopMap.getJSONObject(index);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSenarioName[index] = loop.optString("name", "");
                    mSenarioUUID[index] = loop.optString("uuid", "");
                }
            }
        }
    }


}
