package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.common.UserStatsUtils
import com.whisker.mrr.domain.model.RouteStats
import com.whisker.mrr.domain.repository.UserRepository
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer

class UpdateUserStatsInteractor(
    transformer: CompletableTransformer,
    private val userRepository: UserRepository
) : CompletableUseCase(transformer) {

    companion object {
        const val PARAM_ROUTE_STATS = "param_route_stats"
        const val PARAM_EXP = "param_exp"
    }

    fun updateUserStats(stats: RouteStats, challengeExp: Int) : Completable {
        val data = HashMap<String, Any>()
        data[PARAM_ROUTE_STATS] = stats
        data[PARAM_EXP] = challengeExp
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val routeStats = data?.get(PARAM_ROUTE_STATS)
        val exp = data?.get(PARAM_EXP) ?: 0

        routeStats?.let { stats ->
            return userRepository.getUserStats().firstElement()
                .map { UserStatsUtils.updateUserStats(it, stats as RouteStats, exp as Int) }
                .flatMapCompletable { userRepository.updateUserStats(it) }
        } ?: return Completable.error(IllegalArgumentException("Parameter @stats must be provided."))
    }
}