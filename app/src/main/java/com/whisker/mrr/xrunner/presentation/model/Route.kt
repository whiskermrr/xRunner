package com.whisker.mrr.xrunner.presentation.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Route(
    var name: String = "",
    var routeId: String = "",
    var waypoints: List<LatLng> = listOf(),
    var routeStats: RouteStats = RouteStats(),
    var date: Long = 0L)
: Parcelable