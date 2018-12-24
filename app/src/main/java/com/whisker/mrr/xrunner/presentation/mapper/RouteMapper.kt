package com.whisker.mrr.xrunner.presentation.mapper

import com.whisker.mrr.xrunner.domain.model.RouteEntity
import com.whisker.mrr.xrunner.domain.model.RouteEntityHolder
import com.whisker.mrr.xrunner.domain.model.RouteStatsEntity
import com.whisker.mrr.xrunner.presentation.model.Route
import com.whisker.mrr.xrunner.presentation.model.RouteHolder
import com.whisker.mrr.xrunner.presentation.model.RouteStats
import com.whisker.mrr.xrunner.utils.DateUtils
import com.whisker.mrr.xrunner.utils.XRunnerConstants.MILLISECONDS_PER_HOUR
import com.whisker.mrr.xrunner.utils.XRunnerConstants.MILLISECONDS_PER_MINUTE
import com.whisker.mrr.xrunner.utils.XRunnerConstants.MILLISECONDS_PER_SECOND
import com.whisker.mrr.xrunner.utils.XRunnerConstants.MINUTES_PER_HOUR
import java.util.*

class RouteMapper {

    companion object {

        fun routeToEntityTransform(route: Route) : RouteEntity {
            val entityRoute = RouteEntity()
            entityRoute.date = route.date
            entityRoute.routeId = route.routeId
            entityRoute.name = route.name
            entityRoute.waypoints = LatLngMapper.latLngToCoordsTransform(route.waypoints)
            entityRoute.routeStats = statsToEntityTransform(route.routeStats)

            return entityRoute
        }

        private fun statsToEntityTransform(routeStats: RouteStats) : RouteStatsEntity {
            val entityStats = RouteStatsEntity()
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

        private fun entityToRouteTransform(entityRoute: RouteEntity) : Route {
            val route = Route()
            route.date = entityRoute.date
            route.routeId = entityRoute.routeId
            route.name = entityRoute.name
            route.waypoints = LatLngMapper.coordsToLatLngTransform(entityRoute.waypoints)
            route.routeStats = entityToStatsTransform(entityRoute.routeStats)

            return route
        }

        private fun entityToStatsTransform(entityStats: RouteStatsEntity) : RouteStats {
            val routeStats = RouteStats()
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

        private fun listOfEntityToListOfRoutesTransform(entityRoutes: List<RouteEntity>) : MutableList<Route> {
            val routes = mutableListOf<Route>()
            for(entity in entityRoutes) {
                val route = entityToRouteTransform(entity)
                routes.add(route)
            }

            return routes
        }

        fun listOfEntityHoldersToListOfRouteHolders(entityHolders: List<RouteEntityHolder>) : List<RouteHolder> {
            val holders = mutableListOf<RouteHolder>()
            for(entityHolder in entityHolders) {
                val holder = entityHolderToRouteHolder(entityHolder)
                holders.add(holder)
            }

            return holders
        }

        private fun entityHolderToRouteHolder(entityHolder: RouteEntityHolder) : RouteHolder {
            val routeHolder = RouteHolder()
            routeHolder.routes = listOfEntityToListOfRoutesTransform(entityHolder.routes)
            routeHolder.month = DateUtils.formatDate(entityHolder.month, DateUtils.MMM_yyyy)

            val kilometers = (entityHolder.totalDistance / 1000).toInt()
            val meters = (entityHolder.totalDistance - kilometers * 1000).toInt() / 10
            routeHolder.totalDistance = String.format(Locale.getDefault(), "%d.%02dkm", kilometers, meters)

            val time = entityHolder.totalTime
            val hours = (time / MILLISECONDS_PER_HOUR).toInt()
            val minutes = ((time % MILLISECONDS_PER_HOUR) / MILLISECONDS_PER_MINUTE).toInt()
            routeHolder.totalTime = String.format(Locale.getDefault(), "%dh%dm", hours, minutes)

            val sumOfAverageSpeed = entityHolder.routes.map { it.routeStats.averageSpeed }.sum()
            val pace = MINUTES_PER_HOUR / (sumOfAverageSpeed / entityHolder.routes.size)
            val paceMin = pace.toInt()
            val paceSec = (pace % 1 * 60).toInt()
            routeHolder.averagePace = String.format(Locale.getDefault(), "%d'%d''", paceMin, paceSec)

            return routeHolder
        }
    }
}