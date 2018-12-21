package com.whisker.mrr.xrunner.domain.interactor

import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.domain.repository.LocationRepository
import com.whisker.mrr.xrunner.domain.usecase.FlowableUseCase
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

class StartTrackingInteractor(transformer: FlowableTransformer<LatLng, LatLng>, private val locationRepository: LocationRepository
) : FlowableUseCase<LatLng>(transformer) {

    override fun createFlowable(data: Map<String, Any>?): Flowable<LatLng> {
        return locationRepository.startTracking()
    }
}