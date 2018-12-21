package com.whisker.mrr.xrunner.domain.source

import android.location.Location
import io.reactivex.Flowable
import io.reactivex.Maybe

interface LocationSource {

    fun startTracking() : Flowable<Location>
    fun pauseTracking()
    fun resumeTracking()
    fun stopTracking()
    fun getBestLastKnownLocation() : Maybe<Location>
}