package com.qwwuyu.lib.http.body;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * okhttp上传进度
 */
public class ProgressResponseBody extends RequestBody {
    private final RequestBody requestBody;
    private final ProgressListener listener;

    public ProgressResponseBody(RequestBody requestBody, ProgressListener listener) {
        this.requestBody = requestBody;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(@androidx.annotation.NonNull BufferedSink sink) throws IOException {
        try {
            CountingSink countingSink = new CountingSink(sink);
            BufferedSink bufferedSink = Okio.buffer(countingSink);
            requestBody.writeTo(bufferedSink);
            bufferedSink.flush();
            if (listener != null) listener.onFinish(contentLength());
        } catch (IOException e) {
            if (listener != null) listener.onError();
            throw e;
        }
    }

    private class CountingSink extends ForwardingSink {
        private long written = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(@androidx.annotation.NonNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            written += byteCount;
            if (listener != null) listener.onProgressUpdate(written, contentLength());
        }
    }

    public interface ProgressListener {
        void onProgressUpdate(long read, long length);

        void onError();

        void onFinish(long length);
    }

    public static abstract class MainProgressListener implements ProgressListener {
        private long lastCallTime = 0;
        private final long differTime;
        private final Handler handler;

        public MainProgressListener() {
            this(20);
        }

        public MainProgressListener(long differTime) {
            this(differTime, new Handler(Looper.getMainLooper()));
        }

        public MainProgressListener(long differTime, Handler handler) {
            this.differTime = differTime;
            this.handler = handler;
        }

        @Override
        public final void onProgressUpdate(long read, long length) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - lastCallTime >= differTime) {
                lastCallTime = nowTime;
                handler.post(() -> onMainProgressUpdate(read, length));
            }
        }

        @Override
        public final void onError() {
            handler.post(this::onMainError);
        }

        @Override
        public final void onFinish(long length) {
            handler.post(() -> onMainFinish(length));
        }

        public abstract void onMainProgressUpdate(long read, long length);

        public abstract void onMainError();

        public abstract void onMainFinish(long length);
    }
}
