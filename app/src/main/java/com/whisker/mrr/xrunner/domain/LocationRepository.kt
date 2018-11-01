package com.whisker.mrr.xrunner.domain

import com.whisker.mrr.xrunner.domain.model.RoutePoint
import io.reactivex.Flowable

interface LocationRepository {

    fun startTracking() : Flowable<RoutePoint>
    fun pauseTracking()
    fun stopTracking()
}