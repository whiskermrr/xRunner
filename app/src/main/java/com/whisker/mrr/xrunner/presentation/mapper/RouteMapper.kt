package com.whisker.mrr.xrunner.presentation.mapper

import com.whisker.mrr.domain.common.DomainConstants.MILLISECONDS_PER_SECOND
import com.whisker.mrr.domain.common.DomainConstants.MINUTES_PER_HOUR
import com.whisker.mrr.domain.common.DomainConstants.MMM_yyyy
import com.whisker.mrr.domain.common.formatDate
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.model.RouteHolder
import com.whisker.mrr.domain.model.RouteStats
import com.whisker.mrr.xrunner.presentation.model.RouteModel
import com.whisker.mrr.xrunner.presentation.model.RouteHolderModel
import com.whisker.mrr.xrunner.presentation.model.RouteStatsModel
import com.whisker.mrr.xrunner.utils.KILOMETERS
import com.whisker.mrr.xrunner.utils.METERS
import com.whisker.mrr.xrunner.utils.toDistance
import java.util.*

class RouteMapper {

    companion object {

        fun routeToEntityTransform(route: RouteModel) : Route {
            val entityRoute = Route()
            entityRoute.date = route.date
            entityRoute.routeId = route.routeId
            entityRoute.name = route.name
            entityRoute.waypoints = LatLngMapper.latLngToCoordsTransform(route.waypoints)
            entityRoute.routeStats = statsToEntityTransform(route.routeStats)

            return entityRoute
        }

        private fun statsToEntityTransform(routeStats: RouteStatsModel) : RouteStats {
            val entityStats = RouteStats()
            entityStats.wgs84distance = routeStats.wgs84distance
            entityStats.averageSpeed = routeStats.averageSpeed
            entityStats.paceMin = routeStats.paceMin
            entityStats.paceSec = routeStats.paceSec

            var routeTime = 0L
            routeTime += routeStats.hours * 1000 * 60 * 60
            routeTime += routeStats.minutes * 1000 * 60
            routeTime += routeStats.seconds * 1000
            entityStats.routeTime = routeTime

            return entityStats
        }

        private fun entityToRouteTransform(entityRoute: Route) : RouteModel {
            val route = RouteModel()
            route.date = entityRoute.date
            route.routeId = entityRoute.routeId
            route.name = entityRoute.name
            route.waypoints = LatLngMapper.coordsToLatLngTransform(entityRoute.waypoints)
            route.routeStats = entityToStatsTransform(entityRoute.routeStats)

            return route
        }

        private fun entityToStatsTransform(entityStats: RouteStats) : RouteStatsModel {
            val routeStats = RouteStatsModel()
            routeStats.wgs84distance = entityStats.wgs84distance
            routeStats.averageSpeed = entityStats.averageSpeed
            routeStats.paceMin = entityStats.paceMin
            routeStats.paceSec = entityStats.paceSec

            routeStats.kilometers = (routeStats.wgs84distance / MILLISECONDS_PER_SECOND).toInt()
            routeStats.meters = (routeStats.wgs84distance - routeStats.kilometers * MILLISECONDS_PER_SECOND).toInt()

            val seconds = entityStats.routeTime / 1000 % 60
            val minutes = entityStats.routeTime / (1000 * 60) % 60
            val hours = entityStats.routeTime / (1000 * 60 * 60) % 24

            routeStats.seconds = seconds.toInt()
            routeStats.minutes = minutes.toInt()
            routeStats.hours = hours.toInt()

            return routeStats
        }

        private fun listOfEntityToListOfRoutesTransform(entityRoutes: List<Route>) : MutableList<RouteModel> {
            val routes = mutableListOf<RouteModel>()
            for(entity in entityRoutes) {
                val route = entityToRouteTransform(entity)
                routes.add(route)
            }

            return routes
        }

        fun listOfEntityHoldersToListOfRouteHolders(holders: List<RouteHolder>) : List<RouteHolderModel> {
            val holdersModel = mutableListOf<RouteHolderModel>()
            for(entityHolder in holders) {
                val holder = entityHolderToRouteHolder(entityHolder)
                holdersModel.add(holder)
            }

            return holdersModel
        }

        private fun entityHolderToRouteHolder(holder: RouteHolder) : RouteHolderModel {
            val routeHolder = RouteHolderModel()
            routeHolder.routes = listOfEntityToListOfRoutesTransform(holder.routes)
            routeHolder.month = Date(holder.month).formatDate(MMM_yyyy)

            val distanceMap = holder.totalDistance.toDistance()
            routeHolder.totalDistance =
                    String.format(Locale.getDefault(), "%d.%02dkm", distanceMap[Float.KILOMETERS], distanceMap[Float.METERS])

            val calendar = Calendar.getInstance()
            calendar.time = Date(holder.totalTime)
            calendar.get(Calendar.MINUTE)
            val hours = calendar.get(Calendar.HOUR) - 1
            val minutes = calendar.get(Calendar.MINUTE)
            routeHolder.totalTime = String.format(Locale.getDefault(), "%dh%dm", hours, minutes)

            val sumOfAverageSpeed = holder.routes.map { it.routeStats.averageSpeed }.sum()
            val pace = MINUTES_PER_HOUR / (sumOfAverageSpeed / holder.routes.size)
            val paceMin = pace.toInt()
            val paceSec = (pace % 1 * 60).toInt()
            routeHolder.averagePace = String.format(Locale.getDefault(), "%d'%d''", paceMin, paceSec)

            return routeHolder
        }
    }
}