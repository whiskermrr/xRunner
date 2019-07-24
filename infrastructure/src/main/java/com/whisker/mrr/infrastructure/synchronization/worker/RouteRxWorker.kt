package com.whisker.mrr.infrastructure.synchronization.worker

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.whisker.mrr.domain.repository.RouteRepository
import com.whisker.mrr.infrastructure.synchronization.factory.RxWorkerFactory
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Provider

class RouteRxWorker(
    context: Context,
    params: WorkerParameters,
    private val routeRepository: RouteRepository
) : RxWorker(context, params) {

    override fun createWork(): Single<Result> {
        return routeRepository.synchronizeRoutes()
            .toSingle { Result.success() }
            .onErrorReturn {
                it.printStackTrace()
                Result.failure()
            }
    }

    class Factory @Inject constructor(private val routeRepository: Provider<RouteRepository>) :
        RxWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): RxWorker {
            return RouteRxWorker(context, params, routeRepository.get())
        }
    }
}