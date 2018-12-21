package com.whisker.mrr.xrunner.domain.usecase

abstract class UseCase {

    abstract fun execute(data: Map<String, Any>? = null)
}