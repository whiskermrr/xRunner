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
            .flatMapCompletable { localID ->
                route.routeId = localID
                remoteRouteSource.saveRoute(route)
                    .flatMapCompletable { localRouteSource.updateRouteID(localID, it) }
            }
    }

    override fun getRouteList(): Flowable<List<RouteHolder>> {
        val localFlowable = localRouteSource.getRoutes()
            .map { RouteHolderMapper.transformToRouteHolderList(it) }

        val remoteFlowable = remoteRouteSource.getRoutes()
            .flatMapCompletable { routes ->
                localRouteSource.saveRoutes(routes)
            }.andThen(Flowable.empty<List<RouteHolder>>())

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
            .flatMap { remoteRouteSource.saveRoutes(it) }
            .flatMap { remoteRouteSource.getRoutes() }
            .flatMapCompletable { localRouteSource.saveRoutes(it) }
            .andThen(localRouteSource.removeLocallySavedRoutes())
    }
}