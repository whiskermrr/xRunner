package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.repository.ChallengeRepository
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer

class UpdateChallengesInteractor(
    transformer: CompletableTransformer,
    private val challengeRepository: ChallengeRepository
) : CompletableUseCase(transformer) {

    companion object {
        const val PARAM_CHALLENGES_LIST = "param_challenges_list"
    }

    fun updateChallenges(challenges: List<Challenge>) : Completable {
        val data = HashMap<String, Any>()
        data[PARAM_CHALLENGES_LIST] = challenges
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val param = data?.get(PARAM_CHALLENGES_LIST)

        param?.let {challenges ->
            return if(challenges is List<*>) {
                challengeRepository.updateChallenges(challenges.filterIsInstance<Challenge>())
            } else {
                Completable.error(ClassCastException("Cannot cost parameter @challenges to List<Challenges>"))
            }
        } ?: return Completable.error(IllegalArgumentException("Parameter @challenges must be provided."))
    }
}