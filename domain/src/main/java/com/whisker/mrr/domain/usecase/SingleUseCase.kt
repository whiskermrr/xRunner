package com.whisker.mrr.domain.usecase

import io.reactivex.Single
import io.reactivex.SingleTransformer

abstract class SingleUseCase<T>(private val transformer: SingleTransformer<T, T>) {

    abstract fun createSingle(data: Map<String, Any>? = null) : Single<T>

    fun single(data: Map<String, Any>? = null) : Single<T> {
        return createSingle(data).compose(transformer)
    }
}