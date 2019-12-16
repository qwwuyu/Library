//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qwwuyu.lib.widget.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;

import java.io.File;
import java.util.Map;

/**
 * 默认WebView
 */
public class BaseWebView extends WebView {
    private static final String TAG = "BaseWebView";
    private static final boolean DEBUG = false;
    private boolean isDestroy;

    public BaseWebView(Context context) {
        this(context, null);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initWebView(context);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initWebView(context);
    }

    @TargetApi(21)
    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initWebView(context);
    }

    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt"})
    public void initWebView(Context context) {
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setHorizontalScrollBarEnabled(false);
        this.setVerticalScrollBarEnabled(false);
        this.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        File webViewPath = getWebViewPath(context);
        this.setSaveEnabled(true);
        this.removeJavascriptInterface("searchBoxJavaBridge_");
        this.removeJavascriptInterface("accessibility");
        this.removeJavascriptInterface("accessibilityTraversal");

        WebSettings webSettings = this.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);

        webSettings.setSavePassword(false);
        webSettings.setPluginState(PluginState.ON);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setSaveFormData(true);

        webSettings.setDatabaseEnabled(false);
        if (VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webSettings.setDatabasePath(new File(webViewPath, "database").getPath());
        }

        webSettings.setAppCacheEnabled(true);
        if (webViewPath != null)
            webSettings.setAppCachePath(new File(webViewPath, "cache").getPath());
        if (VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            webSettings.setRenderPriority(RenderPriority.HIGH);
            webSettings.setAppCacheMaxSize(20 * 1024 * 1024);
        }

        if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(0);
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        webSettings.setAllowFileAccess(true);
        if (VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(false);
            webSettings.setAllowUniversalAccessFromFileURLs(false);
        }

        if (VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int mDensity = metrics.densityDpi;
            if (mDensity == 240) {
                webSettings.setDefaultZoom(ZoomDensity.FAR);
            } else if (mDensity == 160) {
                webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
            } else if (mDensity == 120) {
                webSettings.setDefaultZoom(ZoomDensity.CLOSE);
            } else if (mDensity == 320) {
                webSettings.setDefaultZoom(ZoomDensity.FAR);
            } else if (mDensity == 213) {
                webSettings.setDefaultZoom(ZoomDensity.FAR);
            } else {
                webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        this.isDestroy = true;
    }

    @Override
    public void loadUrl(String url, Map<String, String> extraHeaders) {
        if (!this.isDestroy) {
            super.loadUrl(url, extraHeaders);
        }
    }

    @Override
    public void loadUrl(String url) {
        if (!this.isDestroy) {
            super.loadUrl(url);
        }
    }

    private File getWebViewPath(Context context) {
        File cacheDir;
        if (context.getCacheDir() != null) {
            cacheDir = new File(context.getCacheDir(), "webview");
        } else {
            cacheDir = context.getDir("webview", Context.MODE_PRIVATE);
        }
        if (cacheDir != null) {
            try {
                cacheDir.mkdirs();
            } catch (Exception ignored) {
            }
        }
        return cacheDir;
    }

    public boolean isDestroy() {
        return this.isDestroy;
    }
}
