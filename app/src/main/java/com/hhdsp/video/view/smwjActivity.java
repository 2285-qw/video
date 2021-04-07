package com.hhdsp.video.view;

import android.os.Bundle;
import android.view.View;

import com.hhdsp.video.databinding.ActivitySmwjBinding;

/**
 * Time:         2021/4/7
 * Author:       C
 * Description:  smwjActivity
 * on:
 */
public class smwjActivity extends BaseActivity<ActivitySmwjBinding>{
    @Override
    protected void init(Bundle savedInstanceState) {
        viewBinding.include.toolbarTitle.setText("私密文件夹");
        viewBinding.include.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
