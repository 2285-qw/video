package com.hhdsp.video;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hhdsp.video.utils.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.hhdsp.video.utils.StaticClass.loadCover;

/**
 * Time:         2021/4/8
 * Author:       C
 * Description:  mainList
 * on:
 */
public class mainList extends BaseAdapter {
    List KeyList;
    Map<String, List<Material>> map;
    Context mContext;

    public mainList(Map map1,Context context) {
        mContext=context;
        map = map1;
        Set KeySet = map.keySet();
        KeyList = new ArrayList();
        for (Object key : KeySet) {
            KeyList.add(key);
        }
        Log.d("KeyList", KeyList + "");
    }

    @Override
    public int getCount() {
        return KeyList.size();
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
        View view=null;
        if (convertView==null){
            view=View.inflate(mContext,R.layout.list_item,null);
        }else {
            view=convertView;
        }

        TextView title=view.findViewById(R.id.title);
        TextView count=view.findViewById(R.id.count);
        title.setText(KeyList.get(position)+"");
        count.setText(map.get(KeyList.get(position)).size()+"个视频");
        Log.d("URL",map.get(KeyList.get(position)).get(0).getUrl1());

        ImageView imageView=view.findViewById(R.id.image);
        loadCover(imageView, map.get(KeyList.get(position)).get(0).getUrl1(), mContext);
        return view;
    }


}
