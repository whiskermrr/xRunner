package com.whisker.mrr.xrunner.data.repository

import com.whisker.mrr.domain.model.Coords
import com.whisker.mrr.domain.repository.LocationRepository
import com.whisker.mrr.domain.source.LocationSource
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class LocationDataRepository
@Inject constructor(
    private val locationDataSource: LocationSource
) : LocationRepository {

    override fun startTracking() : Flowable<Coords> {
        return locationDataSource.startTracking()
    }

    override fun pauseTracking() {
        locationDataSource.pauseTracking()
    }

    override fun resumeTracking() {
        locationDataSource.resumeTracking()
    }

    override fun stopTracking() {
        locationDataSource.stopTracking()
    }

    override fun getLastKnownLocation(): Single<Coords> {
        return locationDataSource.getBestLastKnownLocation()
            .flatMapSingle {
                Single.just(it)
            }
    }

}