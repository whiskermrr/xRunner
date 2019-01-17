package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Coords
import com.whisker.mrr.domain.repository.LocationRepository
import com.whisker.mrr.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer

class GetLastKnownLocationInteractor(transformer: SingleTransformer<Coords, Coords>, private val locationRepository: LocationRepository)
: SingleUseCase<Coords>(transformer) {

    override fun createSingle(data: Map<String, Any>?): Single<Coords> {
        return locationRepository.getLastKnownLocation()
    }
}