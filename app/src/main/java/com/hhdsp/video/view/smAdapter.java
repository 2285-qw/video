package com.hhdsp.video.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hhdsp.video.R;
import com.hhdsp.video.utils.CustomClickListener;
import com.hhdsp.video.utils.Material;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.hhdsp.video.utils.StaticClass.loadCover;

/**
 * Time:         2021/4/10
 * Author:       C
 * Description:  smAdapter
 * on:
 */
public class smAdapter extends BaseAdapter {
    List<Material> list;
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

        name.setText(list.get(position).getName1().substring(1));

        String times = getTimeShort(list.get(position).getDuration1());

        time.setText(times);
        loadCover(imageView, list.get(position).getUrl1(), mContext);



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
                        Toast.makeText(mContext, "开始解锁", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.delect:
                        Toast.makeText(mContext, "开始删除", Toast.LENGTH_SHORT).show();
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
}
