package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.repository.LocationRepository
import com.whisker.mrr.domain.usecase.UseCase

class PauseTrackingInteractor(private val locationRepository: LocationRepository) : UseCase() {

    override fun execute(data: Map<String, Any>?) {
        locationRepository.pauseTracking()
    }
}