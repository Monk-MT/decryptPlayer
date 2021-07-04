package com.ciee.cau.decryptPlayer.player;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.ciee.cau.decryptPlayer.R;

/**
 * 自定义播放器界面
 */
public class PlayerView extends FrameLayout {
    public static final String TAG = PlayerView.class.getSimpleName();
    /**
     * 播放器上下文
     */
    private Context mContext;
    /**
     * 使用控件的Activity
     */
    private static Activity mActivity;
    /**
     * viewModel
     */
    private PlayerViewModel mPlayerViewModel;
    /**
     * 播放界面（只显示画面）
     */
    private FrameLayout mVPlayerLot;
    /**
     * 视频画面显示
     */
    private SurfaceView mVPlayerSurface;
//    /**
//     * 控制界面
//     */
//    private FrameLayout mVControlLot;
    /**
     * 中间播放（重播）按钮布局
     */
    private LinearLayout mVPlayButtonLot;
    /**
     * 中间播放（重播）按钮
     */
    private ImageView mVCenterPlayBtn;
    /**
     * 中间播放状态
     */
    private TextView mVStatusText;
    /**
     * 加载界面
     */
    private LinearLayout mVLoadingLot;
    /**
     * 加载转圈
     */
    private ProgressBar mVLoadingProgressBar;
    /**
     * 加载时网速
     */
    private TextView mVDownloadSpeedText;
    /**
     * 分辨率选择界面
     */
    private LinearLayout mVStreamSelectLot;
    /**
     * 分辨率选择列表
     */
    private ListView mVStreamsSelectList;
    /**
     * 顶部控制条
     */
    private LinearLayout mVTopControllerLot;
    /**
     * 返回按钮
     */
    private ImageView mVBackBtn;
    /**
     * 视频标题
     */
    private TextView mVTitleText;
    /**
     * 菜单按钮
     */
    private ImageView mVMenuBtn;
    /**
     * 手势控制显示界面
     */
    private FrameLayout mVTouchGesturesShowLot;
    /**
     * 声音显示界面
     */
    private LinearLayout mVVolumeLot;
    /**
     * 声音图标
     */
    private ImageView mVVolumeIcon;
    /**
     * 声音大小
     */
    private TextView mVVolumeText;
    /**
     * 亮度显示界面
     */
    private LinearLayout mVBrightnessLot;
    /**
     * 亮度图标
     */
    private ImageView mVBrightnessIcon;
    /**
     * 亮度大小
     */
    private TextView mVBrightnessText;
    /**
     * 快进快退显示界面
     */
    private LinearLayout mVFastForwardLot;
    /**
     * 快进多少时间
     */
    private TextView mVFastForwardText;
    /**
     * 当前播放时间
     */
    private TextView mVFastForwardCurrentText;
    /**
     * 目标快进到的时间
     */
    private TextView mVFastForwardTargetText;
    /**
     * 底部控制条
     */
    private LinearLayout mVBottomControllerLot;
    /**
     * 视频播放按钮
     */
    private ImageView mVPlayBtn;
    /**
     * 进度条界面
     */
    private LinearLayout mVProcessLot;
    /**
     * 进度条
     */
    private SeekBar mVSeekBar;
    /**
     * 当前播放时长
     */
    private TextView mVCurrentTimeText;
    /**
     * 视频时长
     */
    private TextView mVEndTimeText;
    /**
     * 流选择按钮
     */
    private TextView mVStreamCheckBtn;
    /**
     * 全屏按钮
     */
    private ImageView mVFullscreenBtn;


    /**
     * 按下和移动时的时间，用于判断是否是长按事件
     */
    long timeDown, timeMove;
    /**
     * 是否是长按事件
     */
    boolean isLongClick;
    /**
     * 触摸位置
     */
    float downX, downY, moveX, moveY;
    /**
     * 是否判断滑动方向
     */
    boolean isCheckMoveMode;
    /**
     * 移动方向： 1水平，2左侧竖直，3右侧竖直
     */
    int moveMode = Status.MOVE_NULL;
    /**
     * 处理滑动手势
     */
    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    timeDown = System.currentTimeMillis();
                    isLongClick = false;
                    isCheckMoveMode = true;
                    downX = event.getX();
                    downY = event.getY();

                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "ACTION_MOVE");
                    timeMove = System.currentTimeMillis();
                    if ((timeMove - timeDown) > 500) { // 触摸时长超过半秒则检测滑动
                        isLongClick = true;
                        moveX = event.getX();
                        moveY = event.getY();
                        float moveLenX = moveX - downX; // x左小右大
                        float moveLenY = -(moveY - downY); // y上小下大
                        // 滑动距离大于 10 则代表滑动了
                        if (isCheckMoveMode && (Math.abs(moveLenX) > 10 || Math.abs(moveLenY) > 10)) {
                            if (Math.abs(moveLenX) > Math.abs(moveLenY)) {
                                moveMode = Status.MOVE_HORIZONTAL;
                            } else {
                                if (downX < (mVPlayerLot.getWidth() * 0.5f)) {
                                    moveMode = Status.MOVE_VERTICAL_LEFT;
                                } else {
                                    moveMode = Status.MOVE_VERTICAL_RIGHT;
                                }
                            }
                            isCheckMoveMode = false;
                        }

                        if (moveMode != Status.MOVE_NULL) {
                            mPlayerViewModel.onTouchGesturesBegin(moveMode, moveLenX / mVPlayerLot.getWidth(), moveLenY / mVPlayerLot.getHeight());
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (isLongClick) {
                        mPlayerViewModel.onTouchGesturesStop();
                    } else {
                        mPlayerViewModel.toggleControlLotVisibility();
                    }
                    isLongClick = false;
                    isCheckMoveMode = true;
                    moveMode = Status.MOVE_NULL;
                    Log.i(TAG, "ACTION_CANCEL");
                    break;
            }

            return true;
        }
    };

    /**
     * 处理各个控件的点击事件
     */
    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mVPlayButtonLot.equals(v)) {
                mPlayerViewModel.togglePlayerStatus();
            } else if (mVBackBtn.equals(v)) {
                if (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                } else {
                    mPlayerViewModel.closePlayer();
                    mActivity.finish();
                }
            } else if (mVMenuBtn.equals(v)) {
                mPlayerViewModel.onClickMenuBtn();
            } else if (mVPlayBtn.equals(v)) {
                mPlayerViewModel.togglePlayerStatus();
            } else if (mVStreamCheckBtn.equals(v)) {
                mPlayerViewModel.onClickStreamCheckBtn();
            } else if (mVFullscreenBtn.equals(v)) {
                if (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) { // 当前为横屏
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                } else {
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        }
    };

    /**
     * 处理进度条拖动
     */
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mPlayerViewModel.playerSeekToProgress(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    /*-----------------------------构造函数——————————————————————————————————————*/
    public PlayerView(Context context) {
        super(context);
        initView(context);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    // 被动给PlayerViewModel提供Activity
    public static Activity getActivity() {
        return mActivity;
    }

    /*-----------------------------重写方法——————————————————————————————————————*/
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUI();
        }
        mPlayerViewModel.emmitVideoResolution();
    }

    /*-----------------------------自定义方法——————————————————————————————————————*/
    /**
     * 初始化
     */
    private void initView(Context context) {
        mContext = context;
        mActivity = (Activity)context;
        View.inflate(context, R.layout.view_ijk_player,this);
        mVPlayerLot = findViewById(R.id.video_player_layout);
        mVPlayerSurface = findViewById(R.id.video_player_surface);
//        mVControlLot = findViewById(R.id.video_control_layout);
        mVPlayButtonLot = findViewById(R.id.video_play_button_layout);
        mVCenterPlayBtn = findViewById(R.id.video_center_play_button);
        mVStatusText = findViewById(R.id.video_status_text);
        mVLoadingLot = findViewById(R.id.video_loading_layout);
        mVLoadingProgressBar = findViewById(R.id.video_loading_progress_bar);
        mVDownloadSpeedText = findViewById(R.id.video_download_speed_text);
        mVStreamSelectLot = findViewById(R.id.video_stream_select_layout);
        mVStreamsSelectList = findViewById(R.id.video_streams_select_list);
        mVTopControllerLot = findViewById(R.id.video_top_controller_layout);
        mVBackBtn = findViewById(R.id.video_back_button);
        mVTitleText = findViewById(R.id.video_title_text);
        mVMenuBtn = findViewById(R.id.video_menu_button);
        mVTouchGesturesShowLot = findViewById(R.id.video_touch_gestures_show_layout);
        mVVolumeLot = findViewById(R.id.video_volume_layout);
        mVVolumeIcon = findViewById(R.id.video_volume_icon);
        mVVolumeText = findViewById(R.id.video_volume_text);
        mVBrightnessLot = findViewById(R.id.video_brightness_layout);
        mVBrightnessIcon = findViewById(R.id.video_brightness_icon);
        mVBrightnessText = findViewById(R.id.video_brightness_text);
        mVFastForwardLot = findViewById(R.id.video_fast_forward_layout);
        mVFastForwardText = findViewById(R.id.video_fast_forward_text);
        mVFastForwardCurrentText = findViewById(R.id.video_fast_forward_current_text);
        mVFastForwardTargetText = findViewById(R.id.video_fast_forward_target_text);
        mVBottomControllerLot = findViewById(R.id.video_bottom_controller_layout);
        mVPlayBtn = findViewById(R.id.video_play_button);
        mVProcessLot = findViewById(R.id.video_process_layout);
        mVSeekBar = findViewById(R.id.video_seek_bar);
        mVCurrentTimeText = findViewById(R.id.video_current_time_text);
        mVEndTimeText = findViewById(R.id.video_end_time_text);
        mVStreamCheckBtn = findViewById(R.id.video_stream_check_button);
        mVFullscreenBtn = findViewById(R.id.video_fullscreen_button);

        // 绑定ViewModel
        mPlayerViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(PlayerViewModel.class);

        // 播放器和播放界面绑定生命周期
        AppCompatActivity appCompatActivity = (AppCompatActivity) mActivity;
        appCompatActivity.getLifecycle().addObserver(mPlayerViewModel.getMediaPlayer());

        /*--------------------------设置按钮、文字、界面的观察-----------------------------*/
        LifecycleOwner mLifecycleOwner = (LifecycleOwner) mContext;
        mPlayerViewModel.videoResolution.observe(mLifecycleOwner,
                pair -> mVPlayerSurface.post(() -> resizePlayer(pair.first, pair.second)));

        mPlayerViewModel.title.observe(mLifecycleOwner, s -> mVTitleText.setText(s));

        mPlayerViewModel.loadingSpeed.observe(mLifecycleOwner,
                integer -> mVDownloadSpeedText.setText(FormatSpeed(integer)));

        mPlayerViewModel.volume.observe(mLifecycleOwner,
                integer -> mVVolumeText.setText(FormatVolume(integer)));

        mPlayerViewModel.brightness.observe(mLifecycleOwner,
                aFloat -> mVBrightnessText.setText(FormatBrightness(aFloat)));

        mPlayerViewModel.duration.observe(mLifecycleOwner, aLong -> {
            mVSeekBar.setMax(Math.toIntExact(aLong));
            mVEndTimeText.setText(FormatTime(aLong));
        });

        mPlayerViewModel.currentPosition.observe(mLifecycleOwner, aLong -> {
            mVSeekBar.setProgress(Math.toIntExact(aLong));
            mVFastForwardCurrentText.setText(FormatTime(aLong));
            mVCurrentTimeText.setText(FormatTime(aLong));
        });

        mPlayerViewModel.newPosition.observe(mLifecycleOwner,
                aLong -> mVFastForwardTargetText.setText(FormatTime(aLong)));

        mPlayerViewModel.seekProcess.observe(mLifecycleOwner,
                aLong -> mVFastForwardText.setText(FormatTime(aLong)));

        mPlayerViewModel.bufferPercent.observe(mLifecycleOwner,
                percent -> mVSeekBar.setSecondaryProgress(mVSeekBar.getMax() * percent / 100));

        mPlayerViewModel.playerStatus.observe(mLifecycleOwner, playerStatus -> {
            mVPlayBtn.setEnabled(true);
            switch (playerStatus) {
                case Status.STATE_IDLE:
                    mVPlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_36);
                    break;
                case Status.STATE_PAUSED:
                    mVPlayBtn.setImageResource(R.drawable.ic_baseline_pause_36);
                    break;
                case Status.STATE_COMPLETED:
                    mVPlayBtn.setImageResource(R.drawable.ic_baseline_replay_36);
                    break;
                case Status.STATE_PREPARING:
                    mVPlayBtn.setEnabled(false);
                    break;
                default:
                    mVPlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_36);
            }
        });

        /*--------------------------------设置界面显示的观察------------------------*/
        mPlayerViewModel.playButtonLotVisibility.observe(mLifecycleOwner,
                visibility -> mVPlayButtonLot.setVisibility(visibility));
        mPlayerViewModel.loadingLotVisibility.observe(mLifecycleOwner,
                visibility -> mVLoadingLot.setVisibility(visibility));
        mPlayerViewModel.controlLotVisibility.observe(mLifecycleOwner, visibility -> {
            mVTopControllerLot.setVisibility(visibility);
            mVBottomControllerLot.setVisibility(visibility);
        });
        mPlayerViewModel.touchGesturesShowLotVisibility.observe(mLifecycleOwner,
                visibility -> mVTouchGesturesShowLot.setVisibility(visibility));
        mPlayerViewModel.volumeLotVisibility.observe(mLifecycleOwner,
                visibility -> mVVolumeLot.setVisibility(visibility));
        mPlayerViewModel.brightnessLotVisibility.observe(mLifecycleOwner,
                visibility -> mVBrightnessLot.setVisibility(visibility));
        mPlayerViewModel.fastForwardLotVisibility.observe(mLifecycleOwner,
                visibility -> mVFastForwardLot.setVisibility(visibility));
        mPlayerViewModel.streamSelectLotVisibility.observe(mLifecycleOwner,
                visibility -> mVStreamSelectLot.setVisibility(visibility));

        // 绑定SurfaceHolder和播放器
        mVPlayerSurface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) { }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                mPlayerViewModel.getMediaPlayer().setDisplay(holder);
                mPlayerViewModel.getMediaPlayer().setScreenOnWhilePlaying(true);
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) { }
        });

        /*------------------------------设置点击和滑动事件----------------------------*/
        mVPlayerLot.setOnTouchListener(mOnTouchListener);
//        mVControlLot.setOnTouchListener(mOnTouchListener);
        mVPlayButtonLot.setOnClickListener(mOnClickListener);
        mVBackBtn.setOnClickListener(mOnClickListener);
        mVMenuBtn.setOnClickListener(mOnClickListener);
        mVPlayBtn.setOnClickListener(mOnClickListener);
        mVStreamCheckBtn.setOnClickListener(mOnClickListener);
        mVFullscreenBtn.setOnClickListener(mOnClickListener);
        mVSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        // 更新屏幕方向变化
        if (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) { // 当前为横屏
            mVFullscreenBtn.setImageResource(R.drawable.ic_baseline_fullscreen_exit_36);
        } else {
            mVFullscreenBtn.setImageResource(R.drawable.ic_baseline_fullscreen_36);
        }
    }


    /**
     * 修改播放界面大小
     * @param width 宽度
     * @param height 高度
     */
    private void resizePlayer(int width, int height) {
        if (width == 0 || height == 0) return;
        mVPlayerSurface.setLayoutParams(new FrameLayout.LayoutParams(
                mVPlayerSurface.getHeight() * width / height,
                FrameLayout.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
        ));
    }

    /**
     * 隐藏系统UI
     */
    private void hideSystemUI() {
        ViewCompat.getWindowInsetsController(this).hide(WindowInsets.Type.systemBars());
//        View decorView = mActivity.getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    /**
     * 获取界面方向
     */
    public int getScreenOrientation() {
        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;

        if ((rotation == Surface.ROTATION_0 // if the device's natural orientation is portrait:
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        } else { // if the device's natural orientation is landscape or if the device is square:
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }

    /**
     * 声音大小格式化显示
     */
    private String FormatVolume(Integer volume) {
        int i = (int) (volume * 1.0 / mPlayerViewModel.getMaxVolume() * 100);
        if (i == 0) {
            return "off";
        } else {
            return i + "%";
        }
    }

    /**
     * 亮度大小格式化显示
     */
    private String FormatBrightness(Float aFloat) {
        return ((int) (aFloat * 100)) + "%";
    }

    /**
     * 下载速度格式化显示
     */
    private String FormatSpeed(int size) {
        String showSize = "";
        if (size >= 0 && size < 1024) {
            showSize = size + "Kb/s";
        } else if (size >= 1024 && size < (1024 * 1024)) {
            showSize = Long.toString(size / 1024) + "KB/s";
        } else if (size >= (1024 * 1024) && size < (1024 * 1024 * 1024)) {
            showSize = Long.toString(size / (1024 * 1024)) + "MB/s";
        }
        return showSize;
    }

    /**
     * 时长格式化显示
     */
    @SuppressLint("DefaultLocale")
    private String FormatTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = Math.abs(totalSeconds % 60);
        int minutes = Math.abs((totalSeconds / 60) % 60);
        int hours = Math.abs(totalSeconds / 3600);
        if (time >= 0) {
            return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
        } else {
            return hours > 0 ? String.format("-%02d:%02d:%02d", hours, minutes, seconds) : String.format("-%02d:%02d", minutes, seconds);
        }
    }
}