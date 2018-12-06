package com.whisker.mrr.xrunner.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Route(
    val name: String = "",
    var routeId: String = "",
    var waypoints: List<Coords> = listOf(),
    var routeStats: RouteStats = RouteStats())
: Parcelable