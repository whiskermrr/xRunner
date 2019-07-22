package com.whisker.mrr.domain

import com.whisker.mrr.domain.common.utils.UserStatsUtils
import com.whisker.mrr.domain.model.RouteStats
import com.whisker.mrr.domain.model.UserStats
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserStatsUtilsUnitTest {

    private lateinit var routeStats: RouteStats
    private lateinit var userStats: UserStats

    @Before
    fun setUp() {
        routeStats = RouteStats(
            routeTime = 36000,
            averageSpeed = 9.0f,
            paceMin = 6,
            paceSec = 30,
            wgs84distance = 120f
        )

        userStats = UserStats(
            averagePace = 0f,
            experience = 0,
            totalDistance = 0f,
            totalTime = 0L
        )
    }

    @Test
    fun updateUserStatsTest() {
        val statsProgress = UserStatsUtils.getUserStatsProgress(routeStats, 0)
        UserStatsUtils.updateUserStats(userStats, statsProgress)

        Assert.assertEquals(userStats.totalTime, 36000)
        Assert.assertEquals(userStats.averagePace, 5.0f)
        Assert.assertEquals(userStats.totalDistance, 120f)
    }

    @Test
    fun calculateExpTest() {
        val exp = UserStatsUtils.calculateExp(routeStats)
        Assert.assertEquals(exp, 108)
    }

    @Test
    fun calculateAveragePaceTest() {
        userStats.totalDistance = 120f
        userStats.totalTime = 36000
        val averageSpeed = UserStatsUtils.calculateAveragePace(userStats.totalDistance, userStats.totalTime)
        Assert.assertEquals(averageSpeed, 5.0f)
    }
}