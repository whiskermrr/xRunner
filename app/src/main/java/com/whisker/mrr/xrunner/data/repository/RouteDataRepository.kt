package com.whisker.mrr.xrunner.data.repository

import android.graphics.Bitmap
import com.whisker.mrr.xrunner.data.datasource.RouteDatabaseSource
import com.whisker.mrr.xrunner.data.datasource.SnapshotLocalSource
import com.whisker.mrr.xrunner.data.datasource.SnapshotRemoteSource
import com.whisker.mrr.xrunner.data.datasource.UserDataSource
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.domain.repository.RouteRepository
import io.reactivex.Completable
import javax.inject.Inject

class RouteDataRepository
@Inject constructor(
    private val userDataSource: UserDataSource,
    private val routeDatabaseSource: RouteDatabaseSource,
    private val snapshotRemoteSource: SnapshotRemoteSource,
    private val snapshotLocalSource: SnapshotLocalSource
)
: RouteRepository {

    override fun saveRoute(route: Route): Completable {
        return userDataSource.getUserId()
            .flatMapCompletable {
                routeDatabaseSource.saveRoute(route, it)
            }
    }

    override fun saveSnapshot(bitmap: Bitmap, fileName: String): Completable {
        return snapshotLocalSource.saveSnapshotLocal(bitmap, fileName)
            .flatMapCompletable { filePath ->
                snapshotRemoteSource.saveSnapshotRemote(filePath, fileName)
                    .doOnComplete {
                        snapshotLocalSource.removeSnapshotFromLocal(fileName)
                    }
            }
    }
}