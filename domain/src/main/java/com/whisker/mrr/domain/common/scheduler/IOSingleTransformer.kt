package com.whisker.mrr.domain.common.scheduler

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import io.reactivex.schedulers.Schedulers

class IOSingleTransformer<T>(private val mainThreadScheduler: Scheduler) : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.subscribeOn(Schedulers.io()).observeOn(mainThreadScheduler)
    }
}