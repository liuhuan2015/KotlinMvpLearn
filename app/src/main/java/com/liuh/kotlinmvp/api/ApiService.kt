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

    /**
     * 获取首页数据
     * 实例：http://baobab.kaiyanapp.com/api/v2/feed?num=1
     * **/
    @GET("v2/feed?")
    fun getFirstHomeData(@Query("num") num: Int): Observable<HomeBean>

    /**
     * 根据 nextPageUrl 字段的数值，请求下一页数据
     * 实例：http://baobab.kaiyanapp.com/api/v2/feed?date=1535677200000&num=1
     */
    @GET
    fun getMoreHomeData(@Url url: String): Observable<HomeBean>

    /**
     * 根据item id 获取相关视频
     * 实例：http://baobab.kaiyanapp.com/api/v4/video/related?id=124671
     */
    @GET("v4/video/related?")
    fun getRelatedData(@Query("id") id: Long): Observable<HomeBean.Issue>


}