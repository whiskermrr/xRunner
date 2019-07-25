package com.whisker.mrr.data.repository

import com.whisker.mrr.data.source.SnapshotLocalSource
import com.whisker.mrr.data.source.SnapshotRemoteSource
import com.whisker.mrr.domain.common.bus.RxBus
import com.whisker.mrr.domain.common.bus.event.NetworkStateEvent
import com.whisker.mrr.domain.repository.SnapshotRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Consumer

class SnapshotDataRepository(
    private val snapshotLocalDataSource: SnapshotLocalSource,
    private val snapshotRemoteDataSource: SnapshotRemoteSource
) : SnapshotRepository {

    private var isNetworkAvailable = false

    init {
        RxBus.subscribeSticky(NetworkStateEvent::class.java.name, this, Consumer { event ->
            if(event is NetworkStateEvent) {
                isNetworkAvailable = event.isNetworkAvailable
            }
        })
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

    override fun synchronizeSnapshots(): Completable {
        return Single.fromCallable { snapshotLocalDataSource.getNotSentSnapshotsPaths() }
            .flatMapCompletable { snapshots ->
                Completable.concat(snapshotRemoteDataSource.saveListOfSnapshotsRemote(snapshots.toMutableList()))
                    .andThen(Completable.fromAction {
                        snapshotLocalDataSource.replaceNotSentSnapshotsPaths(snapshots.map { it.second }.toSet())
                    })
            }
    }
}