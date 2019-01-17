package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.RouteEntityHolder
import com.whisker.mrr.domain.repository.RouteRepository
import com.whisker.mrr.domain.source.AuthSource
import com.whisker.mrr.domain.usecase.FlowableUseCase
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

class GetRouteListInteractor(
    transformer: FlowableTransformer<List<RouteEntityHolder>, List<RouteEntityHolder>>,
    private val routeRepository: RouteRepository,
    private val authSource: AuthSource
) : FlowableUseCase<List<RouteEntityHolder>>(transformer) {

    override fun createFlowable(data: Map<String, Any>?): Flowable<List<RouteEntityHolder>> {
        return authSource.getUserId()
            .flatMapPublisher { userId ->
                routeRepository.getRouteList(userId)
            }
    }
}