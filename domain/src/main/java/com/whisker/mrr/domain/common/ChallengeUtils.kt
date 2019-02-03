package com.whisker.mrr.domain.common

import com.whisker.mrr.domain.common.DomainConstants.DEFAULT_SPEED
import com.whisker.mrr.domain.common.DomainConstants.EASY_EXP
import com.whisker.mrr.domain.common.DomainConstants.NORMAL_EXP
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.model.ChallengeDifficulty
import io.reactivex.Single

object ChallengeUtils {

    fun calculateChallengeDifficultyAndExp(challenge: Challenge) : Single<Challenge> {
        var exp: Long = ((challenge.time ?: 0) / 1000) + (challenge.distance?.toLong() ?: 0L)
        val speed: Float = (challenge.speed ?: DEFAULT_SPEED / 10)
        exp = (exp * speed).toLong()

        when {
            exp <= EASY_EXP -> challenge.difficulty = ChallengeDifficulty.EASY
            exp <= NORMAL_EXP -> challenge.difficulty = ChallengeDifficulty.NORMAL
            else -> challenge.difficulty = ChallengeDifficulty.HARD
        }

        challenge.experience = exp.toInt()

        return Single.just(challenge)
    }
}