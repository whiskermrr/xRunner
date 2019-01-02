package com.whisker.mrr.xrunner.presentation.mapper

import com.whisker.mrr.xrunner.domain.common.DomainConstants.EXP_RATIO
import com.whisker.mrr.xrunner.domain.model.UserStatsEntity
import com.whisker.mrr.xrunner.presentation.model.UserStats
import com.whisker.mrr.xrunner.utils.XRunnerConstants
import io.reactivex.Single

class UserStatsMapper {

    companion object {

        fun transformUserStats(statsEntity: UserStatsEntity) : Single<UserStats> {
            val userStats = UserStats()
            userStats.averagePaceMin = statsEntity.averagePace.toInt()
            userStats.averagePaceSec = (statsEntity.averagePace % 1 * 60).toInt()

            userStats.totalHours = (statsEntity.totalTime / XRunnerConstants.MILLISECONDS_PER_HOUR).toInt()
            userStats.totalMinutes = ((statsEntity.totalTime % XRunnerConstants.MILLISECONDS_PER_HOUR) / XRunnerConstants.MILLISECONDS_PER_MINUTE).toInt()

            userStats.totalKilometers = (statsEntity.totalDistance / XRunnerConstants.MILLISECONDS_PER_SECOND).toInt()
            userStats.totalMeters = (statsEntity.totalDistance - userStats.totalKilometers * XRunnerConstants.MILLISECONDS_PER_SECOND).toInt()

            var level = 0
            var expOfNextLevel = 0
            var levelExp = EXP_RATIO

            while(statsEntity.experience > levelExp) {
                level++
                expOfNextLevel = statsEntity.experience - levelExp
                levelExp += levelExp + (level + 1) * EXP_RATIO
            }

            levelExp -= (levelExp - (level + 1) * EXP_RATIO)

            userStats.expToNextLevel = if(level != 0) {
                levelExp - expOfNextLevel
            } else {
                EXP_RATIO - statsEntity.experience
            }


            userStats.level = level
            userStats.percentExp = ((expOfNextLevel.toFloat() / levelExp) * 100).toInt()

            return Single.just(userStats)
        }
     }
}