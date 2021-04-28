package com.hhdsp.video.ad.util;

import android.content.Context;
import android.util.Log;

import com.hhdsp.video.utils.LogUtils;
import com.hhdsp.video.utils.shareUtils;

public class TimeUtil {

    //时间间隔
    public static final long TimeInterval = 1200000;

    public static final String BannerRunTime = "Banner";
    //Banner广告关闭后再次开启广告时间间隔
    public static final long BannerTimeInterval = 600000;

    //广告间隔判断
    public static Boolean OnTime(Context context) {
        Long t = System.currentTimeMillis();
        //获取储存的时间轴
        long time = shareUtils.getLong(context, "UI", t-12000000);
        //计算时间是否大于二十分钟
        int Interval = (int) ((Long) System.currentTimeMillis() - (Long) time - TimeInterval);

        Log.d("time222",Interval+"");
        if (Interval >= 0) {
            //储存当前时间轴
            shareUtils.putLong(context, "UI", t);
            return true;
        } else {
            return false;
        }
    }

    //Banner广告计时
    public static boolean BannerTime(Context context) {
        Long currentTime = System.currentTimeMillis();
        Long pastTime = shareUtils.getLong(context, BannerRunTime, currentTime - 1200000);
        long dif = currentTime - pastTime - BannerTimeInterval;
        LogUtils.d("pastTime"+pastTime);
        LogUtils.d("Dif"+dif);
        if (dif >= 0) {
            return true;
        } else {
            return false;
        }

    }

}
