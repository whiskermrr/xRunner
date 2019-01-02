package com.whisker.mrr.xrunner.domain.repository

import android.graphics.Bitmap
import com.whisker.mrr.xrunner.domain.model.RouteEntity
import com.whisker.mrr.xrunner.domain.model.RouteEntityHolder
import io.reactivex.Completable
import io.reactivex.Flowable

interface RouteRepository {

    fun saveRoute(userId: String, route: RouteEntity) : Completable
    fun saveSnapshot(bitmap: Bitmap, fileName: String) : Completable
    fun getRouteList(userId: String) : Flowable<List<RouteEntityHolder>>
    fun removeRoute(userId: String, routeId: String, date: Long) : Completable
}