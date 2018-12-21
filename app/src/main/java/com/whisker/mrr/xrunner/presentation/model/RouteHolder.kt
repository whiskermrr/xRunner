package com.whisker.mrr.xrunner.presentation.model

data class RouteHolder(
    var month: String = "",
    var totalDistance: String = "",
    var totalTime: String = "",
    var averagePace: String = "",
    var routes: MutableList<Route> = mutableListOf()
)