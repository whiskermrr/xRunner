package com.whisker.mrr.xrunner.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RouteStats(var kilometers: Int = 0,
                 var meters: Int = 0,
                 var hours: Int = 0,
                 var minutes: Int = 0,
                 var seconds: Int = 0,
                 var averageSpeed: Float = 0f,
                 var paceMin: Int = 0,
                 var paceSec: Int = 0,
                 var wgs84distance: Float = 0f) : Parcelable