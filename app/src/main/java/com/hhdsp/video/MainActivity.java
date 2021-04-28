package com.hhdsp.video;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.navigation.NavigationView;
import com.hhdsp.video.ad.util.BannerUtil;
import com.hhdsp.video.ad.util.InteractionUtil;
import com.hhdsp.video.databinding.ActivityMainBinding;
import com.hhdsp.video.utils.LogUtils;
import com.hhdsp.video.utils.Material;
import com.hhdsp.video.utils.StaticClass;
import com.hhdsp.video.utils.shareUtils;
import com.hhdsp.video.view.BaseActivity;
import com.hhdsp.video.view.WebViewActivity;
import com.hhdsp.video.view.smwjActivity;
import com.hhdsp.video.view.spxqActivity;
import com.umeng.commonsdk.debug.UMLogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.hhdsp.video.ad.util.TimeUtil.BannerTime;
import static com.hhdsp.video.ad.util.TimeUtil.OnTime;

public class MainActivity extends BaseActivity<ActivityMainBinding> {


    private long mLastClickTime;
    private long timeInterval = 1000;
    Map<String, List<Material>> map;
    InteractionUtil interactionUtil;
    List<Material> list;
    BannerUtil bannerUtil;

    // 要申请的权限
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void init(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewBinding.include.toolbar.setElevation(0);
        }

        if (shareUtils.getBoolean(this, StaticClass.NOT_NOTICE, true)) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            dialog.setTitle("权限申请说明");
            dialog.setMessage("存储权限：用于导入视频，添加私密视频");
            dialog.setPositiveButton("继续",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // 检查该权限是否已经获取
                                int i = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]);

                                int V = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]);
                                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                                if (i != PackageManager.PERMISSION_GRANTED || V != PackageManager.PERMISSION_GRANTED) {
                                    // 如果没有授予该权限，就去提示用户请求
                                    ActivityCompat.requestPermissions(MainActivity.this, permissions, 0x11);
                                }
                            }

                        }
                    });
            dialog.setNegativeButton("拒绝",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "无储存权限无法使用此应用", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
            dialog.show();
            // 显示


        }


        //mDrawerLayout与mToolbar关联起来
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, viewBinding.drawerLayout,
                viewBinding.include.toolbar, R.string.drawer_open, R.string.drawer_close);


        //初始化状态
        actionBarDrawerToggle.syncState();
        //ActionBarDrawerToggle implements DrawerLayout.DrawerListener
        viewBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);

        viewBinding.include.toolbarTitle.setText("文件夹");

        viewBinding.navigationView.setItemIconTintList(null);
        viewBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                long nowTime = System.currentTimeMillis();
                if (nowTime - mLastClickTime > timeInterval) { // 单次点击事件
                    switch (item.getItemId()) {
                        case R.id.nav_collect:
                            startActivity(new Intent(MainActivity.this, smwjActivity.class));
                            break;
                        case R.id.nav_about:
                            //隐私政策
                            WebViewActivity.openActivity(MainActivity.this, "http://huihaoda.cn/yinsi/");
                            break;
                        case R.id.nav_gallery:
                            //用户协议
                            WebViewActivity.openActivity(MainActivity.this, "http://huihaoda.cn/yhxy/");
                            break;
                        case R.id.nav_manage1:
                            // 获取packagemanager的实例
                            PackageManager packageManager = getPackageManager();
                            // getPackageName()是你当前类的包名，0代表是获取版本信息
                            PackageInfo packInfo = null;
                            try {
                                packInfo = packageManager.getPackageInfo(getPackageName(), 0);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            String version = packInfo.versionName;
                            //用户协议
                            Toast.makeText(MainActivity.this, "当前版本" + version + "已是最新版本", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    mLastClickTime = nowTime;
                }
                return false;
            }
        });

        viewBinding.mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long nowTime = System.currentTimeMillis();
                if (nowTime - mLastClickTime > timeInterval) { // 单次点击事件
                    Set KeySet = map.keySet();
                    List KeyList = new ArrayList();
                    for (Object key : KeySet) {
                        KeyList.add(key);
                    }
                    map.get(KeyList.get(position));
                    spxqActivity.openActivity(MainActivity.this, KeyList.get(position) + "", map.get(KeyList.get(position)));
                }
                mLastClickTime = nowTime;
            }
        });

        //加载差屏广告
        if (OnTime(this)) {
            interactionUtil = new InteractionUtil();
            interactionUtil.loadExpressAd("946057085", 300, 300, this);
        }

        if (BannerTime(this)) {
            //加载Banner广告
            bannerUtil = new BannerUtil();
            bannerUtil.loadExpressAd("946061392", 600, 90, findViewById(R.id.express_container), this);

        }
    }



    //是否获取权限判断
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x11) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //用户拒绝权限
                    Toast.makeText(this, "没有储存权限无法使用此产品", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //获取权限成功提示，可以不要
                    shareUtils.putBoolean(MainActivity.this, StaticClass.NOT_NOTICE, false);
                    Toast toast = Toast.makeText(this, "获取权限成功", Toast.LENGTH_LONG);
                    toast.show();
                    map = StaticClass.getMap(this);
                    viewBinding.mlistview.setAdapter(new mainList(map, this));
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 检查该权限是否已经获取
        int i = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]);
        int V = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]);
        // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
        if (i != PackageManager.PERMISSION_DENIED && V != PackageManager.PERMISSION_DENIED) {
            map = StaticClass.getMap(this);
            viewBinding.mlistview.setAdapter(new mainList(map, this));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!(interactionUtil == null)) {
            interactionUtil.onDestroyAd();
        }

        if (!(bannerUtil == null)) {
            bannerUtil.deDestroy();
        }


    }
}