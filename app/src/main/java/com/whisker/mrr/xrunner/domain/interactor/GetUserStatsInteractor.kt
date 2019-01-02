package com.whisker.mrr.xrunner.domain.interactor

import com.whisker.mrr.xrunner.domain.model.UserStatsEntity
import com.whisker.mrr.xrunner.domain.repository.UserRepository
import com.whisker.mrr.xrunner.domain.source.UserSource
import com.whisker.mrr.xrunner.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer

class GetUserStatsInteractor(
    transformer: SingleTransformer<UserStatsEntity, UserStatsEntity>,
    private val userRepository: UserRepository,
    private val userSource: UserSource
) : SingleUseCase<UserStatsEntity>(transformer) {

    fun getUserStats() : Single<UserStatsEntity> {
        return single()
    }

    override fun createSingle(data: Map<String, Any>?): Single<UserStatsEntity> {
        return userSource.getUserId()
            .flatMap {  userId ->
                userRepository.getUserStats(userId)
            }
    }
}