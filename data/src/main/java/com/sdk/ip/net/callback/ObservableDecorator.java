package com.sdk.ip.net.callback;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * HttpCallback  观察者装饰器
 */
public class ObservableDecorator {

    @NonNull
    public static <T> Observable<BaseResponse<T>> decorateHttp(Observable<String> observable, Class<T> cls) {
        return observable
                .map(new HttpResultFunction<>(cls))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 数据解析 转BaseResponse
     */
    public static class HttpResultFunction<T> implements Function<String, BaseResponse<T>> {

        private Class<T> cls;
        private String tag;
        private Boolean isCache;
        private Disposable disposable;

        HttpResultFunction(Class<T> cls) {
            this.cls = cls;
            isCache = false;
        }

        HttpResultFunction(Class<T> cls, String tag) {
            this.cls = cls;
            this.tag = tag;
            isCache = true;
        }

        @Override
        public BaseResponse<T> apply(@NonNull String response) {
            if (TextUtils.isEmpty(response)) {
                return null;
            }
            BaseResponse result = JsonUtil.jsonToBean(response, BaseResponse.class);
            if (result == null)
                return null;
            BaseResponse<T> baseResponse = new BaseResponse<>();
            baseResponse.setCode(result.getCode());
            baseResponse.setMsg(result.getMsg());
            baseResponse.setData(result.getData());
            String json = baseResponse.getData();
            if (isCache && baseResponse.isSuccess()) {
//                BaseModelUtil.saveBean(tag, baseResponse);
            }
            if (!TextUtils.isEmpty(json)) {
                try {
                    Object jsonElement = new JSONTokener(json).nextValue();
                    if (jsonElement instanceof JSONObject) {
                        T t = JsonUtil.jsonToBean(json, cls);
                        baseResponse.setObject(t);
                    } else if (jsonElement instanceof JSONArray) {
                        List<T> list = JsonUtil.jsonToList(json, cls);
                        baseResponse.setList(list);
                    }
                } catch (JSONException e) {
                    Log.e("context", e.toString());
                }
            }
            return baseResponse;
        }
    }

}
