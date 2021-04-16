package com.hhdsp.video.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hhdsp.video.MainActivity;
import com.hhdsp.video.R;
import com.hhdsp.video.databinding.ActivitySplashBinding;
import com.hhdsp.video.utils.StaticClass;
import com.hhdsp.video.utils.shareUtils;

/**
 * Time:         2021/4/12
 * Author:       C
 * Description:  SplashActivity
 * on:
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.HANDLER_SPLASH:
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    break;
            }

        }
    };

    @Override
    protected void init(Bundle savedInstanceState) {

        //判断是否第一次执行
        if (isFirst()) {
            showSecurityDialog();
        } else {
            //延时2秒发送出去
            handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, 2000);
        }

    }

    private void showSecurityDialog() {
        //TODO 显示提醒对话框
        Dialog securityDialog = new Dialog(this);
        securityDialog.setCancelable(false);//返回键也会屏蔽
        securityDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(this, R.layout.dialog_activity_sercurity, null);
        TextView tv_msg = view.findViewById(R.id.sercurity_tv_msgnotice);
        TextView tv_cancel = view.findViewById(R.id.sercurity_tv_cancel);
        TextView tv_positive = view.findViewById(R.id.sercurity_tv_positive);

        SpannableStringBuilder spannable = new SpannableStringBuilder(tv_msg.getText());
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#C89C3C")), 106, 112, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_msg.setMovementMethod(LinkMovementMethod.getInstance());
        spannable.setSpan(new TextClick(), 106, 112, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_msg.setText(spannable);

        SpannableStringBuilder spannable1 = new SpannableStringBuilder(tv_msg.getText());
        spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#C89C3C")), 113, 119, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_msg.setMovementMethod(LinkMovementMethod.getInstance());
        spannable1.setSpan(new TextClick1(), 113, 119, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_msg.setText(spannable1);

        tv_msg.setHighlightColor(Color.parseColor("#00ffffff"));

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                securityDialog.dismiss();
                shareUtils.putBoolean(SplashActivity.this, StaticClass.SPLASH_IS_FIRST, true);
                finish();
            }
        });
        tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                securityDialog.dismiss();
                //TODO 进入主界面
                shareUtils.putBoolean(SplashActivity.this, StaticClass.SPLASH_IS_FIRST, false);

                startActivity(new Intent(SplashActivity.this, MainActivity.class));

                finish();
            }
        });

        securityDialog.setContentView(view);
        securityDialog.show();
    }

    private class TextClick extends ClickableSpan {
        @Override
        public void onClick(View widget) { //在此处理点击事件
            //startActivity(new Intent(SplashActivity.this,yisiActivity.class));
            //TODO 点击事件处理
            //隐私政策
            WebViewActivity.openActivity(SplashActivity.this,"http://huihaoda.cn/yinsi/");

        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            //ds.setColor(Color.parseColor("#C89C3C"));
        }
    }

    private class TextClick1 extends ClickableSpan {
        @Override
        public void onClick(View widget) { //在此处理点击事件
            //用户协议
            WebViewActivity.openActivity(SplashActivity.this,"http://huihaoda.cn/yhxy/");
            //TODO 点击事件处理
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setColor(Color.parseColor("#C89C3C"));
        }
    }

    //判断程序是否第一次运行
    private boolean isFirst() {
        Boolean isFirst = shareUtils.getBoolean(this, StaticClass.SPLASH_IS_FIRST, true);
        if (isFirst) {
            //是第一次运行
            return true;
        } else {
            return false;
        }
    }
}
