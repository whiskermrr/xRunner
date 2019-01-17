package com.whisker.mrr.domain.common.scheduler

import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.CompletableTransformer
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class ComputationCompletableTransformer(private val mainThreadScheduler: Scheduler) : CompletableTransformer {

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.subscribeOn(Schedulers.computation()).observeOn(mainThreadScheduler)
    }
}