package com.whisker.mrr.xrunner.domain.interactor

import com.whisker.mrr.xrunner.domain.common.whenBothNotNull
import com.whisker.mrr.xrunner.domain.repository.RouteRepository
import com.whisker.mrr.xrunner.domain.source.UserSource
import com.whisker.mrr.xrunner.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException

class RemoveRouteInteractor(
    transformer: CompletableTransformer,
    private val routeRepository: RouteRepository,
    private val userSource: UserSource
) : CompletableUseCase(transformer) {

    companion object {
        const val PARAM_ROUTE_ID = "param_route_id"
        const val PARAM_ROUTE_DATE = "param_route_date"
    }

    fun removeRoute(routeId: String, date: Long) : Completable {
        val data = HashMap<String, Any>()
        data[PARAM_ROUTE_ID]  = routeId
        data[PARAM_ROUTE_DATE] = date
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val routeId = data?.get(PARAM_ROUTE_ID)
        val routeDate = data?.get(PARAM_ROUTE_DATE)

        whenBothNotNull(routeId, routeDate) { id, date ->
            return userSource.getUserId()
                .flatMapCompletable { userId ->
                    routeRepository.removeRoute(userId, id.toString(), date.toString().toLong())
                }
        }

        return Completable.error(IllegalArgumentException("Parameter @routeId must be provided."))
    }
}