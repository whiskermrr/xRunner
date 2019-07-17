package com.whisker.mrr.infrastructure.synchronization

import androidx.work.WorkManager
import com.whisker.mrr.domain.manager.SynchronizationManager

class SynchronizationDataManager(private val workManager: WorkManager) : SynchronizationManager {

    override fun fullDataSynchronization() {

    }

    override fun imageSynchronization() {

    }
}