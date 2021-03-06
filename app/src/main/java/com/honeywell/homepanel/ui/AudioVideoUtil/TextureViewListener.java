package com.honeywell.homepanel.ui.AudioVideoUtil;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.honeywell.homepanel.ui.uicomponent.LiveStream;

/**
 * Created by H135901 on 4/5/2017.
 */

public class TextureViewListener implements TextureView.SurfaceTextureListener {
    private static final String TAG =  "TextureViewListener";
    private Surface videoSurface = null;
    private HVideoDecoder videoDecoder;

    private LiveStream videoStream;

    public TextureViewListener() {
        this.videoDecoder = null;
    }
    public TextureViewListener(LiveStream videoStream){
        this.videoStream = videoStream;
        videoDecoder = null;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1, int arg2) {
        Log.e(TAG, "---onSurfaceTextureAvailable");
        if (null == videoSurface) {
            videoSurface = new Surface(arg0);
            Log.d(TAG, "surface created");
            videoDecoder = new HVideoDecoder(videoSurface);
            if(null != videoStream){
                videoDecoder = new HVideoDecoder(videoSurface);
                videoStream.attachDecoder(videoDecoder);
            }
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
        Log.i(TAG, "onSurfaceTextureDestroyed");
        /*if(null != videoDecoder){
            videoDecoder.detachDisplay();
		}*/
        videoDecoder = null;
        videoSurface = null;
        return false;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1, int arg2) {
        Log.i(TAG, "onSurfaceTextureSizeChanged");
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
//        Log.e("alinmi23", "---onSurfaceTextureUpdated");
        //Log.i(TAG, "onSurfaceTextureUpdated");//每一帧都会调用这个函数
    }

    public Surface getSurface() {
        return videoSurface;
    }

    public void stopDecoder() {
		if(null != videoDecoder){
		  	videoDecoder.closeDecoder();
		  	videoDecoder = null;
		}
    }

    public HVideoDecoder getVideoDecoder() {
        return videoDecoder;
    }

}