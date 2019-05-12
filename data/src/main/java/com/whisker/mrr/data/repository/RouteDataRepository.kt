package com.whisker.mrr.data.repository

import com.whisker.mrr.domain.common.mapper.RouteHolderMapper
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.model.RouteHolder
import com.whisker.mrr.domain.repository.RouteRepository
import com.whisker.mrr.domain.source.LocalRouteSource
import com.whisker.mrr.domain.source.RemoteRouteSource
import io.reactivex.Completable
import io.reactivex.Flowable

class RouteDataRepository(
    private val localRouteSource: LocalRouteSource,
    private val remoteRouteSource: RemoteRouteSource
) : RouteRepository {

    override fun saveRoute(route: Route): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveSnapshot(bitmap: ByteArray, fileName: String): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRouteList(): Flowable<List<RouteHolder>> {
        val localFlowable = localRouteSource.getRoutes()
            .map { RouteHolderMapper.transformToRouteHolderList(it) }

        val remoteFlowable = remoteRouteSource.getRoutes()
            .flatMapPublisher { routes ->
                Completable.fromAction {
                    localRouteSource.saveRoutes(routes)
                }.andThen(Flowable.empty<List<RouteHolder>>())
            }

        return Flowable.concatArray(localFlowable, remoteFlowable)
    }

    override fun removeRoute(routeID: Long): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}