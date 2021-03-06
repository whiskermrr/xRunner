package com.whisker.mrr.xrunner.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.domain.common.utils.DomainConstants.MILLISECONDS_PER_HOUR
import com.whisker.mrr.domain.common.utils.DomainConstants.MILLISECONDS_PER_MINUTE
import com.whisker.mrr.domain.common.utils.DomainConstants.MILLISECONDS_PER_SECOND
import com.whisker.mrr.domain.common.utils.DomainConstants.MINUTES_PER_HOUR
import com.whisker.mrr.xrunner.presentation.model.RouteStatsModel

object LocationUtils {

    private fun calculateWGS84Distance(firstCoords: LatLng, secondCoords: LatLng) : Float {
        val firstLocation = Location("A")
        val secondLocation = Location("B")

        firstLocation.latitude = firstCoords.latitude
        firstLocation.longitude = firstCoords.longitude

        secondLocation.latitude = secondCoords.latitude
        secondLocation.longitude = secondCoords.longitude

        return firstLocation.distanceTo(secondLocation)
    }

    private fun calculateDistance(routeStats: RouteStatsModel, firstCoords: LatLng, secondCoords: LatLng) {
        routeStats.wgs84distance += calculateWGS84Distance(firstCoords, secondCoords)
        routeStats.kilometers = (routeStats.wgs84distance / MILLISECONDS_PER_SECOND).toInt()
        routeStats.meters = (routeStats.wgs84distance - routeStats.kilometers * MILLISECONDS_PER_SECOND).toInt()
    }

    fun calculateRouteTime(routeStats: RouteStatsModel, time: Long) : RouteStatsModel {
        routeStats.hours = (time / MILLISECONDS_PER_HOUR).toInt()
        routeStats.minutes = ((time % MILLISECONDS_PER_HOUR) / MILLISECONDS_PER_MINUTE).toInt()
        routeStats.seconds = ((time % MILLISECONDS_PER_MINUTE) / MILLISECONDS_PER_SECOND).toInt()

        return routeStats
    }

    fun calculateRouteAverageSpeedAndPeace(routeStats: RouteStatsModel, time: Long) {
        val totalDistanceInMeters = routeStats.wgs84distance.toInt()
        val totalTimeInSeconds = (time / MILLISECONDS_PER_SECOND).toInt()

        if(totalTimeInSeconds > 0 && totalDistanceInMeters > 0) {
            routeStats.averageSpeed = (totalDistanceInMeters.toFloat() / totalTimeInSeconds) * 3.6f
            val pace = MINUTES_PER_HOUR / routeStats.averageSpeed
            routeStats.paceMin = pace.toInt()
            routeStats.paceSec = (pace % 1 * 60).toInt()
        }
    }

    fun calculateRouteStats(routeStats: RouteStatsModel, firstCoords: LatLng, secondCoords: LatLng, time: Long) : RouteStatsModel {
        calculateDistance(routeStats, firstCoords, secondCoords)
        calculateRouteAverageSpeedAndPeace(routeStats, time)

        return routeStats
    }
}