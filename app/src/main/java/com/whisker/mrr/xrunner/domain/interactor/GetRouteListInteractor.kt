package com.whisker.mrr.xrunner.domain.interactor

import com.whisker.mrr.xrunner.domain.model.RouteEntityHolder
import com.whisker.mrr.xrunner.domain.repository.RouteRepository
import com.whisker.mrr.xrunner.domain.source.UserSource
import com.whisker.mrr.xrunner.domain.usecase.FlowableUseCase
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

class GetRouteListInteractor(
    transformer: FlowableTransformer<List<RouteEntityHolder>, List<RouteEntityHolder>>,
    private val routeRepository: RouteRepository,
    private val userSource: UserSource
) : FlowableUseCase<List<RouteEntityHolder>>(transformer) {

    override fun createFlowable(data: Map<String, Any>?): Flowable<List<RouteEntityHolder>> {
        return userSource.getUserId()
            .flatMapPublisher { userId ->
                routeRepository.getRouteList(userId)
            }
    }
}