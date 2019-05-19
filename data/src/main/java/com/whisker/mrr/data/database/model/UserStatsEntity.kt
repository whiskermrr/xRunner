package com.whisker.mrr.data.database.model

data class UserStatsEntity(
    var averagePace: Float? = null,
    var experience: Int? = null,
    var totalDistance: Float? = null,
    var totalTime: Long? = null
)