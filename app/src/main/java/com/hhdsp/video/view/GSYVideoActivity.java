package com.hhdsp.video.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.hhdsp.video.R;
import com.hhdsp.video.utils.Material;
import com.hhdsp.video.utils.TestModel;
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.io.Serializable;
import java.util.List;


/**
 * Time:         2021/4/9
 * Author:       C
 * Description:  GSYVideoActivity
 * on:  视频播放器
 */
public class GSYVideoActivity extends GSYBaseActivityDetail<StandardGSYVideoPlayer> {
    StandardGSYVideoPlayer detailPlayer;
    String source1 = "https://vd3.bdstatic.com/mda-mctd1i9e7vuvugr9/1080p/cae_h264/1616894359/mda-mctd1i9e7vuvugr9.mp4?v_from_s=gz_haokan_4469&amp;auth_key=1616991828-0-0-0dfeae51c6a07f4dc266aab2017ffb99&amp;bcevod_channel=searchbox_feed&amp;pd=1&amp;pt=3&amp;abtest=3000159_2";
    int count;
    List<Material> list;
    List<TestModel> list1;

    public static void openVideoActivity(Context context,List list,int count ,String b) {
        Intent intent = new Intent(context, GSYVideoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("list", (Serializable) list);
        intent.putExtra("count",count);
        intent.putExtra("b",b);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsyvideo);
        detailPlayer = findViewById(R.id.videoView);

        resolveNormalVideoUI();

        //普通模式
        initVideo();


        detailPlayer.setIsTouchWiget(true);
        //关闭自动旋转
        detailPlayer.setRotateViewAuto(false);
        detailPlayer.setLockLand(false);
        detailPlayer.setShowFullAnimation(false);
        //detailPlayer.setNeedLockFull(true);
        detailPlayer.setAutoFullWithSize(false);

        detailPlayer.setVideoAllCallBack(this);

        //设置返回键
        detailPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置返回按键功能
        detailPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });

        detailPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });

        count=getIntent().getIntExtra("count",1);
        if (getIntent().getStringExtra("b").equals("sm")){
            list1= (List<TestModel>) getIntent().getSerializableExtra("list");
            Log.d("tagg",list1.size()+"----"+list1.get(count).getUrl());
            Log.d("list11111",list1.get(count).getUrl()+"");
            detailPlayer.setUp(list1.get(count).getUrl(),true,"ee");
        }else {
            list=(List<Material>) getIntent().getSerializableExtra("list");
            detailPlayer.setUp(list.get(count).getUrl1(),true,list.get(count).getName1());
        }


        detailPlayer.startPlayLogic();

    }

    @Override
    public StandardGSYVideoPlayer getGSYVideoPlayer() {
        return detailPlayer;
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
        return true;
    }


    private void resolveNormalVideoUI() {
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        detailPlayer.getBackButton().setVisibility(View.VISIBLE);
    }


    @Override
    protected void onResume() {
        //去掉状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onResume();
    }

}
