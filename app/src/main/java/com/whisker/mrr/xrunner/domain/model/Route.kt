package com.whisker.mrr.xrunner.domain.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Route(
    private val name: String,
    private var waypoints: List<LatLng> = listOf(),
    private var routeStats: RouteStats = RouteStats()) : Parcelable