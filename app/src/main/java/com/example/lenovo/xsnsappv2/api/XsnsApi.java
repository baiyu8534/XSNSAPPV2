package com.example.lenovo.xsnsappv2.api;

import com.example.lenovo.xsnsappv2.bean.ImageProgectListBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author BaiYu
 * @description:
 * @date :2020/5/3 8:18
 */
public interface XsnsApi {
    @GET("/appapi/image")
    Observable<ImageProgectListBean> getImageProjectList(@Query("page_num") String page_num, @Query("page_size") String page_size);
}
