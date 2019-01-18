package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.source.LocationSource
import com.whisker.mrr.domain.usecase.UseCase

class StopTrackingInteractor(private val locationSource: LocationSource) : UseCase() {

    override fun execute(data: Map<String, Any>?) {
        locationSource.stopTracking()
    }
}