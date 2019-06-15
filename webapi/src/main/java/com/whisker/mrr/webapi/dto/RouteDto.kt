package com.whisker.mrr.webapi.dto

import com.google.gson.annotations.SerializedName
import com.whisker.mrr.domain.model.Coords

data class RouteDto(

    @SerializedName("RouteID")
    var routeId: Long? = null,

    @SerializedName("Name")
    var name: String? = null,

    @SerializedName("Waypoints")
    var waypoints: List<Coords>? = null,

    @SerializedName("Date")
    var date: Long? = null,

    @SerializedName("IsDeleted")
    var isDeleted: Boolean = false,

    @SerializedName("RouteStats")
    var routeStats: RouteStatsDto? = null
)