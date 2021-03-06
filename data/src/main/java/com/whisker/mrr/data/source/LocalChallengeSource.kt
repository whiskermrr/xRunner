package com.whisker.mrr.data.source

import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.model.ChallengeProgress
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
    fun saveChallengesProgressListLocally(progressList: List<ChallengeProgress>): Completable
    fun getChallengesProgressList() : Single<List<ChallengeProgress>>
    fun removeChallengesProgressList() : Completable
    fun getChallengesSavedLocallyAndDeleted() : Single<List<Challenge>>
    fun removeChallengesSavedLocally() : Completable
}