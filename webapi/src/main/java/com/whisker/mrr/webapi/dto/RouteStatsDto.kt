package com.whisker.mrr.webapi.dto

import com.google.gson.annotations.SerializedName

data class RouteStatsDto(

    @SerializedName("RouteTime")
    var routeTime: Long? = null,

    @SerializedName("AverageSpeed")
    var averageSpeed: Float? = null,

    @SerializedName("PaceMin")
    var paceMin: Int? = null,

    @SerializedName("PaceSec")
    var paceSec: Int? = null,

    @SerializedName("Distance")
    var wgs84distance: Float? = null
)