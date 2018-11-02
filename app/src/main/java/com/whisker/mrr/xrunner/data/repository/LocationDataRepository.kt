package com.whisker.mrr.xrunner.data.repository

import com.whisker.mrr.xrunner.data.datasource.LocationDataSource
import com.whisker.mrr.xrunner.data.datasource.UserDataSource
import com.whisker.mrr.xrunner.domain.LocationMapper
import com.whisker.mrr.xrunner.domain.LocationRepository
import com.whisker.mrr.xrunner.domain.model.RoutePoint
import io.reactivex.Flowable
import javax.inject.Inject

class LocationDataRepository
@Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val userDataSource: UserDataSource
) : LocationRepository {

    override fun startTracking() : Flowable<RoutePoint> {
        return locationDataSource.startTracking()
            .flatMap {
                LocationMapper.transform(it)
            }
    }

    override fun pauseTracking() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopTracking() {
        locationDataSource.stopTracking()
    }

}