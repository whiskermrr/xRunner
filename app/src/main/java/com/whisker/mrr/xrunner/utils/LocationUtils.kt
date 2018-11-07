package com.whisker.mrr.xrunner.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.domain.model.RouteStats

class LocationUtils {

    companion object {

        private const val MILLISECONDS_PER_HOUR = 3600000
        private const val MILLISECONDS_PER_MINUTE = 60000
        private const val MILLISECONDS_PER_SECOND = 1000
        private const val MINUTES_PER_HOUR = 60

        private fun calculateWGS84Distance(firstCoords: LatLng, secondCoords: LatLng) : Float {
            val firstLocation = Location("A")
            val secondLocation = Location("B")

            firstLocation.latitude = firstCoords.latitude
            firstLocation.longitude = firstCoords.longitude

            secondLocation.latitude = secondCoords.latitude
            secondLocation.longitude = secondCoords.longitude

            return firstLocation.distanceTo(secondLocation)
        }

        fun calculateDistance(routeStats: RouteStats, firstCoords: LatLng, secondCoords: LatLng) : RouteStats {
            routeStats.wgs84distance += calculateWGS84Distance(firstCoords, secondCoords)
            routeStats.kilometers = (routeStats.wgs84distance / MILLISECONDS_PER_SECOND).toInt()
            routeStats.meters = (routeStats.wgs84distance - routeStats.kilometers * MILLISECONDS_PER_SECOND).toInt()

            return routeStats
        }

        fun calculateRouteStats(routeStats: RouteStats, firstCoords: LatLng, secondCoords: LatLng, time: Long) : RouteStats {
            calculateDistance(routeStats, firstCoords, secondCoords)

            routeStats.hours = (time / MILLISECONDS_PER_HOUR).toInt()
            routeStats.minutes = ((time % MILLISECONDS_PER_HOUR) / MILLISECONDS_PER_MINUTE).toInt()
            routeStats.seconds = ((time % MILLISECONDS_PER_MINUTE) / MILLISECONDS_PER_SECOND).toInt()

            val totalDistanceInMeters = routeStats.wgs84distance.toInt()
            val totalTimeInSeconds = (time / MILLISECONDS_PER_SECOND).toInt()

            routeStats.averageSpeed = (totalDistanceInMeters / totalTimeInSeconds) * 3.6f
            routeStats.pace = MINUTES_PER_HOUR / routeStats.averageSpeed

            return routeStats
        }
    }
}