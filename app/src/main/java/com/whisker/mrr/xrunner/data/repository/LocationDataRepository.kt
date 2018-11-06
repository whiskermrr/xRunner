package com.whisker.mrr.xrunner.data.repository

import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.data.datasource.DatabaseSource
import com.whisker.mrr.xrunner.data.datasource.LocationDataSource
import com.whisker.mrr.xrunner.data.datasource.UserDataSource
import com.whisker.mrr.xrunner.domain.mapper.LocationMapper
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.domain.repository.LocationRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class LocationDataRepository
@Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val userDataSource: UserDataSource,
    private val databaseSource: DatabaseSource
) : LocationRepository {

    override fun startTracking() : Flowable<LatLng> {
        return locationDataSource.startTracking()
            .flatMap {
                LocationMapper.transformFlowable(it)
            }
    }

    override fun pauseTracking() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopTracking(route: Route) : Completable {
        locationDataSource.stopTracking()
        return userDataSource.getUserId()
            .flatMapCompletable {
                databaseSource.saveRoute(route, it)
            }
    }

    override fun getLastKnownLocation(): Single<LatLng> {
        return locationDataSource.getBestLastKnownLocation()
            .flatMapSingle {
                LocationMapper.transformSingle(it)
            }
    }

}