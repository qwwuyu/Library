package com.qwwuyu.lib.widget.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import com.qwwuyu.lib.utils.LogUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 默认进度条WebViewClient
 */
public class BaseWebViewClient extends WebViewClient {
    private WebView webView;
    private ProgressBar bar;

    public BaseWebViewClient(WebView webView, @Nullable ProgressBar bar) {
        this.webView = webView;
        this.bar = bar;
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return shouldOverrideUrlLoading(view, request.getUrl().toString());
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (bar != null) bar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (bar != null) bar.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        LogUtils.i(null, "onReceivedError isForMainFrame=%s errorCode=%s description=%s failingUrl=%s",
                request.isForMainFrame(), error.getErrorCode(), error.getDescription(), request.getUrl());
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        LogUtils.i(null, "onReceivedError isForMainFrame=%s errorCode=%s description=%s failingUrl=%s",
                true, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        try {
            handler.proceed();//无视证书错误
        } catch (Exception ignored) {
        }
        //// 默认的处理方式，WebView变成空白页
        //super.onReceivedSslError(view, handler, error);
    }
}
