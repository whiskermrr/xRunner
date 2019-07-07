package com.whisker.mrr.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserStatsProgress")
data class UserStatsProgressEntity(

    @ColumnInfo(name = "distanceProgress")
    var distanceProgress: Float = 0f,

    @ColumnInfo(name = "timeProgress")
    var timeProgress: Long = 0L,

    @ColumnInfo(name = "expProgress")
    var expProgress: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}