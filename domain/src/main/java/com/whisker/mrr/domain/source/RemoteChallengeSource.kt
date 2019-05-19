package com.whisker.mrr.domain.source

import com.whisker.mrr.domain.model.Challenge
import io.reactivex.Completable
import io.reactivex.Single

interface RemoteChallengeSource {
    fun saveChallenge(challenge: Challenge) : Single<Long>
    fun saveChallenges(challenges: List<Challenge>) : Single<List<Long>>
    fun getChallenges() : Single<List<Challenge>>
    fun removeChallengeById(challengeID: Long) : Completable
    fun updateChallenges(challenges: List<Challenge>) : Completable
}