package com.whisker.mrr.domain.model

data class ChallengeProgress(
    var challengeID: Long,
    var distanceProgress: Float? = null,
    var timeProgress: Long? = null
)