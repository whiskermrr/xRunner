package com.whisker.mrr.domain.repository

import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.model.RouteHolder
import io.reactivex.Completable
import io.reactivex.Flowable

interface RouteRepository {
    fun saveRoute(route: Route) : Completable
    fun saveSnapshot(bitmap: ByteArray, fileName: String) : Completable
    fun getRouteList() : Flowable<List<RouteHolder>>
    fun removeRoute(routeID: Long) : Completable
}