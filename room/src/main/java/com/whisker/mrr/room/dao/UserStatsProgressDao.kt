package com.whisker.mrr.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.whisker.mrr.room.model.UserStatsProgressEntity
import io.reactivex.Single

@Dao
abstract class UserStatsProgressDao : BaseDao<UserStatsProgressEntity> {

    @Query("SELECT * FROM UserStatsProgress")
    abstract fun getUserStatsProgressList() : Single<List<UserStatsProgressEntity>>

    @Query("DELETE FROM UserStatsProgress")
    abstract fun clearUserStatsProgressTable()
}