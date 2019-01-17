package com.whisker.mrr.xrunner.domain.common.scheduler

import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.CompletableTransformer
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class IOCompletableTransformer(private val mainThreadScheduler: Scheduler) : CompletableTransformer {

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.subscribeOn(Schedulers.io()).observeOn(mainThreadScheduler)
    }
}