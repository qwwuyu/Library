package com.qwwuyu.lib.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 通过uri获取文件路径,AndroidQ以前获取直接路径,AndroidQ及路径可读则反悔,不可读拷贝后返回
 */
public class PathUtils {
    /**
     * @param context  上下文
     * @param uri      uri
     * @param supportQ targetSdkVersion>=29 传递此对象
     * @return 文件路径
     * @throws Exception 避免异常引起程序崩溃
     */
    @Nullable
    public static String getPath(@NonNull final Context context, @NonNull final Uri uri, @Nullable SupportQ supportQ) throws Exception {
        String path = getPath(context, uri);
        if (supportQ != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (path != null && new File(path).canRead()) return path;
            else return copyFile(context, uri, supportQ);
        }
        return path;
    }

    @Nullable
    private static String getPath(@NonNull final Context context, @NonNull final Uri uri) throws Exception {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else if (exists(System.getenv("SECONDARY_STORAGE") + "/" + split[1])) {
                    return System.getenv("SECONDARY_STORAGE") + "/" + split[1];
                } else if (exists(System.getenv("EXTERNAL_STORAGE") + "/" + split[1])) {
                    return System.getenv("EXTERNAL_STORAGE") + "/" + split[1];
                } else {
                    return null;
                }
            }

            if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (id == null) return null;
                    if (id.startsWith("raw:")) return id.replaceFirst("raw:", "");

                    String[] contentUriPrefixes = new String[]{"content://downloads/public_downloads", "content://downloads/my_downloads"};
                    for (String contentUriPrefix : contentUriPrefixes) {
                        try {
                            final Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.parseLong(id));
                            return getDataColumn(context, contentUri, null, null);
                        } catch (NumberFormatException e) {
                            //In Android 8 and Android P the id is not a number
                            if (uri.getPath() == null) return null;
                            return uri.getPath().replaceFirst("^/document/raw:", "").replaceFirst("^raw:", "");
                        }
                    }
                } else {
                    if (id != null) {
                        if (id.startsWith("raw:")) return id.replaceFirst("raw:", "");
                        try {
                            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
                            return getDataColumn(context, contentUri, null, null);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }

            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                return getDataColumn(context, contentUri, "_id=?", new String[]{split[1]});
            }

            if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }
                return getDataColumn(context, uri, null, null);
            }
        } else {//Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
        }
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(column);
                if (columnIndex >= 0) {
                    return cursor.getString(cursor.getColumnIndex(column));
                }
            }
        } finally {
            close(cursor);
        }
        return null;
    }

    private static String copyFile(Context context, Uri uri, SupportQ supportQ) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE},
                    null, null, null);
            String name = null;
            Long size = null;
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (columnIndex >= 0) name = cursor.getString(columnIndex);
                columnIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (columnIndex >= 0) size = cursor.getLong(columnIndex);
            }
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                File cacheFile = supportQ.getCacheFile(context, Objects.requireNonNull(inputStream), name, size);
                if (cacheFile == null) return null;
                File parentFile = cacheFile.getParentFile();
                if (parentFile != null && !parentFile.exists()) parentFile.mkdirs();
                outputStream = new FileOutputStream(cacheFile);
                int read;
                final byte[] buffers = new byte[10 * 1024];
                while ((read = inputStream.read(buffers)) != -1) {
                    outputStream.write(buffers, 0, read);
                }
                inputStream.close();
                outputStream.close();
                return cacheFile.getPath();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                closeStream(inputStream, outputStream);
            }
        } finally {
            close(cursor);
        }
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static boolean exists(String path) {
        try {
            return new File(path).exists();
        } catch (Exception e) {
            return false;
        }
    }

    private static void close(Cursor cursor) {
        try {
            if (cursor != null) cursor.close();
        } catch (Exception ignored) {
        }
    }

    private static void closeStream(Object... streams) {
        for (Object stream : streams) {
            try {
                if (stream instanceof Closeable) ((Closeable) stream).close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * targetSdkVersion>=29 需要实现此类
     */
    public interface SupportQ {
        /**
         * @param context     上下文
         * @param inputStream uri的流,如果需要使用inputStream,则返回null. 如果需要拷贝,则返回文件路径
         * @param name        文件名
         * @param size        文件大小
         * @return 需要拷贝的路径
         */
        @Nullable
        File getCacheFile(@NonNull Context context, @NonNull InputStream inputStream, @Nullable String name, @Nullable Long size);
    }
}