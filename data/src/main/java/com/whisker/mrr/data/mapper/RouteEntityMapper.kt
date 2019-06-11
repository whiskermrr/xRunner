package com.whisker.mrr.data.mapper

import com.whisker.mrr.data.database.model.RouteEntity
import com.whisker.mrr.domain.model.Route

object RouteEntityMapper {

    fun transformListFromEntities(routeEntities: List<RouteEntity>) : List<Route> {
        val routes = mutableListOf<Route>()
        for(entity in routeEntities) {
            routes.add(transformFromEntity(entity))
        }
        return routes
    }

    fun transformListToEntities(routes: List<Route>) : List<RouteEntity> {
        val entities = mutableListOf<RouteEntity>()
        for(route in routes) {
            entities.add(transformToEntity(route))
        }
        return entities
    }

    fun transformFromEntity(routeEntity: RouteEntity) : Route {
        val route = Route()
        routeEntity.routeId?.let { route.routeId = it }
        routeEntity.name?.let { route.name = it }
        routeEntity.waypoints?.let { route.waypoints = it }
        routeEntity.routeStats?.let { route.routeStats = it }
        routeEntity.date?.let { route.date = it }

        return route
    }

    fun transformToEntity(route: Route) : RouteEntity {
        val routeEntity = RouteEntity()
        routeEntity.routeId = route.routeId
        routeEntity.name = route.name
        routeEntity.waypoints = route.waypoints
        routeEntity.routeStats = route.routeStats
        routeEntity.date = route.date

        return routeEntity
    }
}