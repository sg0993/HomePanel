package com.honeywell.homepanel.ui.uicomponent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.honeywell.homepanel.R;
import com.honeywell.homepanel.Utils.LogMgr;
import com.honeywell.homepanel.common.Message.ui.AlarmHint;
import com.zhy.autolayout.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2/22/2017.
 */

public class AlarmHintPopAdapter extends BaseAdapter {
    private List<AlarmHint> msgList = new ArrayList<AlarmHint>();
    private LayoutInflater inflater;
    private int selectItem;
    private ListView listView;
    private static final String TAG = "AlarmHintPopAdapter";
    private final int ITEM_STATUS_CHECKED = 1;
    private final int ITEM_STATUS_NORMAL = 0;

    private AdapterCallback mAdapterCallBack = null;

    public void setMsgList(List<AlarmHint> mList) {
        this.msgList.clear();
        this.msgList.addAll(mList);
        notifyDataSetChanged();
    }

    public AlarmHintPopAdapter() {
    }

    public AlarmHintPopAdapter(List<AlarmHint> msgList, Context context, AdapterCallback adapterCallback) {
        this.msgList.addAll(msgList);
        this.inflater = LayoutInflater.from(context);
        mAdapterCallBack = adapterCallback;
    }

    @Override
    public int getCount() {
        return msgList == null ? 0 : msgList.size();
    }

    @Override
    public Object getItem(int i) {
        return msgList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        final AlarmHint msg = msgList.get(i);

        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_alarm_hint, null);
//            viewHolder.linearLayout_imageview = (LinearLayout) view.findViewById(R.id.notification_imageview);
            viewHolder.tv_alarmType = (TextView) view.findViewById(R.id.zoneType);
            viewHolder.tv_zoneName = (TextView) view.findViewById(R.id.zoneName);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.alarmTime);
            viewHolder.button = (Button) view.findViewById(R.id.acknowledge);
//            viewHolder.left_btn = (Button) view.findViewById(R.id.left_btn);
//            viewHolder.right_btn = (Button) view.findViewById(R.id.right_btn);
            viewHolder.position = i;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

//        LogMgr.e("msgList.size()=" + msgList.size());
        if (msgList.size() > 1) {
            viewHolder.button.setVisibility(View.GONE);
            viewHolder.button.setOnClickListener(null);
        } else {
            viewHolder.button.setVisibility(View.VISIBLE);
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapterCallBack != null) {
                        mAdapterCallBack.subviewOnclick(0, "0");
                    }
                }
            });
        }

        viewHolder.tv_alarmType.setText(msg.alarmType);
        viewHolder.tv_zoneName.setText(msg.zoneName);
        viewHolder.tv_time.setText(msg.time);
        /*viewHolder.left_btn.setTag(i);
        viewHolder.left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (Integer) view.getTag();
                Log.d(TAG, "left_btn  position:" + i);
                msgList.remove(i);
                AlarmHintPopAdapter.this.notifyDataSetChanged();
                if (msgList.size() == 0) {
                    mAdapterCallBack.subviewOnclick(0, "");
                }
            }
        });
        viewHolder.right_btn.setTag(i);
        viewHolder.right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "right_btn  position:" + view.getTag());
            }
        });*/

        return view;
    }

    class ViewHolder {
        //        Button left_btn = null;
//        Button right_btn = null;
        TextView tv_alarmType = null;
        TextView tv_zoneName = null;
        TextView tv_time = null;
        Button button;
//        LinearLayout linearLayout_imageview;

        int position = 0;
    }
}
