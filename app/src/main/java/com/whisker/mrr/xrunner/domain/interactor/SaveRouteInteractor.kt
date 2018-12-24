package com.whisker.mrr.xrunner.domain.interactor

import com.whisker.mrr.xrunner.domain.model.RouteEntity
import com.whisker.mrr.xrunner.domain.repository.RouteRepository
import com.whisker.mrr.xrunner.domain.source.UserSource
import com.whisker.mrr.xrunner.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException

class SaveRouteInteractor(transformer: CompletableTransformer, private val routeRepository: RouteRepository, private val userSource: UserSource)
: CompletableUseCase(transformer) {

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
            return userSource.getUserId().flatMapCompletable { userId ->
                routeRepository.saveRoute(userId, routeEntity as RouteEntity)
            }
        } ?: return Completable.error(IllegalArgumentException("Argument @route must be provided."))
    }
}