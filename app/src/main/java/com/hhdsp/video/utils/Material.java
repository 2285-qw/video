package com.hhdsp.video.utils;

import java.io.Serializable;

/**
 * Time:         2021/4/7
 * Author:       C
 * Description:  Material
 * on:
 */
public class Material implements Serializable {
    String name;//名字
    long size;//大小
    String url;//路径
    long duration;//时长

    public String getName1() {
        return name;
    }

    public void setName1(String name) {
        this.name = name;
    }

    public long getSize1() {
        return size;
    }

    public void setSize1(long size) {
        this.size = size;
    }

    public String getUrl1() {
        return url;
    }

    public void setUrl1(String url) {
        this.url = url;
    }

    public long getDuration1() {
        return duration;
    }

    public void setDuration1(long duration) {
        this.duration = duration;
    }

}
