package com.ciee.cau.decryptPlayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author 陈明涛 Email:cmt96@foxmail.com
 * @version V1.0
 * @Description: 视频信息集合（单例）
 * @Date 2021/6/29 15:12
 */
public class VideoLab {
    private static VideoLab sVideoLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static VideoLab get(Context context) {
        if (sVideoLab == null) {
            synchronized (VideoLab.class) {
                if (sVideoLab == null) {
                    sVideoLab = new VideoLab(context);
                }
            }
        }
        return sVideoLab;
    }

    private VideoLab(Context context) {
        mContext = context.getApplicationContext();
//        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public List<Video> getVideos() {
        List<Video> videos = new ArrayList<>();

        for(int i = 0; i < 100; i++) {
            Video video = new Video();
            video.setId(i);
            video.setImagePath(mContext.getDir("Voice", MODE_PRIVATE).getAbsolutePath() + File.separator + "1.png");
            video.setTitle("视频  " + i);
            video.setSubtitle(i + " " + i + " "+ i);
            video.setLength(i * i * 1000);
            video.setUrl("no");
            videos.add(video);
        }

        //todo: 从数据库中获取视频信息
        return videos;
    }
}
