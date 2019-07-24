package com.whisker.mrr.infrastructure.synchronization

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.whisker.mrr.domain.manager.SynchronizationManager
import com.whisker.mrr.infrastructure.synchronization.worker.ChallengeRxWorker
import com.whisker.mrr.infrastructure.synchronization.worker.RouteRxWorker
import com.whisker.mrr.infrastructure.synchronization.worker.UserStatsRxWorker
import com.whisker.mrr.infrastructure.toWorkInfoStateCompletable
import io.reactivex.Completable

class SynchronizationDataManager(private val workManager: WorkManager) : SynchronizationManager {

    companion object {
        private const val uniqueFullSynchronizationName = "full_synchronization"
    }

    override fun fullDataSynchronization() : Completable {
        workManager.beginUniqueWork(
            uniqueFullSynchronizationName,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<UserStatsRxWorker>().build())
            .then(OneTimeWorkRequestBuilder<RouteRxWorker>().build())
            .then(OneTimeWorkRequestBuilder<ChallengeRxWorker>().build())
            .enqueue()

        return workManager.getWorkInfosForUniqueWorkLiveData(uniqueFullSynchronizationName)
            .toWorkInfoStateCompletable()
    }

    override fun imageSynchronization() : Completable {
        return Completable.complete()
    }
}