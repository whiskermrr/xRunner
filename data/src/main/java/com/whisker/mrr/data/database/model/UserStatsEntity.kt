package com.whisker.mrr.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "UserStats")
data class UserStatsEntity(

    @SerializedName("UserID")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COL_USER_ID)
    var userID: String,

    @SerializedName("AveragePace")
    @ColumnInfo(name = COL_AVERAGE_PACE)
    var averagePace: Float? = null,

    @SerializedName("Experience")
    @ColumnInfo(name = COL_EXP)
    var experience: Int? = null,

    @SerializedName("TotalDistance")
    @ColumnInfo(name = COL_TOTAL_DISTANCE)
    var totalDistance: Float? = null,

    @SerializedName("TotalTime")
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