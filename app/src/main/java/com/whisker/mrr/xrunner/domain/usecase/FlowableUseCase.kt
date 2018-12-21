package com.whisker.mrr.xrunner.domain.usecase

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

abstract class FlowableUseCase<T>(private val transformer: FlowableTransformer<T, T>) {

    abstract fun createFlowable(data: Map<String, Any>? = null) : Flowable<T>

    fun flowable(data: Map<String, Any>? = null) : Flowable<T> {
        return createFlowable(data).compose(transformer)
    }
}