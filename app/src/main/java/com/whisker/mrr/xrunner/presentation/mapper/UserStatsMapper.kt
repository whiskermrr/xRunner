package com.whisker.mrr.xrunner.presentation.mapper

import com.whisker.mrr.domain.common.DomainConstants.EXP_RATIO
import com.whisker.mrr.domain.common.DomainConstants.MILLISECONDS_PER_HOUR
import com.whisker.mrr.domain.common.DomainConstants.MILLISECONDS_PER_MINUTE
import com.whisker.mrr.domain.common.DomainConstants.MILLISECONDS_PER_SECOND
import io.reactivex.Single

class UserStatsMapper {

    companion object {

        fun transformUserStats(stats: com.whisker.mrr.domain.model.UserStats) : Single<UserStats> {
            val userStats = UserStats()
            userStats.averagePaceMin = stats.averagePace.toInt()
            userStats.averagePaceSec = (stats.averagePace % 1 * 60).toInt()

            userStats.totalHours = (stats.totalTime / MILLISECONDS_PER_HOUR).toInt()
            userStats.totalMinutes = ((stats.totalTime % MILLISECONDS_PER_HOUR) / MILLISECONDS_PER_MINUTE).toInt()

            userStats.totalKilometers = (stats.totalDistance / MILLISECONDS_PER_SECOND).toInt()
            userStats.totalMeters = (stats.totalDistance - userStats.totalKilometers * MILLISECONDS_PER_SECOND).toInt()

            var level = 0
            var expOfNextLevel = 0
            var levelExp = EXP_RATIO

            while(stats.experience > levelExp) {
                level++
                expOfNextLevel = stats.experience - levelExp
                levelExp += (level + 1) * EXP_RATIO
            }

            levelExp -= (levelExp - (level + 1) * EXP_RATIO)

            userStats.expToNextLevel = if(level != 0) {
                levelExp - expOfNextLevel
            } else {
                EXP_RATIO - stats.experience
            }


            userStats.level = level
            userStats.percentExp = ((expOfNextLevel.toFloat() / levelExp) * 100).toInt()

            return Single.just(userStats)
        }
     }
}