package test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

@RunWith(AndroidJUnit4.class)
public class GlideTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context context = InstrumentationRegistry.getTargetContext();
        ImageView imageView = new ImageView(null);
        Glide.with(context);// 绑定Context Activity FragmentActivity Fragment
        Glide.with(context).load("imageUrl").into(imageView);

        RequestOptions options = new RequestOptions()
                .centerCrop()//centerCrop
                .placeholder(-1)//加载中图片
                .error(-1)//加载失败图片
                .transform(new CropCircleTransformation(context))//加载圆形图片
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.ALL)//设置磁盘缓存策略

                .priority(Priority.HIGH);//设置下载优先级

        Glide.with(context).load("imageUrl").apply(options)
                .thumbnail(0.1f)//加载缩略图
                .transition(DrawableTransitionOptions.withCrossFade(200))//加载动画
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })//加载监听
                .into(imageView);

        RequestOptions myOptions = new RequestOptions().fitCenter().override(100, 100);
        Glide.with(context)
                .asBitmap().apply(myOptions).load("imageUrl")
                .transition(BitmapTransitionOptions.withCrossFade(200))//加载动画
                .into(imageView);

        Bitmap bitmap = Glide.with(context).asBitmap().submit(400, 400).get();

        Glide.with(context).clear(imageView);

        Glide.get(context).clearDiskCache();//清理磁盘缓存 需要在子线程中执行
        Glide.get(context).clearMemory();//清理内存缓存  可以在UI主线程中进行

        GlideBuilder builder = null;
        //设置内存缓存占用八分之一
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(memoryCacheSize));
        //设置磁盘缓存大小和位子
        File cacheDir = context.getExternalCacheDir();
        int diskCacheSize = 1024 * 1024 * 30;
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "glide", diskCacheSize));
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "glide", diskCacheSize));
        //基本设置
        builder.setDefaultRequestOptions(options);
    }
}
