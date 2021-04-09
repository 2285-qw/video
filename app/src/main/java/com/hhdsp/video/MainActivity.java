package com.hhdsp.video;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;

import com.google.android.material.navigation.NavigationView;
import com.hhdsp.video.databinding.ActivityMainBinding;
import com.hhdsp.video.utils.Material;
import com.hhdsp.video.utils.StaticClass;
import com.hhdsp.video.view.BaseActivity;
import com.hhdsp.video.view.smwjActivity;
import com.hhdsp.video.view.spxqActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private long mLastClickTime;
    private long timeInterval = 1000;
    Map<String, List<Material>> map;
    List<Material> list;

    @Override
    protected void init(Bundle savedInstanceState) {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x11);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewBinding.include.toolbar.setElevation(0);
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


    }

    @Override
    protected void onResume() {
        super.onResume();
        map = StaticClass.getList(this);
        viewBinding.mlistview.setAdapter(new mainList(map, this));
    }
}