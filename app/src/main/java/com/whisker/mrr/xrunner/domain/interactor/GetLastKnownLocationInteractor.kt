package com.whisker.mrr.xrunner.domain.interactor

import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.domain.repository.LocationRepository
import com.whisker.mrr.xrunner.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer

class GetLastKnownLocationInteractor(transformer: SingleTransformer<LatLng, LatLng>, private val locationRepository: LocationRepository)
: SingleUseCase<LatLng>(transformer) {

    override fun createSingle(data: Map<String, Any>?): Single<LatLng> {
        return locationRepository.getLastKnownLocation()
    }
}