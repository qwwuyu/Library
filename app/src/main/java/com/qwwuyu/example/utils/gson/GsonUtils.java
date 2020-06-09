package com.qwwuyu.example.utils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;

public class GsonUtils {
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    //.addSerializationExclusionStrategy(new Exclusion())
                    //.addDeserializationExclusionStrategy(new Exclusion())
                    .registerTypeAdapter(Void.class, new VoidTypeAdapter())
                    .registerTypeAdapter(Date.class, new DateTypeAdapter())
                    //.registerTypeAdapter(float.class, new FloatTypeAdapter(0f))//处理float返回空字符串
                    //.registerTypeAdapter(Float.class, new FloatTypeAdapter(null))//处理Float返回空字符串
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
     * 判断type是否是String类型
     */
    public static boolean isTypeString(Type type) {
        if (type instanceof Class) {
            return String.class.isAssignableFrom((Class) type);
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
}
