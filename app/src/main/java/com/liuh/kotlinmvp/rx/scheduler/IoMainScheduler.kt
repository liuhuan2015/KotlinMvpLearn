package com.liuh.kotlinmvp.rx.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Date: 2018/8/13 17:19
 * Description:
 */
class IoMainScheduler<T> : BaseScheduler<T>(Scheduler.io(), AndroidSchedulers.mainThread())