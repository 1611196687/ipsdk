package com.sdk.ip.net;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 */

public interface ApiService {

    /**
     * 初始化
     *
     * @return
     */
    @POST("v1/init")
    Observable<String> getInit();

    /**
     * 添加ip
     *
     * @return
     */
    @POST("v1/ip_info_add")
    Observable<String> getIPInfoAdd(@Body RequestBody info);


}
