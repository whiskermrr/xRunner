package com.whisker.mrr.domain.usecase

import io.reactivex.Maybe
import io.reactivex.MaybeTransformer

abstract class MaybeUseCase<T>(private val transformer: MaybeTransformer<T, T>) {

    abstract fun createMaybe(data: Map<String, Any>? = null) : Maybe<T>

    fun maybe(data: Map<String, Any>? = null) : Maybe<T> {
        return createMaybe(data).compose(transformer)
    }
}