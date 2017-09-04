package com.honeywell.homepanel.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMuxer;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;

import com.honeywell.homepanel.AudioPacketWithEC;
import com.honeywell.homepanel.IAvRtpService;
import com.honeywell.homepanel.Utils.RelativeTimer;
import com.honeywell.homepanel.Utils.RelativeTimerTask;
import com.honeywell.homepanel.cloud.IOUtils;
import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.common.CommonJson;
import com.honeywell.homepanel.common.CommonPath;
import com.honeywell.homepanel.common.utils.CommonUtils;
import com.honeywell.homepanel.ui.AudioVideoUtil.AVIUtil;
import com.honeywell.homepanel.ui.AudioVideoUtil.AudioProcessWithEC;
import com.honeywell.homepanel.ui.AudioVideoUtil.CallRecordReadyEvent;
import com.honeywell.homepanel.ui.AudioVideoUtil.VStreamBuffer;
import com.honeywell.homepanel.ui.AudioVideoUtil.VideoInfo;
import com.honeywell.homepanel.ui.RingFile.AudioFilePlay;
import com.honeywell.homepanel.ui.activities.CallActivity;
import com.honeywell.homepanel.ui.activities.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

;

/**
 * Created by H135901 on 4/5/2017.
 */

public class CallBaseFragment extends Fragment {

    private static final int MAX_QUEUE_SIZE = 100;

    private static int SLEEPTIME = 5;
    private static  final  String TAG = "CallBaseFragment";

    public IAvRtpService mIAvrtpService = null;
    //private AudioProcess mAudioProcess = null;
    private AudioProcessWithEC mAudioProcess = null;

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
    AudioFilePlay audioPlay ;
	private RelativeTimerTask mScreenTask = null;



    public void setFragmentAidl(IAvRtpService iAvRtpService,Context context){
        Log.d(TAG, "setFragmentAidl: 11111111111111");
        mIAvrtpService = iAvRtpService;
        //mAudioProcess = new AudioProcess(context,"",mIAvrtpService);
        mAudioProcess = new AudioProcessWithEC(context,"",mIAvrtpService);

        //TODO for test
        mAudioProcess.setUuid(CommonUtils.generateCommonEventUuid());
    }

    public void startAudio(double volumeMultiple){
        Log.d(TAG, "startAudio: mBAudioGet:"+mBAudioGet+",,1111111");
        if (null != mAudioProcess && !mBAudioGet) {
            try {
                mAudioProcess.startPhoneRecordAndPlay(volumeMultiple);
                startAudioGet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startAutoAudioReply() {
        Log.d(TAG, "startAudioRecordFromFile: ");
        if (null != mAudioProcess) {
            mAudioProcess.startPhoneRecordFromFileThread();
        }
    }
    public void waitAutoAudioReplyFinish() {
        Log.d(TAG, "waitAutoAudioReplyFinish: ");
        if (null != mAudioProcess) {
            mAudioProcess.waitPhoneRecordFromFileThread();
        }
    }
    public void stopAutoAudioReply() {
        Log.d(TAG, "stopAutoAudioReply: ");
        if (null != mAudioProcess) {
            mAudioProcess.stopPhoneRecordFromFileThread();
        }
    }
    public    void stopAudio(){
        Log.d(TAG, "stopAudio: 111111111");
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
        Log.d(TAG, "onDestroy: 11111111");
        stopAutoAudioReply();
        stopAudio();
        stopAudioGet();
        stopVideoGet();
        stopThread();
        stopRecord();
        if(mScreenTask != null){
            mScreenTask.cancel();
            mScreenTask = null;
        }
        super.onDestroy();
    }

    public  void stopThread(){
        Log.d(TAG, "stopThread: 1111");
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
                            //Log.d(TAG, "GetThread: 111111111111111111111");
                            getVideoInfo();
                            mVideoPlayQueue.offer(new VStreamBuffer(data));
                            if(mBRecord){
                                mVideoRecordQueue.offer(ByteBuffer.wrap(data));
                            }
                        }
                        Thread.sleep(SLEEPTIME);
                    }

                    if(mBAudioGet) {
                        //byte [] data = mIAvrtpService.getAudioFrame();
                        byte[] data = null;
                        AudioPacketWithEC eachFrame = null;
                        if (null != mIAvrtpService) {
                            eachFrame = mIAvrtpService.getAudioFrameWithEC();
                            if (null != eachFrame) {
                                data = eachFrame.audioFrame;
                                Log.d(TAG, "run: get package with seq " + eachFrame.audioFrameSeq);
                            } else {
                                Thread.sleep(20);
                                continue;
                            }
                        }

                        if(null != data){
                            //mAudioProcess.mPlayQueue.offer(data);
                            mAudioProcess.mPlayQueue.offer(eachFrame);
                            if(mBRecord){
                                mAudioRecordQueue.offer(ByteBuffer.wrap(data));
                            }
                        }
                        else{
                            Thread.sleep(SLEEPTIME/2);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "run() get thread out!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }
    public void startVideoGet(){
        //Log.d(TAG, "startVideoGet: 111111111");
        mBVideoGet = true;
        startGetThread();
    }
    private void startGetThread(){
        //Log.d(TAG, "startGetThread: 11111111");
        mRunning = true;
        if(null == mGetThread){
            mGetThread = new GetThread();
            mGetThread.start();
            return;
        }
    }

    public void startRecord(){
        Log.d(TAG, "startRecord: 11111");
        final String videoRecordStoragePath = CommonPath.getVideoRecordPath();
        if (null == videoRecordStoragePath) {
            Log.d(TAG, "startRecord: 2222222222222222");
            return;
        }
        
        Log.d(TAG, "startRecord: 33333333333333333");

        mBRecord = true;
        if(null == mVideoRecordQueue){
            mVideoRecordQueue = new LinkedBlockingQueue<ByteBuffer>(10);
        }
        if(null == mAudioRecordQueue) {
            mAudioRecordQueue = new LinkedBlockingQueue<ByteBuffer>(10);
        }

        final  String fileName = videoRecordStoragePath+ CommonUtils.getLobbyVideoRecordName();
        File dirFile = new File(videoRecordStoragePath);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "startRecord: 44444444444444");
            e.printStackTrace();
			return;
        }

        Log.d(TAG, "startRecord: file name " + fileName);
        EventBus.getDefault().post(new CallRecordReadyEvent(fileName));
        new Thread(new Runnable() {
            public void run() {
                AVIUtil.init(fileName,320,240,30);
                String audioName = CommonUtils.getFileName(fileName);
                Log.d(TAG, "startRecord() audioName:"+audioName+",,111111");
                File audioFile = IOUtils.createFile(videoRecordStoragePath,audioName+".pcm");
                final DataOutputStream audioDos = IOUtils.getIOStream(audioFile);
                try {
                    long videoTime = 0;
                    long startMillion = 0;
                    long curMillion = 0;
                    while (mBRecord){
                        if(!mVideoRecordQueue.isEmpty()){
                            ByteBuffer buffer = mVideoRecordQueue.poll();
                            if(null != buffer){
                                byte[] videoBytes = buffer.array();
                                curMillion = System.currentTimeMillis();
                                if(startMillion == 0){
                                    startMillion = curMillion;
                                }
                                videoTime = curMillion - startMillion;
                                Log.d(TAG, "record:run() videoTime:"+videoTime+"len,,111111111");
                                AVIUtil.writeVideo(videoBytes,videoBytes.length,videoTime);
                                //startMillion = curMillion;
                            }
                        }
                       if(!mAudioRecordQueue.isEmpty()){
                            ByteBuffer buffer = mAudioRecordQueue.take();
                            byte[] audioBytes = buffer.array();
                            Log.d(TAG, "record:run() audio len:"+audioBytes.length+",,222222");

                            if(null != audioDos){
                                IOUtils.writeDataToFile(audioDos,audioBytes,0,audioBytes.length);
                            }

                          /* //for test,do not wirte audio now
                           AVIUtil.writeAudio(audioBytes,audioBytes.length);*/
                        }
                    }
                    AVIUtil.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    if(null != audioDos){
                        try {
                            audioDos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public void stopRecord(){
        Log.d(TAG, "stopRecord: 11111111");
        mBRecord = false;
        try {
            if(mMuxer != null){
                mMuxer.stop();
                mMuxer.release();
                mMuxer = null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getVideoInfo() throws Exception{
        if(null == mVideoInfo){
            mVideoInfo = new VideoInfo();
            mVideoInfo.mWidth = 320;
            mVideoInfo.mHeight = 240;
            byte[] byteHead = null;
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            byteHead = mIAvrtpService.getVideoSPS();
            if(null != byteHead){
                byteOut.write(byteHead);
                /*mVideoInfo.mSps = ByteBuffer.wrap(byteHead);*/
                int spsEndPos = 3;
                for (int i = spsEndPos; i < byteHead.length; i++) {
                    if(byteHead[i] == 0 && byteHead[i+1] == 0 && byteHead[i+2] == 0 && byteHead[i+3] == 1){
                       spsEndPos = i;
                        Log.d(TAG, "getVideoInfo: spsEndPos:"+spsEndPos+",,aaaaaaaaaaa");
                        break;
                    }
                }
                if(spsEndPos >= byteHead.length -1){
                    mVideoInfo  = null;
                    return;
                }
                byte[] spsBytes = new byte[spsEndPos+1];
                System.arraycopy(byteHead,0,spsBytes,0,spsEndPos+1);
                mVideoInfo.mSps = ByteBuffer.wrap(spsBytes);

               /* for (int i = 0; i < spsBytes.length; i++) {
                    Log.d(TAG, "getVideoInfo: spsBytes["+i+"]"+ Integer.toHexString(spsBytes[i]));
                }*/


                byte[] ppsBytes = new byte[byteHead.length - spsEndPos -1];
                System.arraycopy(byteHead,spsEndPos,ppsBytes,0,byteHead.length - spsEndPos -1);
                mVideoInfo.mPps = ByteBuffer.wrap(ppsBytes);

              /*  for (int i = 0; i < ppsBytes.length; i++) {
                    Log.d(TAG, "getVideoInfo: ppsBytes["+i+"]"+ Integer.toHexString(ppsBytes[i]));
                }*/
                
            }
            else{
                Log.d(TAG, "getVideoInfo: sps null 11111111111111111111!!!!");
            }
           /* byteHead = mIAvrtpService.getVideoPPS();
            if(null != byteHead){
                byteOut.write(byteHead);
                mVideoInfo.mPps = ByteBuffer.wrap(byteHead);
                for (int i = 0; i < byteHead.length; i++) {
                    Log.d(TAG, "getVideoInfo: pps["+i+"]::"+ Integer.toHexString(byteHead[i]));
                }
            }
            else{
                Log.d(TAG, "getVideoInfo: pps null 22222222222!!!!");
            }*/

            mVideoInfo.mCsdInfo = byteOut.toByteArray();
        }
    }
    public void stopVideoGet(){
        Log.d(TAG, "stopVideoGet: 1111111");
        mVideoInfo = null;
        mBVideoGet = false;
    }

    private void startAudioGet(){
        Log.d(TAG, "startAudioGet: 11111111111");
        mBAudioGet = true;
        startGetThread();
    }
    public void stopAudioGet(){
        Log.d(TAG, "stopAudioGet: 1111111111");
        mBAudioGet = false;
    }



    public void stopPlayRing(){
        Log.i(TAG, "stopPlayRing: ");
        if(audioPlay != null)
            audioPlay.stopPlayAudio();
    }


    public MediaPlayer startPlayRing(String ringpath) {
        Log.i(TAG, "startPlayRing: ");
        audioPlay = new AudioFilePlay();
        return audioPlay.startPlayAudio(getActivity().getApplicationContext(), ringpath,0);
    }

    
    public void phoneOneFrame(final TextureView decoderView) {
        if(null == decoderView){
            return;
        }
        mScreenTask = new RelativeTimerTask("ScreenTask"){
            @Override
            public void run() {
                Log.d(TAG, "phoneOneFrame ");

                Bitmap bitmap = decoderView.getBitmap();
                String filePath = CommonUtils.saveBitmap(bitmap, CommonUtils.getLobbyBitMapName());
                //save record image history
                if (null != getActivity()) {
                    ((CallActivity) getActivity()).saveCallHistory(getUpdateImagePathJson(filePath));
                }
                if(bitmap != null && !bitmap.isRecycled()){
                    bitmap.recycle();
                }
                mScreenTask = null;
            }
        };
        RelativeTimer.getDefault().schedule(mScreenTask,5*1000);
    }
    private JSONObject getUpdateImagePathJson(String filePath) {
        JSONObject jsonObject = new JSONObject();
        if (TextUtils.isEmpty(filePath)) {
            return jsonObject;
        }
        try {
            jsonObject.put(CommonJson.JSON_ACTION_KEY, CommonJson.JSON_ACTION_VALUE_EVENT);
            jsonObject.put(CommonJson.JSON_SUBACTION_KEY, CommonJson.JSON_SUBACTION_VALUE_NOTIFICATIONEVENTUPDATE);
            JSONArray jsonArray = new JSONArray();
            JSONObject mapObject = new JSONObject();
            mapObject.put(CommonJson.JSON_UUID_KEY, MainActivity.CallBaseInfo.getCallUuid());
            mapObject.put(CommonData.JSON_KEY_IMAGENAME, filePath);
            jsonArray.put(mapObject);
            jsonObject.put(CommonJson.JSON_LOOPMAP_KEY, jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
