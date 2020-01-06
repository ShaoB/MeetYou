package com.example.android.framework.manager;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import com.example.android.framework.utils.LogUtils;

import java.io.IOException;

import androidx.annotation.NonNull;

/**
 * Founder: shaobin
 * Create Date: 2020/1/6
 * Profile: 媒体播放
 */
public class MediaPlayManager {

    //播放
    private static final int MEDIA_STATUS_PLAY = 0;
    //暂停
    private static final int MEDIA_STATUS_PAUSE = 1;
    //停止
    private static final int MEDIA_STATUS_STOP = 2;
    //默认状态
    private static int MEDIA_STATUS = MEDIA_STATUS_STOP;

    //handler what
    private final int H_PROGRESS = 1000;


    private MediaPlayer mMediaPlayer;
    private OnMusicProgressListener onMusicProgressListener;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case H_PROGRESS:
                    if(onMusicProgressListener != null){
                        //当前时常
                        int CurrentPosition = getCurrentPosition();
                        int pos = (int)((float)CurrentPosition/(float)getDuration()*100);
                        onMusicProgressListener.OnProgress(CurrentPosition,pos);
                        mHandler.sendEmptyMessageDelayed(H_PROGRESS,1000);
                    }
                    break;
            }
            return false;
        }
    });
    public MediaPlayManager() {
        mMediaPlayer = new MediaPlayer();
    }

    //开始播放
    public void startPlay(AssetFileDescriptor path) {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            MEDIA_STATUS = MEDIA_STATUS_PLAY;
            mHandler.sendEmptyMessage(H_PROGRESS);
        } catch (IOException e) {
            LogUtils.e(e.toString());
            e.printStackTrace();
        }
    }

    //暂停播放
    public void pausePlay() {
        if (isPlaying()) {
            mMediaPlayer.pause();
            MEDIA_STATUS = MEDIA_STATUS_PAUSE;
            mHandler.removeMessages(H_PROGRESS);
        }
    }

    //继续播放
    public void continuePlay() {
        mMediaPlayer.start();
        MEDIA_STATUS = MEDIA_STATUS_PLAY;
        mHandler.sendEmptyMessage(H_PROGRESS);
    }

    //停止播放
    public void stopPlay() {
        mMediaPlayer.stop();
        MEDIA_STATUS = MEDIA_STATUS_STOP;
        mHandler.removeMessages(H_PROGRESS);
    }


    //是否在播放
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    //获取当前位置
    public int getCurrentPosition(){
        return mMediaPlayer.getCurrentPosition();
    }

    //获取总时长
    public int getDuration(){
        return mMediaPlayer.getDuration();
    }

    //是否循环
    public void setLooping(boolean isLooping){
        mMediaPlayer.setLooping(isLooping);
    }
    //播放结束
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        mMediaPlayer.setOnCompletionListener(listener);
    }
    //播放错误
    public void setOnErrorListener(MediaPlayer.OnErrorListener listener){
        mMediaPlayer.setOnErrorListener(listener);
    }
    //播放进度
    public void setOnProgressListener(OnMusicProgressListener listener){
        onMusicProgressListener = listener;
    }
    //跳转位置
    public void seekTo(int ms){
        mMediaPlayer.seekTo(ms);
    }

    public interface OnMusicProgressListener{
        void OnProgress(int currentPosition,int pos);
    }
}
