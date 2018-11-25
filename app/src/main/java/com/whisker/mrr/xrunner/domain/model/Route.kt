package com.whisker.mrr.xrunner.domain.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Route(
    val name: String,
    var waypoints: List<LatLng> = listOf(),
    var routeStats: RouteStats = RouteStats()) : Parcelable