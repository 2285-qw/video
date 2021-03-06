package com.hhdsp.video.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Magnifier;
import android.widget.Toast;

import com.hhdsp.video.MainActivity;
import com.hhdsp.video.R;
import com.hhdsp.video.ad.util.BannerUtil;
import com.hhdsp.video.databinding.ActivityMainBinding;
import com.hhdsp.video.databinding.ActivitySpxqBinding;
import com.hhdsp.video.utils.Material;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.hhdsp.video.ad.util.TimeUtil.BannerTime;

/**
 * Time:         2021/4/9
 * Author:       C
 * Description:  spxqActivity
 * on:
 */
public class spxqActivity extends BaseActivity<ActivitySpxqBinding> {

    private long mLastClickTime;
    private long timeInterval = 1000;
    spAdapter adapter;
    BannerUtil bannerUtil;

    public static void openActivity(Context context, String title, List list) {
        Intent intent = new Intent(context, spxqActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("title", title);
        intent.putExtra("list", (Serializable) list);
        context.startActivity(intent);
    }

    public static void update() {

    }


    @Override
    protected void init(Bundle savedInstanceState) {
        viewBinding.include.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewBinding.include.toolbarTitle.setText(getIntent().getStringExtra("title"));

        adapter = new spAdapter((List) getIntent().getSerializableExtra("list"), this);
        viewBinding.mlistview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        viewBinding.mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long nowTime = System.currentTimeMillis();
                if (nowTime - mLastClickTime > timeInterval) { // ??????????????????
                    List<Material> list = (List) getIntent().getSerializableExtra("list");
                    GSYVideoActivity.openVideoActivity(spxqActivity.this, list, position, "");

                }
                mLastClickTime = nowTime;
            }
        });

        if (BannerTime(this)) {
            bannerUtil = new BannerUtil();
            bannerUtil.loadExpressAd("946063601", 600, 90, viewBinding.expressContainer, this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!(bannerUtil == null)) {
            bannerUtil.deDestroy();
        }

    }
}


