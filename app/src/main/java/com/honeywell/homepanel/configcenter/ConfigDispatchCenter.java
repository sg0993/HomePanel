package com.honeywell.homepanel.configcenter;

import android.content.Context;

import com.honeywell.homepanel.configcenter.databases.domain.EventDataElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by H135901 on 3/13/2017.
 */
public class ConfigDispatchCenter implements Runnable{
    private Context mContext = null;
    private volatile boolean mBRuning = true;
    public static final int mMaxRecvQueSize = 100; // 最大缓存100个配置消息(JSON)
    private static ConfigDispatchCenter mInstance = null;
    public static final String TAG = ConfigService.TAG;
    public BlockingQueue<EventDataElement> mRecvDataQue = new LinkedBlockingQueue<EventDataElement>(mMaxRecvQueSize);

    public static synchronized ConfigDispatchCenter getInstance(Context context) {
        if(mInstance == null){
            mInstance = new ConfigDispatchCenter(context);
        }
        return mInstance;
    }

    private ConfigDispatchCenter(Context context) {
        mContext = context;
    }

    @Override
    public void run() {
        while (mBRuning){
            try {
                EventDataElement dataElement = mRecvDataQue.take();
                JSONObject obj = new JSONObject(new String(dataElement.getDataValue()));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void stopThd() {
        mBRuning = false;
    }
}
