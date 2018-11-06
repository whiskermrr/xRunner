package com.whisker.mrr.xrunner.domain.model

import com.google.android.gms.maps.model.LatLng

data class Route(val name: String) {

    constructor(routeName: String, waypoints: List<LatLng>, routeStats: RouteStats) : this(routeName) {
        this.waypoints = waypoints
        this.routeStats = routeStats
    }

    var waypoints: List<LatLng> = listOf()
    var routeStats: RouteStats = RouteStats()
}