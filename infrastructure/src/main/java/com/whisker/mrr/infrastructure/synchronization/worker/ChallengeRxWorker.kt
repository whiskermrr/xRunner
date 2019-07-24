package com.whisker.mrr.infrastructure.synchronization.worker

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.whisker.mrr.domain.repository.ChallengeRepository
import com.whisker.mrr.infrastructure.synchronization.factory.RxWorkerFactory
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Provider

class ChallengeRxWorker(
    context: Context,
    params: WorkerParameters,
    private val challengeRepository: ChallengeRepository
) : RxWorker(context, params) {

    override fun createWork(): Single<Result> {
        return challengeRepository.synchronizeChallenges()
            .toSingle { Result.success() }
            .onErrorReturn {
                it.printStackTrace()
                Result.failure()
            }
    }

    class Factory @Inject constructor(private val challengeRepository: Provider<ChallengeRepository>) : RxWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): RxWorker {
            return ChallengeRxWorker(context, params, challengeRepository.get())
        }
    }
}