package com.whisker.mrr.xrunner.presentation.model

data class ChallengeHolder(
    val activeChallenges: List<ChallengeModel>? = null,
    val finishedChallenges: List<ChallengeModel>? = null
)