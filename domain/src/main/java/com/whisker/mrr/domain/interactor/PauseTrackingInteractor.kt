package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.source.LocationSource
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer

class PauseTrackingInteractor(
    transformer: CompletableTransformer,
    private val locationSource: LocationSource
) : CompletableUseCase(transformer) {

    fun pauseTracking() : Completable {
        return completable()
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        return locationSource.pauseTracking()
    }
}