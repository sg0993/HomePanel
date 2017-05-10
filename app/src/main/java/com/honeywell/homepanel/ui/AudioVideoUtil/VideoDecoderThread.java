package com.honeywell.homepanel.ui.AudioVideoUtil;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by H135901 on 4/5/2017.
 */

public class VideoDecoderThread implements  Runnable{
    private static final String TAG = "VideoDecoderThread";
    private final int QUEUEPOLLTMOUT = 1000;// 1s
    private volatile boolean mRunning;
    private boolean mDecInit;
    private HVideoDecoder mDecoder = null;

    //for input parameter
    private BlockingQueue<VStreamBuffer> mVideoPlayQueue = null;
    private TextureViewListener mDecoderTextureListener;
    private VideoInfo mVideoInfo = null;//从哪里来？？

    public VideoDecoderThread(TextureViewListener mDecoderTextureListener, BlockingQueue<VStreamBuffer> mVideoPlayQueue) {
        this.mDecoderTextureListener = mDecoderTextureListener;
        this.mVideoPlayQueue = mVideoPlayQueue;
    }

    //TODO 何时调用？,和解码有关
    public void setVideoInfo(VideoInfo mVideoInfo) {
        this.mVideoInfo = mVideoInfo;
    }

    public void stop() {
        mRunning = false;
    }

    @Override
    public void run() {
        mRunning = true;
        mDecInit = false;
        VStreamBuffer bufferItem = null;

        while (mRunning) {
            if (null == mDecoder) {
                if (null != mDecoderTextureListener) {
                    mDecoder = mDecoderTextureListener.getVideoDecoder();
                    if (null == mDecoder) {
                        Log.e(TAG, "----VideoDecoderThread----->  decoder is null ???");
                        continue;
                    }
                    else{
                        mDecoder.decoder_init(null,320, 240);
                    }
                } else {
                    Log.e(TAG, "----VideoDecoderThread----->  texture is null ???");
                    continue;
                }
            }

            try {
                bufferItem = mVideoPlayQueue.poll(QUEUEPOLLTMOUT, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Log.e(TAG, "----VideoDecoderThread-----> InterruptedException e " + e.getMessage());
                continue;
            }
            if (null != bufferItem) {
                // decode
               /* if (null == mDecoder) {
                    if (null != mDecoderTextureListener) {
                        mDecoder = mDecoderTextureListener.getVideoDecoder();
                        if (null == mDecoder) {
                            Log.e(TAG, "----VideoDecoderThread----->  decoder is null ???");
                            continue;
                        }
                    } else {
                        Log.e(TAG, "----VideoDecoderThread----->  texture is null ???");
                        continue; // decoder还没有准备好？？
                    }
                }

                mVideoInfo = CallBaseFragment.mVideoInfo;
                if (!mDecInit) { //等待I帧
                    // need init first, find sps/pps
                    if (mVideoInfo != null && mVideoInfo.mCsdInfo != null) {
                        Log.d(TAG, "----VideoDecoderThread----->  init decoder, width=" + mVideoInfo.mWidth + ", height" + mVideoInfo.mHeight);
                        mDecoder.decoder_init(mVideoInfo.mCsdInfo, mVideoInfo.mWidth, mVideoInfo.mHeight);
                        mDecInit = true;
                    } else {
                        // 没有得到sps，无法初始化？？
                        continue;
                    }
                }*/



               /* if (mLoadingText.getVisibility() == View.VISIBLE && mDecoder.isDecodeSuccess()) {
                    mLoadingText.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mLoadingText.getVisibility() != View.GONE) {
                                mLoadingText.setVisibility(View.GONE);
                                new CounterThread().start();
                                mTvCountTime.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }*/
                if(null != mDecoder){
                    mDecoder.onFrameProcess(bufferItem.StreamData.array(), bufferItem.StreamLen, bufferItem.TimeSTP);
                }
                //Log.i(TAG, "----VideoDecoderThread----->  decode one frame.");
            }
        }
        if (null != mDecoder) {
            mDecoder.closeDecoder();
        }
    }
}
