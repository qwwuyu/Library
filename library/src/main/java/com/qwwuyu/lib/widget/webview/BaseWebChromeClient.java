package com.qwwuyu.lib.widget.webview;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

/**
 * 默认进度条WebChromeClient
 */
public class BaseWebChromeClient extends WebChromeClient {
    private WebView webView;
    private ProgressBar bar;

    public BaseWebChromeClient(WebView webView, @Nullable ProgressBar bar) {
        this.webView = webView;
        this.bar = bar;
        if (bar != null) bar.setMax(100);
    }

    @Override
    public void onProgressChanged(WebView view, int progress) {
        super.onProgressChanged(view, progress);
        if (bar != null) {
            if (progress != 100) {
                bar.setVisibility(View.VISIBLE);
                bar.setProgress(progress);
            } else {
                bar.setVisibility(View.GONE);
            }
        }
    }
}
