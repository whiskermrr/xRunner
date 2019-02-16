package com.whisker.mrr.domain.model

data class Challenge(
    var id: String = "",
    var isFinished: Boolean = false,
    val deadline: Long? = null,
    val title: String = "Challenge",
    val distance: Float? = null,
    val speed: Float? = null,
    val time: Long? = null,
    var progress: Int = 0,
    var difficulty: ChallengeDifficulty = ChallengeDifficulty.EASY,
    var experience: Int = 0,
    var finishedDistance: Float = 0f,
    var finishedTime: Long = 0
)