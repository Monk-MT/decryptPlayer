package com.ciee.cau.decryptPlayer.player;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author 陈明涛 Email:cmt96@foxmail.com
 * @version V1.0
 * @Description: 和
 * @Date
 */
public class MyMediaPlayer extends IjkMediaPlayer implements LifecycleObserver {
    private int mPlayStatus = Status.STATE_PLAYING;

    public MyMediaPlayer() {
    }

    public int getPlayStatus() {
        return mPlayStatus;
    }
    public void setPlayStatus(int playStatus) {
        mPlayStatus = playStatus;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pausePlayer() {
        pause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resumePlayer() {
        if (mPlayStatus == Status.STATE_PLAYING) {
            start();
        }
    }
}
