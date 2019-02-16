package com.whisker.mrr.xrunner.presentation.model

import com.whisker.mrr.domain.model.ChallengeDifficulty

data class ChallengeModel(
    var isFinished: Boolean = false,
    var deadline: Long? = null,
    var title: String = "",
    var distance: String? = null,
    var time: String? = null,
    var speed: String? = null,
    var progress: Int = 0,
    var difficulty: ChallengeDifficulty = ChallengeDifficulty.EASY,
    var experience: Int = 0
)