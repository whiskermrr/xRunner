package com.whisker.mrr.domain.common

import com.whisker.mrr.domain.common.DomainConstants.DEFAULT_SPEED
import com.whisker.mrr.domain.common.DomainConstants.EASY_EXP
import com.whisker.mrr.domain.common.DomainConstants.NORMAL_EXP
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.model.ChallengeDifficulty
import com.whisker.mrr.domain.model.RouteStatsEntity
import io.reactivex.Single

object ChallengeUtils {

    fun calculateChallengeDifficultyAndExp(challenge: Challenge) : Single<Challenge> {
        var exp: Long = ((challenge.time ?: 0) / 1000) + (challenge.distance?.toLong() ?: 0L)
        val speed: Float = ((challenge.speed ?: DEFAULT_SPEED) / 10)
        exp = (exp * speed).toLong()

        when {
            exp <= EASY_EXP -> challenge.difficulty = ChallengeDifficulty.EASY
            exp <= NORMAL_EXP -> challenge.difficulty = ChallengeDifficulty.NORMAL
            else -> challenge.difficulty = ChallengeDifficulty.HARD
        }

        challenge.experience = exp.toInt()

        return Single.just(challenge)
    }

    fun updateChallengesProgress(stats: RouteStatsEntity, challenges: List<Challenge>) : List<Challenge> {
        val selectedChallenges = challenges.filter {
            (it.time != null && it.speed == null && it.distance == null)
            || (it.time != null && it.speed == null && it.distance != null)
            || (it.time != null && it.speed != null && it.distance != null && it.speed <= stats.averageSpeed)
            || (it.time != null && it.speed != null && it.distance == null && it.speed <= stats.averageSpeed)
            || (it.time == null && it.speed == null && it.distance != null)
            || (it.time == null && it.speed != null && it.distance != null && it.speed <= stats.averageSpeed)
        }.distinctBy { Triple(it.time == null, it.distance == null, it.speed == null) }

        for(challenge in selectedChallenges) {
            var challengeRatio = 0
            var timeProgress = 0f
            var distanceProgress = 0f

            challenge.time?.let {
                challenge.finishedTime += stats.routeTime
                timeProgress = if(challenge.finishedTime < it) {
                    challenge.finishedTime.toFloat() / it
                } else {
                    1f
                }
                challengeRatio++
            }
            challenge.distance?.let {
                challenge.finishedDistance += stats.wgs84distance
                distanceProgress = if(challenge.finishedDistance < it) {
                    challenge.finishedDistance / it
                } else {
                    1f
                }
                challengeRatio++
            }
            challenge.progress = (((timeProgress + distanceProgress) * 100) / challengeRatio).toInt()
            if(challenge.progress == 100) {
                challenge.isFinished = true
            }
        }

        return selectedChallenges
    }
}