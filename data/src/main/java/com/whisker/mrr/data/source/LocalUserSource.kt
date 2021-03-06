package com.whisker.mrr.data.source

import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.domain.model.UserStatsProgress
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface LocalUserSource {
    fun saveUserStats(userStats: UserStats) : Completable
    fun updateUserStats(userStats: UserStats) : Completable
    fun getUserStats() : Flowable<UserStats>
    fun getUserStatsSingle() : Single<UserStats>
    fun createUserStats(userStats: UserStats) : Completable
    fun saveUserStatsProgressLocally(statsProgress: UserStatsProgress) : Completable
    fun getLocalUserStatsProgressList() : Single<List<UserStatsProgress>>
    fun removeUserStatsProgressList() : Completable
}