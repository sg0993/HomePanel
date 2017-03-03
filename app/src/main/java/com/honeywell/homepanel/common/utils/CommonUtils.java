package com.honeywell.homepanel.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.media.AudioManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.honeywell.homepanel.R;

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
}
