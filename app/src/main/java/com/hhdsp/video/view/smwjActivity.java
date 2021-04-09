package com.hhdsp.video.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.TextView;
import android.widget.Toast;

import com.hhdsp.video.databinding.ActivitySmwjBinding;
import com.hhdsp.video.utils.CustomClickListener;

/**
 * Time:         2021/4/7
 * Author:       C
 * Description:  smwjActivity
 * on:
 */
public class smwjActivity extends BaseActivity<ActivitySmwjBinding> {
    @Override
    protected void init(Bundle savedInstanceState) {
        viewBinding.include.toolbarTitle.setText("私密文件夹");
        viewBinding.include.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewBinding.button.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                Toast.makeText(smwjActivity.this, "点击按钮", Toast.LENGTH_SHORT).show();
                Log.d("ttt", "ttt");
            }
        });
        //初始化设置button不可点击
        viewBinding.button.setEnabled(false);

        viewBinding.exid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("count", s.toString().length() + "");
                //设置button可点击
                if (s.toString().length() == 3) {
                    viewBinding.button.setEnabled(true);
                } else {
                    viewBinding.button.setEnabled(false);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public class MyOnClick extends CustomClickListener {
        @Override
        public void onSingleClick(View v) {

        }
    }


}
