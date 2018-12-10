package com.whisker.mrr.xrunner.data.repository

import android.graphics.Bitmap
import com.whisker.mrr.xrunner.data.datasource.RouteDatabaseSource
import com.whisker.mrr.xrunner.data.datasource.SnapshotLocalSource
import com.whisker.mrr.xrunner.data.datasource.SnapshotRemoteSource
import com.whisker.mrr.xrunner.data.datasource.UserDataSource
import com.whisker.mrr.xrunner.domain.bus.RxBus
import com.whisker.mrr.xrunner.domain.bus.event.NetworkStateEvent
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.domain.repository.RouteRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import javax.inject.Inject

class RouteDataRepository
@Inject constructor(
    private val userDataSource: UserDataSource,
    private val routeDatabaseSource: RouteDatabaseSource,
    private val snapshotRemoteSource: SnapshotRemoteSource,
    private val snapshotLocalSource: SnapshotLocalSource
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

    override fun saveRoute(route: Route): Completable {
        return userDataSource.getUserId()
            .flatMapCompletable {userId ->
                routeDatabaseSource.saveRoute(route, userId)
            }
    }

    override fun getRouteList(): Flowable<List<Route>> {
        return userDataSource.getUserId()
            .flatMapPublisher { userId ->
                routeDatabaseSource.getRoutesByUserId(userId)
            }
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
            emitter.onSuccess(snapshotLocalSource.saveSnapshotLocal(bitmap, fileName))
            }
            .flatMapCompletable { filePath ->
                snapshotRemoteSource.saveSnapshotRemote(filePath, fileName)
                    .doOnComplete {
                        snapshotLocalSource.markSnapshotAsSent(fileName)
                    }
            }
    }

    private fun cacheSnapshot(bitmap: Bitmap, fileName: String) : Completable {
        return Completable.create {emitter ->
            snapshotLocalSource.saveSnapshotLocal(bitmap, fileName)
            emitter.onComplete()
        }
    }
}