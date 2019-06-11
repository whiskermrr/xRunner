package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.domain.repository.UserRepository
import com.whisker.mrr.domain.usecase.FlowableUseCase
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

class GetUserStatsInteractor(
    transformer: FlowableTransformer<UserStats, UserStats>,
    private val userRepository: UserRepository
) : FlowableUseCase<UserStats>(transformer) {

    fun getUserStats() : Flowable<UserStats> {
        return flowable()
    }

    override fun createFlowable(data: Map<String, Any>?): Flowable<UserStats> {
        return userRepository.getUserStats()
    }
}