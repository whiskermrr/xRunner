package com.whisker.mrr.xrunner.domain.interactor

import com.whisker.mrr.xrunner.domain.model.UserStatsEntity
import com.whisker.mrr.xrunner.domain.repository.UserRepository
import com.whisker.mrr.xrunner.domain.source.AuthSource
import com.whisker.mrr.xrunner.domain.usecase.SingleUseCase
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