package com.whisker.mrr.data.database.dao

import androidx.room.Query
import androidx.room.Update
import com.whisker.mrr.data.database.model.UserStatsEntity
import io.reactivex.Completable
import io.reactivex.Flowable

abstract class UserStatsDao : BaseDao<UserStatsEntity> {

    @Update
    abstract fun updateUserStats(userStats: UserStatsEntity) : Completable

    @Query("SELECT * FROM UserStats LIMIT 1")
    abstract fun getUserStats() : Flowable<UserStatsEntity>

    @Query("DELETE FROM UserStats")
    abstract fun clearTable()
}