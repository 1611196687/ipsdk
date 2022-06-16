package com.sdk.ip.net;


import com.sdk.ip.bean.InitEntity;
import com.sdk.ip.net.callback.HttpCallback;
import com.sdk.ip.net.callback.ObservableDecorator;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

import static com.sdk.ip.net.core.RetrofitUtil.getApiService;

/**
 *
 */

public class JavaHttpRequest {

    public static void getInitInfo(HttpCallback<InitEntity> subscriber) {
        Observable<String> observable = getApiService(ApiService.class).getInit();
        ObservableDecorator.decorateHttp(observable, InitEntity.class).subscribe(subscriber);
    }

    public static void getIPInfoAdd(String content, HttpCallback<InitEntity> subscriber) {
        Map<String, Object> map  = new HashMap<>();
        map.put("sign",content);
        Observable<String> observable = getApiService(ApiService.class).getIPInfoAdd(createJson(map));
        ObservableDecorator.decorateHttp(observable, InitEntity.class).subscribe(subscriber);
    }


    //java后台的接口 需要将参数生成json格式上传
    private static RequestBody createJson(Map<String, Object> params) {
        JSONObject jsonObject = new JSONObject(params);
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
    }


}
