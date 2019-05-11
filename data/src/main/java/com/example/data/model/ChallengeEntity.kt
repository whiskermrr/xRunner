package com.example.data.model

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
    var id: String = "",

    @SerializedName("IsFinished")
    @ColumnInfo(name = COL_IS_FINISHED)
    var isFinished: Boolean = false,

    @SerializedName("Deadline")
    @ColumnInfo(name = COL_DEADLINE)
    val deadline: Long? = null,

    @SerializedName("Title")
    @ColumnInfo(name = COL_TITLE)
    val title: String = "Challenge",

    @SerializedName("Distance")
    @ColumnInfo(name = COL_DISTANCE)
    val distance: Float? = null,

    @SerializedName("Speed")
    @ColumnInfo(name = COL_SPEED)
    val speed: Float? = null,

    @SerializedName("Time")
    @ColumnInfo(name = COL_TIME)
    val time: Long? = null,

    @SerializedName("Progress")
    @ColumnInfo(name = COL_PROGRESS)
    var progress: Int = 0,

    @SerializedName("Difficulty")
    @ColumnInfo(name = COL_DIFFICULTY)
    var difficulty: ChallengeDifficulty = ChallengeDifficulty.EASY,

    @SerializedName("Experience")
    @ColumnInfo(name = COL_EXP)
    var experience: Int = 0,

    @SerializedName("FinishedDistance")
    @ColumnInfo(name = COL_FINISHED_DISTANCE)
    var finishedDistance: Float = 0f,

    @SerializedName("FinishedTime")
    @ColumnInfo(name = COL_FINISHED_TIME)
    var finishedTime: Long = 0
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
    }
}