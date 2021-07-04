package com.ciee.cau.decryptPlayer.player;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author 陈明涛 Email:cmt96@foxmail.com
 * @version V1.0
 * @Description: 播放器ViewModel
 * @Date 2021/5/27 15:39
 */

public class PlayerViewModel extends ViewModel {
    public static final String TAG = PlayerViewModel.class.getSimpleName();

    /**
     * 播放器上下文
     */
    private Activity mActivity;
    /**
     * 控制界面显示时长
     */
    private long mControllerShowTime = 0L;
    /**
     * 流选择界面显示时长
     */
    private long mStreamSelecterShowTime = 0L;
    private PlayerSettings mPlayerSettings;
    /**
     * 播放器
     */
    private MyMediaPlayer mMediaPlayer;
    /**
     * 当前播放地址
     */
    private String mCurrentUrl = "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4";
    /**
     * 是否为直播
     */
    private boolean isLive;
    /**
     * 是否为竖屏：true为竖屏
     */
    private boolean isPortrait = true;
    /**
     * 是否自动重连
     */
    private boolean isAutoReConnect = true;
    /**
     * 自动重连时间
     */
    private int mAutoConnectTime = 5000;
    /**
     * 音量最大值
     */
    private int mMaxVolume;

    /**
     * 播放器高宽
     */
    private MutableLiveData<Pair<Integer, Integer>> mVideoResolution = new MutableLiveData<>(new Pair<>(0,0));
    /**
     * 播放器高宽
     */
    LiveData<Pair<Integer, Integer>> videoResolution = mVideoResolution;

    /**
     * 播放状态
     */
    private MutableLiveData<Integer> mPlayerStatus = new MutableLiveData<>();
    /**
     * 播放状态
     */
    LiveData<Integer> playerStatus = mPlayerStatus;

    /**
     * 视频标题
     */
    private MutableLiveData<String> mTietle = new MutableLiveData<>();
    /**
     * 视频标题
     */
    LiveData<String> title = mTietle;

    /**
     * 当前声音大小
     */
    private MutableLiveData<Integer> mVolume = new MutableLiveData<>();
    /**
     * 当前声音大小
     */
    LiveData<Integer> volume = mVolume;

    /**
     * 当前亮度大小
     */
    private MutableLiveData<Float> mBrightness = new MutableLiveData<>();
    /**
     * 当前亮度大小
     */
    LiveData<Float> brightness = mBrightness;

    /**
     * 视频加载速度
     */
    private MutableLiveData<Integer> mLoadingSpeed = new MutableLiveData<>();
    /**
     * 视频加载速度
     */
    LiveData<Integer> loadingSpeed = mLoadingSpeed;

    /**
     * 缓冲进度百分比
     */
    private MutableLiveData<Integer> mBufferPercent = new MutableLiveData<>(0);
    /**
     * 缓冲进度百分比
     */
    LiveData<Integer> bufferPercent = mBufferPercent;

    /**
     * 视频总时长
     */
    private MutableLiveData<Long> mDuration = new MutableLiveData<>();
    /**
     * 视频总时长
     */
    LiveData<Long> duration = mDuration;

    /**
     * 当前播放位置
     */
    private MutableLiveData<Long> mCurrentPosition = new MutableLiveData<>();
    /**
     * 当前播放位置
     */
    LiveData<Long> currentPosition = mCurrentPosition;

    /**
     * 新播放位置
     */
    private MutableLiveData<Long> mNewPosition = new MutableLiveData<>();
    /**
     * 新播放位置
     */
    LiveData<Long> newPosition = mNewPosition;

    /**
     * 快进/快退时长
     */
    private MutableLiveData<Long> mSeekProcess = new MutableLiveData<>();
    /**
     * 快进/快退时长
     */
    LiveData<Long> seekProcess= mSeekProcess;

    /**
     * 中间播放按钮是否显示
     */
    private MutableLiveData<Integer> mPlayButtonLotVisibility = new MutableLiveData<>(View.INVISIBLE);
    /**
     * 中间播放按钮是否显示
     */
    LiveData<Integer> playButtonLotVisibility = mPlayButtonLotVisibility;

    /**
     * 加载界面是否显示
     */
    private MutableLiveData<Integer> mLoadingLotVisibility = new MutableLiveData<>(View.INVISIBLE);
    /**
     * 加载界面是否显示
     */
    LiveData<Integer> loadingLotVisibility = mLoadingLotVisibility;

    /**
     * 上下控制界面是否显示
     */
    private MutableLiveData<Integer> mControlLotVisibility = new MutableLiveData<>(View.VISIBLE);
    /**
     * 上下控制界面是否显示
     */
    LiveData<Integer> controlLotVisibility = mControlLotVisibility;

    /**
     * 滑动手势显示界面是否显示
     */
    private MutableLiveData<Integer> mTouchGesturesShowLotVisibility = new MutableLiveData<>(View.INVISIBLE);
    /**
     * 滑动手势显示界面是否显示
     */
    LiveData<Integer> touchGesturesShowLotVisibility = mTouchGesturesShowLotVisibility;

    /**
     * 声音滑动界面是否显示
     */
    private MutableLiveData<Integer> mVolumeLotVisibility = new MutableLiveData<>(View.INVISIBLE);
    /**
     * 声音滑动界面是否显示
     */
    LiveData<Integer> volumeLotVisibility = mVolumeLotVisibility;

    /**
     * 亮度滑动界面是否显示
     */
    private MutableLiveData<Integer> mBrightnessLotVisibility = new MutableLiveData<>(View.INVISIBLE);
    /**
     * 亮度滑动界面是否显示
     */
    LiveData<Integer> brightnessLotVisibility = mBrightnessLotVisibility;

    /**
     * 快进界面是否显示
     */
    private MutableLiveData<Integer> mFastForwardLotVisibility = new MutableLiveData<>(View.INVISIBLE);
    /**
     * 快进界面是否显示
     */
    LiveData<Integer> fastForwardLotVisibility = mFastForwardLotVisibility;

    /**
     * 流选择界面是否显示
     */
    private MutableLiveData<Integer> mStreamSelectLotVisibility = new MutableLiveData<>(View.INVISIBLE);
    /**
     * 流选择界面是否显示
     */
    LiveData<Integer> streamSelectLotVisibility = mStreamSelectLotVisibility;


    public PlayerViewModel() {
        mActivity = PlayerView.getActivity();
        createPlayer();
        loadVideo();
    }

    public MyMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public int getMaxVolume() {
        return mMaxVolume;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mMediaPlayer.release();
    }

    /**
     * 创建播放器
     * @return ijkMediaPlayer
     */
    public IMediaPlayer createPlayer() {
        mMediaPlayer = new MyMediaPlayer();
        mMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEFAULT);

        mPlayerSettings = new PlayerSettings(mActivity);
////        if (mSettings.getUsingMediaCodec()) {
//            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1); // todo: 使用硬解后无法切进度
////            if (mSettings.getUsingMediaCodecAutoRotate()) {
//                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
////            } else {
////                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);
////            }
////
////            if (mSettings.getMediaCodecHandleResolutionChange()) {
//                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
////            } else {
////                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 0);
////            }
////        } else {
////            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
////        }

//        if (mSettings.getUsingOpenSLES()) {
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);
//        } else {
//            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
//        }

//        String pixelFormat = mSettings.getPixelFormat();
//        if (TextUtils.isEmpty(pixelFormat)) {
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
//        } else {
//            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", pixelFormat);
//        }
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1); // 跳帧处理
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0); // 1：需要准备好后自动播放，0：不需要
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);

        return mMediaPlayer;
    }

    /**
     * 加载视频
     */
    private void loadVideo() {
        mLoadingLotVisibility.setValue(View.VISIBLE);
        mPlayerStatus.setValue(Status.STATE_PREPARING);

        try {
            mMediaPlayer.setDataSource(mCurrentUrl);
            isLive();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(mp -> {
            mLoadingLotVisibility.setValue(View.INVISIBLE);
            mDuration.setValue(mMediaPlayer.getDuration());
            updatePlayerProgress();
            mMediaPlayer.start();
            mPlayerStatus.setValue(Status.STATE_PLAYING);
            toggleControlLotVisibility();
        });

        mMediaPlayer.setOnVideoSizeChangedListener((mp, width, height, sar_num, sar_den) ->
                mVideoResolution.setValue(new Pair<>(width, height)));

        mMediaPlayer.setOnBufferingUpdateListener((mp, percent) -> mBufferPercent.setValue(percent));

        mMediaPlayer.setOnSeekCompleteListener(mp -> {
            mLoadingLotVisibility.setValue(View.INVISIBLE);
        });

        mMediaPlayer.setOnInfoListener((mp, what, extra) -> {
            if (what == Status.MEDIA_INFO_NETWORK_BANDWIDTH || what == Status.MEDIA_INFO_BUFFERING_BYTES_UPDATE) {
                Log.i(TAG, "====extra====   " + extra);
                mLoadingSpeed.setValue(extra);
            }
            return true;
        });

        mMediaPlayer.setOnCompletionListener(mp -> mPlayerStatus.setValue(Status.STATE_COMPLETED));

        mMediaPlayer.prepareAsync();
    }

    /**
     * 调整播放界面大小
     */
    public void emmitVideoResolution() {
        mVideoResolution.setValue(mVideoResolution.getValue());
    }


    /**
     * 当前播放的是否是直播
     */
    public boolean isLive() {
        isLive = mCurrentUrl != null
                && (mCurrentUrl.startsWith("rtmp://")
                || (mCurrentUrl.startsWith("http://") && mCurrentUrl.endsWith(".m3u8"))
                || (mCurrentUrl.startsWith("http://") && mCurrentUrl.endsWith(".flv")));
        return isLive;
    }

    /**
     * 播放状态转变
     */
    public void togglePlayerStatus() {
        switch (mPlayerStatus.getValue()) {
            case Status.STATE_PLAYING:
                mMediaPlayer.pause();
                mMediaPlayer.setPlayStatus(Status.STATE_PAUSED);
                mPlayerStatus.setValue(Status.STATE_PAUSED);
                break;
            case Status.STATE_PAUSED:
                mMediaPlayer.start();
                mMediaPlayer.setPlayStatus(Status.STATE_PLAYING);
                mPlayerStatus.setValue(Status.STATE_PLAYING);
                break;
            case Status.STATE_COMPLETED:
                mMediaPlayer.prepareAsync(); //todo 更好的重播方法
                mPlayerStatus.setValue(Status.STATE_PLAYING);
                break;
            default:
        }
    }

//    /**
//     * 更新下载速度（每秒一次）
//     */
//    private void updateLoadingSpeed() {
//        Handler handler = new Handler(Looper.myLooper());
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mCurrentPosition.setValue(mMediaPlayer.getCurrentPosition());
//                handler.postDelayed(this, 500);
//            }
//        },1000); // 延时1秒
//    }

    /**
     * 更新播放进度(每半秒一次）
     */
    private void updatePlayerProgress() {
        Handler handler = new Handler(Looper.myLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCurrentPosition.setValue(mMediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        },500); // 延时半秒
    }

    /**
     * 通过SeekBar切换播放进度
     * @param progress 播放进度
     */
    public void playerSeekToProgress(int progress) {
        mLoadingLotVisibility.setValue(View.VISIBLE);
        mMediaPlayer.seekTo(progress);
        mCurrentPosition.setValue(mMediaPlayer.getCurrentPosition());
    }

    /**
     * 关闭播放器
     */
    public void closePlayer() {
        mMediaPlayer.release();
    }

    /**
     * 菜单按钮点击事件
     */
    public void onClickMenuBtn() {

    }

    /**
     * 流选择按钮点击事件
     */
    public void onClickStreamCheckBtn() {
        if (mStreamSelectLotVisibility.getValue() == View.INVISIBLE) {
            mControlLotVisibility.setValue(View.INVISIBLE);
            mStreamSelectLotVisibility.setValue(View.VISIBLE);
            mStreamSelecterShowTime = System.currentTimeMillis();
            new Handler(Looper.myLooper()).postDelayed(() -> {
                if (System.currentTimeMillis() - mStreamSelecterShowTime > 3000) {
                    mStreamSelectLotVisibility.setValue(View.INVISIBLE);
                }
            },3000); // 延时5秒
        } else {
            mStreamSelectLotVisibility.setValue(View.INVISIBLE);
        }
    }


    /**
     * 滑动手势处理
     * @param moveMode 滑动模式
     * @param percentX x方向滑动距离占屏幕的百分比
     * @param percentY y方向滑动距离占屏幕的百分比
     */
    public void onTouchGesturesBegin(int moveMode, float percentX, float percentY) {
        mTouchGesturesShowLotVisibility.setValue(View.VISIBLE);
        if (moveMode == Status.MOVE_HORIZONTAL) {
            if (!isLive) {
                // 根据滑动百分比计算快进时长
                long addition;
                if (percentX < 0) {
                    addition =  -(long) Math.pow(2, -percentX * 10) - 1;
                } else {
                    addition = (long) Math.pow(2, percentX * 10) - 1;
                }
                addition *= 1000;

                // 显示滑动进度
                mFastForwardLotVisibility.setValue(View.VISIBLE);
                if (mCurrentPosition.getValue() + addition > mDuration.getValue()) {
                    mNewPosition.setValue(mDuration.getValue());
                    mSeekProcess.setValue(mDuration.getValue() - mCurrentPosition.getValue());
                } else {
                    mNewPosition.setValue(mCurrentPosition.getValue() + addition);
                    mSeekProcess.setValue(addition);
                }
            }
        } else if (moveMode == Status.MOVE_VERTICAL_LEFT) {
            // 获取变更前声音大小
            AudioManager audioManager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
            mVolume.setValue(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            if (mVolume.getValue() < 0)
                mVolume.setValue(0);
            mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

            // 计算声音大小
            int afterChange = (int) (percentY * mMaxVolume * 0.2) + mVolume.getValue();
            if (afterChange > mMaxVolume) {
                afterChange = mMaxVolume;
            } else if (afterChange < 0) {
                afterChange = 0;
            }
            mVolume.setValue(afterChange);

            // 变更声音
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume.getValue(), 0);

            // 显示声音大小
            mVolumeLotVisibility.setValue(View.VISIBLE);

        } else if (moveMode == Status.MOVE_VERTICAL_RIGHT) {
            Window window = mActivity.getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();

            // 获取当前亮度大小
            mBrightness.setValue(layoutParams.screenBrightness);
            if (mBrightness.getValue() <= 0.00f) {
                mBrightness.setValue(0.5f);
            } else if (mBrightness.getValue() < 0.01f) {
                mBrightness.setValue(0.01f);
            }
            // 计算亮度大小
            float newBrightness = mBrightness.getValue() + percentY * 0.2f;
            if (newBrightness > 1.0f) {
                mBrightness.setValue(1.0f);
            } else if (newBrightness < 0.01f) {
                mBrightness.setValue(0.01f);
            } else {
                mBrightness.setValue(newBrightness);
            }

            // 变更亮度
            layoutParams.screenBrightness = mBrightness.getValue();
            window.setAttributes(layoutParams);

            // 显示亮度大小
            mBrightnessLotVisibility.setValue(View.VISIBLE);
        }
    }

    /**
     * 滑动手势结束
     */
    public void onTouchGesturesStop() {
        // 若为快进滑动，则切换进度并显示加载界面
        if (mFastForwardLotVisibility.getValue() == View.VISIBLE) {
            playerSeekToProgress(Math.toIntExact(mNewPosition.getValue()));
        }
        // 隐藏手势界面
        mTouchGesturesShowLotVisibility.setValue(View.INVISIBLE);
        mFastForwardLotVisibility.setValue(View.INVISIBLE);
        mVolumeLotVisibility.setValue(View.INVISIBLE);
        mBrightnessLotVisibility.setValue(View.INVISIBLE);
    }

    /**
     * 控制控制界面是否可见
     */
    public void toggleControlLotVisibility() {
        if (mControlLotVisibility.getValue() == View.INVISIBLE) {
            mControlLotVisibility.setValue(View.VISIBLE);
            mControllerShowTime = System.currentTimeMillis();
            new Handler(Looper.myLooper()).postDelayed(() -> {
                if (System.currentTimeMillis() - mControllerShowTime > 3000) {
                    mControlLotVisibility.setValue(View.INVISIBLE);
                }
            },3000); // 延时3秒
        } else {
            mControlLotVisibility.setValue(View.INVISIBLE);
        }
    }
}