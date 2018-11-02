package com.whisker.mrr.xrunner.domain

import android.location.Location
import com.whisker.mrr.xrunner.domain.model.RoutePoint
import io.reactivex.Flowable

class LocationMapper {

    companion object {
        fun transform(location: Location) : Flowable<RoutePoint> {
            return Flowable.just(RoutePoint(location.latitude, location.longitude))
        }
    }
}