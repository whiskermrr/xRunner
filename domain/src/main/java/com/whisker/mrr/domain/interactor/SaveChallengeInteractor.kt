package com.whisker.mrr.domain.interactor

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

    fun SaveChallenge(challenge: Challenge) : Completable {
        val data = HashMap<String, Any>()
        data[PARAM_ACHIEVEMENT] = challenge
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val achievement = data?.get(PARAM_ACHIEVEMENT)

        achievement?.let {
            return authSource.getUserId()
                .flatMapCompletable {  userId ->
                    challengeRepository.saveChallenge(userId, achievement as Challenge)
                }
        } ?: return Completable.error(IllegalArgumentException("Argument @achievement must be provided."))
    }
}