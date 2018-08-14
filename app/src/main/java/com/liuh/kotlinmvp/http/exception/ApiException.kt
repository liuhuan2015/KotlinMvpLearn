package com.liuh.kotlinmvp.http.exception

/**
 * Date: 2018/8/14 10:57
 * Description:
 */
class ApiException : RuntimeException {

    private var code: Int? = null

    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }

    constructor(message: String) : super(Throwable(message))
}