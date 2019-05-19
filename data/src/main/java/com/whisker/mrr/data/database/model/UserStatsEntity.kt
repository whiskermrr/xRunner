package com.whisker.mrr.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserStats")
data class UserStatsEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COL_USER_ID)
    var userID: String? = null,

    @ColumnInfo(name = COL_AVERAGE_PACE)
    var averagePace: Float? = null,

    @ColumnInfo(name = COL_EXP)
    var experience: Int? = null,

    @ColumnInfo(name = COL_TOTAL_DISTANCE)
    var totalDistance: Float? = null,

    @ColumnInfo(name = COL_TOTAL_TIME)
    var totalTime: Long? = null
) {
    companion object {
        const val COL_USER_ID = "userID"
        const val COL_AVERAGE_PACE = "averagePace"
        const val COL_EXP = "experience"
        const val COL_TOTAL_DISTANCE = "totalDistance"
        const val COL_TOTAL_TIME = "totalTime"
    }
}