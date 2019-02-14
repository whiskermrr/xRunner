package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.repository.ChallengeRepository
import com.whisker.mrr.domain.source.AuthSource
import com.whisker.mrr.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer

class GetActiveChallengesInteractor(
    transformer: SingleTransformer<List<Challenge>, List<Challenge>>,
    private val authSource: AuthSource,
    private val challengeRepository: ChallengeRepository
) : SingleUseCase<List<Challenge>>(transformer) {


    fun getChallenges() : Single<List<Challenge>> {
        return single()
    }

    override fun createSingle(data: Map<String, Any>?): Single<List<Challenge>> {
        return authSource.getUserId()
            .flatMap {  userId ->
                challengeRepository.getActiveChallengesSingle(userId)
            }
    }
}