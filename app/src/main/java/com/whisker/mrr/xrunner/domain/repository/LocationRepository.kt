package com.whisker.mrr.xrunner.domain.repository

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable
import io.reactivex.Single

interface LocationRepository {

    fun startTracking() : Flowable<LatLng>
    fun pauseTracking()
    fun stopTracking()
    fun getLastKnownLocation() : Single<LatLng>
}