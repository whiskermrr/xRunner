package com.whisker.mrr.xrunner.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RouteStatsEntity(
                 var routeTime: Long = 0L,
                 var averageSpeed: Float = 0f,
                 var paceMin: Int = 0,
                 var paceSec: Int = 0,
                 var wgs84distance: Float = 0f) : Parcelable