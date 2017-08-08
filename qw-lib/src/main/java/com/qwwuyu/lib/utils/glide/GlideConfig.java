package com.qwwuyu.lib.utils.glide;

import com.bumptech.glide.request.RequestOptions;

/**
 * Created by qiwei on 2017/8/8
 */
public class GlideConfig {
    public final int memoryCacheSize;
    public final int diskCacheSize;
    public final String cacheDir;
    public final RequestOptions options;

    private GlideConfig(Builder builder) {
        memoryCacheSize = builder.memoryCacheSize;
        diskCacheSize = builder.diskCacheSize;
        cacheDir = builder.cacheDir;
        options = builder.options;
    }

    public static final class Builder {
        private int memoryCacheSize;
        private int diskCacheSize;
        private String cacheDir;
        private RequestOptions options;

        public Builder() {
        }

        public Builder memoryCacheSize(int memoryCacheSize) {
            this.memoryCacheSize = memoryCacheSize;
            return this;
        }

        public Builder diskCacheSize(int diskCacheSize) {
            this.diskCacheSize = diskCacheSize;
            return this;
        }

        public Builder cacheDir(String cacheDir) {
            this.cacheDir = cacheDir;
            return this;
        }

        public Builder options(RequestOptions options) {
            this.options = options;
            return this;
        }

        public GlideConfig build() {
            return new GlideConfig(this);
        }
    }
}
