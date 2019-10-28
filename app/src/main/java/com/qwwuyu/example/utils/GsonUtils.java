package com.qwwuyu.example.utils;

import android.text.TextUtils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;

public class GsonUtils {
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .addSerializationExclusionStrategy(new Exclusion())
                    .addDeserializationExclusionStrategy(new Exclusion())
                    .registerTypeAdapter(Void.class, new VoidTypeAdapter())
                    .registerTypeAdapter(Date.class, new DateTypeAdapter())
                    //.registerTypeAdapter(JsonDeserializer.class, deserializer)
                    .create();
        }
        return gson;
    }

    /**
     * 使用gson解析json,异常则返回空
     */
    @Nullable
    public static <T> T fromJson(String result, Class<T> clazz) {
        try {
            return getGson().fromJson(result, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static <T> T fromType(String result, Type type) {
        try {
            return getGson().fromJson(result, type);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将对象转化为json
     */
    public static String toJson(Object obj) {
        return getGson().toJson(obj);
    }

    /**
     * 获取类泛型第一个参数
     */
    public static Type getActualTypeArgument0(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            return ((ParameterizedType) superclass).getActualTypeArguments()[0];
        }
        return null;
    }

    /**
     * 判断type是否是Void类型
     */
    public static boolean isTypeVoid(Type type) {
        if (type instanceof Class) {
            return Void.class.isAssignableFrom((Class) type);
        }
        return false;
    }

    /**
     * 判断type是否继承List
     */
    public static boolean isTypeList(Type type) {
        if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class) {
                return List.class.isAssignableFrom((Class) rawType);
            }
        }
        return false;
    }

    private static final class Exclusion implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    private static final class VoidTypeAdapter extends TypeAdapter<Void> {
        @Override
        public void write(final JsonWriter out, final Void value) throws IOException {
            out.nullValue();
        }

        @Override
        public Void read(final JsonReader in) throws IOException {
            in.skipValue();
            return null;
        }
    }

    private static final class DateTypeAdapter extends TypeAdapter<Date> {
        private final List<DateFormat> dateFormats = new ArrayList<>();

        public DateTypeAdapter() {
            dateFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()));
            dateFormats.add(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
            if (!Locale.getDefault().equals(Locale.US)) {
                dateFormats.add(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US));
            }
        }

        @Override
        public Date read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            if (peek == JsonToken.NULL) {
                in.nextNull();
                return null;
            } else if (peek == JsonToken.NUMBER) {
                return new Date(in.nextLong());
            }
            return deserializeToDate(in.nextString());
        }

        private synchronized Date deserializeToDate(String json) {
            if (TextUtils.isEmpty(json)) {
                return null;
            }
            try {
                if (json.matches("^[\\d]{13}$")) {
                    return new Date(Long.parseLong(json));
                }
            } catch (Exception ignored) {
            }
            for (DateFormat dateFormat : dateFormats) {
                try {
                    return dateFormat.parse(json);
                } catch (Exception ignored) {
                }
            }
            try {
                return ISO8601Utils.parse(json, new ParsePosition(0));
            } catch (Exception e) {
                //throw new JsonSyntaxException(json, e);
            }
            return null;
        }

        @Override
        public synchronized void write(JsonWriter out, Date value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            String dateFormatAsString = dateFormats.get(0).format(value);
            out.value(dateFormatAsString);
        }
    }
}
