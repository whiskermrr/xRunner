package com.whisker.mrr.domain.source

import com.whisker.mrr.domain.model.UserStats
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface LocalUserSource {
    fun saveUserStats(userStats: UserStats) : Completable
    fun updateUserStats(userStats: UserStats) : Completable
    fun getUserStats() : Flowable<UserStats>
    fun getUserStatsSingle() : Single<UserStats>
    fun createUserStats(userStats: UserStats) : Completable
}