package com.hhdsp.video.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hhdsp.video.databinding.ActivitySpxqBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Time:         2021/4/9
 * Author:       C
 * Description:  spxqActivity
 * on:
 */
public class spxqActivity extends BaseActivity<ActivitySpxqBinding> {

    public static void openActivity(Context context, String title, List list) {
        Intent intent = new Intent(context, spxqActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("title", title);
        intent.putExtra("list",(Serializable)list);
        context.startActivity(intent);
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

        Toast.makeText(this, "list集合"+(List)getIntent().getSerializableExtra("list"), Toast.LENGTH_SHORT).show();

        viewBinding.mlistview.setAdapter(new spAdapter((List)getIntent().getSerializableExtra("list"),this));

    }
}
