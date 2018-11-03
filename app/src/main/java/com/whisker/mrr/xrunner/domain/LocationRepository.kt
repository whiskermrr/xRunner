package com.whisker.mrr.xrunner.domain

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable

interface LocationRepository {

    fun startTracking() : Flowable<LatLng>
    fun pauseTracking()
    fun stopTracking()
}