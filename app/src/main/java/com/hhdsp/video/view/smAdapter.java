package com.hhdsp.video.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hhdsp.video.R;
import com.hhdsp.video.utils.CustomClickListener;
import com.hhdsp.video.utils.Material;
import com.hhdsp.video.utils.TestModel;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hhdsp.video.application.BaseApplication.liteOrm;
import static com.hhdsp.video.utils.StaticClass.getPathFromFilepath;
import static com.hhdsp.video.utils.StaticClass.loadCover;
import static com.hhdsp.video.utils.StaticClass.makePath;
import static com.hhdsp.video.utils.StaticClass.mediaScan;
import static com.hhdsp.video.utils.StaticClass.renameFile;
import static com.hhdsp.video.utils.StaticClass.updateFileFromDatabase;

/**
 * Time:         2021/4/10
 * Author:       C
 * Description:  smAdapter
 * on:
 */
public class smAdapter extends BaseAdapter {
    List<TestModel> list;
    Context mContext;

    public smAdapter(List list1, Context context) {
        list = list1;
        mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.list_sm_item, null);
        } else {
            view = convertView;
        }
        TextView name = view.findViewById(R.id.name);
        TextView time = view.findViewById(R.id.time);
        ImageView imageView = view.findViewById(R.id.image);
        ImageView imageView1 = view.findViewById(R.id.image1);
        PopupMenu popupMenu = new PopupMenu(mContext, imageView1);

        List list1= Arrays.asList(list.get(position).getUrl().split("/"));

        name.setText(list1.get(list1.size()-1)+"");

        String times = getTimeShort(list.get(position).getDate());

        time.setText(times);
        loadCover(imageView, list.get(position).getUrl(), mContext);

        Log.d("url...",list.get(position).getUrl());

        // 这里的view代表popupMenu需要依附的view
        popupMenu = new PopupMenu(mContext, imageView1);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_sm, popupMenu.getMenu());


        PopupMenu finalPopupMenu = popupMenu;

        imageView1.setOnClickListener(new CustomClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onSingleClick(View v) {

                Menu menu = finalPopupMenu.getMenu();
                setIconsVisible(menu, true);

                finalPopupMenu.show();
            }
        });


        // 通过上面这几行代码，就可以把控件显示出来了
        finalPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()) {
                    case R.id.js:
                        //解锁
                        stDialog(list.get(position).getUrl(),position);
                        break;
                    case R.id.delect:
                        //删除
                        showSecurityDialog(list.get(position).getUrl(),position);
                        break;

                }
                return true;
            }
        });


        return view;
    }

    /**
     * 60    * 获取时间 小时:分;秒 HH:mm:ss
     * 61    *
     * 62    * @return
     * 63
     */

    public static String getTimeShort(long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String dateString = formatter.format(date);
        Log.d("time", dateString);
        return dateString;
    }


    private void setIconsVisible(Menu menu, boolean flag) {
        //判断menu是否为空
        if (menu != null) {
            try {
                //如果不为空,就反射拿到menu的setOptionalIconsVisible方法
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                //暴力访问该方法
                method.setAccessible(true);
                //调用该方法显示icon
                method.invoke(menu, flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    //删除
    private void showSecurityDialog( String fileurl, int p) {
        //TODO 显示提醒对话框
        Dialog securityDialog = new Dialog(mContext);
        securityDialog.setCancelable(false);//返回键也会屏蔽
        securityDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(mContext, R.layout.dialog_tishi, null);
        TextView title = view.findViewById(R.id.title);
        TextView ts = view.findViewById(R.id.ts);

        Button f = view.findViewById(R.id.f);
        Button t = view.findViewById(R.id.t);


        f.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                securityDialog.dismiss();
                Toast.makeText(mContext, "你已取消了删除文件", Toast.LENGTH_SHORT).show();
            }
        });

        t.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                securityDialog.dismiss();

                Log.d("wwww", fileurl);
                File file = new File(fileurl);
                boolean b = file.delete();

                if (b) {
                    Toast.makeText(mContext, "文件删除成功", Toast.LENGTH_SHORT).show();

                    updateFileFromDatabase(mContext, file);

                    //数据库中删除数据
                    liteOrm.delete(list.get(p));

                    list.remove(p);
                    notifyDataSetChanged();

                    new spAdapter(list, mContext);

                } else {
                    Toast.makeText(mContext, "文件删除失败", Toast.LENGTH_SHORT).show();
                }


            }
        });

        securityDialog.setContentView(view);
        securityDialog.show();

    }


    //解锁文件
    private void stDialog(String fileurl, int p) {
        //TODO 显示提醒对话框
        Dialog securityDialog = new Dialog(mContext);
        securityDialog.setCancelable(false);//返回键也会屏蔽
        securityDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(mContext, R.layout.dialog_js, null);

        Button f = view.findViewById(R.id.f);
        Button t = view.findViewById(R.id.t);


        f.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                securityDialog.dismiss();
                Toast.makeText(mContext, "您已取消解锁文件", Toast.LENGTH_SHORT).show();
            }
        });

        t.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                securityDialog.dismiss();

                List name = Arrays.asList(fileurl.split("/"));

                //文件名
                Log.d("name", name.get(name.size() - 1) + "");

                //删除文件名前的点
                String newname = name.get(name.size() - 1).toString().substring(1);


                //得到文件路径
                String route = getPathFromFilepath(fileurl);


                //新名字和路径合并得到新路径名
                String newurl = makePath(route, newname);
                Log.d("newmane", newurl);

                //文件重命名
                boolean b = renameFile(fileurl, newurl);
                Log.d("ffff", "fileurl" + fileurl + "newname" + newurl);
                if (b) {
                    //删除媒体库添加更新
                    File file = new File(fileurl);
                    updateFileFromDatabase(mContext, file);
                    //数据库中删除数据
                    liteOrm.delete(list.get(p));


                    Toast.makeText(mContext, "私密文件解锁成功", Toast.LENGTH_SHORT).show();
                    list.remove(p);

                    notifyDataSetChanged();
                    //添加更新媒体库
                    mediaScan(new File(newurl), mContext);
                } else {
                    Toast.makeText(mContext, "私密文件解锁失败", Toast.LENGTH_SHORT).show();
                }


            }
        });

        securityDialog.setContentView(view);
        securityDialog.show();

    }

}
