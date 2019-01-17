package com.whisker.mrr.domain.repository

import com.whisker.mrr.domain.model.Coords
import io.reactivex.Flowable
import io.reactivex.Single

interface LocationRepository {

    fun startTracking() : Flowable<Coords>
    fun pauseTracking()
    fun resumeTracking()
    fun stopTracking()
    fun getLastKnownLocation() : Single<Coords>
}