package com.whisker.mrr.infrastructure.synchronization

import android.content.Context
import androidx.work.WorkManager
import com.whisker.mrr.domain.manager.SynchronizationManager

class SynchronizationDataManager(private val context: Context) : SynchronizationManager {

    private val workManager: WorkManager = WorkManager.getInstance()

    override fun fullDataSynchronization() {

    }

    override fun imageSynchronization() {

    }
}