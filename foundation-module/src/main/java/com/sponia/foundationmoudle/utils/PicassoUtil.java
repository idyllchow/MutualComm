package com.sponia.foundationmoudle.utils;

import com.sponia.foundationmoudle.common.Common;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.LruCache;

/**
 * @packageName com.sponia.foundationmoudle.utils
 * @description picasso处理类
 * @date 15/9/9
 * @auther shibo
 */
public class PicassoUtil {
    private static Picasso picasso = null;
    private static LruCache lruCache = null;
    /**返回一个LruCache图片缓存集合*/
    private static LruCache getCache() {
        if (lruCache == null)
            //传入context底层自动调用计算可用运行内存的1/7作为LruCache集合的运行时内存可用值
            lruCache = new LruCache(Common.application);

        return lruCache;
    }
    /**获取一个Picasso对象,以上面的LruCache集合作为内存缓存空间*/
    public static Picasso getPicasso() {
        if (picasso == null)
            picasso = new Picasso.Builder(Common.application).memoryCache(getCache()).build();
        return picasso;
    }
    /**清空LruCache集合*/
    public static void clearCache() {
        getCache().clear();
    }
}