package com.whisker.mrr.xrunner.domain.mapper

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable

class LocationMapper {

    companion object {
        fun transform(location: Location) : Flowable<LatLng> {
            return Flowable.just(LatLng(location.latitude, location.longitude))
        }
    }
}