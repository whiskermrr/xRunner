package com.whisker.mrr.xrunner.domain.model

data class Achievement(
    var id: String = "",
    var isFinished: Boolean = false,
    var deadline: Long? = null,
    var title: String = "Achievement",
    var distance: Int? = null,
    var speed: Float? = null,
    var time: Long? = null,
    var progress: Int = 0,
    var difficulty: Int = 0,
    var experience: Int = 0
)