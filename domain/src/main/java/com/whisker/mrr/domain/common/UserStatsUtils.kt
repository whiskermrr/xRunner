package com.whisker.mrr.domain.common

import com.whisker.mrr.domain.model.RouteStats
import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.domain.model.UserStatsProgress
import kotlin.math.roundToInt

object UserStatsUtils {

    fun updateUserStats(userStats: UserStats, statsProgress: UserStatsProgress) : UserStats {
        userStats.totalDistance += statsProgress.distanceProgress
        userStats.totalTime += statsProgress.timeProgress
        userStats.experience += statsProgress.expProgress
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

    fun getUserStatsProgress(routeStats: RouteStats, exp: Int) : UserStatsProgress {
        val statsProgress = UserStatsProgress()
        statsProgress.distanceProgress = routeStats.wgs84distance
        statsProgress.timeProgress = routeStats.routeTime
        statsProgress.expProgress = calculateExp(routeStats)
        statsProgress.expProgress += exp
        return statsProgress
    }
}