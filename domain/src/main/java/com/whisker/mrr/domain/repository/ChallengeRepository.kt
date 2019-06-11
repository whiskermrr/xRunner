package com.whisker.mrr.domain.repository

import com.whisker.mrr.domain.model.Challenge
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface ChallengeRepository {

    fun saveChallenge(challenge: Challenge) : Completable
    fun updateChallenges(challenges: List<Challenge>) : Completable
    fun getChallenges() : Flowable<List<Challenge>>
    fun getActiveChallenges() : Flowable<List<Challenge>>
    fun getActiveChallengesSingle() : Single<List<Challenge>>
}