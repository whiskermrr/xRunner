package com.whisker.mrr.xrunner.domain.interactor

import com.whisker.mrr.xrunner.domain.model.RouteEntity
import com.whisker.mrr.xrunner.domain.repository.RouteRepository
import com.whisker.mrr.xrunner.domain.repository.UserRepository
import com.whisker.mrr.xrunner.domain.source.AuthSource
import com.whisker.mrr.xrunner.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException

class SaveRouteInteractor(
    transformer: CompletableTransformer,
    private val routeRepository: RouteRepository,
    private val authSource: AuthSource,
    private val userRepository: UserRepository
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
            return authSource.getUserId().flatMapCompletable { userId ->
                val saveCompletable = routeRepository.saveRoute(userId, routeEntity as RouteEntity)
                val updateStatsCompletable = userRepository.updateUserStats(userId, routeEntity.routeStats)

                Completable.concatArray(saveCompletable, updateStatsCompletable)
            }
        } ?: return Completable.error(IllegalArgumentException("Argument @route must be provided."))
    }
}