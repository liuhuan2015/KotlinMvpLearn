package com.liuh.kotlinmvp.rx.scheduler

/**
 * Date: 2018/8/13 17:17
 * Description:
 */
object SchedulerUtils {
    fun <T> ioToMain(): IoMainScheduler<T> {
        return ToMainScheduler()
    }
}