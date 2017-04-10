package com.honeywell.homepanel.ui.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.honeywell.homepanel.IAvRtpService;
import com.honeywell.homepanel.common.utils.CommonUtils;
import com.honeywell.homepanel.ui.AudioVideoUtil.AudioProcess;
import com.honeywell.homepanel.ui.AudioVideoUtil.VStreamBuffer;
import com.honeywell.homepanel.ui.AudioVideoUtil.VideoInfo;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by H135901 on 4/5/2017.
 */

public class CallBaseFragment extends Fragment {

    private static final int MAX_QUEUE_SIZE = 100;

    private static int SLEEPTIME = 500;
    private static  final  String TAG = "CallBaseFragment";

    public IAvRtpService mIAvrtpService = null;
    private AudioProcess mAudioProcess = null;
    public BlockingQueue<VStreamBuffer> mVideoPlayQueue = new LinkedBlockingQueue<VStreamBuffer>(MAX_QUEUE_SIZE);

    private volatile  boolean mVideoRunning = false;
    private  Thread  mVideoGetThread = null;
    private volatile  boolean mAudioRunning = false;
    private Thread mAudioGetThread = null;

    public static VideoInfo mVideoInfo = null;

    public void setFragmentAidl(IAvRtpService iAvRtpService){
        mIAvrtpService = iAvRtpService;
        mAudioProcess = new AudioProcess(getActivity().getApplicationContext(),"",mIAvrtpService);

        //TODO for test
        mAudioProcess.setUuid(CommonUtils.generateCommonEventUuid());
    }

    public  void startAudio(){
        if (null != mAudioProcess) {
            try {
                mAudioProcess.startPhoneRecordAndPlay();
                startAudioGet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public  void stopAudio(){
        try {
            if (null != mAudioProcess) {
                mAudioProcess.stopPhoneRecordAndPlay();
            }
        } catch (Exception e) {
            Log.e(TAG, " stopAudio()  " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        stopAudio();
        stopAudiooGet();
        stopVideoGet();
        super.onDestroy();
    }


    public void startVideoGet(){
        mVideoRunning = true;
        mVideoGetThread = new Thread(){
            public void run() {
               while (mVideoRunning){
                   try {
                       if(null == mIAvrtpService){
                           Log.d(TAG,"startVideoGet() mIAvrtpService null !");
                           Thread.sleep(SLEEPTIME);
                           continue;
                       }
                       byte [] data = mIAvrtpService.getVideoFrame();
                       if(null != data){
                           getVideoInfo();
                           mVideoPlayQueue.offer(new VStreamBuffer(data));
                       }
                       else{
                            Thread.sleep(SLEEPTIME);
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
            }
        };
        mVideoGetThread.start();
    }
    private void getVideoInfo() throws Exception{
        if(null == mVideoInfo){
            mVideoInfo = new VideoInfo();
            mVideoInfo.mWidth = 1920;
            mVideoInfo.mHeight = 1200;
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            byteOut.write(mIAvrtpService.getVideoSPS());
            byteOut.write(mIAvrtpService.getVideoPPS());
            mVideoInfo.mCsdInfo = byteOut.toByteArray();
        }
    }
    public void stopVideoGet(){
        mVideoRunning = false;
        mVideoInfo = null;
    }

    public void startAudioGet(){
        mAudioRunning = true;
        mAudioGetThread = new Thread(){
            public void run() {
                while (mAudioRunning){
                    try {
                        if(null == mIAvrtpService){
                            Log.d(TAG,"startAudioGet() mIAvrtpService null !");
                            Thread.sleep(SLEEPTIME);
                            continue;
                        }
                        byte [] data = mIAvrtpService.getAudioFrame();
                        if(null != data){
                            mAudioProcess.mPlayQueue.offer(data);
                        }
                        else{
                            Thread.sleep(SLEEPTIME);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mAudioGetThread.start();
    }
    public void stopAudiooGet(){
        mAudioRunning = false;
        mVideoInfo = null;
    }
}
