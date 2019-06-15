package com.whisker.mrr.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.whisker.mrr.room.model.Preferences
import io.reactivex.Single

@Dao
abstract class PreferencesDao : BaseDao<Preferences> {

    @Query("SELECT stringValue FROM Preferences WHERE `key` = :key LIMIT 1")
    abstract fun getStringValue(key: String) : Single<String>

    @Query("SELECT longValue FROM Preferences WHERE `key` = :key LIMIT 1")
    abstract fun getLongValue(key: String) : Single<Long>

    @Query("SELECT booleanValue FROM Preferences WHERE `key` = :key LIMIT 1")
    abstract fun getBooleanValue(key: String) : Single<Boolean>

    @Query("DELETE FROM Preferences WHERE `key` = :key")
    abstract fun deleteValue(key: String)

    @Query("DELETE FROM Preferences WHERE `key` in (:keys)")
    abstract fun deleteValues(keys: List<String>)
}