package com.whisker.mrr.domain.model

data class Challenge(
    var id: Long = -1L,
    var isFinished: Boolean = false,
    var deadline: Long? = null,
    var title: String = "Challenge",
    var distance: Float? = null,
    var speed: Float? = null,
    var time: Long? = null,
    var progress: Int = 0,
    var difficulty: ChallengeDifficulty = ChallengeDifficulty.EASY,
    var experience: Int = 0,
    var finishedDistance: Float = 0f,
    var finishedTime: Long = 0,
    var isDeleted: Boolean = false
)