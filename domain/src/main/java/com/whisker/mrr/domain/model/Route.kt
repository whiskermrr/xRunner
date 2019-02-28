package com.whisker.mrr.domain.model


data class Route(
    var name: String = "",
    var routeId: String = "",
    var waypoints: List<Coords> = listOf(),
    var routeStats: RouteStats = RouteStats(),
    var date: Long = 0L)