package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Coords
import com.whisker.mrr.domain.source.LocationSource
import com.whisker.mrr.domain.usecase.MaybeUseCase
import io.reactivex.Maybe
import io.reactivex.MaybeTransformer

class GetLastKnownLocationInteractor(transformer: MaybeTransformer<Coords, Coords>, private val locationSource: LocationSource)
: MaybeUseCase<Coords>(transformer) {

    override fun createMaybe(data: Map<String, Any>?): Maybe<Coords> {
        return locationSource.getBestLastKnownLocation()
    }
}