package com.whisker.mrr.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.whisker.mrr.data.database.model.UserStatsEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class UserStatsDao : BaseDao<UserStatsEntity> {

    @Update
    abstract fun updateUserStats(userStats: UserStatsEntity)

    @Query("SELECT * FROM UserStats LIMIT 1")
    abstract fun getUserStats() : Flowable<UserStatsEntity>

    @Query("SELECT * FROM UserStats LIMIT 1")
    abstract fun getUserStatsSingle() : Single<UserStatsEntity>

    @Query("DELETE FROM UserStats")
    abstract fun clearTable()
}