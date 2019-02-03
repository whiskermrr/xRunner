package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.common.ChallengeUtils
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.repository.ChallengeRepository
import com.whisker.mrr.domain.source.AuthSource
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException

class SaveChallengeInteractor(
    transformer: CompletableTransformer,
    private val authSource: AuthSource,
    private val challengeRepository: ChallengeRepository
) : CompletableUseCase(transformer) {

    companion object {
        const val PARAM_ACHIEVEMENT = "param_achievement"
    }

    fun saveChallenge(challenge: Challenge) : Completable {
        val data = HashMap<String, Any>()
        data[PARAM_ACHIEVEMENT] = challenge
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val achievement = data?.get(PARAM_ACHIEVEMENT)

        achievement?.let {
            return ChallengeUtils.calculateChallengeDifficultyAndExp(achievement as Challenge)
                .flatMapCompletable { challenge ->
                    authSource.getUserId()
                        .flatMapCompletable {  userId ->
                            challengeRepository.saveChallenge(userId, challenge)
                        }
                }

        } ?: return Completable.error(IllegalArgumentException("Argument @achievement must be provided."))
    }
}