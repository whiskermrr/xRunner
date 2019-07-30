package com.whisker.mrr.infrastructure.synchronization.worker

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.whisker.mrr.domain.repository.SnapshotRepository
import com.whisker.mrr.infrastructure.synchronization.factory.RxWorkerFactory
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Provider

class SnapshotRxWorker(
    context: Context,
    params: WorkerParameters,
    private val snapshotRepository: SnapshotRepository
) : RxWorker(context, params) {

    override fun createWork(): Single<Result> {
        return snapshotRepository.synchronizeSnapshots()
            .toSingle { Result.success() }
            .onErrorReturn {
                it.printStackTrace()
                Result.failure()
            }
    }

    class Factory @Inject constructor(private val snapshotRepository: Provider<SnapshotRepository>) : RxWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): RxWorker {
            return SnapshotRxWorker(context, params, snapshotRepository.get())
        }
    }
}