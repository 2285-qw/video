package com.hhdsp.video.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    //闪屏业延时名
    public static final int HANDLER_SPLASH = 1001;
    //判断程序是否是第一次运行
    public static final String SPLASH_IS_FIRST = "isFIRST";

    //获取手机里的视频文件
    public static Map<String, List<Material>> getMap(Context context) {
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

                    if (!(title.indexOf(".") == 0)) {
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

    //将密码存到本地
    public static boolean writeObjectIntoLocal(Context context, String fileName, String passwod) {
        try {
            // 通过openFileOutput方法得到一个输出流，方法参数为创建的文件名（不能有斜杠），操作模式
            @SuppressWarnings("deprecation")
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(passwod);//写入
            fos.close();//关闭输入流
            oos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(WebviewTencentActivity.this, "出现异常2",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //读取本地密码
    public static String readObjectFromLocal(Context context, String fielName) {
        String bean;
        try {
            FileInputStream fis = context.openFileInput(fielName);//获得输入流
            ObjectInputStream ois = new ObjectInputStream(fis);
            bean = (String) ois.readObject();
            fis.close();
            ois.close();
            return bean;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            //Toast.makeText(ShareTencentActivity.this,"出现异常6",Toast.LENGTH_LONG).show();//弹出Toast消息
            e.printStackTrace();
            return null;
        }
    }

    // 判断文件是否存在
    public static boolean fileIsExists(String strFile, Context context) {
        try {
            File file = context.getFileStreamPath(strFile);
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    //获取手机里的视频文件
    public static List<Material> getList(Context context) {
        List<Material> list = null;
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            if (cursor != null) {
                list = new ArrayList<>();
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

                    if (title.indexOf(".") == 0) {
                        video.setName1(title);
                        video.setSize1(size);
                        video.setUrl1(path);
                        video.setDuration1(duration);
                        Log.d("PATH1", "title" + title + "size" + size + "path" + path + "duration" + duration);

                        list.add(video);
                    }
                }
                cursor.close();

            }
        }
        return list;
    }

    /**
     * @param filepath 文件全路径名称，like mnt/sda/XX.xx
     * @return 根路径，like mnt/sda
     * @Description 得到文件所在路径（即全路径去掉完整文件名）
     */
    public static String getPathFromFilepath(final String filepath) {
        int pos = filepath.lastIndexOf('/');
        if (pos != -1) {
            return filepath.substring(0, pos);
        }
        return "";
    }

    /**
     * @param path1 路径一
     * @param path2 路径二
     * @return 新路径
     * @Description 重新整合路径，将路径一和文件名通过'/'连接起来得到新路径
     */
    public static String makePath(final String path1, final String path2) {
        if (path1.endsWith(File.separator)) {
            return path1 + path2;
        }
        return path1 + File.separator + path2;
    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        file.setExecutable(true, false);
        file.setReadable(true, false);
        file.setWritable(true, false);
        if (file.isFile() && file.exists()) {
            return forceDelete(file);
        }
        return false;
    }

    /**
     * 删除已存储的文件
     */
    public static boolean deletefile(String fileName) {
        try {
            // 找到文件所在的路径并删除该文件
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            file.delete();
            return file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //垃圾回收再次删除
    public static boolean forceDelete(File f) {
        boolean result = false;
        int tryCount = 0;
        while (!result && tryCount++ < 10) {
            System.gc();
            result = f.delete();
        }
        return result;
    }


}
