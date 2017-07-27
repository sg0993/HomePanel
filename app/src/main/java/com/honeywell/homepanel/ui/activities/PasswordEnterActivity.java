package com.honeywell.homepanel.ui.activities;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.LogMgr;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.common.Message.subphoneuiservice.SUISMessagesUIScenario;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bouncycastle.asn1.x509.X509ObjectIdentifiers.id;

/**
 * Created by H135901 on 2/16/2017.
 */

public class PasswordEnterActivity extends Activity implements View.OnClickListener, AdapterCallback {
    private static final String TAG = "PasswordEnterActivity";
    private GridView gridView;
    private Button mCancelBtn = null;
    private StringBuffer mPasswordStr = new StringBuffer();
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
    }

    private void initGridView() {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new PasswordAdapter(getApplicationContext(), this, IMAGES, IMAGES_DOWN));
    }

    private void comparePassword(StringBuffer inputPwd) {
        if (inputPwd.length() == CommonData.SRCURITY_PASSWORD_LENGTH) {
            if (mScenarioIdx != CommonData.SENCES_EDIT) {
//                String dbPwd = getFromDBForPwd();
//                if (dbPwd.equals(inputPwd.toString()))
                //{//authentication success
                Log.d(TAG, "comparePassword: success");
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
//                            Log.e(TAG, "comparePassword: error" + scenarioMap.size());
//                        }
//                    } else {
//                        Log.e(TAG, "comparePassword: null");
//                    }
//                }
//                else {
//                    Log.w(TAG, "comparePassword: password inconsistent!");
//                    clearInputText();
//                    setPasswordImages(0);
//                }
            } else {
                Intent intent = new Intent(this, AlarmZoneActivity.class);
                String mSenarioUUID = getIntent().getStringExtra(CommonData.INTENT_KEY_SCENARIO_UUID);
                String mSenarioName = getIntent().getStringExtra(CommonData.INTENT_KEY_SCENARIO_NAME);
                intent.putExtra(CommonData.INTENT_KEY_SCENARIO_UUID, mSenarioUUID);
                intent.putExtra(CommonData.INTENT_KEY_SCENARIO_NAME, mSenarioName);
                startActivity(intent);
                finish();
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
        } else if (mScenarioIdx == CommonData.SENCES_EDIT) {
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
            } else if (mPasswordStr.length() > 0) {
                mPasswordStr.deleteCharAt(mPasswordStr.length() - 1);
            }
        } else if (mPasswordStr.length() < CommonData.SRCURITY_PASSWORD_LENGTH) {
            if (position == 10) {
                mPasswordStr.append("0");
            } else {
                mPasswordStr.append(position + 1);
            }
        }

        setPasswordImages(mPasswordStr.length());
        comparePassword(mPasswordStr);
        if (TextUtils.equals(mPasswordHint.getText().toString().trim(),
                getString(R.string.password_hint_passworderror))) {
            mPasswordHint.setTextColor(getResources().getColor(R.color.black));
            mPasswordHint.setText(getString(R.string.password_hint_disarm));
        }
    }

    private void clearInputText() {
        if (mPasswordStr.length() > 0) {
            mPasswordStr.replace(0, mPasswordStr.length(), "");
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
//        unbindService(mIConfigServiceConnect);
        super.onDestroy();
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
            if (errorcode.equals("0")) {
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
            } else if (errorcode.equals("4")) {
                mPasswordHint.setTextColor(getResources().getColor(R.color.colorAccent));
                mPasswordHint.setText(getString(R.string.password_hint_passworderror));
                clearInputText();
                setPasswordImages(mPasswordStr.length());
                Log.d(TAG, "OnMessageEvent: scenario switch errorcode:" + errorcode);
            }
//            else if (errorcode.equals("-1")) {
//                LogMgr.d("OnMessageEvent: scenario switch errorcode:" + errorcode);
//                showAbnormalWindow(abnormalArray);
//                finish();
//            }
            else {
                Toast.makeText(PasswordEnterActivity.this, "Scenario Error Code: " + errorcode, Toast.LENGTH_SHORT).show();
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
