package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.repository.ChallengeRepository
import com.whisker.mrr.domain.source.AuthSource
import com.whisker.mrr.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer

class GetChallengesInteractor(
    transformer: SingleTransformer<List<Challenge>, List<Challenge>>,
    private val authSource: AuthSource,
    private val challengeRepository: ChallengeRepository
) : SingleUseCase<List<Challenge>>(transformer) {

    companion object {
        const val PARAM_ACTIVE = "param_active"
    }

    fun GetChallenges(active: Boolean = false) : Single<List<Challenge>> {
        val data = HashMap<String, Any>()
        data[PARAM_ACTIVE] = active
        return single(data)
    }

    override fun createSingle(data: Map<String, Any>?): Single<List<Challenge>> {
        val active = data?.get(PARAM_ACTIVE)

        active?.let {
            return authSource.getUserId()
                .flatMap { userId ->
                    if((active as Boolean)) {
                        return@flatMap challengeRepository.getActiveChallenges(userId)
                    } else {
                        return@flatMap challengeRepository.getChallenges(userId)
                    }
                }
        }
    }
}