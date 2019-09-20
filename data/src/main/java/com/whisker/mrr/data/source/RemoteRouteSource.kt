package com.whisker.mrr.data.source

import com.whisker.mrr.domain.model.Route
import io.reactivex.Completable
import io.reactivex.Single

interface RemoteRouteSource {
    fun saveRoute(route: Route) : Completable
    fun saveRoutes(routes: List<Route>) : Completable
    fun getRoutes() : Single<List<Route>>
    fun removeRouteById(routeId: Long) : Completable
}