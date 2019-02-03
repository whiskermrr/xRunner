package com.whisker.mrr.domain.repository

import com.whisker.mrr.domain.model.RouteStatsEntity
import com.whisker.mrr.domain.model.UserStatsEntity
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun updateUserStats(userId: String, userStats: UserStatsEntity) : Completable
    fun getUserStats(userId: String) : Single<UserStatsEntity>
    fun createUserStats(userId: String) : Completable
}