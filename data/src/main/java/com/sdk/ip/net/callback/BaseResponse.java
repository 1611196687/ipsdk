package com.sdk.ip.net.callback;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class BaseResponse<T> {

    private static final int RESULT_SUCCESS = 0;
    private static final int RESULT_CODE_SUCCESS = 200;

    private int code;
    private String msg;
    private String data;
    @JSONField(serialize = false)
    private T object;
    @JSONField(serialize = false)
    private List<T> list;

    private BasePagerEntity<T> pagerEntity;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return code == RESULT_SUCCESS || code == RESULT_CODE_SUCCESS;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public BasePagerEntity<T> getPagerEntity() {
        return pagerEntity;
    }

    public void setPagerEntity(BasePagerEntity<T> pagerEntity) {
        this.pagerEntity = pagerEntity;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                ", object=" + object +
                ", list=" + list +
                ", pagerEntity=" + pagerEntity +
                '}';
    }
}
