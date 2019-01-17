package com.whisker.mrr.domain.usecase

abstract class UseCase {

    abstract fun execute(data: Map<String, Any>? = null)
}