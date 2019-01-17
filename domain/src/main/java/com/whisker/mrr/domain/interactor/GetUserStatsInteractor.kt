package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.UserStatsEntity
import com.whisker.mrr.domain.repository.UserRepository
import com.whisker.mrr.domain.source.AuthSource
import com.whisker.mrr.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer

class GetUserStatsInteractor(
    transformer: SingleTransformer<UserStatsEntity, UserStatsEntity>,
    private val userRepository: UserRepository,
    private val authSource: AuthSource
) : SingleUseCase<UserStatsEntity>(transformer) {

    fun getUserStats() : Single<UserStatsEntity> {
        return single()
    }

    override fun createSingle(data: Map<String, Any>?): Single<UserStatsEntity> {
        return authSource.getUserId()
            .flatMap {  userId ->
                userRepository.getUserStats(userId)
            }
    }
}