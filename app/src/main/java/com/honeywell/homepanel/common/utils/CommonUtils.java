package com.honeywell.homepanel.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.honeywell.homepanel.R;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by H135901 on 3/3/2017.
 */

public class CommonUtils {

    public static SeekBar  showCallVolumeDialog(Activity activity, View.OnClickListener onClickListener,
                      SeekBar.OnSeekBarChangeListener seekBarChangeListener,boolean bSpeaker){
        final WindowManager manager = activity.getWindowManager();
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_volume_adjust, null);
        View volume_decrease = view.findViewById(R.id.volume_decrease);
        volume_decrease.setOnClickListener(onClickListener);
        View volume_increase = view.findViewById(R.id.volume_increase);
        volume_increase.setOnClickListener(onClickListener);

        SeekBar seekBar = (SeekBar) view.findViewById(R.id.volume_seekbar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        AudioManager am =(AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
        if(bSpeaker){
            seekBar.setMax(am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
            seekBar.setProgress(am.getStreamVolume(AudioManager.STREAM_SYSTEM));
        }
        else{

        }
        AlertDialog alert = new AlertDialog.Builder(activity).create();
        alert.show();
        alert.getWindow().setLayout(width/2, height/4);
        alert.setTitle("测试");
        alert.getWindow().setContentView(view);
        return  seekBar;
    }

    public static void setWindowAlpha(Window window, float f) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = f;
        window.setAttributes(lp);
    }

    public static void startAndBindService(Context context,String serviceAction,ServiceConnection connection){
        if(null == serviceAction || null == connection){
            return;
        }
        Intent intent = new Intent(serviceAction);
        intent.setPackage(context.getPackageName());
        context.startService(intent);
        context.bindService(intent, connection, BIND_AUTO_CREATE);
    }

    public static String deleteTrim(String str){//去掉IP字符串前后所有的空格
        while(str.startsWith(" ")){
            str= str.substring(1,str.length()).trim();
        }
        while(str.endsWith(" ")){
            str= str.substring(0,str.length()-1).trim();
        }
        return str;
    }
    public static boolean isIp(String ip){//判断是否是一个IP
        boolean b = false;
        ip = deleteTrim(ip);
        if(ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){
            String s[] = ip.split("\\.");
            if(Integer.parseInt(s[0])<255)
                if(Integer.parseInt(s[1])<255)
                    if(Integer.parseInt(s[2])<255)
                        if(Integer.parseInt(s[3])<255)
                            b = true;
        }
        return b;
    }

}
