package com.liuh.kotlinmvp.api

import io.reactivex.Observable
import mvp.model.bean.HomeBean
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Date: 2018/8/13 15:50
 * Description:接口定义
 */
interface ApiService {

    @GET("v2/feed?")
    fun getFirstHomeData(@Query("num") num: Int): Observable<HomeBean>

    /**
     * 根据 nextPageUrl 字段的数值，加载更多数据
     */
    @GET
    fun getMoreHomeData(@Url url: String): Observable<HomeBean>


}