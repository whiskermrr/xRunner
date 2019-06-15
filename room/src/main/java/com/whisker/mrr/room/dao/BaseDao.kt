package com.whisker.mrr.room.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(objects: List<T>) : List<Long>
}