package com.whisker.mrr.xrunner.domain.usecase

import io.reactivex.Completable
import io.reactivex.CompletableTransformer

abstract class CompletableUseCase(private val transformer: CompletableTransformer) {

    abstract fun createCompletable(data: Map<String, Any>? = null) : Completable

    fun completable(data: Map<String, Any>? = null) : Completable {
        return createCompletable(data).compose(transformer)
    }
}