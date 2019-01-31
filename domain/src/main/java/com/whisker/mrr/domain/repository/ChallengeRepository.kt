package com.whisker.mrr.domain.repository

import com.whisker.mrr.domain.model.Challenge
import io.reactivex.Completable
import io.reactivex.Single

interface ChallengeRepository {

    fun saveChallenge(userId: String, challenge: Challenge) : Completable
    fun updateChallenges(userId: String, challenges: List<Challenge>) : Completable
    fun getChallenges(userId: String) : Single<List<Challenge>>
    fun getActiveChallenges(userId: String) : Single<List<Challenge>>
}