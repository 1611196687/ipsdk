package com.sdk.ip.net.callback;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


/**
 * 标准已解析数据回调
 * 🐵 需要接口返回值格式与 BaseResponse参数一致
 * 🐵 子线程已解析数据，性能优化
 */
public abstract class HttpCallback<T> implements Observer<BaseResponse<T>>, LifecycleObserver {
    private Disposable disposable;

    public HttpCallback() {
    }

    public HttpCallback(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
    }


    @Override
    public void onNext(BaseResponse<T> response) {
        if (response != null && response.isSuccess()) {
            if (response.getObject() != null) {
                onSuccess(response);
            } else if (response.getList() != null) {
                onSuccess(response);
            } else if (response.getPagerEntity() != null) {
                onSuccess(response);
            } else {
                onFailed(response.getCode(), response.getMsg());
            }
        } else {
            if (response != null) {
                onFailed(response.getCode(), response.getMsg());
            } else {
                onFailed(400, "数据获取失败");
            }
        }
        onFinish();
    }

    @Override
    public void onError(Throwable throwable) {
        String errorInfo = parseHttpErrorInfo(throwable);
        onFailed(500, errorInfo);
        onFinish();
    }

    /**
     * 解析服务器错误信息
     */
    public static String parseHttpErrorInfo(Throwable throwable) {
        String errorInfo = throwable.getMessage();
        if (throwable instanceof HttpException) {
            errorInfo = "服务器异常";
        }
        else {
            if (throwable instanceof UnknownHostException) {
                errorInfo = "网络不可用";
            }
        }

        return errorInfo;
    }

    @Override
    public void onComplete() {
    }

    /*******************对外方法**********************/
    public abstract void onSuccess(BaseResponse<T> response);

    public void onFailed(int code, String message) {
//        ToastUtil.showToastWarning(message);
    }

    public void onFinish() {
    }
}
