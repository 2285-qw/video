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

        // ?????????view??????popupMenu???????????????view
        popupMenu = new PopupMenu(mContext, imageView1);
        // ??????????????????
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


        // ???????????????????????????????????????????????????????????????
        finalPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // ???????????????item???????????????
                switch (item.getItemId()) {
                    case R.id.js:
                        //??????
                        stDialog(list.get(position).getUrl(),position);
                        break;
                    case R.id.delect:
                        //??????
                        showSecurityDialog(list.get(position).getUrl(),position);
                        break;

                }
                return true;
            }
        });


        return view;
    }

    /**
     * 60    * ???????????? ??????:???;??? HH:mm:ss
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
        //??????menu????????????
        if (menu != null) {
            try {
                //???????????????,???????????????menu???setOptionalIconsVisible??????
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                //?????????????????????
                method.setAccessible(true);
                //?????????????????????icon
                method.invoke(menu, flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    //??????
    private void showSecurityDialog( String fileurl, int p) {
        //TODO ?????????????????????
        Dialog securityDialog = new Dialog(mContext);
        securityDialog.setCancelable(false);//?????????????????????
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
                Toast.makeText(mContext, "???????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "??????????????????", Toast.LENGTH_SHORT).show();

                    updateFileFromDatabase(mContext, file);

                    //????????????????????????
                    liteOrm.delete(list.get(p));

                    list.remove(p);
                    notifyDataSetChanged();

                    new spAdapter(list, mContext);

                } else {
                    Toast.makeText(mContext, "??????????????????", Toast.LENGTH_SHORT).show();
                }


            }
        });

        securityDialog.setContentView(view);
        securityDialog.show();

    }


    //????????????
    private void stDialog(String fileurl, int p) {
        //TODO ?????????????????????
        Dialog securityDialog = new Dialog(mContext);
        securityDialog.setCancelable(false);//?????????????????????
        securityDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(mContext, R.layout.dialog_js, null);

        Button f = view.findViewById(R.id.f);
        Button t = view.findViewById(R.id.t);


        f.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                securityDialog.dismiss();
                Toast.makeText(mContext, "????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });

        t.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                securityDialog.dismiss();

                List name = Arrays.asList(fileurl.split("/"));

                //?????????
                Log.d("name", name.get(name.size() - 1) + "");

                //????????????????????????
                String newname = name.get(name.size() - 1).toString().substring(1);


                //??????????????????
                String route = getPathFromFilepath(fileurl);


                //??????????????????????????????????????????
                String newurl = makePath(route, newname);
                Log.d("newmane", newurl);

                //???????????????
                boolean b = renameFile(fileurl, newurl);
                Log.d("ffff", "fileurl" + fileurl + "newname" + newurl);
                if (b) {
                    //???????????????????????????
                    File file = new File(fileurl);
                    updateFileFromDatabase(mContext, file);
                    //????????????????????????
                    liteOrm.delete(list.get(p));


                    Toast.makeText(mContext, "????????????????????????", Toast.LENGTH_SHORT).show();
                    list.remove(p);

                    notifyDataSetChanged();
                    //?????????????????????
                    mediaScan(new File(newurl), mContext);
                } else {
                    Toast.makeText(mContext, "????????????????????????", Toast.LENGTH_SHORT).show();
                }


            }
        });

        securityDialog.setContentView(view);
        securityDialog.show();

    }

}
