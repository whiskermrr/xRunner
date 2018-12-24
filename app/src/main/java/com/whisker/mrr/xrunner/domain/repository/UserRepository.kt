package com.whisker.mrr.xrunner.domain.repository

import com.whisker.mrr.xrunner.domain.model.RouteStatsEntity
import com.whisker.mrr.xrunner.domain.model.UserStatsEntity
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun updateUserStats(route: RouteStatsEntity) : Completable
    fun getUserStats(userId: String) : Single<UserStatsEntity>
}