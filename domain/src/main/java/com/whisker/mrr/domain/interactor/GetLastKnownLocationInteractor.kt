package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Coords
import com.whisker.mrr.domain.manager.LocationManager
import com.whisker.mrr.domain.usecase.MaybeUseCase
import io.reactivex.Maybe
import io.reactivex.MaybeTransformer

class GetLastKnownLocationInteractor(transformer: MaybeTransformer<Coords, Coords>, private val locationManager: LocationManager)
: MaybeUseCase<Coords>(transformer) {

    override fun createMaybe(data: Map<String, Any>?): Maybe<Coords> {
        return locationManager.getBestLastKnownLocation()
    }
}