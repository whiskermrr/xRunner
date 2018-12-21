package com.whisker.mrr.xrunner.domain.usecase

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

abstract class FlowableUseCase<T>(private val transformer: FlowableTransformer<T, T>) {

    abstract fun createObservable(data: Map<String, Any>? = null) : Flowable<T>

    fun observable(data: Map<String, Any>? = null) : Flowable<T> {
        return createObservable(data).compose(transformer)
    }
}