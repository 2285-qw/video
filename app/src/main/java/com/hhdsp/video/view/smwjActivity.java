package com.hhdsp.video.view;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.hhdsp.video.databinding.ActivitySmwjBinding;
import com.hhdsp.video.utils.CustomClickListener;
import com.hhdsp.video.utils.Material;
import com.hhdsp.video.utils.StaticClass;

import java.util.List;

/**
 * Time:         2021/4/7
 * Author:       C
 * Description:  smwjActivity
 * on:
 */
public class smwjActivity extends BaseActivity<ActivitySmwjBinding> {

    private long mLastClickTime;
    private long timeInterval = 1000;

    public String password = "password";

    @Override
    protected void init(Bundle savedInstanceState) {

        //设置返回键
        viewBinding.include.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewBinding.include.toolbarTitle.setText("私密文件夹");

        if (StaticClass.fileIsExists(password, this)) {
            //文件已存在
            viewBinding.lldl.setVisibility(View.VISIBLE);

            //初始化设置button不可点击
            viewBinding.button1.setEnabled(false);

            //输入框监听
            viewBinding.exid1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.d("count", s.toString().length() + "");
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //设置button可点击
                    viewBinding.tishi.setVisibility(View.GONE);
                    if (s.toString().length() == 4) {
                        viewBinding.button1.setEnabled(true);
                    } else {
                        viewBinding.button1.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            //button点击事件
            viewBinding.button1.setOnClickListener(new CustomClickListener() {
                @Override
                public void onSingleClick(View v) {

                    String s = StaticClass.readObjectFromLocal(smwjActivity.this, password);
                    String s2 = viewBinding.exid1.getText().toString().trim();

                    Log.d("s2", s);
                    if (s.equals(s2)) {
                        //登录成功
                        //关闭软键盘
                        viewBinding.lldl.setVisibility(View.GONE);
                        viewBinding.mlistview.setVisibility(View.VISIBLE);


                        //关闭软键盘
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        //得到InputMethodManager的实例
                        if (imm.isActive()) {
                            //如果开启
                            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                            //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
                        }


                        Toast.makeText(smwjActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    } else {
                        //密码错误登录失败
                        viewBinding.tishi.setVisibility(View.VISIBLE);
                        Toast.makeText(smwjActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } else {
            //文件不存在，需要创建
            viewBinding.llcj.setVisibility(View.VISIBLE);
            viewBinding.button.setOnClickListener(new CustomClickListener() {
                @Override
                public void onSingleClick(View v) {
                    StaticClass.writeObjectIntoLocal(smwjActivity.this, password, viewBinding.exid.getText().toString());

                    String s = StaticClass.readObjectFromLocal(smwjActivity.this, password);
                    String s2 = viewBinding.exid.getText().toString().trim();

                    if (s.equals(s2)) {
                        Toast.makeText(smwjActivity.this, "私密保险箱创建成功", Toast.LENGTH_SHORT).show();

                        viewBinding.llcj.setVisibility(View.GONE);
                        viewBinding.mlistview.setVisibility(View.VISIBLE);

                        //关闭软键盘
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        //得到InputMethodManager的实例
                        if (imm.isActive()) {
                            //如果开启
                            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                            //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
                        }

                    } else {
                        Toast.makeText(smwjActivity.this, "私密保险箱创建失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //初始化设置button不可点击
            viewBinding.button.setEnabled(false);

            viewBinding.exid.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.d("count", s.toString().length() + "");
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //设置button可点击
                    if (s.toString().length() == 4) {
                        viewBinding.button.setEnabled(true);
                    } else {
                        viewBinding.button.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        }

        viewBinding.mlistview.setAdapter(new smAdapter(StaticClass.getList(this), this));

        viewBinding.mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long nowTime = System.currentTimeMillis();
                if (nowTime - mLastClickTime > timeInterval) { // 单次点击事件
                    List<Material> list=StaticClass.getList(smwjActivity.this);
                    GSYVideoActivity.openVideoActivity(smwjActivity.this,list,position);

                }
                mLastClickTime = nowTime;
            }

        });

    }


    public class MyOnClick extends CustomClickListener {
        @Override
        public void onSingleClick(View v) {

        }
    }


}
