package com.whisker.mrr.webapi.dto

import com.google.gson.annotations.SerializedName
import com.whisker.mrr.domain.model.ChallengeDifficulty

data class ChallengeDto(

    @SerializedName("ChallengeID")
    var id: Long? = null,

    @SerializedName("IsFinished")
    var isFinished: Boolean? = null,

    @SerializedName("Deadline")
    var deadline: Long? = null,

    @SerializedName("Title")
    var title: String? = null,

    @SerializedName("Distance")
    var distance: Float? = null,

    @SerializedName("Speed")
    var speed: Float? = null,

    @SerializedName("Time")
    var time: Long? = null,

    @SerializedName("Progress")
    var progress: Int? = null,

    @SerializedName("Difficulty")
    var difficulty: ChallengeDifficulty? = null,

    @SerializedName("Experience")
    var experience: Int? = null,

    @SerializedName("FinishedDistance")
    var finishedDistance: Float? = null,

    @SerializedName("FinishedTime")
    var finishedTime: Long? = null,

    @SerializedName("IsDeleted")
    var isDeleted: Boolean = false
)