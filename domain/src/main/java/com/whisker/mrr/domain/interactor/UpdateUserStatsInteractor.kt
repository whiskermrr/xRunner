package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.domain.repository.UserRepository
import com.whisker.mrr.domain.source.AuthSource
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer

class UpdateUserStatsInteractor(
    transformer: CompletableTransformer,
    private val authSource: AuthSource,
    private val userRepository: UserRepository
) : CompletableUseCase(transformer) {

    companion object {
        const val PARAM_USER_STATS = "param_user_stats"
    }

    fun updateUserStats(userStats: UserStats) : Completable {
        val data = HashMap<String, Any>()
        data[PARAM_USER_STATS] = userStats
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val param = data?.get(PARAM_USER_STATS)

        param?.let { userStats ->
            return authSource.getUserId()
                .flatMapCompletable {  userId ->
                    userRepository.updateUserStats(userId, userStats as UserStats)
                }
        } ?: return Completable.error(IllegalArgumentException("Parameter @challenges must be provided."))
    }
}