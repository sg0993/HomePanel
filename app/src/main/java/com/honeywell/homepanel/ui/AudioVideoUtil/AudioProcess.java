package com.honeywell.homepanel.ui.AudioVideoUtil;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.util.Log;

import com.honeywell.homepanel.IAvRtpService;
import com.honeywell.homepanel.common.CommonData;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by H135901 on 4/5/2017.
 */

public class AudioProcess implements Runnable {

    private final static int Sample_Rate = 8000;//每秒8K个点,需在4000-192000之间，太低或太高都不行，人耳分辨率在20HZ到40KHZ之间
    private final static int ChannelRecordConfiguration = AudioFormat.CHANNEL_IN_MONO;//单声道
    private final static int ChannelPlayConfiguration = AudioFormat.CHANNEL_OUT_MONO;//单声道
    private final static int AUDIOENCODING = AudioFormat.ENCODING_PCM_16BIT;//一个采样点16比特-2个字节
    private static final String TAG = AudioProcess.class.getSimpleName() + " IPCTAG_AudioProcess";
    private AudioRecord mAudioRecord = null;
    private AudioTrack mAudioTrack = null;
    private volatile boolean mStoped = false;
    private int mRecBufferSize = 0;
    private int mPlayBufferSize = 0;
    private Thread mPlayThread = null, mRecordThread = null;
    private static final int MAX_QUEUE_SIZE = 200;
    public BlockingQueue<byte[]> mPlayQueue = new LinkedBlockingQueue<byte[]>(MAX_QUEUE_SIZE);
    private String mP2PUUID = null;
    private Context mContext = null;
    private File audioFile;

    //IPVDP only support 160 bytes per package
    private byte[] mRestData = null;
    private final static int BYTESENDLEN = 160;

    private int mSeq = 0;
    private long mTs = 0;
    // 80:V,P,X,CC;     08:C,M,PT;
    private static byte[] VPXCC_BYTE = {(byte) 0x80, 0x08};
    // SSRC随机生成
    private static byte[] SSRC_BYTE = {0x24, (byte) 0xf2, (byte) 0xe7, 0x16};

    private IAvRtpService mIAvRtpService = null;

    public AudioProcess(Context context, String p2pUUid, IAvRtpService iAvRtpService) {
        mP2PUUID = p2pUUid;
        mContext = context;
        //在这里我们创建一个文件，用于保存录制内容
        File fpath = new File(CommonData.AUDIO_PATH);
        if (!fpath.exists()) {
            fpath.mkdirs();//创建文件夹
        }
        //创建临时文件,注意这里的格式为.pcm audioFile = File.createTempFile("recording", ".pcm", fpath);
        audioFile = new File(fpath, "recording.pcm");
        if (audioFile.exists()) {
            audioFile.delete();
        }
        try {
            audioFile.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "audioFile.createNewFile IOException = " + e.getMessage());
            e.printStackTrace();
        }
        mRestData = new byte[0];
        mIAvRtpService = iAvRtpService;
    }

    public void startPhoneRecordAndPlay() throws Exception {
        initAudioHardware();
        mStoped = false;

        //start record Thread
        mRecordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startPhoneMicRecord();
                } catch (Exception e) {
                    Log.e(TAG, " startPhoneMicRecord() Exception e ----- " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        mRecordThread.start();

        //start playing
        mPlayThread = new Thread(this);
        mPlayThread.start();
    }

    private void initAudioHardware() throws Exception {
        //根据采样率，单双声道 ，采样精度来得到frame的大小。 注意，按照数字音频的知识，这个算出来的是一秒钟buffer的大小。我们得到一个满足最小要求的缓冲区大小
        /*
            音频中最常见的是frame这个单位，什么意思？经过多方查找，最后还是在ALSA的wiki中
            找到解释了。一个frame就是1个采样点的字节数*声道。为啥搞个frame出来？因为对于多声道的话，用1个采样点的字节数表示不全，因为播放的时候肯定是多个声道的数据都要播出来才行。
            所以为了方便，就说1秒钟有多少个frame，这样就能抛开声道数，把意思表示全了。
            */
        mPlayBufferSize = AudioTrack.getMinBufferSize(Sample_Rate, ChannelPlayConfiguration, AUDIOENCODING);

        //根据采样率，单双声道 ，采样精度来得到frame的大小。
        mRecBufferSize = AudioRecord.getMinBufferSize(Sample_Rate, ChannelRecordConfiguration, AUDIOENCODING);
        Log.e(TAG, " -------- 12345initAudioHardware() ---------mRecBufferSize =  " + mRecBufferSize + " , mPlayBufferSize =  " + mPlayBufferSize);

        //根据音频获取来源、音频采用率、音频录制声道、音频数据格式和缓冲区大小来创建AudioRecord对象
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC // 音频获取源
                , Sample_Rate
                , ChannelRecordConfiguration// 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
                , AUDIOENCODING// 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
                , mRecBufferSize);



        /*
        StreamType:
        这个在构造AudioTrack的第一个参数中使用。这个参数和Android中的AudioManager有关系，涉及到手机上的音频管理策略。
        Android将系统的声音分为以下几类常见的（未写全）：
        STREAM_ALARM：警告声
        STREAM_MUSCI：音乐声，例如music等
        STREAM_RING：铃声
        STREAM_SYSTEM：系统声音
        STREAM_VOCIE_CALL：电话声音
        为什么要分这么多呢？以前在台式机上开发的时候很少知道有这么多的声音类型，不过仔细思考下，发现这样做是有道理的。例如你在听music的时候接到电话，这个时候music播放肯定会停止，此时你只能听到电话，
        如果你调节音量的话，这个调节肯定只对电话起作用。当电话打完了，再回到music，你肯定不用再调节音量了。
        其实系统将这几种声音的数据分开管理，所以，这个参数对AudioTrack来说，它的含义就是告诉系统，我现在想使用的是哪种类型的声音，这样系统就可以对应管理他们了。


        AudioTrack.MODE_STREAM的意思：
        AudioTrack中有MODE_STATIC和MODE_STREAM两种分类。STREAM的意思是由用户在应用程序通过write方式把数据一次一次得写到audiotrack中。这个和我们在socket中发送数据一样，应用层从某个地方获取数据，
        例如通过编解码得到PCM数据，然后write到audiotrack。
        这种方式的坏处就是总是在JAVA层和Native层交互，效率损失较大。
        而STATIC的意思是一开始创建的时候，就把音频数据放到一个固定的buffer，然后直接传给audiotrack，后续就不用一次次得write了。AudioTrack会自己播放这个buffer中的数据。
        这种方法对于铃声等内存占用较小，延时要求较高的声音来说很适用*/
        //创建AudioTrack
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, Sample_Rate, ChannelPlayConfiguration, AUDIOENCODING, mPlayBufferSize, AudioTrack.MODE_STREAM);

    }

    @Override
    public void run() {
        try {
            startPhoneSpkPlay();
        } catch (Exception e) {
            Log.e(TAG, " startPhoneSpkPlay() Exception e ----- " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void startPhoneMicRecord() throws Exception {
        mSeq = 0;
        mTs = 0;
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(audioFile)));
        mAudioRecord.startRecording();

        int restLength = 0;
        while ((!Thread.interrupted()) && !mStoped) {
            if (mStoped) {
                break;
            }
            byte[] compressedVoice = new byte[mRecBufferSize];
            int num = mAudioRecord.read(compressedVoice, 0, mRecBufferSize);
            dos.write(compressedVoice, 0, num);

            byte[] writePCMData = new byte[num / 2];
            int length = 0;
            for (int f = 0; f < num / 2; f++) {
                int a = compressedVoice[f * 2];
                int b = compressedVoice[f * 2 + 1];
                int ab = (b << 8) + a;
                byte data = s16_to_alaw(ab);
                writePCMData[length] = data;
                length++;
            }
            int totalLength = restLength + length;
            byte[] totalData = new byte[totalLength];
            if (restLength > 0) {
                System.arraycopy(mRestData, 0, totalData, 0, restLength);
            }
            System.arraycopy(writePCMData, 0, totalData, restLength, length);

            int count = totalLength / BYTESENDLEN;
            Log.i(TAG, "sendCallByteDataAById count =" + count + " , mP2PUUID = " + mP2PUUID);
            for (int i = 0; i < count; i++) {
                byte[] sendData = new byte[BYTESENDLEN];
                System.arraycopy(totalData, i * BYTESENDLEN, sendData, 0, BYTESENDLEN);
                if (!TextUtils.isEmpty(mP2PUUID)) {
                    mSeq++;
                    mTs += BYTESENDLEN;
                    byte[] sendData2 = new byte[BYTESENDLEN + 12];
                    System.arraycopy(VPXCC_BYTE, 0, sendData2, 0, 2);//0x80, 0x08
                    System.arraycopy(int2byte(mSeq, 2), 0, sendData2, 2, 2);
                    System.arraycopy(long2byte(mTs, 4), 0, sendData2, 4, 4);
                    System.arraycopy(SSRC_BYTE, 0, sendData2, 8, 4);//0x24, 0xf2, 0xe7, 0x16
                    System.arraycopy(sendData, 0, sendData2, 12, BYTESENDLEN);
                    if(null != mIAvRtpService){
                        mIAvRtpService.setAudioFrame(sendData2);
                    }
                    /*P2PConn.sendCallByteDataAById(sendData2, BYTESENDLEN + 12, mP2PUUID);*/
                    Log.i(TAG, "12345    P2PConn.sendCallByteDataAById(sendData, BYTESENDLEN, mP2PUUID);");
                }
            }
            restLength = (totalLength - count * BYTESENDLEN);
            mRestData = new byte[restLength];
            System.arraycopy(totalData, count * BYTESENDLEN, mRestData, 0, restLength);

        }
        dos.close();
    }

    public static String bytes2HexString(byte[] b, int length) {
        StringBuilder sb = new StringBuilder();
        sb.append(" - ");
        for (int i = 0; i < length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
            sb.append(" - ");
        }
        return sb.toString();
    }

    public static String bytes2BinaryString(byte[] b, int length) {
        StringBuilder sb = new StringBuilder();
        sb.append(" - ");
        for (int i = 0; i < length; i++) {
            String hex = Integer.toBinaryString(b[i] & 0xFF);
            if (hex.length() == 7) {
                hex = '0' + hex;
            } else if (hex.length() == 6) {
                hex = "00" + hex;
            } else if (hex.length() == 5) {
                hex = "000" + hex;
            } else if (hex.length() == 4) {
                hex = "0000" + hex;
            } else if (hex.length() == 3) {
                hex = "00000" + hex;
            } else if (hex.length() == 2) {
                hex = "000000" + hex;
            } else if (hex.length() == 1) {
                hex = "0000000" + hex;
            }
            sb.append(hex.toUpperCase());
            sb.append(" - ");
        }
        return sb.toString();
    }

    String ss(byte[] data, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(Byte.toString(data[i]));
            sb.append(" - ");
        }
        return sb.toString();
    }

    String ss2(byte[] data, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(Byte.toString(data[i]));
            sb.append(" - ");
        }
        return sb.toString();
    }

    byte[] int2byte(int value, int length) {
        byte[] result = new byte[length];
        int temp = value;
        int s = 256;
        for (int i = 0; i < length; i++) {
            int j = length - 1 - i;
            result[j] = (byte) (temp % s);
            temp = temp / s;
        }
        return result;
    }

    byte[] long2byte(long value, int length) {
        byte[] result = new byte[length];
        long temp = value;
        int s = 256;
        for (int i = 0; i < length; i++) {
            int j = length - 1 - i;
            result[j] = (byte) (temp % s);
            temp = temp / s;
        }
        return result;
    }

    byte s16_to_alaw(int pcm_val) {
        int mask = 0;
        int seg = 0;
        byte aval = 0;

        if (pcm_val >= 0) {
            mask = 0xD5;
        } else {
            mask = 0x55;
            pcm_val = -pcm_val;
            if (pcm_val > 0x7fff)
                pcm_val = 0x7fff;
        }

        if (pcm_val < 256)
            aval = (byte) (pcm_val >> 4);
        else {
            /* Convert the scaled magnitude to segment number. */
            seg = val_seg(pcm_val);
            aval = (byte) ((seg << 4) | ((pcm_val >> (seg + 3)) & 0x0f));
        }
        return (byte) (aval ^ mask);
    }

    private int alaw_to_s16(byte a_val) {
        int t = 0;
        int seg = 0;
        a_val ^= 0x55;
        t = a_val & 0x7f;
        if (t < 16)
            t = (t << 4) + 8;
        else {
            seg = (t >> 4) & 0x07;
            t = ((t & 0x0f) << 4) + 0x108;
            t <<= seg - 1;
        }
        return (((a_val & 0x80) > 0) ? t : -t);
    }

    int val_seg(int val) {
        int r = 0;
        val >>= 7;
        if ((val & 0xf0) > 0) {
            val >>= 4;
            r += 4;
        }
        if ((val & 0x0c) > 0) {
            val >>= 2;
            r += 2;
        }
        if ((val & 0x02) > 0)
            r += 1;
        return r;
    }

    private final int QUEUEPOLLTMOUT = 500;// 1s

    private void startPhoneSpkPlay() throws Exception {
        int numBytesRead = 0;
        byte[] gsmdata;
        //开始
        mAudioTrack.play();
        try {
            while ((!Thread.interrupted()) && !mStoped) {
                if (mStoped) {
                    break;
                }
                try {
                    gsmdata = mPlayQueue.poll(QUEUEPOLLTMOUT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(TAG, "----startPhoneSpkPlay-----> InterruptedException e " + e.getMessage());
                    continue;
                }
                if (null != gsmdata) {
                    numBytesRead = gsmdata.length;
                    if (numBytesRead >= 0) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        for (int f = 0; f < numBytesRead; f++) {
                            int pcm = alaw_to_s16(gsmdata[f]);
                            byte low = (byte) (pcm & 0xff);
                            byte high = (byte) ((pcm >> 8) & 0xff);
                            outputStream.write(low);
                            outputStream.write(high);
                        }
                        byte[] array = outputStream.toByteArray();
                        mAudioTrack.write(array, 0, array.length);//往track中写数据
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "startPhoneSpkPlay Exception e =" + e.getMessage(), e);
        }
    }


    public void stopPhoneRecordAndPlay() throws Exception {
        Log.e(TAG, "stopPhoneRecordAndPlay ");
        mStoped = true;
        // stop record
        if (null != mAudioRecord) {
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
            Log.e(TAG, "stopPhoneRecordAndPlay mAudioRecord.stop()");
        }
        // stop play
        if (null != mAudioTrack) {
            mAudioTrack.stop();//停止播放
            mAudioTrack.release();//释放底层资源。
            mAudioTrack = null;
        }
    }

    public void setUuid(String uuid) {
        mP2PUUID = uuid;
    }

    /**
     * Created by H135901 on 4/5/2017.
     */


}
