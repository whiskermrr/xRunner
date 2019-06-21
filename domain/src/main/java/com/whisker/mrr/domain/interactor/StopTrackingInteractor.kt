package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.manager.LocationManager
import com.whisker.mrr.domain.usecase.UseCase

class StopTrackingInteractor(private val locationManager: LocationManager) : UseCase() {

    override fun execute(data: Map<String, Any>?) {
        locationManager.stopTracking()
    }
}