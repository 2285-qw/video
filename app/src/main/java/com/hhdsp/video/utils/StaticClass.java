package com.hhdsp.video.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Time:         2021/4/7
 * Author:       C
 * Description:  StaticClass
 * on:
 */
public class StaticClass {

    //获取手机里的视频文件
    public static Map<String, List<Material>> getList(Context context) {
        List<Material> list = null;
        Map<String, List<Material>> map = new HashMap<>();
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            if (cursor != null) {

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String album = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                    String artist = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                    String displayName = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    String mimeType = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    long duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                    Material video = new Material();
                    video.setName1(title);
                    video.setSize1(size);
                    video.setUrl1(path);
                    video.setDuration1(duration);
                    Log.d("PATH", "title" + title + "size" + size + "path" + path + "duration" + duration);
                    list = new ArrayList<Material>();
                    List strings = Arrays.asList(path.split("/"));
                    list.add(video);
                    //判断文件夹是否存在
                    if (map.containsKey(strings.get(strings.size() - 2))) {
                        list = map.get(strings.get(strings.size() - 2));
                        list.add(video);
                        map.put((String) strings.get(strings.size() - 2), list);
                    } else {
                        map.put((String) strings.get(strings.size() - 2), list);
                    }

                }
                cursor.close();
            }
        }
        return map;
    }

    /**
     * 加载第四秒的帧数作为封面
     * url就是视频的地址
     */
    public static void loadCover(ImageView imageView, String url, Context context) {

        //Glide设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(20);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        // RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(20, 20);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerCrop()

                )
                .load(url)
                .apply(options)
                .into(imageView);
    }
}