package com.whisker.mrr.data.source

import com.whisker.mrr.domain.model.Route
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface LocalRouteSource {
    fun saveRoute(route: Route) : Single<Long>
    fun saveRoutes(routes: List<Route>) : Completable
    fun getRoutes() : Flowable<List<Route>>
    fun markRouteAsDeleted(routeID: Long) : Completable
    fun removeRouteById(routeID: Long) : Completable
    fun updateRouteID(oldID: Long, newID: Long) : Completable
    fun getRoutesSavedLocallyAndDeleted() : Single<List<Route>>
    fun removeLocallySavedRoutes() : Completable
}