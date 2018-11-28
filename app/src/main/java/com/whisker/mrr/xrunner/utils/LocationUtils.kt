package com.whisker.mrr.xrunner.utils

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.domain.model.RouteStats
import kotlin.math.pow

class LocationUtils {

    companion object {

        private const val EQUATOR_LENGTH_IN_METERS = 40075004.0
        private const val EQUATOR_LENGTH_IN_PIXELS = 256
        private const val MAX_ZOOM = 19
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

        private fun calculateDistance(routeStats: RouteStats, firstCoords: LatLng, secondCoords: LatLng) {
            routeStats.wgs84distance += calculateWGS84Distance(firstCoords, secondCoords)
            routeStats.kilometers = (routeStats.wgs84distance / MILLISECONDS_PER_SECOND).toInt()
            routeStats.meters = (routeStats.wgs84distance - routeStats.kilometers * MILLISECONDS_PER_SECOND).toInt()
        }

        fun calculateRouteTime(routeStats: RouteStats, time: Long) : RouteStats {
            routeStats.hours = (time / MILLISECONDS_PER_HOUR).toInt()
            routeStats.minutes = ((time % MILLISECONDS_PER_HOUR) / MILLISECONDS_PER_MINUTE).toInt()
            routeStats.seconds = ((time % MILLISECONDS_PER_MINUTE) / MILLISECONDS_PER_SECOND).toInt()

            return routeStats
        }

        fun calculateRouteAverageSpeedAndPeace(routeStats: RouteStats, time: Long) {
            val totalDistanceInMeters = routeStats.wgs84distance.toInt()
            val totalTimeInSeconds = (time / MILLISECONDS_PER_SECOND).toInt()

            if(totalTimeInSeconds > 0 && totalDistanceInMeters > 0) {
                routeStats.averageSpeed = (totalDistanceInMeters.toFloat() / totalTimeInSeconds) * 3.6f
                val pace = MINUTES_PER_HOUR / routeStats.averageSpeed
                routeStats.paceMin = pace.toInt()
                routeStats.paceSec = (pace % 1 * 60).toInt()
            }
        }

        fun calculateRouteStats(routeStats: RouteStats, firstCoords: LatLng, secondCoords: LatLng, time: Long) : RouteStats {
            calculateDistance(routeStats, firstCoords, secondCoords)
            calculateRouteAverageSpeedAndPeace(routeStats, time)

            return routeStats
        }

        private fun calculateMiddleLatLng(firstLocation: Location, secondLocation: Location) : LatLng {

            val averageLat = (firstLocation.latitude + secondLocation.latitude) / 2
            val averageLng =(firstLocation.longitude + secondLocation.longitude) / 2

            return LatLng(averageLat, averageLng)
        }

        fun getDistanceBetweenMostDistinctPoints(points: List<LatLng>) : Pair<LatLng, Float> {
            var minLat = points[0].latitude
            var maxLat = points[0].latitude
            var minLng = points[0].longitude
            var maxLng = points[0].longitude

            for(point in points) {
                if(point.latitude > maxLat) {
                    maxLat = point.latitude
                } else if(point.latitude < minLat) {
                    minLat = point.latitude
                }

                if(point.longitude > maxLng) {
                    maxLng = point.longitude
                } else if(point.longitude < minLng) {
                    minLng = point.longitude
                }
            }

            val minLocation = Location("A")
            val maxLocation = Location("B")

            minLocation.latitude = minLat
            minLocation.longitude = minLng

            maxLocation.latitude = maxLat
            maxLocation.longitude = maxLng

            val centerOfRoute = calculateMiddleLatLng(minLocation, maxLocation)

            return Pair(centerOfRoute, minLocation.distanceTo(maxLocation))
        }

        fun getZoomBasedOnDistance(distance: Float, screenWidth: Int) : Float {
            var metersPerPixel = EQUATOR_LENGTH_IN_METERS / (EQUATOR_LENGTH_IN_PIXELS * 2.0.pow(MAX_ZOOM))
            var currentZoom = MAX_ZOOM
            var visibleDistance: Double = metersPerPixel * screenWidth

            while(visibleDistance < distance) {
                metersPerPixel *= 2
                visibleDistance = metersPerPixel * screenWidth
                currentZoom--
            }

            val ratio = (distance - visibleDistance / 2) / (visibleDistance / 2)
            val zoomRatio = if(ratio > 0.5) {
                currentZoom - 1.0 - ratio
            } else {
                currentZoom - ratio - 1.0
            }

            return zoomRatio.toFloat()
        }
    }
}