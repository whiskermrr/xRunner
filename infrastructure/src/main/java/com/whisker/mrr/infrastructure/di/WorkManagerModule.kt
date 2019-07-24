package com.whisker.mrr.infrastructure.di

import com.whisker.mrr.infrastructure.synchronization.factory.RxWorkerFactory
import com.whisker.mrr.infrastructure.synchronization.worker.ChallengeRxWorker
import com.whisker.mrr.infrastructure.synchronization.worker.RouteRxWorker
import com.whisker.mrr.infrastructure.synchronization.worker.UserStatsRxWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkManagerModule {

    @Binds
    @IntoMap
    @WorkerKey(ChallengeRxWorker::class)
    fun bindChallengeRxWorker(challengeWorkerFactory: ChallengeRxWorker.Factory) : RxWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(RouteRxWorker::class)
    fun bindRouteRxWorker(routeWorkerFactory: RouteRxWorker.Factory) : RxWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(UserStatsRxWorker::class)
    fun bindUserStatsRxWorker(userStatsWorkerFactory: UserStatsRxWorker.Factory) : RxWorkerFactory
}