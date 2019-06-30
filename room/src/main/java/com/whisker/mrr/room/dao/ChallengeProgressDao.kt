package com.whisker.mrr.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.whisker.mrr.room.model.ChallengeProgressEntity
import io.reactivex.Single

@Dao
abstract class ChallengeProgressDao : BaseDao<ChallengeProgressEntity> {


    @Query("SELECT * FROM ChallengeProgress")
    abstract fun getChallengeProgreesList() : Single<List<ChallengeProgressEntity>>

    @Query("DELETE FROM ChallengeProgress")
    abstract fun clearChallengeProgressTable()
}