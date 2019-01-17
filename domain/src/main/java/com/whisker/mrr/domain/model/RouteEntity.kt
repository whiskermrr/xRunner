package com.whisker.mrr.domain.model


data class RouteEntity(
    var name: String = "",
    var routeId: String = "",
    var waypoints: List<Coords> = listOf(),
    var routeStats: RouteStatsEntity = RouteStatsEntity(),
    var date: Long = 0L)