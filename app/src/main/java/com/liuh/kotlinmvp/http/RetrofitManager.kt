package com.liuh.kotlinmvp.http

import com.liuh.kotlinmvp.MyApplication
import com.liuh.kotlinmvp.api.ApiService
import com.liuh.kotlinmvp.api.UrlConstant
import com.liuh.kotlinmvp.utils.NetWorkUtil
import com.liuh.kotlinmvp.utils.SPreference
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Date: 2018/8/13 16:43
 * Description:
 */
object RetrofitManager {

    private var client: OkHttpClient? = null
    private var retrofit: Retrofit? = null

    val service: ApiService by lazy { getRetrofit()!!.create(ApiService::class.java) }
    private var token: String by SPreference("token", "")

    /**
     * 设置公共参数
     */
    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url().newBuilder()
                    .addQueryParameter("phoneSystem", "")
                    .addQueryParameter("phoneModel", "")
                    .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }

    /**
     * 设置头
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                    .header("token", token)
                    .method(originalRequest.method(), originalRequest.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    /**
     * 设置缓存
     */
    private fun addCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (NetWorkUtil.isNetworkAvailable(MyApplication.context)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            }

            val response = chain.proceed(request)

            if (NetWorkUtil.isNetworkAvailable(MyApplication.context)) {
                val maxAge = 0
                //有网络时，设置缓存时间为0个小时，意思就是不读取缓存数据，只对get有用，post没有缓存
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Retrofit")//清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build()
            } else {
                //无网络时，设置超过4周，只对get有用，post没有缓存
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached,max-stale=" + maxStale)
                        .removeHeader("nyn")
                        .build()

            }
            response
        }

    }


    private fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            synchronized(RetrofitManager::class.java) {
                if (retrofit == null) {
                    //添加一个Log打印器，打印所有的Log
                    val httpLoggingInterceptor = HttpLoggingInterceptor()
                    //设置请求过滤的level，body，basic，headers
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

                    //设置请求的缓存的大小和位置
                    val cacheFile = File(MyApplication.context.cacheDir, "chche")
                    val cache = Cache(cacheFile, 1024 * 1024 * 50) //50Mb 缓存的大小

                    client = OkHttpClient.Builder()
                            .addInterceptor(addQueryParameterInterceptor())// 参数添加
                            .addInterceptor(addHeaderInterceptor()) // token过滤
                            .addInterceptor(httpLoggingInterceptor) // 日志，所有的请求和相应都能看到
                            .cache(cache) //添加缓存
                            .connectTimeout(60L, TimeUnit.SECONDS)
                            .readTimeout(60L, TimeUnit.SECONDS)
                            .writeTimeout(60L, TimeUnit.SECONDS)
                            .build()

                    //获取Retrofit实例
                    retrofit = Retrofit.Builder()
                            .baseUrl(UrlConstant.BASE_URL)
                            .client(client!!)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                }
            }
        }
        return retrofit
    }


}