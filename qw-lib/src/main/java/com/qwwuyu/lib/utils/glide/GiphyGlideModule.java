package com.qwwuyu.lib.utils.glide;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

@GlideModule
public final class GiphyGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置内存缓存占用八分之一
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(memoryCacheSize));
        //设置磁盘缓存大小和位子
        File cacheDir = context.getExternalCacheDir();
        int diskCacheSize = 1024 * 1024 * 30;
        builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), "glide", diskCacheSize));
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "glide", diskCacheSize));
//        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "glide", diskCacheSize));
        //基本设置
        RequestOptions options = new RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888);
        builder.setDefaultRequestOptions(options);
        Log.i("asdasd", "applyOptions");
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
        Log.i("asdasd", "registerComponents");
    }

    @Override
    public boolean isManifestParsingEnabled() {
        Log.i("asdasd", "isManifestParsingEnabled");
        return false;
    }
}
