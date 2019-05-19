package com.whisker.mrr.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.whisker.mrr.data.database.model.ChallengeEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class ChallengeDao : BaseDao<ChallengeEntity> {

    @Query("SELECT * FROM Challenge WHERE isDeleted = 0")
    abstract fun getChallenges() : Flowable<List<ChallengeEntity>>

    @Query("SELECT * FROM Challenge WHERE isDeleted = 0 AND isFinished = 0")
    abstract fun getActiveChallenges() : Flowable<List<ChallengeEntity>>

    @Query("SELECT * FROM Challenge WHERE isDeleted = 0 AND isFinished = 0")
    abstract fun getActiveChallengesSingle() : Single<List<ChallengeEntity>>

    @Query("DELETE FROM Challenge WHERE id = :challengeID")
    abstract fun deleteChallengeById(challengeID: Long)

    @Update
    abstract fun updateChallenges(challenges: List<ChallengeEntity>) : Completable

    @Query("DELETE FROM Challenge WHERE isDeleted = 1")
    abstract fun deleteIsDeleted()

    @Query("UPDATE Challenge SET id = :newID WHERE id = :oldID")
    abstract fun updateChallengeID(oldID: Long, newID: Long)

    @Query("UPDATE Challenge SET isDeleted = 1 WHERE id = :id")
    abstract fun markChallengeAsDeleted(id: Long)

    @Query("SELECT MIN(id) - 1 FROM Challenge WHERE id < 0")
    abstract fun getNextLocalID() : Long?

    @Query("DELETE FROM Challenge")
    abstract fun clearTable()
}