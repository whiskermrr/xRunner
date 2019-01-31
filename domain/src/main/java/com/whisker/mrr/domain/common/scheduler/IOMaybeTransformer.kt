package com.whisker.mrr.domain.common.scheduler

import io.reactivex.*
import io.reactivex.schedulers.Schedulers

class IOMaybeTransformer<T>(private val mainThreadScheduler: Scheduler) : MaybeTransformer<T, T> {

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream.subscribeOn(Schedulers.io()).observeOn(mainThreadScheduler)
    }
}