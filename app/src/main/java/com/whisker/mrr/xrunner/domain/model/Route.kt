package com.whisker.mrr.xrunner.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Route(
    var name: String = "",
    var routeId: String = "",
    var waypoints: List<Coords> = listOf(),
    var routeStats: RouteStats = RouteStats(),
    var date: Long = 0L)
: Parcelable