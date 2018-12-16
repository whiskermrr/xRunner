package com.whisker.mrr.xrunner.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RouteEntity(
    var name: String = "",
    var routeId: String = "",
    var waypoints: List<Coords> = listOf(),
    var routeStats: RouteStatsEntity = RouteStatsEntity(),
    var date: Long = 0L)
    : Parcelable