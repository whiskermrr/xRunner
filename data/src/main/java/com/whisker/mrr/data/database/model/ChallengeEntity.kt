package com.whisker.mrr.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.whisker.mrr.domain.model.ChallengeDifficulty

@Entity(tableName = "Challenge")
data class ChallengeEntity(
    @SerializedName("ChallengeID")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COL_ID)
    var id: Long? = null,

    @SerializedName("IsFinished")
    @ColumnInfo(name = COL_IS_FINISHED)
    var isFinished: Boolean? = null,

    @SerializedName("Deadline")
    @ColumnInfo(name = COL_DEADLINE)
    var deadline: Long? = null,

    @SerializedName("Title")
    @ColumnInfo(name = COL_TITLE)
    var title: String? = null,

    @SerializedName("Distance")
    @ColumnInfo(name = COL_DISTANCE)
    var distance: Float? = null,

    @SerializedName("Speed")
    @ColumnInfo(name = COL_SPEED)
    var speed: Float? = null,

    @SerializedName("Time")
    @ColumnInfo(name = COL_TIME)
    var time: Long? = null,

    @SerializedName("Progress")
    @ColumnInfo(name = COL_PROGRESS)
    var progress: Int? = null,

    @SerializedName("Difficulty")
    @ColumnInfo(name = COL_DIFFICULTY)
    var difficulty: ChallengeDifficulty? = null,

    @SerializedName("Experience")
    @ColumnInfo(name = COL_EXP)
    var experience: Int? = null,

    @SerializedName("FinishedDistance")
    @ColumnInfo(name = COL_FINISHED_DISTANCE)
    var finishedDistance: Float? = null,

    @SerializedName("FinishedTime")
    @ColumnInfo(name = COL_FINISHED_TIME)
    var finishedTime: Long? = null,

    @SerializedName("IsDeleted")
    @ColumnInfo(name = COL_IS_DELETED)
    var isDeleted: Boolean = false
) {
    companion object {
        const val COL_ID = "id"
        const val COL_IS_FINISHED = "isFinished"
        const val COL_DEADLINE = "deadline"
        const val COL_TITLE = "title"
        const val COL_DISTANCE = "distance"
        const val COL_SPEED = "speed"
        const val COL_TIME = "time"
        const val COL_PROGRESS = "progress"
        const val COL_DIFFICULTY = "difficulty"
        const val COL_EXP = "experience"
        const val COL_FINISHED_DISTANCE = "finishedDistance"
        const val COL_FINISHED_TIME = "finishedTime"
        const val COL_IS_DELETED = "isDeleted"
    }
}