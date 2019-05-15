package com.whisker.mrr.domain.source

import com.whisker.mrr.domain.model.Route
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface LocalRouteSource {
    fun saveRoute(route: Route) : Single<Long>
    fun saveRoutes(routes: List<Route>) : Completable
    fun getRoutes() : Flowable<List<Route>>
    fun removeRouteById(routeID: Long) : Completable
    fun updateRouteID(oldID: Long, newID: Long) : Completable
}