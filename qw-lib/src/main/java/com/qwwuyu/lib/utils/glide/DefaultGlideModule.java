package com.qwwuyu.lib.utils.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

@GlideModule
public final class DefaultGlideModule extends AppGlideModule {
    private static GlideConfig g;

    public static void setGlideConfig(GlideConfig g) {
        DefaultGlideModule.g = g;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        if (g == null) {
            g = new GlideConfig.Builder().build();
        }
        int memoryCacheSize = g.memoryCacheSize == 0 ? (int) (Runtime.getRuntime().maxMemory() / 8) : g.memoryCacheSize;
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(memoryCacheSize));
        int diskCacheSize = g.diskCacheSize == 0 ? 25 * 1024 * 1024 : g.diskCacheSize;
        if (g.cacheDir != null) {
            builder.setDiskCache(new DiskLruCacheFactory(g.cacheDir, diskCacheSize));
        } else {
            builder.setDiskCache(new DiskLruCacheFactory(context.getCacheDir().getPath(), "glide", diskCacheSize));
        }
        if (g.options != null) {
            builder.setDefaultRequestOptions(g.options);
        }
        g = null;
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}