package com.whisker.mrr.xrunner.domain.common.scheduler

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

class IOFlowableTransformer<T>(private val mainThreadScheduler: Scheduler) : FlowableTransformer<T, T> {

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.subscribeOn(Schedulers.io()).observeOn(mainThreadScheduler)
    }
}