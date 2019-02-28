package com.whisker.mrr.infrastructure

import com.whisker.mrr.domain.common.UserStatsUtils
import com.whisker.mrr.domain.model.RouteStats
import com.whisker.mrr.domain.model.UserStats
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserStatsUtilsUnitTest {

    lateinit var routeStats: RouteStats
    lateinit var userStats: UserStats

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
        UserStatsUtils.updateUserStats(userStats, routeStats)

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