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
            var expToNextLevel = 0
            var levelExp = EXP_RATIO

            while(statsEntity.experience > levelExp) {
                level++
                expToNextLevel = statsEntity.experience - levelExp
                levelExp += levelExp + (level + 1) * EXP_RATIO
            }

            userStats.level = level
            userStats.nextLevelExp = levelExp
            userStats.experience = statsEntity.experience


            userStats.expToNextLevel = if(level != 0) {
                statsEntity.experience - expToNextLevel
            } else {
                EXP_RATIO - statsEntity.experience
            }

            return Single.just(userStats)
        }
     }
}