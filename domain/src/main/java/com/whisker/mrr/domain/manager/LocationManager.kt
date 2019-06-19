package com.whisker.mrr.domain.manager

import com.whisker.mrr.domain.model.Coords
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

interface LocationManager {

    fun startTracking() : Flowable<Coords>
    fun pauseTracking() : Completable
    fun resumeTracking() : Completable
    fun stopTracking()
    fun getBestLastKnownLocation() : Maybe<Coords>
}