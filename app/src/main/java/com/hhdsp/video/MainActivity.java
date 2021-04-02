package com.hhdsp.video;

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
import com.hhdsp.video.view.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

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

        viewBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_about:
                }
                Toast.makeText(MainActivity.this, "点击了----- $title" + item.getGroupId(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }
}