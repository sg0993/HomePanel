package com.honeywell.homepanel.ui.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.IConfigServiceManageUtil;
import com.honeywell.homepanel.Utils.LogMgr;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUIScenario;
import com.honeywell.homepanel.common.utils.CommonUtils;
import com.honeywell.homepanel.ui.domain.TopStaus;
import com.honeywell.homepanel.ui.domain.ZoneAbnormalInfo;
import com.honeywell.homepanel.ui.uicomponent.AdapterCallback;
import com.honeywell.homepanel.ui.uicomponent.PasswordAdapter;
import com.honeywell.homepanel.ui.uicomponent.UIScenarioSwitchRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.honeywell.homepanel.common.CommonData.SENCES_EDIT;

/**
 * Created by H135901 on 2/16/2017.
 */

public class PasswordEnterActivity extends Activity implements View.OnClickListener, AdapterCallback {
    private static final String TAG = "PasswordEnterActivity";
    private GridView gridView;
    private Button mCancelBtn = null;
    private StringBuffer p = new StringBuffer();
    private List<ImageView> mImageViews = new ArrayList<ImageView>();

    private TextView mPasswordHint = null;
    private TextView mPasswordWelcome = null;


    private static final int IMAGES[] = {
            R.mipmap.one, R.mipmap.two, R.mipmap.three,
            R.mipmap.four, R.mipmap.five, R.mipmap.six,
            R.mipmap.seven, R.mipmap.eight, R.mipmap.nine,
            R.mipmap.clear, R.mipmap.zero, R.mipmap.delete,
    };

    private static final int IMAGES_DOWN[] = {
            R.mipmap.one_down, R.mipmap.two_down, R.mipmap.three_down,
            R.mipmap.four_down, R.mipmap.five_down, R.mipmap.six_down,
            R.mipmap.seven_down, R.mipmap.eight_down, R.mipmap.nine_down,
            R.mipmap.clear_down, R.mipmap.zero_down, R.mipmap.delete_down,
    };
    private int mScenarioIdx = 1;
    private Map<Integer, ScenarioLoopMap> scenarioMap = new HashMap<Integer, ScenarioLoopMap>();

//    private IConfigService mIConfigService = null;
//    private ServiceConnection mIConfigServiceConnect = new configServiceConnection();


    class ScenarioLoopMap {
        public String name;
        public String uuid;

        public ScenarioLoopMap(String name, String uuid) {
            this.name = name;
            this.uuid = uuid;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.layout_passwordenter);
//        CommonUtils.startAndBindService(this, CommonData.ACTION_CONFIG_SERVICE, mIConfigServiceConnect);
        initData();
        initGridView();
        initViews();
    }

    private void initData() {
        mScenarioIdx = getIntent().getIntExtra(CommonData.INTENT_KEY_SCENARIO, 1);
        UIScenarioSwitchRequest.getScenarioListString();

        if (mScenarioIdx == SENCES_EDIT) {
            IConfigServiceManageUtil.getInstance(this);
        }
    }


    private String getFromDBForPwd() {
        String pwd = "";
        try {
            pwd = IConfigServiceManageUtil.getStringMapConfigM(CommonData.JSON_KEY_ALARM_PWD);
            Log.d("Password Enter  ", "PWD: " + pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pwd;
    }

    private void initGridView() {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new PasswordAdapter(getApplicationContext(), this, IMAGES, IMAGES_DOWN));
    }

    private void cP(StringBuffer inputPwd) {
        if (inputPwd.length() == CommonData.SRCURITY_PASSWORD_LENGTH) {
            if (mScenarioIdx != SENCES_EDIT) {
//                String dbPwd = getFromDBForPwd();
//                if (dbPwd.equals(inputPwd.toString()))
                //{//authentication success
                Log.d(TAG, "cP: success");
                UIScenarioSwitchRequest.sendScenarioSwitchCommand(String.valueOf(mScenarioIdx), inputPwd.toString());
//                    if (scenarioMap != null) {
//                        if (scenarioMap.size() > 0) {
//                            String uuid = scenarioMap.get(mScenarioIdx).uuid;
//                            if (!TextUtils.isEmpty(uuid)) {
//                                UIScenarioSwitchRequest.sendScenarioSwitchCommand(uuid);
//                            } else {
//                                Log.e(TAG, "uuid is empty");
//                            }
//                        } else {
//                            Log.e(TAG, "cP: error" + scenarioMap.size());
//                        }
//                    } else {
//                        Log.e(TAG, "cP: null");
//                    }
//                }
//                else {
//                    Log.w(TAG, "cP: password inconsistent!");
//                    clearInputText();
//                    setPasswordImages(0);
//                }
            } else {

                String alarmPwd = getFromDBForPwd();
                LogMgr.d("inputPwd.toString():" + inputPwd.toString() + " getFromDBForPwd:" + alarmPwd);
                boolean check = CommonUtils.checkBcryptStr(inputPwd.toString(), alarmPwd);

                if (check) {
                    Intent intent = new Intent(this, AlarmZoneActivity.class);
                    String mSenarioUUID = getIntent().getStringExtra(CommonData.INTENT_KEY_SCENARIO_UUID);
                    String mSenarioName = getIntent().getStringExtra(CommonData.INTENT_KEY_SCENARIO_NAME);
                    intent.putExtra(CommonData.INTENT_KEY_SCENARIO_UUID, mSenarioUUID);
                    intent.putExtra(CommonData.INTENT_KEY_SCENARIO_NAME, mSenarioName);
                    startActivity(intent);
                    finish();
                } else {
                    mPasswordHint.setTextColor(getResources().getColor(R.color.colorAccent));
                    mPasswordHint.setText(getString(R.string.password_hint_passworderror));
                    clearInputText();
                    setPasswordImages(p.length());
                }

            }
        }
    }

    private void setPasswordImages(int passwordLength) {
        for (int i = 0; i < CommonData.SRCURITY_PASSWORD_LENGTH; i++) {
            if (i < passwordLength) {
                mImageViews.get(i).setImageResource(R.mipmap.passwordenter);
            } else {
                mImageViews.get(i).setImageResource(R.mipmap.nopasswordenter);
            }
        }
    }

    private void initViews() {
        mCancelBtn = (Button) findViewById(R.id.cancel);
        mCancelBtn.setOnClickListener(this);
        mImageViews.add((ImageView) findViewById(R.id.oneImage));
        mImageViews.add((ImageView) findViewById(R.id.twoImage));
        mImageViews.add((ImageView) findViewById(R.id.threeImage));
        mImageViews.add((ImageView) findViewById(R.id.fourImage));
        mImageViews.add((ImageView) findViewById(R.id.fiveImage));
        mImageViews.add((ImageView) findViewById(R.id.sixImage));

        mPasswordHint = (TextView) findViewById(R.id.tv_enterhint);
        mPasswordWelcome = (TextView) findViewById(R.id.tv_welcomehome);
        if (mScenarioIdx == CommonData.SCENARIO_HOME || mScenarioIdx == CommonData.SCENARIO_WAKEUP) {
            mPasswordHint.setText(getString(R.string.password_hint_disarm));
        } else if (mScenarioIdx == CommonData.SCENARIO_AWAY || mScenarioIdx == CommonData.SCENARIO_SLEEP) {
            mPasswordHint.setText(getString(R.string.password_hint_arm));
        } else if (mScenarioIdx == SENCES_EDIT) {
            mPasswordHint.setText(getString(R.string.password_sences_edit));
            mPasswordWelcome.setText("");
        }

    }

//    private String getFromDBForPwd() {
//        String pwd = "";
//        try {
//            if(mIConfigService != null){
//                pwd = mIConfigService.getStringMapConfig(CommonData.JSON_KEY_ALARM_PWD);
//                Log.d("GetFromDBForPwd ", "PWD: " + pwd);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return pwd;
//    }


//    private class configServiceConnection implements ServiceConnection {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            String serviceClassName = name.getClassName();
//            if(serviceClassName.equals(CommonData.ACTION_CONFIG_SERVICE)){
//                mIConfigService = IConfigService.Stub.asInterface(service);
//            }
//        }
//        public void onServiceDisconnected(ComponentName name) {
//            String serviceClassName = name.getClassName();
//            if(serviceClassName.equals(CommonData.ACTION_CONFIG_SERVICE)){
//                mIConfigService = null;
//            }
//        }
//    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.cancel:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void subviewOnclick(int position, String more) {
        if (position == 9) {
//            clearInputText();
            finish();
        } else if (position == 11) {
            if (TextUtils.equals("long", more)) {
                clearInputText();
            } else if (p.length() > 0) {
                p.deleteCharAt(p.length() - 1);
            }
        } else if (p.length() < CommonData.SRCURITY_PASSWORD_LENGTH) {
            if (position == 10) {
                p.append(2015 + 5 - 2020);
            } else {
                p.append(position + 1);
            }
        }

        setPasswordImages(p.length());

        if (TextUtils.equals(mPasswordHint.getText().toString().trim(),
                getString(R.string.password_hint_passworderror))) {
            mPasswordHint.setTextColor(getResources().getColor(R.color.black));
            mPasswordHint.setText(getString(R.string.password_hint_disarm));
        }

        cP(p);
    }

    private void clearInputText() {
        if (p.length() > 0) {
            p.replace(0, p.length(), "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        unbindService(mIConfigServiceConnect);

        if (!TextUtils.isEmpty(p.toString())) {
            p.delete(0, p.length() - 1);
        }
        p = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIScenario.SUISSwitchScenarioMessageRsp switchScenarioResp) {
        String action = switchScenarioResp.optString(CommonJson.JSON_ACTION_KEY, "");
        String subaction = switchScenarioResp.optString(CommonJson.JSON_SUBACTION_KEY, "");
        if (subaction.equals(CommonJson.JSON_SUBACTION_VALUE_SWITCHSCENARIO)) {
            String uuid = switchScenarioResp.optString(CommonJson.JSON_UUID_KEY, "");
            String errorcode = switchScenarioResp.optString(CommonJson.JSON_ERRORCODE_KEY, "");
            JSONArray abnormalArray = switchScenarioResp.optJSONArray(CommonData.JSON_KEY_ABNORMALSTATUS);
            Log.d(TAG, "SCSwitchScenarioMessageRsp: " + switchScenarioResp.toString());
            if (errorcode.equals(CommonJson.JSON_ERRORCODE_VALUE_OK)) {
                Log.d(TAG, "OnMessageEvent: scenario switch success");

                try {
                    int scenarioId = TopStaus.getInstance
                            (PasswordEnterActivity.this.getApplicationContext()).getCurScenario();
                    TopStaus.getInstance(PasswordEnterActivity.this.
                            getApplicationContext()).setCurScenario(Integer.valueOf(uuid));
                    if (scenarioId != Integer.valueOf(uuid)) {
                        switchScenario(uuid, mScenarioIdx);
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                finish();
            } else if (errorcode.equals(CommonJson.JSON_ERRORCODE_VALUE_PASSWORD)) {
                mPasswordHint.setTextColor(getResources().getColor(R.color.colorAccent));
                mPasswordHint.setText(getString(R.string.password_hint_passworderror));
                clearInputText();
                setPasswordImages(p.length());
                Log.d(TAG, "OnMessageEvent: scenario switch errorcode:" + errorcode);
            }
//            else if (errorcode.equals("-1")) {
//                LogMgr.d("OnMessageEvent: scenario switch errorcode:" + errorcode);
//                showAbnormalWindow(abnormalArray);
//                finish();
//            }
            else {
                Log.d(TAG, "PasswordEnterActivity: scenario switch errorcode:" + errorcode);
                Toast.makeText(PasswordEnterActivity.this, getString(R.string.scenario_error), Toast.LENGTH_SHORT).show();
                showAbnormalWindow(abnormalArray);
                finish();
            }
        } else {
            Log.d(TAG, "OnMessageEvent: subaction not correct: " + subaction);
        }
    }

    private void showAbnormalWindow(JSONArray abnormalArray) {
        if (abnormalArray == null) return;
        if (abnormalArray.length() == 0) {
            Log.e(TAG, "showAbnormalWindow: abnormalarray is empty");
            return;
        }

        Intent intent = new Intent(this, ArmAbnormalPopupWindow.class);
        List<ZoneAbnormalInfo> msgList = new ArrayList<ZoneAbnormalInfo>();

        Log.d(TAG, "showAbnormalWindow: abnormalArray lenght:" + abnormalArray.length());
        JSONObject Obj = null;
        JSONArray array = null;
        String alias = null;
        String alaryType = null;
        for (int i = 0; i < abnormalArray.length(); i++) {
            try {
                Obj = abnormalArray.getJSONObject(i);
                alias = Obj.optString(CommonJson.JSON_ALIASNAME_KEY);
                array = Obj.optJSONArray(CommonJson.JSON_LOOPMAP_KEY);
                alaryType = array.getJSONObject(0).optString(CommonData.COMMUNITY_KEY_TYPE);
                Log.d(TAG, "showAbnormalWindow: alias = " + alias);
                Log.d(TAG, "showAbnormalWindow: alaryType = " + alaryType);
                ZoneAbnormalInfo zinfo = new ZoneAbnormalInfo(alias, alaryType);
                msgList.add(zinfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(CommonData.ABNORMAL_INTENT_KEY, (Serializable) msgList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void switchScenario(String uuid, int scenarioIdx) {
        Log.d(TAG, "switchScenario: uuid:" + uuid + " index:" + scenarioIdx);
        Intent intent = new Intent(this, ScenarioSelectHintActivity.class);
        intent.putExtra(CommonData.INTENT_KEY_SCENARIO, scenarioIdx);
        startActivity(intent);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(SUISMessagesUIScenario.SUISGetScenarioListMessageRsp scenarioListResp) {
        String action = scenarioListResp.optString(CommonJson.JSON_ACTION_KEY, "");
        String subaction = scenarioListResp.optString(CommonJson.JSON_SUBACTION_KEY, "");
        if (subaction.equals(CommonJson.JSON_SUBACTION_VALUE_GETSCENARIOLIST)) {
            JSONArray scenarioList = scenarioListResp.optJSONArray(CommonJson.JSON_LOOPMAP_KEY);
            if (scenarioList == null) return;
            for (int i = 0; i < scenarioList.length(); i++) {
                JSONObject tmp = scenarioList.optJSONObject(i);
                String scenarioName = tmp.optString(CommonData.JSON_KEY_NAME);
                String scenarioUuid = tmp.optString(CommonJson.JSON_UUID_KEY);
                if (TextUtils.isEmpty(scenarioName) || TextUtils.isEmpty(scenarioUuid)) {
                    Log.e(TAG, "OnMessageEvent: null");
                    break;
                }
                Log.d(TAG, "scenario name:" + scenarioName + " uuid:" + scenarioUuid);
//                ScenarioLoopMap map = new ScenarioLoopMap(scenarioName, scenarioUuid);
//                int scenarioIndex = CommonUtils.convertScenarioNameToIndex(scenarioName);
//                if (scenarioIndex > 0) {
//                    scenarioMap.put(scenarioIndex, map);
//                } else {
//                    Log.e(TAG, "get scenarioIndex error");
//                }
            }
        } else {
            Log.d(TAG, "OnMessageEvent: subaction not correct: " + subaction);
        }
    }


}
