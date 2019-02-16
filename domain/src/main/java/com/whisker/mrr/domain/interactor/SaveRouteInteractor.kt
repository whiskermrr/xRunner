package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.common.UserStatsUtils
import com.whisker.mrr.domain.model.RouteEntity
import com.whisker.mrr.domain.repository.RouteRepository
import com.whisker.mrr.domain.repository.UserRepository
import com.whisker.mrr.domain.source.AuthSource
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException

class SaveRouteInteractor(
    transformer: CompletableTransformer,
    private val routeRepository: RouteRepository,
    private val userRepository: UserRepository,
    private val authSource: AuthSource
) : CompletableUseCase(transformer) {

    companion object {
        private const val PARAM_ROUTE = "param_route"
    }

    fun saveRoute(route: RouteEntity) : Completable {
        val data = HashMap<String, RouteEntity>()
        data[PARAM_ROUTE] = route
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val routeEntity = data?.get(PARAM_ROUTE)

        routeEntity?.let {
            return authSource.getUserId()
                .flatMapCompletable { userId ->
                    Completable.concatArray(
                        routeRepository.saveRoute(userId, routeEntity as RouteEntity),
                        userRepository.getUserStats(userId)
                            .map { userStats ->
                                    UserStatsUtils.updateUserStats(userStats, routeEntity.routeStats)
                                userStats
                            }.flatMapCompletable { updatedStats ->
                                userRepository.updateUserStats(userId, updatedStats)
                            }
                    )
                }
        } ?: return Completable.error(IllegalArgumentException("Argument @route must be provided."))
    }


}