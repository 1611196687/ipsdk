package com.sdk.ip.net.callback;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;


public class JsonUtil {

   private static String tag = "CONTEXT";

    /**
     * 把json string 转化成类对象
     */
    public static <T> T jsonToBean(String json, Class<T> cls) {
        try {
            if (!TextUtils.isEmpty(json.trim())) {
                return JSON.parseObject(json, cls);
            }
        } catch (Exception e) {
            Log.e(tag, e.toString());
        }
        return null;
    }

    public static <T> T jsonTypeToBean(String json, Type type) {
        try {
            if (!TextUtils.isEmpty(json.trim())) {
                return JSON.parseObject(json, type);
            }
        } catch (Exception e) {
            Log.e(tag, e.toString());
        }
        return null;
    }

    public static BaseResponse parseToMap(String json, Type type) {
        return JSON.parseObject(json,
                new TypeReference<BaseResponse>(type) {
                });
    }

    /**
     * 把json string 转化成类对象
     */
    public static <T> List<T> jsonToList(String json, final Class<T> cls) {
        try {
            if (!TextUtils.isEmpty(json.trim())) {
                return JSON.parseArray(json, cls);
            }
        } catch (Exception e) {
            Log.e(tag, e.toString());
        }
        return Collections.emptyList();
    }

    /**
     * 把类对象转化成json string
     */
    public static <T> String toJsonString(T t) {
        try {
            return JSON.toJSONString(t);
        } catch (Exception e) {
            Log.e(tag, e.toString());
        }
        return null;
    }

    public static boolean isJsonObject(String json) {
        Object jsonElement;
        try {
            jsonElement = new JSONTokener(json).nextValue();
            if (jsonElement instanceof JSONObject) {
                return true;
            } else if (jsonElement instanceof JSONArray) {
                return false;
            }
        } catch (JSONException e) {
            Log.e(tag, e.toString());
        }
        return true;
    }
}