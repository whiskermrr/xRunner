package com.whisker.mrr.data.repository

import com.whisker.mrr.domain.common.bus.RxBus
import com.whisker.mrr.domain.common.bus.event.NetworkStateEvent
import com.whisker.mrr.domain.common.mapper.RouteHolderMapper
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.model.RouteHolder
import com.whisker.mrr.domain.repository.RouteRepository
import com.whisker.mrr.domain.source.LocalRouteSource
import com.whisker.mrr.domain.source.RemoteRouteSource
import com.whisker.mrr.domain.source.SnapshotLocalSource
import com.whisker.mrr.domain.source.SnapshotRemoteSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Consumer

class RouteDataRepository(
    private val localRouteSource: LocalRouteSource,
    private val remoteRouteSource: RemoteRouteSource,
    private val snapshotRemoteDataSource: SnapshotRemoteSource,
    private val snapshotLocalDataSource: SnapshotLocalSource
) : RouteRepository {

    private var isNetworkAvailable = false

    init {
        RxBus.subscribeSticky(NetworkStateEvent::class.java.name, this, Consumer { event ->
            if(event is NetworkStateEvent) {
                isNetworkAvailable = event.isNetworkAvailable
            }
        })
    }

    override fun saveRoute(route: Route): Completable {
        return localRouteSource.saveRoute(route)
            .flatMap { localID ->
                route.routeId = localID
                remoteRouteSource.saveRoute(route)
            }
            .flatMapCompletable { localRouteSource.updateRouteID(route.routeId, it) }
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
        return localRouteSource.markRouteAsDeleted(routeID)
            .andThen(remoteRouteSource.removeRouteById(routeID))
            .andThen(localRouteSource.removeRouteById(routeID))
    }

    override fun saveSnapshot(bitmap: ByteArray, fileName: String): Completable {
        return if(isNetworkAvailable) {
            saveSnapshotRemote(bitmap, fileName)
        } else {
            cacheSnapshot(bitmap, fileName)
        }
    }

    private fun saveSnapshotRemote(bitmap: ByteArray, fileName: String) : Completable {
        return Single.fromCallable {
            snapshotLocalDataSource.saveSnapshotLocal(bitmap, fileName)
        }
        .flatMapCompletable { filePath ->
            snapshotRemoteDataSource.saveSnapshotRemote(filePath, fileName)
                .doOnComplete {
                    snapshotLocalDataSource.markSnapshotAsSent(fileName)
                }
        }
    }

    private fun cacheSnapshot(bitmap: ByteArray, fileName: String) : Completable {
        return Completable.fromAction {
            snapshotLocalDataSource.saveSnapshotLocal(bitmap, fileName)
        }
    }
}