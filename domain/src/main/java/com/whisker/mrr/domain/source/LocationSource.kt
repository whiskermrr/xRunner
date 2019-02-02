package com.whisker.mrr.domain.source

import com.whisker.mrr.domain.model.Coords
import io.reactivex.Flowable
import io.reactivex.Maybe

interface LocationSource {

    fun startTracking() : Flowable<Coords>
    fun pauseTracking()
    fun resumeTracking()
    fun stopTracking()
    fun getBestLastKnownLocation() : Maybe<Coords>
}