package com.whisker.mrr.domain.source

import com.whisker.mrr.domain.model.Coords
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

interface LocationSource {

    fun startTracking() : Flowable<Coords>
    fun pauseTracking() : Completable
    fun resumeTracking() : Completable
    fun stopTracking()
    fun getBestLastKnownLocation() : Maybe<Coords>
}