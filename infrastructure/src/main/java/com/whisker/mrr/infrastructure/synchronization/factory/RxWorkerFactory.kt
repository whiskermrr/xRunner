package com.whisker.mrr.infrastructure.synchronization.factory

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters

interface RxWorkerFactory {
    fun create(context: Context, params: WorkerParameters) : RxWorker
}