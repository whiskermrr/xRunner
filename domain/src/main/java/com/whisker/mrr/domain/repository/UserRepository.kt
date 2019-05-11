package com.whisker.mrr.domain.repository

import com.whisker.mrr.domain.model.UserStats
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun updateUserStats(userStats: UserStats) : Completable
    fun getUserStats() : Single<UserStats>
    fun createUserStats() : Completable
}