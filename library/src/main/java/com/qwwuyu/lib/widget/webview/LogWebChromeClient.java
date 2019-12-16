package com.qwwuyu.lib.widget.webview;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;


import com.qwwuyu.lib.utils.LogUtils;

import java.util.Arrays;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class LogWebChromeClient extends WebChromeClient {
    private static final String TAG = "LogWebChromeClient";

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        LogUtils.i(TAG, "newProgress=%s", newProgress);
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        LogUtils.i(TAG, "title=%s", title);
        super.onReceivedTitle(view, title);
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        LogUtils.i(TAG, "url=%s precomposed=", url, precomposed);
        super.onReceivedTouchIconUrl(view, url, precomposed);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        super.onShowCustomView(view, requestedOrientation, callback);
    }

    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onRequestFocus(WebView view) {
        super.onRequestFocus(view);
    }

    @Override
    public void onCloseWindow(WebView window) {
        super.onCloseWindow(window);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        return super.onJsBeforeUnload(view, url, message, result);
    }

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
    }

    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
        super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    @Override
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public void onPermissionRequest(PermissionRequest request) {
        LogUtils.i(TAG, "request=%s", Arrays.asList(request.getResources()));
        super.onPermissionRequest(request);
    }

    @Override
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public void onPermissionRequestCanceled(PermissionRequest request) {
        LogUtils.i(TAG, "request=%s", Arrays.asList(request.getResources()));
        super.onPermissionRequestCanceled(request);
    }

    @Override
    public boolean onJsTimeout() {
        return super.onJsTimeout();
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        super.onConsoleMessage(message, lineNumber, sourceID);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return super.onConsoleMessage(consoleMessage);
    }

    @Nullable
    @Override
    public Bitmap getDefaultVideoPoster() {
        return super.getDefaultVideoPoster();
    }

    @Nullable
    @Override
    public View getVideoLoadingProgressView() {
        return super.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        super.getVisitedHistory(callback);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }
}
