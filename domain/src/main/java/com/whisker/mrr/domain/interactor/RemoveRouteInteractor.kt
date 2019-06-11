package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.common.whenBothNotNull
import com.whisker.mrr.domain.repository.RouteRepository
import com.whisker.mrr.domain.source.AuthSource
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException

class RemoveRouteInteractor(
    transformer: CompletableTransformer,
    private val routeRepository: RouteRepository,
    private val authSource: AuthSource
) : CompletableUseCase(transformer) {

    companion object {
        const val PARAM_ROUTE_ID = "param_route_id"
    }

    fun removeRoute(routeId: Long) : Completable {
        val data = HashMap<String, Any>()
        data[PARAM_ROUTE_ID]  = routeId
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val routeId = data?.get(PARAM_ROUTE_ID)

        routeId?.let { id ->
            return routeRepository.removeRoute(id as Long)
        }

        return Completable.error(IllegalArgumentException("Parameter @routeId must be provided."))
    }
}