package com.whisker.mrr.xrunner.domain.repository

import android.graphics.Bitmap
import com.whisker.mrr.xrunner.domain.model.Route
import io.reactivex.Completable

interface RouteRepository {

    fun saveRoute(route: Route) : Completable
    fun saveSnapshot(bitmap: Bitmap, fileName: String) : Completable
}