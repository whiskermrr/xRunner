package com.whisker.mrr.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ChallengeProgress")
data class ChallengeProgressEntity(

    @ColumnInfo(name = "challengeID")
    var challengeID: Long,

    @ColumnInfo(name = "distanceProgress")
    var distanceProgress: Float? = null,

    @ColumnInfo(name = "timeProgress")
    var timeProgress: Long? = null
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}