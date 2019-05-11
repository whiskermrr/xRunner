package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.domain.repository.UserRepository
import com.whisker.mrr.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer

class GetUserStatsInteractor(
    transformer: SingleTransformer<UserStats, UserStats>,
    private val userRepository: UserRepository
) : SingleUseCase<UserStats>(transformer) {

    fun getUserStats() : Single<UserStats> {
        return single()
    }

    override fun createSingle(data: Map<String, Any>?): Single<UserStats> {
        return userRepository.getUserStats()
    }
}