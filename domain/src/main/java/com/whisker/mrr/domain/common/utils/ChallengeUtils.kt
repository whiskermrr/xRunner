package com.whisker.mrr.domain.common.utils

import com.whisker.mrr.domain.common.utils.DomainConstants.DEFAULT_SPEED
import com.whisker.mrr.domain.common.utils.DomainConstants.EASY_EXP
import com.whisker.mrr.domain.common.utils.DomainConstants.NORMAL_EXP
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.model.ChallengeDifficulty
import com.whisker.mrr.domain.model.ChallengeProgress
import com.whisker.mrr.domain.model.RouteStats
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

    fun getSelectedChallenges(stats: RouteStats, challenges: List<Challenge>) : List<Challenge> {
        return challenges.filter {
            (it.time != null && it.speed == null && it.distance == null)
                    || (it.time != null && it.speed == null && it.distance != null)
                    || (it.time != null && it.speed != null && it.distance != null && it.speed!! <= stats.averageSpeed)
                    || (it.time != null && it.speed != null && it.distance == null && it.speed!! <= stats.averageSpeed)
                    || (it.time == null && it.speed == null && it.distance != null)
                    || (it.time == null && it.speed != null && it.distance != null && it.speed!! <= stats.averageSpeed)
        }.distinctBy { Triple(it.time == null, it.distance == null, it.speed == null) }
    }

    fun updateChallengesProgress(stats: RouteStats, selectedChallenges: List<Challenge>) : List<Challenge> {
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

    fun getChallengesProgress(stats: RouteStats, selectedChallenges: List<Challenge>) : List<ChallengeProgress> {
        val challengeProgressList = mutableListOf<ChallengeProgress>()

        for(challenge in selectedChallenges) {
            val progress = ChallengeProgress(challenge.id)
            challenge.time?.let {
                progress.timeProgress = stats.routeTime
            }
            challenge.distance?.let {
                progress.distanceProgress = stats.wgs84distance
            }
            challengeProgressList.add(progress)
        }

        return challengeProgressList
    }
}