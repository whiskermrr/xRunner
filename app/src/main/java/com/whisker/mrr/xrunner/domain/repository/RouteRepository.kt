package com.whisker.mrr.xrunner.domain.repository

import com.whisker.mrr.xrunner.domain.model.Route
import io.reactivex.Completable

interface RouteRepository {

    fun saveRoute(route: Route) : Completable
}