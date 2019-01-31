package com.whisker.mrr.domain.model

data class Challenge(
    var id: String = "",
    var isFinished: Boolean = false,
    var deadline: Long? = null,
    var title: String = "Challenge",
    var distance: Float? = null,
    var speed: Float? = null,
    var time: Long? = null,
    var progress: Int = 0,
    var difficulty: Int = 0,
    var experience: Int = 0
)