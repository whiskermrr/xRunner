package com.whisker.mrr.xrunner.presentation.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RouteModel(
    var name: String = "",
    var routeId: String = "",
    var waypoints: MutableList<LatLng> = mutableListOf(),
    var routeStats: RouteStatsModel = RouteStatsModel(),
    var date: Long = 0L)
: Parcelable