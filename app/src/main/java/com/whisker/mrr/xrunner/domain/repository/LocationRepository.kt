package com.whisker.mrr.xrunner.domain.repository

import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.domain.model.Route
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface LocationRepository {

    fun startTracking() : Flowable<LatLng>
    fun pauseTracking()
    fun resumeTracking()
    fun stopTracking(route: Route) : Completable
    fun getLastKnownLocation() : Single<LatLng>
}