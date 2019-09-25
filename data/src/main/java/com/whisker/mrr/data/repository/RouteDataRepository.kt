package com.whisker.mrr.data.repository

import com.whisker.mrr.domain.common.mapper.RouteHolderMapper
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.model.RouteHolder
import com.whisker.mrr.domain.repository.RouteRepository
import com.whisker.mrr.data.source.LocalRouteSource
import com.whisker.mrr.data.source.RemoteRouteSource
import io.reactivex.Completable
import io.reactivex.Flowable

class RouteDataRepository(
    private val localRouteSource: LocalRouteSource,
    private val remoteRouteSource: RemoteRouteSource
) : RouteRepository {

    override fun saveRoute(route: Route): Completable {
        return localRouteSource.saveRoute(route)
            .flatMapCompletable { synchronizeRoutes() }
    }

    override fun getRouteList(): Flowable<List<RouteHolder>> {
        val localFlowable = localRouteSource.getRoutes()
            .map { RouteHolderMapper.transformToRouteHolderList(it) }

        val remoteFlowable =  synchronizeRoutes()
            .andThen(Flowable.empty<List<RouteHolder>>())

        return localFlowable.mergeWith(remoteFlowable)
    }

    override fun removeRoute(routeID: Long): Completable {
        return Completable.concatArray(
            localRouteSource.markRouteAsDeleted(routeID),
            remoteRouteSource.removeRouteById(routeID)
                .andThen(localRouteSource.removeRouteById(routeID))
        )
    }

    override fun synchronizeRoutes(): Completable {
        return localRouteSource.getRoutesSavedLocallyAndDeleted()
            .flatMapCompletable { remoteRouteSource.saveRoutes(it) }
            .andThen(remoteRouteSource.getRoutes())
            .flatMapCompletable { localRouteSource.saveRoutes(it) }
            .andThen(localRouteSource.removeLocallySavedRoutes())
            .onErrorComplete()
    }
}