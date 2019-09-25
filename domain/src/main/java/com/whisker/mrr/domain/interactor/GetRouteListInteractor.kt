package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.RouteHolder
import com.whisker.mrr.domain.repository.RouteRepository
import com.whisker.mrr.domain.usecase.FlowableUseCase
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

class GetRouteListInteractor(
    transformer: FlowableTransformer<List<RouteHolder>, List<RouteHolder>>,
    private val routeRepository: RouteRepository
) : FlowableUseCase<List<RouteHolder>>(transformer) {

    fun getRoutes() : Flowable<List<RouteHolder>> {
        return flowable()
    }

    override fun createFlowable(data: Map<String, Any>?): Flowable<List<RouteHolder>> {
        return routeRepository.getRouteList()
    }
}