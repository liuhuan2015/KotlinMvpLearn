package com.liuh.kotlinmvp.rx.scheduler

import io.reactivex.*
import org.reactivestreams.Publisher

/**
 * Date: 2018/8/13 17:24
 * Description:
 */
abstract class BaseScheduler<T> protected constructor(private val subscribeOnScheduler: Scheduler,
                                                      private val observeOnScheduler: Scheduler) : ObservableTransformer<T, T>,

        SingleTransformer<T, T>,
        MaybeTransformer<T, T>,
        CompletableTransformer,
        FlowableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {

    }

    override fun apply(upstream: Single<T>): SingleSource<T> {

    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {

    }

    override fun apply(upstream: Completable): CompletableSource {

    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {

    }
}