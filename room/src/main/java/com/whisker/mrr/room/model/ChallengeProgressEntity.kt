package com.whisker.mrr.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "ChallengeProgress")
data class ChallengeProgressEntity(
    @ColumnInfo(name = "challengeID")
    var challengeID: Long,

    @ColumnInfo(name = "distanceProgress")
    var distanceProgress: Float? = null,

    @ColumnInfo(name = "timeProgress")
    var timeProgress: Long? = null
)