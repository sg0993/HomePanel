package com.honeywell.homepanel.ui.fragment;

import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.honeywell.homepanel.IAvRtpService;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.utils.CommonUtils;
import com.honeywell.homepanel.ui.AudioVideoUtil.AudioProcess;
import com.honeywell.homepanel.ui.AudioVideoUtil.VStreamBuffer;
import com.honeywell.homepanel.ui.AudioVideoUtil.VideoInfo;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
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

    private volatile  boolean mRunning = false;
    private  Thread mGetThread = null;

    public static VideoInfo mVideoInfo = null;
    private volatile boolean mBAudioGet = false;
    private volatile boolean mBVideoGet = false;
    private volatile boolean mBRecord = false;

    private BlockingQueue<ByteBuffer> mVideoRecordQueue = null;
    private BlockingQueue<ByteBuffer> mAudioRecordQueue = null;
    private MediaMuxer mMuxer = null;

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
            stopAudioGet();
        } catch (Exception e) {
            Log.e(TAG, " stopAudio()  " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        stopAudio();
        stopAudioGet();
        stopVideoGet();
        stopThread();
        stopRecord();
        super.onDestroy();
    }

    public  void stopThread(){
        mRunning = false;
        mGetThread = null;
    }

    private  class GetThread extends Thread{
        public void run() {
            while (mRunning){
                try {
                    if(null == mIAvrtpService){
                        Log.d(TAG,"startVideoGet() mIAvrtpService null !");
                        Thread.sleep(SLEEPTIME);
                        continue;
                    }
                    if(mBVideoGet) {
                        byte[] data = mIAvrtpService.getVideoFrame();
                        if (null != data) {
                            getVideoInfo();
                            mVideoPlayQueue.offer(new VStreamBuffer(data));
                            if(mBRecord){
                                mVideoRecordQueue.offer(ByteBuffer.wrap(data));
                            }
                        }
                    }
                    if(mBAudioGet){
                        byte [] data = mIAvrtpService.getAudioFrame();
                        if(null != data){
                            mAudioProcess.mPlayQueue.offer(data);
                            if(mBRecord){
                                mAudioRecordQueue.offer(ByteBuffer.wrap(data));
                            }
                        }
                        else{
                            Thread.sleep(SLEEPTIME);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void startVideoGet(){
        mBVideoGet = true;
        startGetThread();
    }
    private void startGetThread(){
        mRunning = true;
        if(null == mGetThread){
            mGetThread = new GetThread();
            mGetThread.start();
            return;
        }
    }

    public void startRecord(){
        mBRecord = true;
        if(null == mVideoRecordQueue){
            mVideoRecordQueue = new LinkedBlockingQueue<ByteBuffer>(10);
        }
        if(null == mAudioRecordQueue) {
            mAudioRecordQueue = new LinkedBlockingQueue<ByteBuffer>(10);
        }
        final  String fileName = CommonData.VIDEO_PATH + CommonUtils.getVideoRecordName();
        new Thread(new Runnable() {
            public void run() {
                 try {
                     if(mMuxer != null){
                         mMuxer.stop();
                         mMuxer.release();
                     }
                     mMuxer = new MediaMuxer(fileName,MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                     MediaFormat audioFormat = new MediaFormat().createAudioFormat(MediaFormat.MIMETYPE_AUDIO_G711_ALAW,AudioProcess.Sample_Rate,1);
                     MediaFormat videoFormat = CommonUtils.getVideoFormat(mVideoInfo);
                     int audioTrackIndex = mMuxer.addTrack(audioFormat);
                     int videoTrackIndex = mMuxer.addTrack(videoFormat);
                     mMuxer.start();
                     while (!mBRecord){
                         if(!mAudioRecordQueue.isEmpty()){
                             ByteBuffer buffer = mAudioRecordQueue.take();
                             mMuxer.writeSampleData(audioTrackIndex,buffer,CommonUtils.getBufferInfo(buffer.array().length));
                         }
                         if(!mVideoRecordQueue.isEmpty()){
                             ByteBuffer buffer = mVideoRecordQueue.take();
                             mMuxer.writeSampleData(videoTrackIndex,buffer,CommonUtils.getBufferInfo(buffer.array().length));
                         }
                     }
                 }
                 catch (Exception e){
                     e.printStackTrace();
                 }
            }
        });
    }

    public void stopRecord(){
        mBRecord = false;
        if(mMuxer != null){
            mMuxer.stop();
            mMuxer.release();
        }

    }
    private void getVideoInfo() throws Exception{
        if(null == mVideoInfo){
            mVideoInfo = new VideoInfo();
            mVideoInfo.mWidth = CommonData.VIDEO_WIDTH;
            mVideoInfo.mHeight = CommonData.VIDEO_HEIGHT;
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            byteOut.write(mIAvrtpService.getVideoSPS());
            byteOut.write(mIAvrtpService.getVideoPPS());
            mVideoInfo.mCsdInfo = byteOut.toByteArray();
        }
    }
    public void stopVideoGet(){
        mVideoInfo = null;
        mBVideoGet = false;
    }

    public void startAudioGet(){
        mBAudioGet = true;
        startGetThread();
    }
    public void stopAudioGet(){
        mBAudioGet = false;
    }
}
