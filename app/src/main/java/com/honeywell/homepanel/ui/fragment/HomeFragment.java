package com.honeywell.homepanel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.PlistUtil;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.Message.MessageEvent;
import com.honeywell.homepanel.ui.activities.CallActivity;
import com.honeywell.homepanel.ui.activities.ScenarioSelectActivity;
import com.honeywell.homepanel.ui.domain.MenuCity;
import com.honeywell.homepanel.ui.domain.TopStaus;
import com.honeywell.homepanel.ui.uicomponent.PageViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by H135901 on 1/25/2017.
 */

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment implements View.OnClickListener{
    private String mTitle = "";
    private static  final  String TAG = "HomeFragment";
    private PageViewAdapter mPageAdaper = null;
    private ImageView choose_scenarioImageView = null;
    private Context mContext = null;

    private ListView mEvent_listview;
    private List<Map<String, Object>> mEventData;

    private TextView mGoodWhatHasEventTv = null;
    private TextView mGoodWhatNoEventTv = null;
    private TextView mArmHintHasEventTv = null;
    private TextView mArmHintNoEventTv = null;
    private TextView mCurrentScenarioTv = null;

    /*********For Event view logic*************/
    private boolean  bHasEvent = true;
    private View mArmStatusNoevent = null;
    private View mArmStatusHasevent = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_home, null);
        mPageAdaper = new PageViewAdapter(view,getActivity(),R.layout.mainpage01,R.layout.mainpage02,
                R.id.pagerRootId,R.id.dotViewRoot,R.id.pageView);

        initViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int scenario = TopStaus.getInstance(getActivity().getApplicationContext()).getCurScenario();
        Log.d(TAG,"onResume() mCurScenario:"+ scenario);
        setViewTextByScenario(scenario);
    }

    private void setViewTextByScenario(int mCurScenario) {
        String goodWhat = null;
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if(hour < 12){
            goodWhat = getString(R.string.goodmoring);
        }
        else if(hour < 16){
            goodWhat = getString(R.string.goodafternoon);
        }
        else{
            goodWhat = getString(R.string.goodevening);
        }

        String armHint = "";
        if(mCurScenario == CommonData.SCENARIO_AWAY){
            armHint = getString(R.string.armhint);
        }
        else {
            armHint = getString(R.string.disarmhint);
        }

        int scenarioResId = 0;
        int scenarioImgResId = 0;
        switch (mCurScenario){
            case CommonData.SCENARIO_HOME:
                scenarioResId = R.string.scenario_home;
                scenarioImgResId = R.mipmap.choose_scenario_home;
                break;
            case CommonData.SCENARIO_AWAY:
                scenarioResId = R.string.scenario_away;
                scenarioImgResId = R.mipmap.choose_scenario_away;
                break;
            case CommonData.SCENARIO_SLEEP:
                scenarioResId = R.string.scenario_sleep;
                scenarioImgResId = R.mipmap.choose_scenario_sleeping;
                break;
            case CommonData.SCENARIO_WAKEUP:
                scenarioResId = R.string.scenario_wakeup;
                scenarioImgResId = R.mipmap.choose_scenario_weakup;
                break;
            default:
                break;
        }
        String currentStr = getString(R.string.currnt) + getString(scenarioResId);
        mGoodWhatHasEventTv.setText(goodWhat);
        mGoodWhatNoEventTv.setText(goodWhat);

        mArmHintHasEventTv.setText(armHint);
        mArmHintNoEventTv.setText(armHint);
        mCurrentScenarioTv.setText(currentStr);
        if (choose_scenarioImageView != null) {
            choose_scenarioImageView.setImageResource(scenarioImgResId);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public HomeFragment(String title) {
        super();
        this.mTitle = title;
    }
    public HomeFragment() {
        super();
    }

    private void initViews() {
        View mainPage1 = mPageAdaper.pageViews.get(0);
        choose_scenarioImageView = (ImageView) mainPage1.findViewById(R.id.choose_scenarioImage);
        choose_scenarioImageView.setOnClickListener(this);

        mEvent_listview = (ListView)mainPage1.findViewById(R.id.event_listview);
        mEventData = getmEventData();
        MyAdapter adapter = new MyAdapter(getActivity());
        mEvent_listview.setAdapter(adapter);

        mGoodWhatHasEventTv = (TextView)mainPage1.findViewById(R.id.goodwhat_hasevent);
        mGoodWhatNoEventTv = (TextView)mainPage1.findViewById(R.id.goodwhat_noevent);
        mArmHintHasEventTv = (TextView)mainPage1.findViewById(R.id.arm_hint_hasevent);
        mArmHintNoEventTv = (TextView)mainPage1.findViewById(R.id.arm_hint_noevent);
        mCurrentScenarioTv = (TextView)mainPage1.findViewById(R.id.currentscenario);

        mArmStatusNoevent = mainPage1.findViewById(R.id.arm_status_noevent);
        mArmStatusHasevent = mainPage1.findViewById(R.id.arm_status_hasevent);
        setArmView(bHasEvent);

        /***************For Test Call********************************/
        mGoodWhatHasEventTv.setOnClickListener(this);// for test Neighbor OutGoing call
        mArmHintHasEventTv.setOnClickListener(this);// for test Neighbor Incoming call
        mCurrentScenarioTv.setOnClickListener(this);// for test Lobby Incoming call
        /***************For Test Call********************************/
    }

    private void setArmView(boolean bHasEvent) {
        if(bHasEvent){
            mArmStatusNoevent.setVisibility(View.GONE);
            mArmStatusHasevent.setVisibility(View.VISIBLE);
        }
        else{
            mArmStatusNoevent.setVisibility(View.VISIBLE);
            mArmStatusHasevent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.choose_scenarioImage:
                showScenarioSelect();
                break;
            case R.id.goodwhat_hasevent:// for test Neighbor OutGoing call
                //testCall("100-202",CommonData.CALL_OUTGOING_NEIGHBOR);
                ArrayList<MenuCity> lists = PlistUtil.getDefaultCities(getActivity().getApplicationContext());
                Log.d(TAG,"size :" + lists.size() +",,,1111111111");


                break;
            case R.id.arm_hint_hasevent:// for test Neighbor Incoming call
                //testCall("100-202",CommonData.CALL_INCOMING_NEIGHBOR);
                testCall("office",CommonData.CALL_GUARD_INCOMMING);
                break;
            case R.id.currentscenario:// for test Lobby Incoming call
                testCall("Lobby",CommonData.CALL_LOBBY_INCOMMING);
                break;
            default:
                break;
        }
    }
    private void testCall(String unit,int call_type){
        Intent intent = new Intent(mContext, CallActivity.class);
        intent.putExtra(CommonData.INTENT_KEY_UNIT,unit);
        intent.putExtra(CommonData.INTENT_KEY_CALL_TYPE,call_type);
        startActivity(intent);
    }

    private void showScenarioSelect() {
        startActivity(new Intent(mContext, ScenarioSelectActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(MessageEvent event) {

    }

    private List<Map<String, Object>> getmEventData(){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(getHashMap(R.mipmap.home_event,getString(R.string.lowbattery)));
        list.add(getHashMap(R.mipmap.home_temperature,getString(R.string.temp_hot)));
        list.add(getHashMap(R.mipmap.home_visitor,getString(R.string.newvisitor)));
        return list;
    }

    private Map<String, Object> getHashMap(int image, String text) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("img", image);
        map.put("text",text);
        return map;
    }

    //ViewHolder静态类
    static class ViewHolder {
        public ImageView img;
        public TextView event_tv;
    }
    public class MyAdapter extends BaseAdapter
    {
        private LayoutInflater mInflater = null;
        private MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            if (mEventData != null) {
                return mEventData.size();
            }
            return 0;
        }
        @Override
        public Object getItem(int position) {
            return mEventData.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_homevent, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.event_tv = (TextView)convertView.findViewById(R.id.event_tv);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.img.setImageResource((Integer) mEventData.get(position).get("img"));
            holder.event_tv.setText((String) mEventData.get(position).get("text"));
            return convertView;
        }
    }

}
