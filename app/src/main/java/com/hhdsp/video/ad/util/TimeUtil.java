package com.hhdsp.video.ad.util;

import android.content.Context;
import android.util.Log;

import com.hhdsp.video.utils.shareUtils;

public class TimeUtil {

    //时间间隔
    public static final long TimeInterval = 1200000;

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

}
