package com.whisker.mrr.xrunner.domain.mapper

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable
import io.reactivex.Single

class LocationMapper {

    companion object {
        fun transformFlowable(location: Location) : Flowable<LatLng> {
            return Flowable.just(LatLng(location.latitude, location.longitude))
        }

        fun transformSingle(location: Location) : Single<LatLng> {
            return Single.just(LatLng(location.latitude, location.longitude))
        }
    }
}