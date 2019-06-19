package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Coords
import com.whisker.mrr.domain.manager.LocationManager
import com.whisker.mrr.domain.usecase.FlowableUseCase
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

class StartTrackingInteractor(transformer: FlowableTransformer<Coords, Coords>, private val locationManager: LocationManager
) : FlowableUseCase<Coords>(transformer) {

    override fun createFlowable(data: Map<String, Any>?): Flowable<Coords> {
        return locationManager.startTracking()
    }
}