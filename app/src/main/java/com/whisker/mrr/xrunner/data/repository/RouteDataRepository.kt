package com.whisker.mrr.xrunner.data.repository

import android.graphics.Bitmap
import com.whisker.mrr.xrunner.domain.model.RouteEntity
import com.whisker.mrr.xrunner.domain.model.RouteEntityHolder
import com.whisker.mrr.xrunner.domain.bus.RxBus
import com.whisker.mrr.xrunner.domain.bus.event.NetworkStateEvent
import com.whisker.mrr.xrunner.domain.repository.RouteRepository
import com.whisker.mrr.xrunner.domain.source.RouteSource
import com.whisker.mrr.xrunner.domain.source.SnapshotLocalSource
import com.whisker.mrr.xrunner.domain.source.SnapshotRemoteSource
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

    override fun saveRoute(userId: String, route: RouteEntity): Completable {
        return routeDatabaseSource.saveRoute(route, userId)
    }

    override fun getRouteList(userId: String): Flowable<List<RouteEntityHolder>> {
        return routeDatabaseSource.getRoutesByUserId(userId)
    }

    override fun saveSnapshot(bitmap: Bitmap, fileName: String): Completable {
        return if(isNetworkAvailable) {
            saveSnapshotRemote(bitmap, fileName)
        } else {
            cacheSnapshot(bitmap, fileName)
        }
    }

    private fun saveSnapshotRemote(bitmap: Bitmap, fileName: String) : Completable {
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

    private fun cacheSnapshot(bitmap: Bitmap, fileName: String) : Completable {
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