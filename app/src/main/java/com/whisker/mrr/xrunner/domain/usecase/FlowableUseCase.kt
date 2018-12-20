package com.whisker.mrr.xrunner.domain.usecase

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class FlowableUseCase<T>(private val transformer: ObservableTransformer<T, T>) {

    abstract fun createObservable(data: Map<String, Any>? = null) : Observable<T>

    fun observable(data: Map<String, Any>? = null) : Observable<T> {
        return createObservable(data).compose(transformer)
    }
}