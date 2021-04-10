package com.hhdsp.video.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhdsp.video.R;
import com.hhdsp.video.utils.Material;

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

        name.setText(list.get(position).getName1().substring(1));

        String times = getTimeShort(list.get(position).getDuration1());

        time.setText(times);
        loadCover(imageView, list.get(position).getUrl1(), mContext);


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

}
