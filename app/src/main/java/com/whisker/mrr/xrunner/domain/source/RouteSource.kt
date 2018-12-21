package com.whisker.mrr.xrunner.domain.source

import com.whisker.mrr.xrunner.domain.model.RouteEntity
import com.whisker.mrr.xrunner.domain.model.RouteEntityHolder
import io.reactivex.Completable
import io.reactivex.Flowable

interface RouteSource {

    fun saveRoute(route: RouteEntity, userId : String) : Completable
    fun getRoutesByUserId(userId: String) : Flowable<List<RouteEntityHolder>>
}