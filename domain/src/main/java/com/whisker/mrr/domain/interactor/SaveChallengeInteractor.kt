package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.common.utils.ChallengeUtils
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.repository.ChallengeRepository
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException

class SaveChallengeInteractor(
    transformer: CompletableTransformer,
    private val challengeRepository: ChallengeRepository
) : CompletableUseCase(transformer) {

    companion object {
        const val PARAM_CHALLENGE = "param_challenge"
    }

    fun saveChallenge(challenge: Challenge) : Completable {
        val data = HashMap<String, Any>()
        data[PARAM_CHALLENGE] = challenge
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val challenge = data?.get(PARAM_CHALLENGE)
        challenge?.let {
            return challengeRepository.saveChallenge(
                ChallengeUtils.calculateChallengeDifficultyAndExp(challenge as Challenge)
            )

        } ?: return Completable.error(IllegalArgumentException("Argument @challenge must be provided."))
    }
}