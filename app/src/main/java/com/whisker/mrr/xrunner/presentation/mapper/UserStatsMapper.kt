package com.whisker.mrr.xrunner.presentation.mapper

import com.whisker.mrr.domain.common.DomainConstants.EXP_RATIO
import com.whisker.mrr.domain.common.DomainConstants.MILLISECONDS_PER_HOUR
import com.whisker.mrr.domain.common.DomainConstants.MILLISECONDS_PER_MINUTE
import com.whisker.mrr.domain.common.DomainConstants.MILLISECONDS_PER_SECOND
import com.whisker.mrr.domain.model.UserStatsEntity
import com.whisker.mrr.xrunner.presentation.model.UserStats
import io.reactivex.Single

class UserStatsMapper {

    companion object {

        fun transformUserStats(statsEntity: UserStatsEntity) : Single<UserStats> {
            val userStats = UserStats()
            userStats.averagePaceMin = statsEntity.averagePace.toInt()
            userStats.averagePaceSec = (statsEntity.averagePace % 1 * 60).toInt()

            userStats.totalHours = (statsEntity.totalTime / MILLISECONDS_PER_HOUR).toInt()
            userStats.totalMinutes = ((statsEntity.totalTime % MILLISECONDS_PER_HOUR) / MILLISECONDS_PER_MINUTE).toInt()

            userStats.totalKilometers = (statsEntity.totalDistance / MILLISECONDS_PER_SECOND).toInt()
            userStats.totalMeters = (statsEntity.totalDistance - userStats.totalKilometers * MILLISECONDS_PER_SECOND).toInt()

            var level = 0
            var expOfNextLevel = 0
            var levelExp = EXP_RATIO

            while(statsEntity.experience > levelExp) {
                level++
                expOfNextLevel = statsEntity.experience - levelExp
                levelExp += (level + 1) * EXP_RATIO
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