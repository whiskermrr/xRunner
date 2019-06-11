package com.whisker.mrr.domain.model


data class Route(
    var name: String = "",
    var routeId: Long = -1L,
    var waypoints: List<Coords> = listOf(),
    var routeStats: RouteStats = RouteStats(),
    var date: Long = 0L
)