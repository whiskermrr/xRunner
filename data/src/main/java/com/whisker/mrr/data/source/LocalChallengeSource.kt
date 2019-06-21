package com.whisker.mrr.data.source

import com.whisker.mrr.domain.model.Challenge
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface LocalChallengeSource {
    fun saveChallenge(challenge: Challenge) : Single<Long>
    fun saveChallenges(challenges: List<Challenge>) : Completable
    fun getChallenges() : Flowable<List<Challenge>>
    fun getActiveChallenges() : Flowable<List<Challenge>>
    fun getActiveChallengesSingle() : Single<List<Challenge>>
    fun removeChallengeById(challengeID: Long) : Completable
    fun markChallengeAsDeleted(challengeID: Long) : Completable
    fun updateChallenges(challenges: List<Challenge>) : Completable
    fun updateChallengeID(oldID: Long, newID: Long) : Completable
}