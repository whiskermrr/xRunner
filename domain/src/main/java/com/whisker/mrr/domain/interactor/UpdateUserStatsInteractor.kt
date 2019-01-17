package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.RouteStatsEntity
import com.whisker.mrr.domain.repository.UserRepository
import com.whisker.mrr.domain.source.AuthSource
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException

class UpdateUserStatsInteractor(
    transformer: CompletableTransformer,
    private val userRepository: UserRepository,
    private val authSource: AuthSource
) : CompletableUseCase(transformer) {

    companion object {
        private const val PARAM_ROUTE_STATS = "param_route_stats"
    }

    fun updateUserStats(routeStats: RouteStatsEntity) : Completable {
        val data = HashMap<String, RouteStatsEntity>()
        data[PARAM_ROUTE_STATS] = routeStats
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val statsEntity = data?.get(PARAM_ROUTE_STATS)

        statsEntity?.let { stats ->
            return authSource.getUserId()
                .flatMapCompletable { userId ->
                    userRepository.updateUserStats(userId, stats as RouteStatsEntity)
                }
        } ?: return Completable.error(IllegalArgumentException("Argument @stats must be provided."))
    }
}