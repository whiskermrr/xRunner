package com.whisker.mrr.domain.source

import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.model.RouteHolder
import io.reactivex.Completable
import io.reactivex.Flowable

interface RouteSource {

    fun saveRoute(route: Route, userId : String) : Completable
    fun getRoutesByUserId(userId: String) : Flowable<List<RouteHolder>>
    fun removeRouteById(userId: String, routeId: String, date: Long) : Completable
}