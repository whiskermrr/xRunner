package com.whisker.mrr.firebase.repository

import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.model.RouteHolder
import com.whisker.mrr.domain.common.bus.RxBus
import com.whisker.mrr.domain.common.bus.event.NetworkStateEvent
import com.whisker.mrr.domain.repository.RouteRepository
import com.whisker.mrr.domain.source.RouteSource
import com.whisker.mrr.domain.source.SnapshotLocalSource
import com.whisker.mrr.domain.source.SnapshotRemoteSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import javax.inject.Inject

class RouteDataRepository
@Inject constructor(
    private val routeDatabaseSource: RouteSource,
    private val snapshotRemoteDataSource: SnapshotRemoteSource,
    private val snapshotLocalDataSource: SnapshotLocalSource
)
: RouteRepository {

    private var isNetworkAvailable = false

    init {
        RxBus.subscribeSticky(NetworkStateEvent::class.java.name, this, Consumer { event ->
            if(event is NetworkStateEvent) {
                isNetworkAvailable = event.isNetworkAvailable
            }
        })
    }

    override fun saveRoute(userId: String, route: Route): Completable {
        return routeDatabaseSource.saveRoute(route, userId)
    }

    override fun getRouteList(userId: String): Flowable<List<RouteHolder>> {
        return routeDatabaseSource.getRoutesByUserId(userId)
    }

    override fun saveSnapshot(bitmap: ByteArray, fileName: String): Completable {
        return if(isNetworkAvailable) {
            saveSnapshotRemote(bitmap, fileName)
        } else {
            cacheSnapshot(bitmap, fileName)
        }
    }

    private fun saveSnapshotRemote(bitmap: ByteArray, fileName: String) : Completable {
        return Single.create<String> {emitter ->
            emitter.onSuccess(snapshotLocalDataSource.saveSnapshotLocal(bitmap, fileName))
            }
            .flatMapCompletable { filePath ->
                snapshotRemoteDataSource.saveSnapshotRemote(filePath, fileName)
                    .doOnComplete {
                        snapshotLocalDataSource.markSnapshotAsSent(fileName)
                    }
            }
    }

    private fun cacheSnapshot(bitmap: ByteArray, fileName: String) : Completable {
        return Completable.create {emitter ->
            snapshotLocalDataSource.saveSnapshotLocal(bitmap, fileName)
            emitter.onComplete()
        }
    }

    override fun removeRoute(userId: String, routeId: String, date: Long) : Completable {
        // TODO: remove snapshot
        return routeDatabaseSource.removeRouteById(userId, routeId, date)
    }
}