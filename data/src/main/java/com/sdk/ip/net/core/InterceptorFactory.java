package com.sdk.ip.net.core;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.sdk.ip.BuildConfig;
import com.sdk.ip.IpContent;
import com.sdk.ip.util.DeviceUuidFactory;

import java.io.IOException;
import java.lang.reflect.Method;

import okhttp3.*;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Create by Administrator on 2019/4/23
 * 设置okHttp拦截器
 */
class InterceptorFactory {

    static Interceptor getHeadInterceptor() {
        return getTokenHead();
    }

    static Interceptor getCommonLogInterceptor() {
        return new CommonLog();
    }


    //有Token head
    private static Interceptor getTokenHead() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();
                String time = System.currentTimeMillis() / 1000 + "";
                String device_id = MD5Util.md5(DeviceUuidFactory.getInstance((Application) IpContent.mContext).toString() + "app");
                requestBuilder
                        .addHeader("platform", "android")
                        .addHeader("device-id", device_id)
                        .addHeader("time", time + "")
                        .addHeader("version", "1")
                        .addHeader("channel", IpContent.channel)
//                        .addHeader("oaId", DeviceIdentifier.getOAID(IpContent.mContext))
                        .addHeader("androidId", getAndroidId_(IpContent.mContext))
                        .addHeader("user-type", "1")
                        .addHeader("manufacturer", Build.MANUFACTURER)
                        .addHeader("device_model", getModel())
                        .addHeader("imei", getIMEI(IpContent.mContext));
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }

    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    public static String getAndroidId_(Context context) {
        String imei = Settings.System.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return imei == null ? "" : imei;
    }

    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                imei = tm.getDeviceId();
            } else {
                Method method = tm.getClass().getMethod("getImei");
                imei = (String) method.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei == null ? "" : imei;
    }

    /**
     * 设置没有网络的情况下，
     * 的缓存时间
     * 通过：addInterceptor 设置
     */
    public static class CommonNoNetCache implements Interceptor {

        public CommonNoNetCache(int maxCacheTimeSecond) {
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
//            if (!NetworkUtils.isConnected()) {
//
//            }
            return chain.proceed(request).newBuilder()
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .header("Cache-Control", "public,only-if-cached,max-stale=" + 2419200)
                    .build();
        }
    }

    /**
     * 设置在有网络的情况下的缓存时间
     * 在有网络的时候，会优先获取缓存
     * 通过：addNetworkInterceptor 设置
     */
    public static class CommonNetCache implements Interceptor {

        private int maxCacheTimeSecond = 0;

        public CommonNetCache(int maxCacheTimeSecond) {
            this.maxCacheTimeSecond = maxCacheTimeSecond;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            Response originalResponse = chain.proceed(request);
            int maxAge = maxCacheTimeSecond;    // 在线缓存,单位:秒
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        }
    }

    /**
     * 设置一个日志打印拦截器
     * 通过：addInterceptor 设置
     */
    public static class CommonLog implements Interceptor {

        //统一的日志输出控制，可以构造方法传入，统一控制日志
        private boolean logOpen = true;
        //log的日志TAG
        String logTag = "CommonLog";

        CommonLog() {
        }

        public CommonLog(boolean logOpen) {
            this.logOpen = logOpen;
        }

        public CommonLog(String logTag) {
            this.logTag = logTag;
        }

        public CommonLog(boolean logOpen, String logTag) {
            this.logOpen = logOpen;
            this.logTag = logTag;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {

            Request request = chain.request();
            System.currentTimeMillis();//请求发起的时间
            Response response = chain.proceed(request);
            System.currentTimeMillis();//收到响应的时间

            if (logOpen) {
                //这里不能直接使用response.body().string()的方式输出日志
                //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
                //个新的response给应用层处理
                response.peekBody(1024 * 1024);
            }

            return response;
        }
    }

}

