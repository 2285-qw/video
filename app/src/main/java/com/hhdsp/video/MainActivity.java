package com.hhdsp.video;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.navigation.NavigationView;
import com.hhdsp.video.databinding.ActivityMainBinding;
import com.hhdsp.video.utils.Material;
import com.hhdsp.video.utils.StaticClass;
import com.hhdsp.video.view.BaseActivity;
import com.hhdsp.video.view.smwjActivity;

import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    Map<String, List<Material>> map;

    @Override
    protected void init(Bundle savedInstanceState) {

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
                switch (item.getItemId()) {
                    case R.id.nav_collect:
                        startActivity(new Intent(MainActivity.this, smwjActivity.class));
                        break;
                    case R.id.nav_about:
                }
                return false;
            }
        });

        map=StaticClass.getList(this);
    }


}