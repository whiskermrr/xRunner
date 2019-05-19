package com.whisker.mrr.domain.source

import com.whisker.mrr.domain.model.UserStats
import io.reactivex.Completable
import io.reactivex.Flowable

interface LocalUserSource {
    fun updateUserStats(userStats: UserStats) : Completable
    fun getUserStats() : Flowable<UserStats>
    fun createUserStats(userStats: UserStats) : Completable
}