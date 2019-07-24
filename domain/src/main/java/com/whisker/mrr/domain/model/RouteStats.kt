package com.whisker.mrr.domain.model

data class RouteStats(
     var routeTime: Long = 0L,
     var averageSpeed: Float = 0f,
     var paceMin: Int = 0,
     var paceSec: Int = 0,
     var wgs84distance: Float = 0f
)