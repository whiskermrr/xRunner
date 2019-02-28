package com.whisker.mrr.domain.common

import com.whisker.mrr.domain.model.RouteStats
import com.whisker.mrr.domain.model.UserStats
import kotlin.math.roundToInt

object UserStatsUtils {

    fun updateUserStats(userStats: UserStats, routeStats: RouteStats) : UserStats {
        userStats.totalDistance += routeStats.wgs84distance
        userStats.totalTime += routeStats.routeTime
        userStats.experience += calculateExp(routeStats)
        userStats.averagePace = calculateAveragePace(userStats.totalDistance, userStats.totalTime)

        return userStats
    }

    fun calculateExp(stats: RouteStats) : Int {
        return ((stats.averageSpeed * stats.wgs84distance) / 10f).roundToInt()
    }

    fun calculateAveragePace(totalDistanceInMeters: Float, time: Long) : Float {
        val totalTimeInSeconds = (time / DomainConstants.MILLISECONDS_PER_SECOND).toInt()
        val averagePace = DomainConstants.MINUTES_PER_HOUR / ((totalDistanceInMeters / totalTimeInSeconds) * 3.6f)
        return Math.round(averagePace * 100.0f) / 100.0f
    }
}