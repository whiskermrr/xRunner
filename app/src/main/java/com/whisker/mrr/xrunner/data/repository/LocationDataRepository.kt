package com.whisker.mrr.xrunner.data.repository

import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.data.mapper.LocationMapper
import com.whisker.mrr.xrunner.domain.repository.LocationRepository
import com.whisker.mrr.xrunner.domain.source.LocationSource
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class LocationDataRepository
@Inject constructor(
    private val locationDataSource: LocationSource
) : LocationRepository {

    override fun startTracking() : Flowable<LatLng> {
        return locationDataSource.startTracking()
            .flatMap {
                LocationMapper.transformFlowable(it)
            }
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

    override fun getLastKnownLocation(): Single<LatLng> {
        return locationDataSource.getBestLastKnownLocation()
            .flatMapSingle {
                LocationMapper.transformSingle(it)
            }
    }

}