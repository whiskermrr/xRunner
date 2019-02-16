package com.whisker.mrr.domain.common

import com.whisker.mrr.domain.model.RouteStatsEntity
import com.whisker.mrr.domain.model.UserStatsEntity
import kotlin.math.roundToInt

object UserStatsUtils {

    fun updateUserStats(userStats: UserStatsEntity, routeStats: RouteStatsEntity) : UserStatsEntity {
        userStats.totalDistance += routeStats.wgs84distance
        userStats.totalTime += routeStats.routeTime
        userStats.experience += calculateExp(routeStats)
        userStats.averagePace = calculateAveragePace(userStats.totalDistance, userStats.totalTime)

        return userStats
    }

    fun calculateExp(stats: RouteStatsEntity) : Int {
        return ((stats.averageSpeed * stats.wgs84distance) / 10f).roundToInt()
    }

    fun calculateAveragePace(totalDistanceInMeters: Float, time: Long) : Float {
        val totalTimeInSeconds = (time / DomainConstants.MILLISECONDS_PER_SECOND).toInt()
        return DomainConstants.MINUTES_PER_HOUR / ((totalDistanceInMeters / totalTimeInSeconds) * 3.6f)
    }
}