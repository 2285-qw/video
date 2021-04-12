package com.hhdsp.video.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hhdsp.video.R;
import com.hhdsp.video.databinding.ActivitySpxqBinding;

import java.io.Serializable;
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
        intent.putExtra("list", (Serializable) list);
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

        viewBinding.mlistview.setAdapter(new spAdapter((List) getIntent().getSerializableExtra("list"), this));

    }


    public void Dialog1(){
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(spxqActivity.this);
        normalDialog.setIcon(R.mipmap.ic_launcher);
        normalDialog.setTitle("我是一个普通Dialog");
        normalDialog.setMessage("你要点击哪一个按钮呢?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(spxqActivity.this,"点击了确认", Toast.LENGTH_SHORT).show();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(spxqActivity.this,"点击了关闭", Toast.LENGTH_SHORT).show();
                    }
                });
        // 显示
        normalDialog.show();
    }

}


