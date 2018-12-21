package com.whisker.mrr.xrunner.domain.interactor

import com.whisker.mrr.xrunner.domain.repository.LocationRepository
import com.whisker.mrr.xrunner.domain.usecase.UseCase

class StopTrackingInteractor(private val locationRepository: LocationRepository) : UseCase() {

    override fun execute(data: Map<String, Any>?) {
        locationRepository.stopTracking()
    }
}