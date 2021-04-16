package com.hhdsp.video.application;

import android.app.Application;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Time:         2020/12/23
 * Author:       C
 * Description:  BaseApplication
 * on:
 */
public class BaseApplication extends Application {
    public static LiteOrm liteOrm;
    @Override
    public void onCreate() {
        super.onCreate();

        //调试 log
        UMConfigure.setLogEnabled(true);
        //初始化sdk
        UMConfigure.init(this,"60793c4e9e4e8b6f6171a42a", "BFQ", UMConfigure.DEVICE_TYPE_PHONE,null);
        //页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        //创建数据库
        if (liteOrm == null) {
            DataBaseConfig config = new DataBaseConfig(this, "sm.db");
            //"liteorm.db"是数据库名称，名称里包含路径符号"/"则将数据库建立到该路径下，可以使用sd卡路径。 不包含则在系统默认路径下创建DB文件。
            //例如 public static final String DB_NAME = SD_CARD + "/lite/orm/liteorm.db";     DataBaseConfig config = new DataBaseConfig(this, DB_NAME);
            config.dbVersion = 1; // set database version
            config.onUpdateListener = null; // set database update listener
            //独立操作，适用于没有级联关系的单表操作，
            liteOrm = LiteOrm.newSingleInstance(config);
            //级联操作,适用于多表级联操作
            // liteOrm=LiteOrm.newCascadeInstance(config);
        }
        liteOrm.setDebugged(true); // open the log


    }
}
