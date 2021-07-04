package com.ciee.cau.decryptPlayer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ciee.cau.decryptPlayer.R;

import java.util.List;

public class VideoSelectionFragment extends Fragment {

    private VideoAdapter mAdapter;
    /**
     * 视频列表
     */
    private RecyclerView mVideoRecyclerView;
    /**
     * 列表中没有视频时显示的警告界面
     */
    private LinearLayout mNoVideoWarnLot;

    private Toolbar mToolbar;

    public static VideoSelectionFragment newInstance() {
        return new VideoSelectionFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_selection, container, false);

        mToolbar = view.findViewById(R.id.selection_toolbar);
        mToolbar.setOnMenuItemClickListener(item -> {
            startActivity(SettingsActivity.newIntent(getActivity())); // 设置界面
            return true;
        });
        mVideoRecyclerView = view.findViewById(R.id.video_recycle_view);
        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNoVideoWarnLot = view.findViewById(R.id.no_video_warn);

        updateUI();

        return view;
    }



    public void updateUI() {
        VideoLab videoLab = VideoLab.get(getActivity());
        List<Video> videos = videoLab.getVideos();
        if (videos.size() == 0) {
            mVideoRecyclerView.setVisibility(View.GONE);
            mNoVideoWarnLot.setVisibility(View.VISIBLE);
        } else {
            mVideoRecyclerView.setVisibility(View.VISIBLE);
            mNoVideoWarnLot.setVisibility(View.GONE);
        }

        if (mAdapter == null) {
            mAdapter = new VideoAdapter(videos);
            mVideoRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setVideos(videos);
            mAdapter.notifyDataSetChanged();
        }
    }

/*-------------------------------------------------------------------------------------------------*/
    private class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {
        /**
         * 视频信息列表
         */
        private List<Video> mVideos;

        public VideoAdapter(List<Video> videos) { // Adapter构造器
            mVideos = videos;
        }

        /**
         * 设置视频信息列表
         * @param videos
         */
        public void setVideos(List<Video> videos) {
            mVideos = videos;
        }

        @Override
        public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());//加载布局
            return new VideoHolder(layoutInflater, parent); // 创建ViewHolder
        }

        @Override //用于对子项的数据进行赋值，在子项被滚动的屏幕时执行
        public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
            Video video = mVideos.get(position);
            holder.bind(video);
        }

        @Override //用于告诉RecyclerView一共有多少子项
        public int getItemCount() {
            return mVideos.size();
        }
    }

/*-------------------------------------------------------------------------------------------------*/
    class VideoHolder extends RecyclerView.ViewHolder {

        /**
         * 视频信息
         */
        private Video mVideo;
        /**
         * 视频略缩图
         */
        private ImageView mVideoSimpleImage;
        /**
         * 视频标题
         */
        private TextView mVideoTitle;
        /**
         * 视频副标题
         */
        private TextView mVideoSubtitle;
        /**
         * 视频时长
         */
        private TextView mVideoLengthTime;

        public VideoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_video_list, parent, false));
            itemView.setOnClickListener(v -> {
                //todo: 给要播放的视频创建一个单例
                startActivity(PlayerActivity.newIntent(getActivity())); // 播放视频
            });
            mVideoSimpleImage = itemView.findViewById(R.id.simple_image);
            mVideoTitle = itemView.findViewById(R.id.title);
            mVideoSubtitle = itemView.findViewById(R.id.subtitle);
            mVideoLengthTime = itemView.findViewById(R.id.length_time);
        }

        /**
         * 处理ViewHolder显示的数据
         * @param video
         */
        public void bind(Video video) {
            mVideo = video;
//            mVideoSimpleImage.setImageURI(Uri.parse(video.getImagePath())); // todo
            mVideoSimpleImage.setImageResource(R.drawable.ic_baseline_play_arrow_60);
            mVideoTitle.setText(video.getTitle());
            mVideoSubtitle.setText(video.getSubtitle());
            mVideoLengthTime.setText(FormatTime(video.getLength()));
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
            return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
        }
    }
}
