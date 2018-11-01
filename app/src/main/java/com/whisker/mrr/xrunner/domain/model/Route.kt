package com.whisker.mrr.xrunner.domain.model

data class Route(val routeName: String) {

    var waypoints: List<RoutePoint> = listOf()
    var routeStats: RouteStats = RouteStats()
}