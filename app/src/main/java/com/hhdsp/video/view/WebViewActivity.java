package com.hhdsp.video.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hhdsp.video.databinding.ActivityWebViewBinding;

/**
 * Time:         2021/4/15
 * Author:       C
 * Description:  WebviewActivity
 * on:
 */
public class WebViewActivity extends BaseActivity<ActivityWebViewBinding> {

    public static void openActivity(Context context, String url) {
        Log.d("URL",url);
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        String url = getIntent().getStringExtra("url");

        viewBinding.webView.setProgressBar(viewBinding.pbWebBase);
        viewBinding.webView.loadUrl(url);

    }
}
