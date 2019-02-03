package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.repository.ChallengeRepository
import com.whisker.mrr.domain.source.AuthSource
import com.whisker.mrr.domain.usecase.FlowableUseCase
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

class GetChallengesInteractor(
    transformer: FlowableTransformer<List<Challenge>, List<Challenge>>,
    private val authSource: AuthSource,
    private val challengeRepository: ChallengeRepository
) : FlowableUseCase<List<Challenge>>(transformer) {

    companion object {
        const val PARAM_ACTIVE = "param_active"
    }

    fun getChallenges(active: Boolean = false) : Flowable<List<Challenge>> {
        val data = HashMap<String, Any>()
        data[PARAM_ACTIVE] = active
        return flowable(data)
    }

    override fun createFlowable(data: Map<String, Any>?): Flowable<List<Challenge>> {
        val active = data?.get(PARAM_ACTIVE)

        active?.let {
            return authSource.getUserId()
                .flatMapPublisher { userId ->
                    if((active as Boolean)) {
                        return@flatMapPublisher challengeRepository.getActiveChallenges(userId)
                    } else {
                        return@flatMapPublisher challengeRepository.getChallenges(userId)
                    }
                }
        }
    }
}