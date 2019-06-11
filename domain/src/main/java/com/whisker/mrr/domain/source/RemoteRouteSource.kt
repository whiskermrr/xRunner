package com.whisker.mrr.domain.source

import com.whisker.mrr.domain.model.Route
import io.reactivex.Completable
import io.reactivex.Single

interface RemoteRouteSource {
    fun saveRoute(route: Route) : Single<Long>
    fun saveRoutes(routes: List<Route>) : Single<List<Long>>
    fun getRoutes() : Single<List<Route>>
    fun removeRouteById(routeId: Long) : Completable
}