package com.whisker.mrr.xrunner.data.model

data class RouteEntityHolder(
    var month: Long = 0L,
    var totalDistance: Float = 0f,
    var totalTime: Long = 0L,
    var routes: MutableList<RouteEntity> = mutableListOf()
    )