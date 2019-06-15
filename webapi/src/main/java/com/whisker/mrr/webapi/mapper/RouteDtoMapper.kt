package com.whisker.mrr.webapi.mapper

import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.model.RouteStats
import com.whisker.mrr.webapi.dto.RouteDto
import com.whisker.mrr.webapi.dto.RouteStatsDto

object RouteDtoMapper {

    fun transformListFromDtoList(routeDtoList: List<RouteDto>) : List<Route> {
        val routes = mutableListOf<Route>()
        for(entity in routeDtoList) {
            routes.add(transformFromDto(entity))
        }
        return routes
    }

    fun transformListToDtoList(routes: List<Route>) : List<RouteDto> {
        val routeDtoList = mutableListOf<RouteDto>()
        for(route in routes) {
            routeDtoList.add(transformToDto(route))
        }
        return routeDtoList
    }

    fun transformFromDto(routeDto: RouteDto) : Route {
        val route = Route()
        routeDto.routeId?.let { route.routeId = it }
        routeDto.name?.let { route.name = it }
        routeDto.waypoints?.let { route.waypoints = it }
        routeDto.date?.let { route.date = it }

        routeDto.routeStats?.let { statsDto ->
            val stats = RouteStats()
            statsDto.routeTime?.let { stats.routeTime = it }
            statsDto.averageSpeed?.let { stats.averageSpeed = it }
            statsDto.paceMin?.let { stats.paceMin = it }
            statsDto.paceSec?.let { stats.paceSec = it }
            statsDto.wgs84distance?.let { stats.wgs84distance = it }
            route.routeStats = stats
        }

        return route
    }

    fun transformToDto(route: Route) : RouteDto {
        val routeDto = RouteDto()
        routeDto.routeId = route.routeId
        routeDto.name = route.name
        routeDto.waypoints = route.waypoints
        routeDto.date = route.date

        val statsDto = RouteStatsDto()
        statsDto.routeTime = route.routeStats.routeTime
        statsDto.averageSpeed = route.routeStats.averageSpeed
        statsDto.paceMin = route.routeStats.paceMin
        statsDto.paceSec = route.routeStats.paceSec
        statsDto.wgs84distance = route.routeStats.wgs84distance

        routeDto.routeStats = statsDto

        return routeDto
    }
}