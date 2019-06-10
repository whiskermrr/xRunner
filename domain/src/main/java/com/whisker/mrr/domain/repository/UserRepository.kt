package com.whisker.mrr.domain.repository

import com.whisker.mrr.domain.model.UserStats
import io.reactivex.Completable
import io.reactivex.Flowable

interface UserRepository {
    fun updateUserStats(userStats: UserStats) : Completable
    fun getUserStats() : Flowable<UserStats>
    fun createUserStats(userID: String) : Completable
    fun synchroUserStats() : Completable
}