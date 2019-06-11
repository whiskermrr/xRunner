package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.repository.RouteRepository
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException

class SaveRouteInteractor(
    transformer: CompletableTransformer,
    private val routeRepository: RouteRepository
) : CompletableUseCase(transformer) {

    companion object {
        private const val PARAM_ROUTE = "param_route"
    }

    fun saveRoute(route: Route) : Completable {
        val data = HashMap<String, Route>()
        data[PARAM_ROUTE] = route
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val routeEntity = data?.get(PARAM_ROUTE)

        routeEntity?.let {
            return routeRepository.saveRoute(routeEntity as Route)
        } ?: return Completable.error(IllegalArgumentException("Argument @route must be provided."))
    }
}