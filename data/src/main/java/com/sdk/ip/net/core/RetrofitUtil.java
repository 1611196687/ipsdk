package com.sdk.ip.net.core;

import android.os.AsyncTask;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Retrofit核心类
 */
public class RetrofitUtil {

    private static Retrofit retrofit;

    private static OkHttpClient httpClient;
    private static String baseURL = "http://trace.ssoapi.com/"; //正式地址
//    private static String baseURL = "http://10.3.3.177:17001/"; //正式地址
    public static final Boolean ISTEXT = true;

    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    public static void loadData(){
        // OkHttpClient
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(20, TimeUnit.MINUTES)
                .writeTimeout(20, TimeUnit.MINUTES)
                // 统一添加的Header
                .addInterceptor(InterceptorFactory.getHeadInterceptor())
                .addInterceptor(InterceptorFactory.getCommonLogInterceptor())
                //LOG
                .addInterceptor(logInterceptor)
                .build();

        // Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(ScalarsConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // rxjava 响应式编程
                .client(httpClient)
                .callbackExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .build();
    }

    public static <T> T getApiService(Class<T> tClass) {
        loadData();
        return retrofit.create(tClass);
    }

    public static void changeBaseUrl(String baseUrl) {
//        baseUrl = PlanB.getBase_url();
        loadData();
        baseURL = baseUrl;
        retrofit = retrofit.newBuilder().baseUrl(baseUrl).build();
    }

    //替换默认URL
    public static <T> T getApiService(Class<T> tClass, String baseUrl) {
        changeBaseUrl(baseUrl);
        return retrofit.create(tClass);
    }
}
