package com.hhdsp.video.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import static com.hhdsp.video.utils.StaticClass.setFileReleaseNames;
import static com.hhdsp.video.utils.StaticClass.updateFileFromDatabase;

/**
 * Time:         2021/4/9
 * Author:       C
 * Description:  spAdapter
 * on:
 */
public class spAdapter extends BaseAdapter {
    List<Material> list;
    Context mContext;

    public spAdapter(List list1) {
        list = list1;

    }

    public spAdapter(List list1, Context context) {
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

    @SuppressLint("RestrictedApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.list_sp_item, null);
        } else {
            view = convertView;
        }

        TextView name = view.findViewById(R.id.name);
        TextView time = view.findViewById(R.id.time);
        ImageView imageView = view.findViewById(R.id.image);
        ImageView imageView1 = view.findViewById(R.id.image1);
        PopupMenu popupMenu = new PopupMenu(mContext, imageView1);

        name.setText(list.get(position).getName1());

        String times = getTimeShort(list.get(position).getDuration1());
        time.setText(times);
        loadCover(imageView, list.get(position).getUrl1(), mContext);


        // 这里的view代表popupMenu需要依附的view
        popupMenu = new PopupMenu(mContext, imageView1);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_sp, popupMenu.getMenu());


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

        String fileurl = list.get(position).getUrl1();
        String nametitle = list.get(position).getName1();
        long date = list.get(position).getDuration1();
        File file = new File(fileurl);
        // 通过上面这几行代码，就可以把控件显示出来了
        finalPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()) {
                    case R.id.remove:
                        //重命名按钮
                        cmmDialog(fileurl, nametitle, position);

                        break;
                    case R.id.delect:
                        //删除文件按钮
                        Log.d("rrrrr", file.exists() + "");
                        showSecurityDialog("删除文件", "此文件将被永久删除", fileurl, position);

                        break;
                    case R.id.sd:
                        //私密文件夹按钮
                        stDialog(fileurl, position, date);
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
    private void showSecurityDialog(String title1, String ts1, String fileurl, int p) {
        //TODO 显示提醒对话框
        Dialog securityDialog = new Dialog(mContext);
        securityDialog.setCancelable(false);//返回键也会屏蔽
        securityDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(mContext, R.layout.dialog_tishi, null);
        TextView title = view.findViewById(R.id.title);
        TextView ts = view.findViewById(R.id.ts);
        Button f = view.findViewById(R.id.f);
        Button t = view.findViewById(R.id.t);
        title.setText(title1);
        ts.setText(ts1);

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

    //锁定私密文件夹
    private void stDialog(String fileurl, int p, long date) {
        //TODO 显示提醒对话框
        Dialog securityDialog = new Dialog(mContext);
        securityDialog.setCancelable(false);//返回键也会屏蔽
        securityDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(mContext, R.layout.dialog_st, null);

        Button f = view.findViewById(R.id.f);
        Button t = view.findViewById(R.id.t);


        f.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                securityDialog.dismiss();
                Toast.makeText(mContext, "您已取消了加入私密文件夹", Toast.LENGTH_SHORT).show();
            }
        });

        t.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                securityDialog.dismiss();

                List name = Arrays.asList(fileurl.split("/"));

                //文件名
                Log.d("name", name.get(name.size() - 1) + "");

                //文件名前加点隐藏文件得到新的文件名
                String newname = "." + name.get(name.size() - 1);


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

                    TestModel testModel = new TestModel();
                    testModel.setUrl(newurl);
                    testModel.setDate(date);
                    liteOrm.insert(testModel);


                    Toast.makeText(mContext, "私密文件添加成功", Toast.LENGTH_SHORT).show();
                    list.remove(p);

                    notifyDataSetChanged();
                    //添加更新媒体库
                    mediaScan(new File(newurl), mContext);
                } else {
                    Toast.makeText(mContext, "私密文件添加失败", Toast.LENGTH_SHORT).show();
                }


            }
        });

        securityDialog.setContentView(view);
        securityDialog.show();

    }

    //文件重命名
    private void cmmDialog(String fileurl, String name, int p) {
        //TODO 显示提醒对话框
        Dialog securityDialog = new Dialog(mContext);
        securityDialog.setCancelable(false);//返回键也会屏蔽
        securityDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(mContext, R.layout.dialog_cmm, null);
        EditText newname = view.findViewById(R.id.newname);
        newname.setText(name);
        Button f = view.findViewById(R.id.f);
        Button t = view.findViewById(R.id.t);


        f.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                securityDialog.dismiss();
                Toast.makeText(mContext, "您已取消文件重命名", Toast.LENGTH_SHORT).show();
            }
        });

        t.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                securityDialog.dismiss();
                Log.d("newname", newname.getText() + "1111");
                if (newname.getText().equals(null)) {
                    Toast.makeText(mContext, "重名名文件不为空", Toast.LENGTH_SHORT).show();
                } else {
                    //得到文件所在路径

                    List suffix = Arrays.asList(fileurl.split("\\."));

                    Log.d("suffix", suffix.get(suffix.size() - 1) + "");

                    //得到需要修改成的文件名
                    String tnewname = newname.getText().toString().trim() + "." + suffix.get(suffix.size() - 1);

                    //对传入的文件名进行判断得到最终文件名
                    String mnewname = setFileReleaseNames(tnewname, getPathFromFilepath(fileurl));

                    //对文件重命名
                    boolean b = renameFile(fileurl, makePath(getPathFromFilepath(fileurl), mnewname));

                    //删除媒体库添加更新
                    File file = new File(fileurl);
                    updateFileFromDatabase(mContext, file);
                    if (b) {
                        //重命名成功
                        File file1 = new File(makePath(getPathFromFilepath(fileurl), mnewname));

                        //更新媒体库
                        mediaScan(file1, mContext);
                        mediaScan(new File(fileurl), mContext);

                        list.get(p).setUrl1(makePath(getPathFromFilepath(fileurl), mnewname));
                        list.get(p).setName1(mnewname);

                        notifyDataSetChanged();
                        Toast.makeText(mContext, "文件重命名成功", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });

        securityDialog.setContentView(view);
        securityDialog.show();

    }


}
