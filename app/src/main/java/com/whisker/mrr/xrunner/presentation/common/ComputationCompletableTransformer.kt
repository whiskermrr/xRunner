package com.whisker.mrr.xrunner.presentation.common

import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.CompletableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ComputationCompletableTransformer : CompletableTransformer {

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
    }
}