package com.whisker.mrr.xrunner.domain.model

data class RoutePoint(val latitude: Double, val longitude: Double) {

    constructor() : this(
        latitude = 0.0,
        longitude = 0.0
    )
}