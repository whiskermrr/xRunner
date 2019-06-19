package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.manager.LocationManager
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer

class ResumeTrackingInteractor(
    transformer: CompletableTransformer,
    private val locationManager: LocationManager
) : CompletableUseCase(transformer) {

    fun resumeTracking() : Completable {
        return completable()
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        return locationManager.resumeTracking()
    }
}