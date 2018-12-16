package com.whisker.mrr.xrunner.domain.model

data class RouteHolder(
    var month: String = "",
    var totalDistance: String = "",
    var totalTime: String = "",
    var averagePace: String = "",
    var routes: MutableList<Route> = mutableListOf()
)