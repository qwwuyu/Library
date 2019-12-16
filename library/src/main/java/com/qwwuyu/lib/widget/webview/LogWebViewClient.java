package com.qwwuyu.lib.widget.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.qwwuyu.lib.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class LogWebViewClient extends WebViewClient {
    private static final String TAG = "LogWebViewClient";

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogUtils.i(TAG, "url=%s", url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        LogUtils.i(TAG, "url=%s", request.getUrl());
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        LogUtils.i(TAG, "url=%s", url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        LogUtils.i(TAG, "url=%s", url);
        super.onPageFinished(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        LogUtils.i(TAG, "url=%s", url);
        super.onLoadResource(view, url);
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        LogUtils.i(TAG, "url=%s", url);
        super.onPageCommitVisible(view, url);
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        LogUtils.i(TAG, "url=%s", url);
        return super.shouldInterceptRequest(view, url);
    }

    @Nullable
    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        LogUtils.i(TAG, "url=%s", request.getUrl());
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        LogUtils.i(TAG, "cancelMsg=%s continueMsg=%s", cancelMsg, continueMsg);
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        LogUtils.i(TAG, "errorCode=%s description=%s failingUrl=%s", errorCode, description, failingUrl);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        LogUtils.i(TAG, "errorCode=%s description=%s failingUrl=%s", error.getErrorCode(), error.getDescription(), request.getUrl());
        super.onReceivedError(view, request, error);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        String data = null;
        try {
            data = new String(toByteArray(errorResponse.getData()), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.i(TAG, "getStatusCode=%s data=%s url=%s", errorResponse.getStatusCode(), data, request.getUrl());
        super.onReceivedHttpError(view, request, errorResponse);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        LogUtils.i(TAG, "dontResend=%s resend=%s", dontResend, resend);
        super.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        LogUtils.i(TAG, "url=%s isReload=%s", url, isReload);
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        LogUtils.i(TAG, "handler=%s error=%s", handler, error);
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        LogUtils.i(TAG, "request=%s", request);
        super.onReceivedClientCertRequest(view, request);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        LogUtils.i(TAG, "handler=%s host=%s realm=%s", handler, host, realm);
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        super.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
        LogUtils.i(TAG, "realm=%s account=%s args=%s", realm, account, args);
        super.onReceivedLoginRequest(view, realm, account, args);
    }

    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        return super.onRenderProcessGone(view, detail);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {
        LogUtils.i(TAG, "request=%s threatType=%s callback=%s", request.getUrl(), threatType, callback);
        super.onSafeBrowsingHit(view, request, threatType, callback);
    }

    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
