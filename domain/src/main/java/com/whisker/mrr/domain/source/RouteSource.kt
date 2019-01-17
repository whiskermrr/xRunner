package com.whisker.mrr.domain.source

import com.whisker.mrr.domain.model.RouteEntity
import com.whisker.mrr.domain.model.RouteEntityHolder
import io.reactivex.Completable
import io.reactivex.Flowable

interface RouteSource {

    fun saveRoute(route: RouteEntity, userId : String) : Completable
    fun getRoutesByUserId(userId: String) : Flowable<List<RouteEntityHolder>>
    fun removeRouteById(userId: String, routeId: String, date: Long) : Completable
}