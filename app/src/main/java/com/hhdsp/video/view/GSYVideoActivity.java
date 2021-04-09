package com.hhdsp.video.view;

import android.os.Bundle;

import com.hhdsp.video.R;
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * Time:         2021/4/9
 * Author:       C
 * Description:  GSYVideoActivity
 * on:  视频播放器
 */
public class GSYVideoActivity extends GSYBaseActivityDetail<StandardGSYVideoPlayer> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsyvideo);
    }



    @Override
    public StandardGSYVideoPlayer getGSYVideoPlayer() {
        return null;
    }

    @Override
    public GSYVideoOptionBuilder getGSYVideoOptionBuilder() {
        return null;
    }

    @Override
    public void clickForFullScreen() {

    }

    @Override
    public boolean getDetailOrientationRotateAuto() {
        return false;
    }
}
