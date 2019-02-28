package com.whisker.mrr.domain.repository

import com.whisker.mrr.domain.model.UserStats
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun updateUserStats(userId: String, userStats: UserStats) : Completable
    fun getUserStats(userId: String) : Single<UserStats>
    fun createUserStats(userId: String) : Completable
}