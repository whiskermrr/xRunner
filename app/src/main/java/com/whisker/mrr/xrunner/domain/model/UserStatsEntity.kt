package com.whisker.mrr.xrunner.domain.model

data class UserStatsEntity(
    var averagePace: Float = 0f,
    var experience: Int = 0,
    var totalDistance: Float = 0f,
    var totalTime: Long = 0L
)