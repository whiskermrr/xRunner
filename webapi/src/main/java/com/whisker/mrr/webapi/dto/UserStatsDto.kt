package com.whisker.mrr.webapi.dto

import com.google.gson.annotations.SerializedName

data class UserStatsDto(

    @SerializedName("UserID")
    var userID: String,

    @SerializedName("AveragePace")
    var averagePace: Float? = null,

    @SerializedName("Experience")
    var experience: Int? = null,

    @SerializedName("TotalDistance")
    var totalDistance: Float? = null,

    @SerializedName("TotalTime")
    var totalTime: Long? = null
)