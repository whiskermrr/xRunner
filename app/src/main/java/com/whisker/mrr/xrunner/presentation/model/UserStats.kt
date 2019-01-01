package com.whisker.mrr.xrunner.presentation.model

data class UserStats(
    var averagePaceMin: Int = 0,
    var averagePaceSec: Int = 0,
    var totalKilometers: Int = 0,
    var totalMeters: Int = 0,
    var totalHours: Int = 0,
    var totalMinutes: Int = 0,
    var level: Int = 0,
    var expToNextLevel: Int = 0
)