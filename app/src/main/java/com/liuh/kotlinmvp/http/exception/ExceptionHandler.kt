package com.liuh.kotlinmvp.http.exception

import com.google.gson.JsonParseException
import com.orhanobut.logger.Logger
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * Date: 2018/8/14 10:37
 * Description: 异常处理类
 */
class ExceptionHandler {

    companion object {

        var errorCode = ErrorStatus.UNKNOWN_ERROR
        var errorMsg = "请求失败，请稍后重试"

        fun handleException(e: Throwable): String {
            e.printStackTrace()
            if (e is SocketTimeoutException) {
                //网络超时

            } else if (e is ConnectException) {
                //网络连接异常

            } else if (e is JsonParseException
                    || e is JSONException
                    || e is ParseException) {
                //数据解析异常

            } else if (e is ApiException) {


            } else if (e is UnknownHostException) {
                //网络连接异常

            } else if (e is IllegalArgumentException) {
                //参数不合法异常

            } else {//未知错误

            }

            return ""
        }


    }


}