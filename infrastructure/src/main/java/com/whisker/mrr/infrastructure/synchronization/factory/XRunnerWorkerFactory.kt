package com.whisker.mrr.infrastructure.synchronization.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.RxWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider

class XRunnerWorkerFactory @Inject constructor(
    private val workerFactories: Map<Class<out RxWorker>, @JvmSuppressWildcards Provider<RxWorkerFactory>>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val entry = workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
        val factoryProvider = entry?.value ?: throw IllegalArgumentException("Unknown worker")
        return factoryProvider.get().create(appContext, workerParameters)
    }
}