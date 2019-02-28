package com.whisker.mrr.domain.repository

import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.model.RouteHolder
import io.reactivex.Completable
import io.reactivex.Flowable

interface RouteRepository {

    fun saveRoute(userId: String, route: Route) : Completable
    fun saveSnapshot(bitmap: ByteArray, fileName: String) : Completable
    fun getRouteList(userId: String) : Flowable<List<RouteHolder>>
    fun removeRoute(userId: String, routeId: String, date: Long) : Completable
}