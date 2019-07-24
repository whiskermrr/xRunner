package com.whisker.mrr.infrastructure.synchronization.worker

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.whisker.mrr.domain.repository.UserRepository
import com.whisker.mrr.infrastructure.synchronization.factory.RxWorkerFactory
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Provider

class UserStatsRxWorker(
    context: Context,
    params: WorkerParameters,
    private val userRepository: UserRepository
) : RxWorker(context, params) {

    override fun createWork(): Single<Result> {
        return userRepository.synchronizeUserStats()
            .toSingle { Result.success() }
            .onErrorReturn {
                it.printStackTrace()
                Result.failure()
            }
    }

    class Factory @Inject constructor(private val userRepository: Provider<UserRepository>) : RxWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): RxWorker {
            return UserStatsRxWorker(context, params, userRepository.get())
        }
    }
}