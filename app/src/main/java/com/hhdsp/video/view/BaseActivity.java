package com.hhdsp.video.view;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.gyf.barlibrary.ImmersionBar;
import com.hhdsp.video.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * Time:         2021/4/1
 * Author:       C
 * Description:  BaseActivity
 * on:
 */
public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    protected T viewBinding;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class cls = (Class) type.getActualTypeArguments()[0];
        try {
            Method inflate = cls.getDeclaredMethod("inflate", LayoutInflater.class);
            viewBinding = (T) inflate.invoke(null, getLayoutInflater());
            setContentView(viewBinding.getRoot());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        init(savedInstanceState);

        if (isImmersionBarEnabled()) //初始化沉浸式状态栏,所有子类都将继承这些相同的属性,请在设置界面之后设置
        initImmersionBar();
//        if (isRegisterBus())
//            BusUtils.register(this);


    }

    protected abstract void init(Bundle savedInstanceState);


    /**
     * 是否需要开启沉浸式
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 默认设置状态栏配置(状态栏为粉红色、字体自动变色(须指定状态栏颜色),同时解决状态栏和布局重叠问题)
     */
    private void initImmersionBar() {
        initImmersionBar(R.color.beijng);
    }

    /**
     * 设置其他颜色,主要用于和通用标题栏颜色不符的情况
     */
    protected void initImmersionBar(int color) {
        ImmersionBar.with(this).transparentNavigationBar().statusBarColor(color).autoStatusBarDarkModeEnable(true, 0.2f).fitsSystemWindows(true).init();
        //ImmersionBar.with(this).fitsSystemWindows(true).init();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy();
    }

    // 如果你的app可以横竖屏切换，并且适配4.4或者emui3手机请务必在onConfigurationChanged方法里添加这句话
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ImmersionBar.with(this).init();
    }



}