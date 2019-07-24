package com.whisker.mrr.data.source

import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.domain.model.UserStatsProgress
import io.reactivex.Completable
import io.reactivex.Single

interface RemoteUserSource {
    fun updateUserStats(userStats: UserStats) : Completable
    fun getUserStats() : Single<UserStats>
    fun createUserStats(userStats: UserStats) : Completable
    fun updateUserStats(progressList: List<UserStatsProgress>) : Completable
}