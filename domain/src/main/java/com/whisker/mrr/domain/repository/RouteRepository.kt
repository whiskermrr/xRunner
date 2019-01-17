package com.whisker.mrr.domain.repository

import com.whisker.mrr.domain.model.RouteEntity
import com.whisker.mrr.domain.model.RouteEntityHolder
import io.reactivex.Completable
import io.reactivex.Flowable

interface RouteRepository {

    fun saveRoute(userId: String, route: RouteEntity) : Completable
    fun saveSnapshot(bitmap: ByteArray, fileName: String) : Completable
    fun getRouteList(userId: String) : Flowable<List<RouteEntityHolder>>
    fun removeRoute(userId: String, routeId: String, date: Long) : Completable
}